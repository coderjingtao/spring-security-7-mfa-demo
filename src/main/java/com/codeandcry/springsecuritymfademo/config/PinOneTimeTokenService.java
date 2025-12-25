package com.codeandcry.springsecuritymfademo.config;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.ott.*;
import org.springframework.util.Assert;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PinOneTimeTokenService implements OneTimeTokenService {
    private static final int PIN_LENGTH = 6;
    private static final int MAX_PIN_VALUE = 1_000_000;

    private final Map<String, OneTimeToken> oneTimeTokens = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();

    private Clock clock = Clock.systemUTC();
    private Duration tokenExpiresIn = Duration.ofMinutes(5);

    @Override
    public OneTimeToken generate(GenerateOneTimeTokenRequest request) {
        String token = generatePin();
        Instant expiresAt = this.clock.instant().plus(tokenExpiresIn);
        OneTimeToken ott = new DefaultOneTimeToken(token, request.getUsername(), expiresAt);
        this.oneTimeTokens.put(token, ott);
        cleanExpiredTokensIfNeeded();
        return ott;
    }

    @Override
    public @Nullable OneTimeToken consume(OneTimeTokenAuthenticationToken authenticationToken) {
        OneTimeToken ott = this.oneTimeTokens.remove(authenticationToken.getTokenValue());
        if(ott == null || isExpired(ott)) {
            return null;
        }
        return ott;
    }

    private String generatePin() {
        int pin = secureRandom.nextInt(MAX_PIN_VALUE);
        return String.format("%0"+PIN_LENGTH+"d", pin);
    }

    private boolean isExpired(OneTimeToken ott) {
        return this.clock.instant().isAfter(ott.getExpiresAt());
    }

    private void cleanExpiredTokensIfNeeded() {
        if(this.oneTimeTokens.size() < 100){
            return;
        }
        for (Map.Entry<String, OneTimeToken> entry : this.oneTimeTokens.entrySet()) {
            if(isExpired(entry.getValue())) {
                this.oneTimeTokens.remove(entry.getKey());
            }
        }
    }

    public void setTokenExpiresIn(Duration tokenExpiresIn) {
        this.tokenExpiresIn = tokenExpiresIn;
    }

    public void setClock(Clock clock) {
        Assert.notNull(clock, "clock must not be null");
        this.clock = clock;
    }
}
