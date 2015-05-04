package hu.zalatnai.auth.dto.output;

import java.time.Instant;
import java.util.UUID;

public class ClientDTO {
    private final UUID clientUuid;
    private final String deviceUuid;
    private final String deviceName;
    private final String accessToken;
    private final String refreshToken;
    private final String applicationId;
    private final Instant created;
    private final Instant tokenExpirationTime;
    private final Instant tokenIssuedAt;

    public ClientDTO(UUID clientUuid, String deviceUuid, String deviceName, String accessToken, String refreshToken, Instant created, Instant tokenExpirationTime, Instant tokenIssuedAt, String applicationId) {
        this.clientUuid = clientUuid;
        this.deviceUuid = deviceUuid;
        this.deviceName = deviceName;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.created = created;
        this.tokenExpirationTime = tokenExpirationTime;
        this.tokenIssuedAt = tokenIssuedAt;
        this.applicationId = applicationId;
    }

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Instant getTokenExpirationTime() {
        return tokenExpirationTime;
    }

    public Instant getTokenIssuedAt() {
        return tokenIssuedAt;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public UUID getClientUuid() {
        return clientUuid;
    }
}
