package com.battlemetrics.dao.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerResponse {
    private ServerData data;
    private List<IncludedPlayer> included;

    @Data
    public static class ServerData {
        private String type;
        private String id;
        private ServerAttributes attributes;
        private ServerRelationships relationships;
    }

    @Data
    public static class ServerAttributes {
        private String name;
        private String ip;
        private int port;
        private int players;
        private int maxPlayers;
        private int rank;
        private String status;
        private ServerDetails details;
        private boolean isPrivate;
        private String createdAt;
        private String updatedAt;
        private int portQuery;
        private String country;
        private String queryStatus;
    }

    @Data
    public static class ServerDetails {
        private boolean official;
        private String rustType;
        private String map;
        private String environment;
        private String rustBuild;
        private int rustEntCntI;
        private int rustFps;
        private double rustFpsAvg;
        private int rustGcCl;
        private int rustGcMb;
        private String rustHash;
        private String rustHeaderImage;
        private String rustMemPv;
        private String rustMemWs;
        private boolean pve;
        private long rustUptime;
        private String rustUrl;
        private long rustWorldSeed;
        private int rustWorldSize;
        private String rustWorldLevelUrl;
        private RustMaps rustMaps;
        private String rustDescription;
        private boolean rustModded;
        private int rustQueuedPlayers;
        private String rustGameMode;
        private String rustBorn;
        private String rustLastEntDrop;
        private String rustLastSeedChange;
        private String rustLastWipe;
        private int rustLastWipeEnt;
        private String serverSteamId;
    }

    @Data
    public static class RustMaps {
        private long seed;
        private int size;
        private String url;
        private String thumbnailUrl;
        private int monuments;
        private boolean barren;
        private String updatedAt;
        private String mapUrl;
    }

    @Data
    public static class ServerRelationships {
        private Game game;
    }

    @Data
    public static class Game {
        private GameData data;
    }

    @Data
    public static class GameData {
        private String type;
        private String id;
    }
}
