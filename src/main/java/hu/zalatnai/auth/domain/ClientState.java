package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.domain.exception.OperationInapplicableException;
import hu.zalatnai.sdk.service.domain.State;
import hu.zalatnai.sdk.service.domain.StateRepository;
import org.jetbrains.annotations.NotNull;

abstract public class ClientState implements State<ClientState> {

    public static final int STATE_TRANSIENT = 1;
    public static final int STATE_PERSISTENT = 2;

    protected StateRepository<ClientState> clientStateRepository;

    public void setStateRepository(StateRepository<ClientState> clientStateRepository) {
        this.clientStateRepository = clientStateRepository;
    }

    @NotNull
    public ClientState persist(@NotNull Client client) {
        throw new OperationInapplicableException();
    }

    @NotNull
    public ClientState refreshToken(@NotNull Client client, byte[] refreshToken) {
        throw new OperationInapplicableException();
    }

    @NotNull
    public ClientState blacklist(@NotNull Client client) {
        throw new OperationInapplicableException();
    }

    public byte[] getAccessToken(@NotNull Client client) {
        throw new OperationInapplicableException();

    }

    public byte[] getRefreshToken(@NotNull Client client) {
        throw new OperationInapplicableException();
    }

    abstract public int getId();
}