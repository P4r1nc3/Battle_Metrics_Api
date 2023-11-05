package com.battlemetrics.service;

import com.battlemetrics.model.dao.TrackedPlayer;
import com.battlemetrics.model.response.PlayerStatusResponse;
import com.battlemetrics.repository.TrackedPlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TrackerService {
    private final PlayerService playerService;
    private final TrackedPlayerRepository trackedPlayerRepository;

    public void addPlayerToTracking(String playerId) {
        PlayerStatusResponse status = playerService.isPlayerOnline(playerId);

        TrackedPlayer trackedPlayer = new TrackedPlayer();
        trackedPlayer.setPlayerId(playerId);
        trackedPlayer.setOnline(status.isOnline());

        trackedPlayerRepository.save(trackedPlayer);
    }

    public void removePlayerFromTracking(String playerId) {
        trackedPlayerRepository.deleteById(playerId);
    }

    public List<TrackedPlayer> getTrackedPlayers() {
        return  trackedPlayerRepository.findAll();
    }
}
