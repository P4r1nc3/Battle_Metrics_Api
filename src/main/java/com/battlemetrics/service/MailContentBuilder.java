package com.battlemetrics.service;

import com.battlemetrics.dao.response.PlayerSessionResponse;
import com.battlemetrics.dao.response.ServerResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class MailContentBuilder {
    private final ServerService serverService;

    // TODO Refactor this code
    public String buildPlayerOnlineContent(PlayerSessionResponse playerSession) {
        String playerId = playerSession.getData().get(0).getRelationships().getPlayer().getData().getId();
        String playerName = playerSession.getData().get(0).getAttributes().getName();
        String serverId = playerSession.getData().get(0).getRelationships().getServer().getData().getId();
        Instant startTime = playerSession.getData().get(0).getAttributes().getStart();

        ServerResponse serverResponse = serverService.getServerById(serverId);

        String serverName = serverResponse.getData().getAttributes().getName();
        String ipAddress = serverResponse.getData().getAttributes().getIp();
        int port = serverResponse.getData().getAttributes().getPort();
        int players = serverResponse.getData().getAttributes().getPlayers();
        int maxPlayers = serverResponse.getData().getAttributes().getMaxPlayers();

        String serverConnectInfo = "connect " + ipAddress + ":" + port;

        return "Player with ID " + playerId +
                " and name " + playerName +
                " is now online on server " + serverName +
                " (Server ID: " + serverId +
                ", Players: " + players +
                "/" + maxPlayers + ")" +
                "\nConnect to the server: " + serverConnectInfo +
                "\nStart Time: " + startTime;
    }

    public String buildPlayerOfflineContent(PlayerSessionResponse playerSession) {
        String playerId = playerSession.getData().get(0).getRelationships().getPlayer().getData().getId();
        String playerName = playerSession.getData().get(0).getAttributes().getName();
        Instant stopTime = playerSession.getData().get(0).getAttributes().getStop();

        return "Player ID: " + playerId +
                "\nPlayer Name: " + playerName +
                "\nStop Time: " + stopTime +
                "\nis now offline";
    }
}