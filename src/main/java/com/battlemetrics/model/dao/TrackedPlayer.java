package com.battlemetrics.model.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "tracked_player")
public class TrackedPlayer {
    @Id
    private String playerId;
    private boolean online;
    // TODO new fields here, like since when player is being tracked etc.
}
