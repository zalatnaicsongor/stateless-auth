package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.domain.exception.ClientStateNotFoundException;
import hu.zalatnai.auth.domain.exception.DuplicateClientStateException;
import hu.zalatnai.sdk.service.domain.State;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
class StateRepository<T extends State> {

    private Map<Integer, T> stateMap;

    StateRepository() {
        stateMap = new HashMap<>();
    }

    public void register(@NotNull List<T> stateList) {
        for (T state : stateList) {
            if (stateMap.containsKey(state.getId())) {
                throw new DuplicateClientStateException();
            }

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
