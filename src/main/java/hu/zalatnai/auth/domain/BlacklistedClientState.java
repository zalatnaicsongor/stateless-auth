package hu.zalatnai.auth.domain;

import org.springframework.stereotype.Service;

@Service
public class BlacklistedClientState extends ClientState {
    @Override
    public int getId() {
        return ClientState.STATE_BLACKLISTED;
    }
}
