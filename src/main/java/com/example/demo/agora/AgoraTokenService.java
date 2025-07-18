package com.example.demo.agora;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.agora.media.RtcTokenBuilder2;

@Service
public class AgoraTokenService {

    @Value("${agora.app-id}")
    private String appId;

    @Value("${agora.app-certificate}")
    private String appCertificate;

    /**
     * Generate an RTC token for a given channel and user.
     *
     * @param channelName Name of the Agora channel.
     * @param uid         The user ID for the Agora session (use 0 for auto-generated).
     * @param role        The role (publisher/subscriber).
     * @param expireTime  Time in seconds for token validity.
     * @return            Generated Agora token.
     */
    public String generateRtcToken(String channelName, int uid, RtcTokenBuilder2.Role role, int expireTime) {
        RtcTokenBuilder2 tokenBuilder = new RtcTokenBuilder2();

        // Pass 7 arguments (last 2 can be privilege expiration times)
        return tokenBuilder.buildTokenWithUid(
                appId,
                appCertificate,
                channelName,
                uid,
                role,
                expireTime,   // privilege expiration time
                expireTime    // optional: same as above
        );
    }
}
