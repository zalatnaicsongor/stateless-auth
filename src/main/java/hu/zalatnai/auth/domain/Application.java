package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.domain.exception.InvalidDefaultTokenLifetimeException;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

import java.time.Duration;

public class Application {
    private String id;

    protected Duration defaultTokenLifetime;

    /**
     * @param id    id
     */
    public Application(@NotNull String id, @NotNull Duration defaultTokenLifetime) {
        Assert.hasText(id);

        if (defaultTokenLifetime.isNegative()) {
            throw new InvalidDefaultTokenLifetimeException();
        }

        this.id = id;
        this.defaultTokenLifetime = defaultTokenLifetime;
    }

    @NotNull public String getId() {
        return id;
    }

    @NotNull public Duration getDefaultTokenLifetime() {
        return defaultTokenLifetime;
    }
}
