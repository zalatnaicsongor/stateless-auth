package hu.zalatnai.auth;

import hu.zalatnai.auth.domain.ApplicationRepository;
import hu.zalatnai.auth.domain.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    ClientRepository clientRepository;
}
