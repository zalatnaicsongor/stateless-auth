package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.service.infrastructure.client.ClientStateToIntegerConverter;
import org.apache.commons.lang3.Validate;
import org.hibernate.validator.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Entity
public class Client {

    @Id
    @NotBlank
    private String uuid;

    @Embedded
    private Token token;

    @NotBlank
    private String applicationId;

    @NotBlank
    private String deviceUuid;

    @NotBlank
    private String deviceName;

    @NotBlank
    private Instant created;

    @Convert(converter = ClientStateToIntegerConverter.class)
    private ClientState state;

    @Column(nullable = true)
    private String userId;

    Client() {
    }

    protected Client(@NotNull Application application, @NotNull String deviceUuid, @NotNull String deviceName, Clock clock, TransientClientState clientState, TokenGenerator tokenGenerator) {
        Validate.notBlank(deviceName);
        Validate.notBlank(deviceUuid);

        this.deviceUuid = deviceUuid;
        this.deviceName = deviceName;
        this.applicationId = application.getId();
        this.state = state;
        this.created = clock.instant();
        this.token = tokenGenerator.generate(application.getDefaultTokenLifetime());
    }

    @NotNull
    public String getApplicationId() {
        return applicationId;
    }

    @NotNull
    public Token getToken() {
        return token;
    }

    void setToken(Token token) {
        this.token = token;
    }

    public void refresh() {
        state = state.refresh(this);
    }

    public void persist() {
        state = state.persist(this);
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

    public String getUuid() {
        return uuid;
    }

    public Instant getCreated() {
        return created;
    }
}
