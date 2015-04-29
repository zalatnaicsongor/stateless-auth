package hu.zalatnai.auth.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class PersistentClientState extends ClientState {
    @Override
    public int getId() {
        return STATE_PERSISTENT;
    }
}