package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.domain.exception.InvalidDefaultTokenLifetimeException;
import org.junit.Test;

import java.time.Duration;

public class ApplicationTest {

    @Test(expected = IllegalArgumentException.class)
    public void itCannotBeConstructedIfTheIdIsNull() {
        new Application(null, Duration.ofDays(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void itCannotBeConstructedIfTheIdIsBlank() {
        new Application("", Duration.ofDays(10));
    }

    @Test(expected = InvalidDefaultTokenLifetimeException.class)
    public void itCannotBeConstructedIfTheExpiryDurationIsNegative() {
        new Application("applicationId", Duration.ofNanos(-1));
    }
}
