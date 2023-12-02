package com.battlemetrics.repository;

import com.battlemetrics.model.TrackedPlayer;
import com.battlemetrics.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackedPlayerRepository extends JpaRepository<TrackedPlayer, String> {
    List<TrackedPlayer> findByUsers(User user);
    TrackedPlayer findByUsersAndPlayerId(User user, int playerId);
}

