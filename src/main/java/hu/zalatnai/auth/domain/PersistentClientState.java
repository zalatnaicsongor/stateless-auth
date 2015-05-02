package hu.zalatnai.auth.domain;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
class PersistentClientState extends ClientState {
    @Override
    public int getId() {
        return STATE_PERSISTENT;
    }

    @NotNull
    @Override
    public ClientState refreshToken(@NotNull Client client, byte[] refreshToken) {
        client.getToken().refresh(refreshToken);
        return clientStateRepository.getById(ClientState.STATE_PERSISTENT);
    }

    @NotNull
    @Override
    public ClientState blacklist(@NotNull Client client) {
        return clientStateRepository.getById(ClientState.STATE_BLACKLISTED);
    }
}
