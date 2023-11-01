package com.battlemetrics.controller;

import com.battlemetrics.model.response.Server;
import com.battlemetrics.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/servers")
public class ServerController {
    private ServerService serverService;

    @GetMapping("/{serverId}")
    public Server getServerById(@PathVariable String serverId) {
        return serverService.getServerById(serverId);
    }
}