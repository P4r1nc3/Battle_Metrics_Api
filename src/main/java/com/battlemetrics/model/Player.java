package com.battlemetrics.model;

import lombok.*;

import java.util.Objects;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class Player {
    private String nick;
    private String steamId;
    private String steamUrl;
    private String battleMetricsId;
    private String battleMetricsUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(nick, player.nick);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nick);
    }
}
