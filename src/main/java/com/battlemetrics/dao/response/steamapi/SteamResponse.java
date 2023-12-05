package com.battlemetrics.dao.response.steamapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SteamResponse {

    private Response Response;

    @Data
    public static class Response {
        private String steamid;
        private int success;
    }
}
