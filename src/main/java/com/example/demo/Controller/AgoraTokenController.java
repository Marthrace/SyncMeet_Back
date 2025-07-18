package com.example.demo.controller;

import com.example.demo.agora.AgoraTokenService;
import io.agora.media.RtcTokenBuilder2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/agora")
public class AgoraTokenController {

    @Autowired
    private AgoraTokenService tokenService;

    /**
     * GET endpoint to fetch an Agora RTC token.
     * Example: /api/agora/token?channelName=test&uid=123
     *
     * @param channelName The Agora channel name.
     * @param uid         The user ID.
     * @return            A JSON object with the token.
     */
    @GetMapping("/token")
    public Map<String, String> getRtcToken(
            @RequestParam String channelName,
            @RequestParam(required = false, defaultValue = "0") int uid) {

        int expireTime = 3600; // 1 hour token
        String token = tokenService.generateRtcToken(channelName, uid, RtcTokenBuilder2.Role.ROLE_PUBLISHER, expireTime);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }
}
