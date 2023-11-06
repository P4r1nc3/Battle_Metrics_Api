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
    private String nick;
    private boolean online;
    private boolean notifications = true;
}
