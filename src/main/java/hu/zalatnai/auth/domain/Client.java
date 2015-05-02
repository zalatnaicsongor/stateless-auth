package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.service.infrastructure.client.ClientStateToIntegerConverter;
import hu.zalatnai.sdk.service.domain.exception.UUIDAlreadyAssignedException;
import hu.zalatnai.sdk.service.infrastructure.InstantToUnixTimestampConverter;
import org.apache.commons.lang3.Validate;
import org.hibernate.validator.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Entity
public class Client {

    @Id
    private UUID clientUuid;

    @Embedded
    private Token token;

    private String deviceUuid;

    private String applicationId;

    @NotBlank
    private String deviceName;

    @Convert(converter = InstantToUnixTimestampConverter.class)
    private Instant created;

    @Convert(converter = ClientStateToIntegerConverter.class)
    private ClientState clientState;

    @Column(nullable = true)
    private String userId;

    Client() {
    }

    protected Client(@NotNull Application application, @NotNull String deviceUuid, @NotNull String deviceName, Clock clock, TransientClientState clientState, TokenGenerator tokenGenerator) {
        Validate.notBlank(deviceName);
        Validate.notBlank(deviceUuid);

        this.deviceUuid = deviceUuid;
        this.applicationId = application.getId();
        this.deviceName = deviceName;
        this.clientState = clientState;
        this.created = clock.instant();
        this.token = tokenGenerator.generate(application.getDefaultTokenLifetime());
    }

    Token getToken() {
        return token;
    }

    @NotNull
    public String getApplicationId() {
        return applicationId;
    }

    public void refreshToken(byte[] refreshToken) {
        clientState = clientState.refreshToken(this, refreshToken);
    }

    public void persist() {
        clientState = clientState.persist(this);
    }

    public void blacklist() {
        clientState = clientState.blacklist(this);
    }

    public byte[] getAccessToken() {
        return clientState.getAccessToken(this);
    }

    public byte[] getRefreshToken() {
        return clientState.getRefreshToken(this);
    }

    public void addUserToClient(@NotNull String userId) {
        this.userId = userId;
    }

    @NotNull
    public String getDeviceName() {
        return deviceName;
    }

    @NotNull
    public String getDeviceUuid() {
        return deviceUuid;
    }

    public Optional<String> getUserId() {
        return Optional.ofNullable(userId);
    }

    public Instant getCreated() {
        return created;
    }

    public int getStatus() {
        return clientState.getId();
    }

    public UUID getClientUuid() {
        return clientUuid;
    }

    void assignClientUUID(UUID clientUuid) {
        if (this.clientUuid != null) {
            throw new UUIDAlreadyAssignedException();
        }
        this.clientUuid = clientUuid;
    }
}
