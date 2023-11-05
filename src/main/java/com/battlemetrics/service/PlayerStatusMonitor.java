package com.battlemetrics.service;

import com.battlemetrics.model.dao.TrackedPlayer;
import com.battlemetrics.repository.TrackedPlayerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class PlayerStatusMonitor {
    private final PlayerService playerService;
    private final TrackerService trackerService;
    private final TrackedPlayerRepository trackerRepository;
    private final MailService mailService;

    @Scheduled(fixedDelay = 10000)
    public void checkTrackedPlayersOnlineStatus() {
        List<TrackedPlayer> trackedPlayers = trackerService.getTrackedPlayers();

        for (TrackedPlayer trackedPlayer : trackedPlayers) {
            String playerId = trackedPlayer.getPlayerId();

            boolean currentStatus = playerService.isPlayerOnline(playerId).isOnline();
            boolean lastStatus = trackedPlayer.isOnline();

            if (currentStatus != lastStatus) {
                mailService.sendNotification(currentStatus);
                trackedPlayer.setOnline(currentStatus);
                trackerRepository.save(trackedPlayer);
            }
        }
    }
}

