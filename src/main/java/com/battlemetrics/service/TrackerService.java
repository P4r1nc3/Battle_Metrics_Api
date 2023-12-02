package com.battlemetrics.service;

import com.battlemetrics.model.TrackedPlayer;
import com.battlemetrics.dao.response.PlayerSessionResponse;
import com.battlemetrics.model.User;
import com.battlemetrics.repository.TrackedPlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TrackerService {
    private final PlayerService playerService;
    private final TrackedPlayerRepository trackedPlayerRepository;

    @Transactional
    public void addPlayerToTracking(int playerId, User user) {
        PlayerSessionResponse playerSession = playerService.getPlayerSessionsById(playerId);
        boolean status = playerService.isPlayerOnline(playerSession).isOnline();
        String nick = playerService.getPlayerNick(playerSession);

        TrackedPlayer trackedPlayer = new TrackedPlayer();
        trackedPlayer.setPlayerId(playerId);
        trackedPlayer.setNick(nick);
        trackedPlayer.setOnline(status);
        trackedPlayer.setUser(user);

        trackedPlayerRepository.save(trackedPlayer);
    }

    public void removePlayerFromTracking(int playerId, User user) {
        TrackedPlayer trackedPlayer = trackedPlayerRepository.findByUserAndPlayerId(user, playerId);
        if (trackedPlayer != null) {
            trackedPlayerRepository.delete(trackedPlayer);
        } else {
            throw new EntityNotFoundException("Tracked player not found with id: " + playerId);
        }
    }

    public List<TrackedPlayer> getTrackedPlayers(User user) {
        return trackedPlayerRepository.findByUser(user);
    }

    public Optional<TrackedPlayer> getTrackedPlayer(int playerId) {
        return trackedPlayerRepository.findById(Integer.toString(playerId));
    }
}
