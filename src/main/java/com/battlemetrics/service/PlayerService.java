package com.battlemetrics.service;

import com.battlemetrics.Constants;
import com.battlemetrics.model.response.PlayerResponse;
import com.battlemetrics.model.response.PlayerStatusResponse;
import com.battlemetrics.model.response.PlayerSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class PlayerService {
    public PlayerResponse getPlayerById(String playerId) {
        String apiUrl = Constants.API_URL + "/players/" + playerId;
        return new RestTemplate().getForObject(apiUrl, PlayerResponse.class);
    }

    public PlayerSession getPlayerSessionsById(String playerId) {
        String apiUrl = Constants.API_URL + "/players/" + playerId + "/relationships" +"/sessions";
        return new RestTemplate().getForObject(apiUrl, PlayerSession.class);
    }

    public PlayerStatusResponse isPlayerOnline(String playerId) {
        PlayerSession playerSession = getPlayerSessionsById(playerId);

        if (playerSession != null && playerSession.getData() != null && !playerSession.getData().isEmpty()) {
            PlayerSession.SessionData latestSession = playerSession.getData().get(0);
            boolean isOnline = latestSession.getAttributes().getStop() == null;
            return new PlayerStatusResponse(isOnline);
        }
        return new PlayerStatusResponse(false);
    }
}
