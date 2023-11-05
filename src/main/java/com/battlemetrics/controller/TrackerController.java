package com.battlemetrics.controller;

import com.battlemetrics.model.response.PlayerStatusResponse;
import com.battlemetrics.service.MailService;
import com.battlemetrics.service.PlayerStatusMonitor;
import com.battlemetrics.service.TrackerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/tracker")
public class TrackerController {
    private final TrackerService trackerService;
    private final MailService mailService;

    @PostMapping("/add/{playerId}")
    public ResponseEntity<String> addPlayerToTracking(@PathVariable String playerId) {
        trackerService.addPlayerToTracking(playerId);
        return ResponseEntity.ok("Player added to tracking list.");
    }

    @DeleteMapping("/remove/{playerId}")
    public ResponseEntity<String> removePlayerFromTracking(@PathVariable String playerId) {
        trackerService.removePlayerFromTracking(playerId);
        return ResponseEntity.ok("Player removed from tracking list.");
    }

    @GetMapping("/trackedPlayers")
    public ResponseEntity<Map<String, PlayerStatusResponse>> getTrackedPlayers() {
        Map<String, PlayerStatusResponse> trackedPlayers = trackerService.getTrackedPlayers();
        return ResponseEntity.ok(trackedPlayers);
    }
}