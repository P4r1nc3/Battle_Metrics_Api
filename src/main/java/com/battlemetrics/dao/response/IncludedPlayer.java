package com.battlemetrics.dao.response;

import lombok.Data;

import java.util.List;

@Data
public class IncludedPlayer {
    private String type;
    private String id;
    private PlayerAttributes attributes;
    private PlayerRelationships relationships;
    private PlayerMeta meta;
}

@Data
class PlayerAttributes {
    private String id;
    private String name;
    private boolean isPrivate;
    private boolean positiveMatch;
    private String createdAt;
    private String updatedAt;
    // Add other attributes as needed
}

@Data
class PlayerRelationships {
    private ServerData data;
    // Add other relationships as needed
}

@Data
class PlayerMeta {
    private List<Metadata> metadata;
}

@Data
class Metadata {
    private String key;
    private boolean value;
    private boolean isPrivate;
}

@Data
class ServerData {
    private String type;
    private String id;
}

