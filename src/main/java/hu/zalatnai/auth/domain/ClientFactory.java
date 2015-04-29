package hu.zalatnai.auth.domain;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
class ClientFactory {

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private TransientClientState transientClientState;

    @Autowired
    private Clock clock;

    public Client create(@NotNull Application application, @NotNull String deviceUuid, @NotNull String deviceName) {
        return new Client(application, deviceUuid, deviceName, clock, transientClientState, tokenGenerator);
    }
}
