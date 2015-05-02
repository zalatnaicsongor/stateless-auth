package hu.zalatnai.auth;

import hu.zalatnai.auth.domain.ClientState;
import hu.zalatnai.auth.domain.TokenState;
import hu.zalatnai.sdk.service.domain.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class StateRepositoryConfiguration {
    @Autowired
    List<ClientState> clientStateList;

    @Autowired
    List<TokenState> tokenStateList;

    @Bean
    public StateRepository<ClientState> getClientStateRepository() {
        StateRepository<ClientState> stateRepository = new StateRepository<>();
        stateRepository.register(clientStateList);
        return stateRepository;
    }

    @Bean
    public StateRepository<TokenState> getTokenStateRepository() {
        StateRepository<TokenState> stateRepository = new StateRepository<>();
        stateRepository.register(tokenStateList);
        return stateRepository;
    }
}
