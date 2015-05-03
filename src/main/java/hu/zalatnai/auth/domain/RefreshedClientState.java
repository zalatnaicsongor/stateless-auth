package hu.zalatnai.auth.domain;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
class RefreshedClientState extends ClientState {
    @Override
    public int getId() {
        return ClientState.STATE_REFRESHED;
    }

    @NotNull
    @Override
    public ClientState persist(@NotNull Client client) {
        client.getToken().hash();
        return clientStateRepository.getById(STATE_PERSISTENT);
    }
}
