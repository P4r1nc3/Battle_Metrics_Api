package com.battlemetrics.service;

import com.battlemetrics.Constants;
import com.battlemetrics.dao.response.bmapi.ServerResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class ServerService {
    public ServerResponse getServerById(String serverId, String include) {
        String apiUrl = Constants.API_URL + "/servers/" + serverId;

        if (include != null && !include.isEmpty()) {
            apiUrl += "?include=" + include;
        }

        return new RestTemplate().getForObject(apiUrl, ServerResponse.class);
    }
}
