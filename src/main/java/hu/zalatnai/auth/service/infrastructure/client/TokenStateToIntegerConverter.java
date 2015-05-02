package hu.zalatnai.auth.service.infrastructure.client;

import hu.zalatnai.auth.domain.ClientState;
import hu.zalatnai.auth.domain.TokenState;
import org.springframework.stereotype.Service;

import javax.persistence.AttributeConverter;

// See Bug HHH-8854
@Service
public class TokenStateToIntegerConverter extends StateToIntegerConverter<TokenState> implements AttributeConverter<TokenState, Integer> {
    @Override
    public TokenState convertToEntityAttribute(Integer integer) {
        return null;
    }
}
