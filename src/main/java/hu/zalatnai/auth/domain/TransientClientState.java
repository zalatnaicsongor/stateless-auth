package hu.zalatnai.auth.domain;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class TransientClientState extends ClientState {

    @Override
    public int getId() {
        return STATE_TRANSIENT;
    }

    @NotNull
    @Override
    public ClientState persist(@NotNull Client client) {
        return (ClientState) clientStateRepository.getById(STATE_PERSISTENT);
    }
}
