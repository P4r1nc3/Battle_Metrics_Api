package com.battlemetrics.controller;

import com.battlemetrics.dao.request.SteamRequest;
import com.battlemetrics.model.Friend;
import com.battlemetrics.service.SteamService;
import com.battlemetrics.service.TeamDetectorService;
import lombok.AllArgsConstructor;
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

    @GetMapping("/teamDetector")
    public String teamDetector(@RequestParam String battlemetricsUrl, @RequestParam String steamUrl) {
        return teamDetectorService.detectTeams(battlemetricsUrl, steamUrl);
    }

    @PostMapping("/friendsList")
    public List<Friend> getFriendsList(@RequestBody SteamRequest steamRequest) {
        String steamUrl = steamRequest.getSteamUrl();
        return teamDetectorService.getFriendsList(steamUrl);
    }
}
