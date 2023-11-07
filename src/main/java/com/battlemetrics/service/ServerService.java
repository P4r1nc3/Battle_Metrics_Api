package com.battlemetrics.service;

import com.battlemetrics.Constants;
import com.battlemetrics.dao.response.ServerResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class ServerService {
    public ServerResponse getServerById(String serverId) {
        String apiUrl = Constants.API_URL + "/servers/" + serverId;
        return new RestTemplate().getForObject(apiUrl, ServerResponse.class);
    }
}
