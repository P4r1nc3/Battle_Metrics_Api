package com.battlemetrics.controller;

import com.battlemetrics.dao.request.SteamRequest;
import com.battlemetrics.model.Player;
import com.battlemetrics.service.SteamService;
import com.battlemetrics.service.TeamDetectorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/steam")
public class SteamController {
    private final SteamService steamService;
    private final TeamDetectorService teamDetectorService;

    @PostMapping
    public String getSteamId(@RequestBody SteamRequest steamRequest) {
        String steamUrl = steamRequest.getSteamUrl();
        return steamService.getSteamId(steamUrl);
    }

    @PostMapping("/friends")
    public List<Player> getFriendsList(@RequestBody SteamRequest steamRequest) {
        String steamUrl = steamRequest.getSteamUrl();
        return teamDetectorService.getFriendsList(steamUrl);
    }

    @PostMapping("/team")
    public String teamDetector(@RequestBody SteamRequest steamRequest) {
        String steamUrl = steamRequest.getSteamUrl();
        String serverId = steamRequest.getServerId();
        return teamDetectorService.detectTeams(serverId, steamUrl);
    }
}
