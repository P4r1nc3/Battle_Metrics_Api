package com.battlemetrics.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tracked_players")
public class TrackedPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String playerId;
    private String nick;
    private boolean online;
    private boolean notifications = true;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "trackedPlayers", cascade = { CascadeType.ALL })
    private Set<User> users = new HashSet<>();
}
