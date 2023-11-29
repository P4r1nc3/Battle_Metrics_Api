package com.battlemetrics.controller;

import com.battlemetrics.dao.response.bmapi.ServerResponse;
import com.battlemetrics.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/servers")
public class ServerController {
    private ServerService serverService;

    @GetMapping("/{serverId}")
    public ServerResponse getServerById(@PathVariable String serverId, @RequestParam(required = false) String include) {
        return serverService.getServerById(serverId, include);
    }
}
