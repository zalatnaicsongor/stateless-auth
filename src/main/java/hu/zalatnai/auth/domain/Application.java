package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.domain.exception.InvalidDefaultTokenLifetimeException;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class Application {
    private String id;

    protected Duration defaultTokenLifetime;

    /**
     * @param id    id
     */
    public Application(@NotNull String id, @NotNull Duration defaultTokenLifetime) {
        Validate.notBlank(id);

        if (defaultTokenLifetime.isNegative()) {
            throw new InvalidDefaultTokenLifetimeException();
        }

        this.id = id;
        this.defaultTokenLifetime = defaultTokenLifetime;
    }

    @NotNull public String getId() {
        return id;
    }

    @NotNull public Client instantiate(ClientFactory clientFactory, String deviceUuid, String deviceName) {
        return clientFactory.create(this, deviceUuid, deviceName);
    }

    @NotNull public Duration getDefaultTokenLifetime() {
        return defaultTokenLifetime;
    }
}
