package com.battlemetrics.controller;

import com.battlemetrics.model.TrackedPlayer;
import com.battlemetrics.model.User;
import com.battlemetrics.service.TrackerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/tracker")
public class TrackerController {
    private final TrackerService trackerService;

    @PostMapping("/{playerId}")
    public ResponseEntity<String> addPlayerToTracking(@PathVariable String playerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        trackerService.addPlayerToTracking(playerId, user);
        return ResponseEntity.ok("Player added to tracking list.");
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<TrackedPlayer> getTrackedPlayer(@PathVariable String playerId) {
        TrackedPlayer trackedPlayer = trackerService.getTrackedPlayer(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + playerId));
        return ResponseEntity.ok(trackedPlayer);
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<String> removePlayerFromTracking(@PathVariable String playerId, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        trackerService.removePlayerFromTracking(playerId, currentUser);
        return ResponseEntity.ok("Player removed from tracking list.");
    }

    @GetMapping("/trackedPlayers")
    public ResponseEntity<List<TrackedPlayer>> getTrackedPlayersForCurrentUser(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        List<TrackedPlayer> trackedPlayers = trackerService.getTrackedPlayers(currentUser);
        return ResponseEntity.ok(trackedPlayers);
    }
}