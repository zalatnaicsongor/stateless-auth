package hu.zalatnai.auth.service.infrastructure.client;

import hu.zalatnai.auth.domain.ClientState;
import org.springframework.stereotype.Service;

import javax.persistence.AttributeConverter;

// See Bug HHH-8854
@Service
public class ClientStateToIntegerConverter extends StateToIntegerConverter<ClientState> implements AttributeConverter<ClientState, Integer> {
    @Override
    public ClientState convertToEntityAttribute(Integer integer) {
        return null;
    }
}
