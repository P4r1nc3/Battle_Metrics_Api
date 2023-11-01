package com.battlemetrics.service;

import com.battlemetrics.Constants;
import com.battlemetrics.model.response.Player;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class PlayerService {
    public Player getPlayerById(String playerId) {
        String apiUrl = Constants.API_URL + "/players/" + playerId;
        return new RestTemplate().getForObject(apiUrl, Player.class);
    }
}
