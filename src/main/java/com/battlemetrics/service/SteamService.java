package com.battlemetrics.service;

import com.battlemetrics.Constants;
import com.battlemetrics.dao.response.steamapi.SteamResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class SteamService {
    @Value("${steam.key}")
    private String steamKey;

    public String getSteamId(String steamUrl) {
        validateSteamUrl(steamUrl);

        if (steamUrl.contains("/profiles/")) {
            return getSteamIdFromProfilesUrl(steamUrl);
        } else if (steamUrl.contains("/id/")) {
            return getSteamIdFromVanityUrl(steamUrl);
        } else {
            throw new IllegalArgumentException("Invalid Steam URL format");
        }
    }

    private void validateSteamUrl(String steamUrl) {
        try {
            URL url = new URL(steamUrl);
            if (!url.getHost().equals("steamcommunity.com")) {
                throw new IllegalArgumentException("Invalid host in Steam URL");
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid Steam URL format");
        }
    }

    private String getSteamIdFromProfilesUrl(String steamUrl) {
        String[] parts = steamUrl.split("/profiles/");
        if (parts.length == 2) {
            return parts[1];
        } else {
            throw new IllegalArgumentException("Invalid Steam URL format");
        }
    }

    private String getSteamIdFromVanityUrl(String steamUrl) {
        String[] parts = steamUrl.split("/id/");
        if (parts.length == 2) {
            String nick = parts[1];
            String steamApiUrl = Constants.STEAM_API_URL + "/ISteamUser" + "/ResolveVanityURL" + "/v0001" + "/?key=" + steamKey + "&vanityurl=" + nick;
            System.out.println(steamApiUrl);
            SteamResponse steamApiResponse = new RestTemplate().getForObject(steamApiUrl, SteamResponse.class);
            if (steamApiResponse != null && steamApiResponse.getResponse() != null
                    && steamApiResponse.getResponse().getSuccess() == 1) {
                return steamApiResponse.getResponse().getSteamid();
            } else {
                throw new RuntimeException("Failed to retrieve Steam ID for the given nickname");
            }
        } else {
            throw new IllegalArgumentException("Invalid Steam URL format");
        }
    }
}
