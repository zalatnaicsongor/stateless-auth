package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.domain.exception.StateAlreadyResolvedException;
import hu.zalatnai.sdk.service.domain.StateRepository;
import hu.zalatnai.sdk.service.domain.exception.UUIDAlreadyAssignedException;
import hu.zalatnai.sdk.service.infrastructure.InstantToUnixTimestampConverter;
import org.apache.commons.lang3.Validate;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Entity
public class Client {

    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID clientUuid;

    @Embedded
    private Token token;

    private String deviceUuid;

    private String applicationId;

    @NotBlank
    private String deviceName;

    @Convert(converter = InstantToUnixTimestampConverter.class)
    private Instant created;

    @Transient
    private ClientState clientState;

    private int clientStatus;

    Client() {
    }

    protected Client(@NotNull Application application, @NotNull String deviceUuid, @NotNull String deviceName, Clock clock, TransientClientState clientState, TokenGenerator tokenGenerator) {
        Validate.notBlank(deviceName);
        Validate.notBlank(deviceUuid);

        this.deviceUuid = deviceUuid;
        this.applicationId = application.getId();
        this.deviceName = deviceName;
        this.created = clock.instant();
        this.token = tokenGenerator.generate(application.getDefaultTokenLifetime());

        this.setClientState(clientState);
    }

    Token getToken() {
        return token;
    }

    private void setClientState(ClientState clientStatus) {
        clientState = clientStatus;
        this.clientStatus = clientState.getId();
    }

    public void resolveStateFromStatus(StateRepository<ClientState> clientStateRepository, StateRepository<TokenState> tokenStateStateRepository) {
        if (clientState != null) {
            throw new StateAlreadyResolvedException();
        }

        setClientState(clientStateRepository.getById(clientStatus));
        token.resolveStateFromStatus(tokenStateStateRepository);
    }

    public void refreshToken(byte[] hashedRefreshToken) {
        setClientState(clientState.refreshToken(this, hashedRefreshToken));
    }

    public void persist() {
        setClientState(clientState = clientState.persist(this));
    }

    public byte[] getAccessToken() {
        return token.getAccessToken();
    }

    public byte[] getRefreshToken() {
        return token.getRefreshToken();
    }

    public UUID getClientUuid() {
        return clientUuid;
    }

    public Instant getTokenIssuedAt() {
        return token.getIssuedAt();
    }

    public Instant getTokenExpirationTime() {
        return token.getExpirationTime();
    }

    void assignClientUUID(UUID clientUuid) {
        if (this.clientUuid != null) {
            throw new UUIDAlreadyAssignedException();
        }
        this.clientUuid = clientUuid;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public Instant getCreated() {
        return created;
    }
}
