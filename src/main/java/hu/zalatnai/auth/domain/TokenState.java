package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.domain.exception.OperationInapplicableException;
import hu.zalatnai.sdk.service.domain.State;

public abstract class TokenState implements State {

    public static final int STATE_TRANSIENT = 1;
    public static final int STATE_PERSISTENT = 2;
    public static final int STATE_BLACKLISTED = 3;

    private final StateRepository<TokenState> tokenStateRepository;

    TokenState(StateRepository<TokenState> tokenStateRepository) {
        this.tokenStateRepository = tokenStateRepository;
    }

    public TokenState hash(Token token) {
        throw new OperationInapplicableException();
    }

    public TokenState blacklist(Token token) {
        throw new OperationInapplicableException();
    }
}
