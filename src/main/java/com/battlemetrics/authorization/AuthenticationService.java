package com.battlemetrics.authorization;

import com.battlemetrics.dao.request.SignUpRequest;
import com.battlemetrics.dao.request.SignInRequest;
import com.battlemetrics.dao.response.JwtAuthenticationResponse;


public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SignInRequest request);
}
