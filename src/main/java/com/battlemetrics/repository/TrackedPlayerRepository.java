package com.battlemetrics.repository;

import com.battlemetrics.model.TrackedPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackedPlayerRepository extends JpaRepository<TrackedPlayer, String> {
}
