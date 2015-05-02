package hu.zalatnai.sdk.service.domain;

import hu.zalatnai.auth.domain.exception.ClientStateNotFoundException;
import hu.zalatnai.auth.domain.exception.DuplicateClientStateException;
import hu.zalatnai.sdk.service.domain.State;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateRepository<T extends State<T>> {

    private Map<Integer, T> stateMap;

    public StateRepository() {
        stateMap = new HashMap<>();
    }

    public void register(@NotNull List<T> states) {
        for (T state : states) {
            if (stateMap.containsKey(state.getId())) {
               throw new DuplicateClientStateException();
            }
            state.setStateRepository(this);
            stateMap.put(state.getId(), state);
        }
    }

    @NotNull public T getById(int id) {
        if (!stateMap.containsKey(id)) {
            throw new ClientStateNotFoundException();
        }

        return stateMap.get(id);
    }
}
