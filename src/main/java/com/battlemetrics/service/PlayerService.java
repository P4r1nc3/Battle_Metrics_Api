package com.battlemetrics.service;

import com.battlemetrics.Constants;
import com.battlemetrics.dao.response.PlayerResponse;
import com.battlemetrics.dao.response.PlayerStatusResponse;
import com.battlemetrics.dao.response.PlayerSessionResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class PlayerService {
    public PlayerResponse getPlayerById(int playerId) {
        String apiUrl = Constants.API_URL + "/players/" + playerId;
        return new RestTemplate().getForObject(apiUrl, PlayerResponse.class);
    }

    public PlayerSessionResponse getPlayerSessionsById(int playerId) {
        String apiUrl = Constants.API_URL + "/players/" + playerId + "/relationships" +"/sessions";
        return new RestTemplate().getForObject(apiUrl, PlayerSessionResponse.class);
    }

    //TODO decide what to do with below methods

    public PlayerStatusResponse isPlayerOnline(PlayerSessionResponse playerSession) {
        if (playerSession != null && playerSession.getData() != null && !playerSession.getData().isEmpty()) {
            PlayerSessionResponse.SessionData latestSession = playerSession.getData().get(0);
            boolean isOnline = latestSession.getAttributes().getStop() == null;
            return new PlayerStatusResponse(isOnline);
        }
        return new PlayerStatusResponse(false);
    }

    public String getPlayerNick(PlayerSessionResponse playerSession) {
        if (playerSession != null && playerSession.getData() != null && !playerSession.getData().isEmpty()) {
            PlayerSessionResponse.SessionData latestSession = playerSession.getData().get(0);
            return latestSession.getAttributes().getName();
        }
        return null;
    }
}
