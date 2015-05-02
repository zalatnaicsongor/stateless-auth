package hu.zalatnai.auth.domain;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class ApplicationInstantiator {

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private TransientClientState transientClientState;

    @Autowired
    private Clock clock;

    public Client create(@NotNull Application application, @NotNull String deviceUuid, @NotNull String deviceName) {
        Validate.notBlank(deviceUuid);
        Validate.notBlank(deviceName);

        return new Client(application, deviceUuid, deviceName, clock, transientClientState, tokenGenerator);
    }
}
