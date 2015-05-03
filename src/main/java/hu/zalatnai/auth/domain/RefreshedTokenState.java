package hu.zalatnai.auth.domain;

import hu.zalatnai.sdk.service.SHA256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class RefreshedTokenState extends TokenState {

    @Autowired
    private SHA256 sha256;

    @Override
    public TokenState hash(Token token) {
        token.setAccessToken(sha256.hash(token.getAccessToken()));

        return tokenStateRepository.getById(TokenState.STATE_HASHED);
    }

    @Override
    public int getId() {
        return STATE_REFRESHED;
    }
}
