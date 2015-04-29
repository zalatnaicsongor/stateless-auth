package hu.zalatnai.auth.domain;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
class TransientClientState extends ClientState {

    TransientClientState(StateRepository<ClientState> clientStateRepository) {
        super(clientStateRepository);
    }

    @Override
    public int getId() {
        return STATE_TRANSIENT;
    }

    @NotNull
    @Override
    public ClientState persist(@NotNull Client client) {
        return clientStateRepository.getById(STATE_PERSISTENT);
    }
}