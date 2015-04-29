package hu.zalatnai.auth.domain;

import hu.zalatnai.sdk.service.SHA256;
import hu.zalatnai.sdk.service.domain.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class TransientTokenState extends TokenState {

    @Autowired
    private SHA256 sha256;

    @Override
    public TokenState hash(Token token) {
        token.setRefreshToken(sha256.hash(token.getRefreshToken()));
        token.setAccessToken(sha256.hash(token.getAccessToken()));

        return (TokenState) tokenStateRepository.getById(TokenState.STATE_PERSISTENT);
    }

    @Override
    public int getId() {
        return STATE_TRANSIENT;
    }
}
