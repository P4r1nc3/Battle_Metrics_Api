package com.battlemetrics.service;

import com.battlemetrics.dao.response.bmapi.IncludedPlayer;
import com.battlemetrics.dao.response.bmapi.ServerResponse;
import com.battlemetrics.model.Friend;
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
        GraphGenerator generator = new GraphGenerator();
        List<String> friends = new ArrayList<>();
        generator.addNode(steamUrl);

        List<Player> battlemetricsPlayers = getPlayersList(serverId);
        log.info("Players currently plying on server with id={}: {}", serverId, battlemetricsPlayers);

        List<Friend> initialFriendList = getFriendsList(steamUrl);
        log.info("Steam friends of player {}: {}", steamUrl, initialFriendList);

        List<String> leftToCheck = comparePlayers(battlemetricsPlayers, initialFriendList);
        log.info("Friends of player {}, which are currently playing on server with id={}: {}", steamUrl, serverId, leftToCheck);

        for (String steamId : leftToCheck) {
            friends.add(steamId);
            generator.addNode(steamId);
            generator.addEdge(0, generator.getNodeIndex(steamId));
        }

        while (!leftToCheck.isEmpty()) {
            List<String> newLeft = new ArrayList<>();
            for (String steamId : leftToCheck) {
                String friendUrl = "https://steamcommunity.com/profiles/" + steamId;
                log.info("Requested for friends of player {}", friendUrl);

                List<Friend> friendList = getFriendsList(friendUrl + "/friends");
                log.info("Friends of player {}: {}", friendUrl, friendList);

                List<String> leftToCheckFriend = comparePlayers(battlemetricsPlayers, friendList);
                log.info("Friends of player {}, which are currently playing on server with id={}: {}", friendUrl, serverId, leftToCheck);

                for (String friendId : leftToCheckFriend) {
                    if (!friends.contains(friendId) && !newLeft.contains(friendId)) {
                        generator.addNode(friendId);
                        generator.addEdge(generator.getNodeIndex(steamId), generator.getNodeIndex(friendId));
                        newLeft.add(friendId);
                        friends.add(friendId);
                    }
                }
            }
            leftToCheck = newLeft;
        }

        System.out.println("Final in graph we have:");
        for (String steamId : friends) {
            log.info("https://steamcommunity.com/profiles/" + steamId);
        }

        JSONObject graph = generator.getGraph();
        log.info(graph.toString(2));

        return graph.toString();
    }

    private List<String> comparePlayers(List<Player> battlemetricsPlayers, List<Friend> friendList) {
        List<String> players = new ArrayList<>();
        for (Friend friend : friendList) {
            String name = friend.getNick();
            for (Player bmPlayer : battlemetricsPlayers) {
                if (bmPlayer.getNick().equals(name)) {
                    players.add(friend.getSteamId());
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

            Player player = new Player(nick, id, url);
            playersList.add(player);
        }

        return playersList;
    }

    public List<Friend> getFriendsList(String steamUrl) {
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

    private static List<Friend> parseFriends(String content) {
        List<Friend> friends = new ArrayList<>();

        Document doc = Jsoup.parse(content);
        Elements friendBlocks = doc.select(".friend_block_v2[data-steamid]");

        for (Element friendBlock : friendBlocks) {
            String friendName = extractFriendName(friendBlock);
            String friendSteamId = extractFriendSteamId(friendBlock);
            String friendLink = extractFriendLink(friendBlock);

            Friend friend = new Friend(friendName, friendSteamId, friendLink);
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

    private String request(String url) {
        return new RestTemplate().getForObject(url, String.class);
    }
}
