package hu.zalatnai.auth.domain;

class PersistentClientState extends ClientState {

    PersistentClientState(StateRepository<ClientState> clientStateRepository) {
        super(clientStateRepository);
    }

    @Override
    public int getId() {
        return STATE_PERSISTENT;
    }
}
