package com.battlemetrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Friend {
    private String friendName;
    private String friendLink;
    private String steamId;
}

