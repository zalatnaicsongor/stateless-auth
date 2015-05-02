package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.domain.exception.OperationInapplicableException;
import hu.zalatnai.sdk.service.domain.State;
import hu.zalatnai.sdk.service.domain.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class TokenState implements State<TokenState> {

    public static final int STATE_UNHASHED = 1;
    public static final int STATE_HASHED = 2;
    public static final int STATE_BLACKLISTED = 3;

    protected StateRepository<TokenState> tokenStateRepository;

    @Override
    public void setStateRepository(StateRepository<TokenState> tokenStateRepository) {
        this.tokenStateRepository = tokenStateRepository;
    }

    public TokenState hash(Token token) {
        throw new OperationInapplicableException();
    }

    public TokenState refresh(Token token, byte[] hashedRefreshToken) {
        throw new OperationInapplicableException();
    }
}
