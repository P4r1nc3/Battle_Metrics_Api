package com.battlemetrics.controller;

import com.battlemetrics.model.response.PlayerResponse;
import com.battlemetrics.model.response.PlayerStatusResponse;
import com.battlemetrics.model.response.PlayerSessionResponse;
import com.battlemetrics.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/players")
public class PlayerController {
    private PlayerService playerService;

    @GetMapping("/{playerId}")
    public PlayerResponse getPlayerById(@PathVariable String playerId) {
        return playerService.getPlayerById(playerId);
    }

    @GetMapping("/{playerId}/sessions")
    public PlayerSessionResponse getPlayerSessionsById(@PathVariable String playerId) {
        return playerService.getPlayerSessionsById(playerId);
    }

    @GetMapping("/{playerId}/isOnline")
    public PlayerStatusResponse isPlayerOnline(@PathVariable String playerId) {
        PlayerSessionResponse playerSession = playerService.getPlayerSessionsById(playerId);
        return playerService.isPlayerOnline(playerSession);
    }
}