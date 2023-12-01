package com.battlemetrics.service;

import com.battlemetrics.dao.response.bmapi.IncludedPlayer;
import com.battlemetrics.dao.response.bmapi.ServerResponse;
import com.battlemetrics.model.Friend;
import com.battlemetrics.model.Player;
import lombok.AllArgsConstructor;
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
public class TeamDetectorService {
    private final ServerService serverService;

    public void detectTeams(String serverId, String steamUrl) {
        List<Player> battlemetricsPlayers = getPlayersList(serverId);
        List<Friend> initialFriendList = getFriendsList(steamUrl);

        List<String> friends = new ArrayList<>();
        List<String> leftToCheck = comparePlayers(battlemetricsPlayers, initialFriendList);

        for(String id : leftToCheck) {
            friends.add(id);
        }

        while (!leftToCheck.isEmpty()) {
            List<String> newLeft = new ArrayList<>();
            for (String id : leftToCheck) {
                List<Friend> friendList = getFriendsList("https://steamcommunity.com/profiles/" + id + "/friends");
                for (String friendC : comparePlayers(battlemetricsPlayers, friendList)) {
                    if (!friends.contains(friendC) && !newLeft.contains(friendC)) {
                        newLeft.add(friendC);
                        friends.add(friendC);
                    }
                }
            }
            leftToCheck = newLeft;
        }
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
        String friendLink = linkElement.attr("href");
        return friendLink;
    }

    private String request(String url) {
        return new RestTemplate().getForObject(url, String.class);
    }
}
