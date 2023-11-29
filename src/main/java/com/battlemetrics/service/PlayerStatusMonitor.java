package com.battlemetrics.service;

import com.battlemetrics.model.TrackedPlayer;
import com.battlemetrics.dao.response.bmapi.PlayerSessionResponse;
import com.battlemetrics.model.User;
import com.battlemetrics.repository.TrackedPlayerRepository;
import com.battlemetrics.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PlayerStatusMonitor {
    private final MailService mailService;
    private final PlayerService playerService;
    private final UserRepository userRepository;
    private final TrackedPlayerRepository trackerRepository;

    @Transactional
    @Scheduled(fixedDelay = 10000)
    public void checkTrackedPlayersOnlineStatus() {
        Optional<User> userOptional = userRepository.findByEmail("draber21@gmail.com");

        userOptional.ifPresent(user -> {
            List<TrackedPlayer> trackedPlayers = user.getTrackedPlayers().stream().toList();

            for (TrackedPlayer trackedPlayer : trackedPlayers) {
                String playerId = trackedPlayer.getPlayerId();
                PlayerSessionResponse playerSession = playerService.getPlayerSessionsById(playerId);

                boolean currentStatus = playerService.isPlayerOnline(playerSession).isOnline();
                boolean lastStatus = trackedPlayer.isOnline();

                if (currentStatus != lastStatus) {
                    if(trackedPlayer.isNotifications()){
                        mailService.sendNotification(playerSession);
                    }
                    trackedPlayer.setOnline(currentStatus);
                    trackerRepository.save(trackedPlayer);
                }
            }
        });
    }
}