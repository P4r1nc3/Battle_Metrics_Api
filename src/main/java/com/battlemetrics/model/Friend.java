package com.battlemetrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Friend {
    private String nick;
    private String steamId;
    private String profileUrl;
}

