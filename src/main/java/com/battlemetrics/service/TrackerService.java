package com.battlemetrics.service;

import com.battlemetrics.model.dao.TrackedPlayer;
import com.battlemetrics.model.response.PlayerSessionResponse;
import com.battlemetrics.model.response.PlayerStatusResponse;
import com.battlemetrics.repository.TrackedPlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TrackerService {
    private final PlayerService playerService;
    private final TrackedPlayerRepository trackedPlayerRepository;

    public void addPlayerToTracking(String playerId) {
        PlayerSessionResponse playerSession = playerService.getPlayerSessionsById(playerId);
        boolean status = playerService.isPlayerOnline(playerSession).isOnline();
        String nick = playerService.getPlayerNick(playerSession);

        TrackedPlayer trackedPlayer = new TrackedPlayer();
        trackedPlayer.setPlayerId(playerId);
        trackedPlayer.setNick(nick);
        trackedPlayer.setOnline(status);

        trackedPlayerRepository.save(trackedPlayer);
    }

    public void removePlayerFromTracking(String playerId) {
        trackedPlayerRepository.deleteById(playerId);
    }

    public List<TrackedPlayer> getTrackedPlayers() {
        return  trackedPlayerRepository.findAll();
    }

    public Optional<TrackedPlayer> getTrackedPlayer(String playerId) {
        return trackedPlayerRepository.findById(playerId);
    }
}
