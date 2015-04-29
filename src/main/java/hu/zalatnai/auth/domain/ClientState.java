package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.domain.exception.OperationInapplicableException;
import hu.zalatnai.sdk.service.domain.State;
import org.jetbrains.annotations.NotNull;

abstract public class ClientState implements State {

    protected final StateRepository<ClientState> clientStateRepository;

    ClientState(StateRepository<ClientState> clientStateRepository) {
        this.clientStateRepository = clientStateRepository;
    }

    public static final int STATE_TRANSIENT = 1;
    public static final int STATE_PERSISTENT = 2;

    @NotNull
    public ClientState persist(@NotNull Client client) {
        throw new OperationInapplicableException();
    }

    @NotNull
    public ClientState refresh(@NotNull Client client) {
        throw new OperationInapplicableException();
    }

    abstract public int getId();
}
