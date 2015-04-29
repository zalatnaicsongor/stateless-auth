package hu.zalatnai.auth.domain;

class TransientTokenState extends TokenState {
    TransientTokenState(StateRepository<TokenState> tokenStateRepository) {
        super(tokenStateRepository);
    }

    @Override
    public TokenState hash(Token token) {
        return null;
    }

    @Override
    public int getId() {
        return STATE_TRANSIENT;
    }
}
