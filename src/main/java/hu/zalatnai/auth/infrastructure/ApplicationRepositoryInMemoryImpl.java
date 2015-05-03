package hu.zalatnai.auth.infrastructure;

import hu.zalatnai.auth.domain.Application;
import hu.zalatnai.auth.domain.ApplicationRepository;
import hu.zalatnai.auth.domain.exception.ApplicationNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApplicationRepositoryInMemoryImpl implements ApplicationRepository {

    Map<String, Application> applicationMap;

    public ApplicationRepositoryInMemoryImpl() {
        applicationMap = new HashMap<>();

        applicationMap.put("8vW+TABgk4Vy7sPIjeb5LVvntKb7+I2nwgsDXBDhtnc=",
                new Application("8vW+TABgk4Vy7sPIjeb5LVvntKb7+I2nwgsDXBDhtnc=", Duration.ofMinutes(
                        15)));

        applicationMap.put("bLIxrCpaMd/uLo7KQH+8TSUSSx8xIBtKcFND7GRK1iE=",
                new Application("bLIxrCpaMd/uLo7KQH+8TSUSSx8xIBtKcFND7GRK1iE=", Duration.ofMinutes(
                        15)));

        applicationMap.put("MAFvOlz8lB27KGU8vSXUcjBnQq61izacVOuIC2b1t48=",
                new Application("MAFvOlz8lB27KGU8vSXUcjBnQq61izacVOuIC2b1t48=", Duration.ofMinutes(
                        15)));
    }

    @Override
    public Application getById(String id) {
        if (applicationMap.containsKey(id)) {
            return applicationMap.get(id);
        }

        throw new ApplicationNotFoundException();
    }
}
