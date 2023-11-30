package com.battlemetrics.controller;

import com.battlemetrics.dao.response.bmapi.ServerResponse;
import com.battlemetrics.model.Player;
import com.battlemetrics.service.ServerService;
import com.battlemetrics.service.TeamDetectorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/servers")
public class ServerController {
    private final ServerService serverService;
    private final TeamDetectorService teamDetectorService;

    @GetMapping("/{serverId}")
    public ServerResponse getServerById(@PathVariable String serverId, @RequestParam(required = false) String include) {
        return serverService.getServerById(serverId, include);
    }

    @GetMapping("/{serverId}/players")
    public List<Player> getPlayersList(@PathVariable String serverId) {
        return teamDetectorService.getPlayersList(serverId);
    }
}
