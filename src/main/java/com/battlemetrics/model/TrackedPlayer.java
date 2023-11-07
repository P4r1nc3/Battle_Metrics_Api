package com.battlemetrics.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tracked_player")
public class TrackedPlayer {
    @Id
    private String playerId;
    private String nick;
    private boolean online;
    private boolean notifications = true;
}
