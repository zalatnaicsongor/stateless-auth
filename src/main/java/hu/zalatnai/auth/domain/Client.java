package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.service.infrastructure.client.ClientStateToIntegerConverter;
import hu.zalatnai.sdk.service.infrastructure.InstantToUnixTimestampConverter;
import org.apache.commons.lang3.Validate;
import org.hibernate.validator.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Entity
public class Client {

    @Embedded
    private Token token;

    @EmbeddedId
    private ClientIdentifier clientIdentifier;

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

        this.clientIdentifier = new ClientIdentifier(deviceUuid, application.getId());
        this.deviceName = deviceName;
        this.clientState = clientState;
        this.created = clock.instant();
        this.token = tokenGenerator.generate(application.getDefaultTokenLifetime());
    }

    @NotNull
    public String getApplicationId() {
        return clientIdentifier.getApplicationId();
    }

    @NotNull
    public Token getToken() {
        return token;
    }

    void setToken(Token token) {
        this.token = token;
    }

    public void refresh() {
        clientState = clientState.refresh(this);
    }

    public void persist() {
        token.persist();
        clientState = clientState.persist(this);
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
        return clientIdentifier.getDeviceUuid();
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
}

@Embeddable
class ClientIdentifier implements Serializable {
    private static final long serialVersionUID = 1L;

    private String deviceUuid;

    private String applicationId;

    String getDeviceUuid() {
        return deviceUuid;
    }

    void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }

    String getApplicationId() {
        return applicationId;
    }

    void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    ClientIdentifier(String deviceUuid, String applicationId) {
        this.deviceUuid = deviceUuid;
        this.applicationId = applicationId;
    }

    ClientIdentifier() {
    }
}