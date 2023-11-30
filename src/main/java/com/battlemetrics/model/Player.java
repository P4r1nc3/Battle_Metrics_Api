package com.battlemetrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Player {
    private String nick;
    private String battleMetricsId;
    private String profileUrl;
}
