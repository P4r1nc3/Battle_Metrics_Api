package com.battlemetrics.dao.response.bmapi;

import lombok.Data;

import java.util.List;

@Data
public class IncludedPlayer {
    private String type;
    private String id;
    private PlayerAttributes attributes;
    private PlayerRelationships relationships;
    private PlayerMeta meta;

    @Data
    public static class PlayerAttributes {
        private String id;
        private String name;
        private boolean isPrivate;
        private boolean positiveMatch;
        private String createdAt;
        private String updatedAt;
    }

    @Data
    public static class PlayerRelationships {
        private ServerData data;
    }

    @Data
    public static class PlayerMeta {
        private List<Metadata> metadata;
    }

    @Data
    public static class Metadata {
        private String key;
        private boolean value;
        private boolean isPrivate;
    }

    @Data
    public static class ServerData {
        private String type;
        private String id;
    }
}
