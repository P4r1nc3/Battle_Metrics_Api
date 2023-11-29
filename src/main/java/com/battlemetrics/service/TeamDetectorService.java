package com.battlemetrics.service;

import com.battlemetrics.model.Friend;
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

    // TODO implementation of below methods
    public String detectTeams(String battlemetricsUrl, String steamUrl) {
        return null;
    }

    private List<String> getPlayersList(String battlemetricsUrl) {
        return null;
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
            String steamId = extractSteamId(friendBlock);
            String friendName = extractFriendName(friendBlock);
            String friendLink = extractFriendLink(friendBlock);

            Friend friend = new Friend(steamId, friendName, friendLink);
            friends.add(friend);
        }

        return friends;
    }

    private static String extractSteamId(Element friendBlock) {
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
