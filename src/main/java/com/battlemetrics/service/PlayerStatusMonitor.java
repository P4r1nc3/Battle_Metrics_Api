package com.battlemetrics.service;

import com.battlemetrics.model.response.PlayerStatusResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class PlayerStatusMonitor {
    private final PlayerService playerService;
    private final TrackerService trackerService;
    private final MailService mailService;

    @Scheduled(fixedDelay = 2000)
    public void checkTrackedPlayersOnlineStatus() {
        Map<String, PlayerStatusResponse> trackedPlayers = trackerService.getTrackedPlayers();

        for (Map.Entry<String, PlayerStatusResponse> entry : trackedPlayers.entrySet()) {
            String playerId = entry.getKey();

            PlayerStatusResponse currentStatus = playerService.isPlayerOnline(playerId);
            PlayerStatusResponse lastStatus = trackerService.getLastStatus(playerId);

            if (currentStatus.isOnline() != lastStatus.isOnline()) {
                mailService.sendNotification(currentStatus);
            }

            trackerService.setLastStatus(playerId, currentStatus);
        }
    }
}

