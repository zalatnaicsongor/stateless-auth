package hu.zalatnai.auth.domain;

import hu.zalatnai.sdk.service.UUIDGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class TransientClientState extends ClientState {

    @Autowired
    UUIDGenerator uuidGenerator;

    @Override
    public int getId() {
        return STATE_TRANSIENT;
    }

    @NotNull
    @Override
    public ClientState persist(@NotNull Client client) {
        client.assignClientUUID(uuidGenerator.generateV4());
        client.getToken().hash();
        return clientStateRepository.getById(STATE_PERSISTENT);
    }

    @NotNull
    public byte[] getAccessToken(@NotNull Client client) {
        return client.getToken().getAccessToken();
    }

    @NotNull
    public byte[] getRefreshToken(@NotNull Client client) {
        return client.getToken().getRefreshToken();
    }
}
