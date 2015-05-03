package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.domain.exception.OperationInapplicableException;
import hu.zalatnai.sdk.service.domain.State;
import hu.zalatnai.sdk.service.domain.StateRepository;
import org.jetbrains.annotations.NotNull;

abstract public class ClientState implements State<ClientState> {

    public static final int STATE_TRANSIENT = 1;
    public static final int STATE_PERSISTENT = 2;
    public static final int STATE_BLACKLISTED = 3;
    public static final int STATE_REFRESHED = 4;

    protected StateRepository<ClientState> clientStateRepository;

    public void setStateRepository(StateRepository<ClientState> clientStateRepository) {
        this.clientStateRepository = clientStateRepository;
    }

    @NotNull
    public ClientState persist(@NotNull Client client) {
        throw new OperationInapplicableException();
    }

    @NotNull
    public ClientState refreshToken(@NotNull Client client, byte[] hashedRefreshToken) {
        throw new OperationInapplicableException();
    }

    @NotNull
    public ClientState blacklist(@NotNull Client client) {
        throw new OperationInapplicableException();
    }

    abstract public int getId();
}