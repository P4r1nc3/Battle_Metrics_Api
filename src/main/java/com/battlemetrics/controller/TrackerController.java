package com.battlemetrics.controller;

import com.battlemetrics.model.dao.TrackedPlayer;
import com.battlemetrics.service.TrackerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/tracker")
public class TrackerController {
    private final TrackerService trackerService;

    @PostMapping("/{playerId}")
    public ResponseEntity<String> addPlayerToTracking(@PathVariable String playerId) {
        trackerService.addPlayerToTracking(playerId);
        return ResponseEntity.ok("Player added to tracking list.");
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<TrackedPlayer> getTrackedPlayer(@PathVariable String playerId) {
        TrackedPlayer trackedPlayer = trackerService.getTrackedPlayer(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + playerId));
        return ResponseEntity.ok(trackedPlayer);
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<String> removePlayerFromTracking(@PathVariable String playerId) {
        trackerService.removePlayerFromTracking(playerId);
        return ResponseEntity.ok("Player removed from tracking list.");
    }

    @GetMapping("/trackedPlayers")
    public ResponseEntity<List<TrackedPlayer>> getTrackedPlayers() {
        List<TrackedPlayer> trackedPlayers = trackerService.getTrackedPlayers();
        return ResponseEntity.ok(trackedPlayers);
    }
}