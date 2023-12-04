package com.battlemetrics.service;

import com.battlemetrics.dao.response.bmapi.IncludedPlayer;
import com.battlemetrics.dao.response.bmapi.ServerResponse;
import com.battlemetrics.model.Player;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Service
@AllArgsConstructor
@Slf4j
public class TeamDetectorService {
    private final ServerService serverService;

    public String detectTeams(String serverId, String steamUrl) {
        List<Player> result = new ArrayList<>();
        GraphGenerator generator = new GraphGenerator();

        Player root = Player.builder()
                .nick(getNick(steamUrl))
                .steamUrl(steamUrl)
                .build();

        result.add(root);
        generator.addNode(getNick(steamUrl));

        List<Player> battlemetricsPlayers = getPlayersList(serverId);
        log.info("Players currently plying on server with id={}: {}", serverId, battlemetricsPlayers);

        List<Player> initialFriendList = getFriendsList(steamUrl);
        log.info("Steam friends of player {}: {}", steamUrl, initialFriendList);

        List<Player> leftToCheck = comparePlayers(battlemetricsPlayers, initialFriendList);
        log.info("Friends of player {}, which are currently playing on server with id={}: {}", steamUrl, serverId, leftToCheck);

        for (Player player : leftToCheck) {
            result.add(player);
            generator.addNode(player.getNick());
            generator.addEdge(generator.getNodeIndex(root.getNick()), generator.getNodeIndex(player.getNick()));
        }

        while (!leftToCheck.isEmpty()) {
            List<Player> newLeft = new ArrayList<>();
            for (Player player : leftToCheck) {
                String friendUrl = player.getSteamUrl() + "/friends";
                log.info("Requested for friends of player {}", friendUrl);

                List<Player> friendList = getFriendsList(friendUrl);
                log.info("Friends of player {}: {}", friendUrl, friendList);

                List<Player> leftToCheckFriend = comparePlayers(battlemetricsPlayers, friendList);
                log.info("Friends of player {}, which are currently playing on server with id={}: {}", friendUrl, serverId, leftToCheck);

                for (Player friend : leftToCheckFriend) {
                    if (!result.contains(friend) && !newLeft.contains(friend)) {
                        result.add(friend);
                        newLeft.add(friend);
                        generator.addNode(friend.getNick());
                    }
                    generator.addEdge(generator.getNodeIndex(player.getNick()), generator.getNodeIndex(friend.getNick()));
                }
            }
            leftToCheck = newLeft;
        }

        JSONObject graph = generator.getGraph();
        log.info(graph.toString(2));

        return graph.toString();
    }

    private List<Player> comparePlayers(List<Player> battleMetricsPlayers, List<Player> steamFriendsList) {
        List<Player> players = new ArrayList<>();
        for (Player steamFriend : steamFriendsList) {
            String name = steamFriend.getNick();
            for (Player bmPlayer : battleMetricsPlayers) {
                if (bmPlayer.getNick().equals(name)) {
                    Player player = Player.builder()
                            .nick(name)
                            .steamId(steamFriend.getSteamId())
                            .steamUrl(steamFriend.getSteamUrl())
                            .battleMetricsId(bmPlayer.getBattleMetricsId())
                            .battleMetricsUrl(bmPlayer.getBattleMetricsUrl())
                            .build();
                    players.add(player);
                    break;
                }
            }
        }
        return players;
    }


    public List<Player> getPlayersList(String serverId) {
        ServerResponse serverResponse = serverService.getServerById(serverId, "player");

        List<Player> playersList = new ArrayList<>();

        for (IncludedPlayer includedPlayer : serverResponse.getIncluded()) {
            String nick = includedPlayer.getAttributes().getName();
            String id = includedPlayer.getAttributes().getId();
            String url = "https://www.battlemetrics.com/players/" + id;

            Player player = new Player(nick, "", "", id, url);
            playersList.add(player);
        }

        return playersList;
    }

    public List<Player> getFriendsList(String steamUrl) {
        try {
            String url = steamUrl.contains("friends") ? steamUrl : steamUrl + "friends";
            String content = request(url);

            if (content != null) {
                return parseFriends(content);
            } else {
                throw new RuntimeException("Could not request friend list page");
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not retrieve Steam friend list.", e);
        }
    }

    private static List<Player> parseFriends(String content) {
        List<Player> friends = new ArrayList<>();

        Document doc = Jsoup.parse(content);
        Elements friendBlocks = doc.select(".friend_block_v2[data-steamid]");

        for (Element friendBlock : friendBlocks) {
            String friendName = extractFriendName(friendBlock);
            String friendSteamId = extractFriendSteamId(friendBlock);
            String friendSteamUrl = extractFriendLink(friendBlock);

            Player friend = new Player(friendName, friendSteamId, friendSteamUrl, "", "");
            friends.add(friend);
        }

        return friends;
    }


    private static String extractFriendSteamId(Element friendBlock) {
        return friendBlock.attr("data-steamid");
    }

    private static String extractFriendName(Element friendBlock) {
        Element friendContent = friendBlock.select(".friend_block_content").first();
        String friendName = friendContent.ownText().trim();
        return friendName;
    }

    private static String extractFriendLink(Element friendBlock) {
        Element linkElement = friendBlock.select("a.selectable_overlay").first();
        if (linkElement != null) {
            return linkElement.attr("href");
        } else {
            return "";
        }
    }

    private String getNick(String url) {
        try {
            String content = request(url);

            if (content != null) {
                Document doc = Jsoup.parse(content);
                Element profileNameElement = doc.select(".actual_persona_name").first();

                if (profileNameElement != null) {
                    return profileNameElement.text().trim();
                } else {
                    throw new RuntimeException("Nickname not found on the Steam profile page");
                }
            } else {
                throw new RuntimeException("Could not get nick");
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not get nick.", e);
        }
    }

    private String request(String url) {
        return new RestTemplate().getForObject(url, String.class);
    }
}
