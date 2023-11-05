package com.battlemetrics.service;

import com.battlemetrics.model.response.PlayerStatusResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class TrackerService {
    private final PlayerService playerService;
    private final Map<String, PlayerStatusResponse> trackedPlayers = new HashMap<>();

    public void addPlayerToTracking(String playerId) {
        PlayerStatusResponse status = playerService.isPlayerOnline(playerId);
        trackedPlayers.put(playerId, status);
    }

    public void removePlayerFromTracking(String playerId) {
        trackedPlayers.remove(playerId);
    }

    public Map<String, PlayerStatusResponse> getTrackedPlayers() {
        return new HashMap<>(trackedPlayers);
    }

    public PlayerStatusResponse getLastStatus(String playerId) {
        return trackedPlayers.getOrDefault(playerId, new PlayerStatusResponse());
    }

    public void setLastStatus(String playerId, PlayerStatusResponse status) {
        trackedPlayers.put(playerId, status);
    }
}
