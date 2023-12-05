package com.battlemetrics.dao.response.bmapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerSessionResponse {
    private List<SessionData> data;
    private Links links;
    private List<Object> included;

    @Data
    public static class SessionData {
        private String type;
        private String id;
        private SessionAttributes attributes;
        private SessionRelationships relationships;
    }

    @Data
    public static class SessionAttributes {
        private Instant start;
        private Instant stop;
        private boolean firstTime;
        private String name;
        private boolean isPrivate;
    }

    @Data
    public static class SessionRelationships {
        private ServerData server;
        private PlayerData player;
        private IdentifiersData identifiers;
    }

    @Data
    public static class ServerData {
        private ServerDataItem data;
    }

    @Data
    public static class ServerDataItem {
        private String type;
        private String id;
    }

    @Data
    public static class PlayerData {
        private PlayerDataItem data;
    }

    @Data
    public static class PlayerDataItem {
        private String type;
        private String id;
    }

    @Data
    public static class IdentifiersData {
        private List<IdentifierDataItem> data;
    }

    @Data
    public static class IdentifierDataItem {
        private String type;
        private String id;
    }

    @Data
    public static class Links {
        private String next;
    }
}
