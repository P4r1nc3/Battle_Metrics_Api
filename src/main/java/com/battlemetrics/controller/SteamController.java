package com.battlemetrics.controller;

import com.battlemetrics.dao.request.SteamRequest;
import com.battlemetrics.service.SteamService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/steam")
public class SteamController {
    private final SteamService steamService;

    @PostMapping
    public String getSteamId(@RequestBody SteamRequest steamRequest) {
        String steamUrl = steamRequest.getSteamUrl();
        return steamService.getSteamId(steamUrl);
    }
}
