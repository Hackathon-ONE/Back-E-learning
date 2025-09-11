package learning.platform.service;

import learning.platform.entity.LoginAttempt;
import learning.platform.repository.LoginAttemptRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoginAttemptService {

    private final LoginAttemptRepository repository;
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int BLOCK_MINUTES = 15;

    public LoginAttemptService(LoginAttemptRepository repository) {
        this.repository = repository;
    }

    public void registraIntentoDeLogin(String email, String ipAddress, boolean success){
        LoginAttempt intento = new LoginAttempt();
        intento.setEmail(email);
        intento.setIpAddress(ipAddress);
        intento.setSuccess(success);
        repository.save(intento);
    }

    public Boolean estaBloqueado(String email){
        LocalDateTime tiempo = LocalDateTime.now().minusMinutes(BLOCK_MINUTES);
        List<LoginAttempt> intentos = repository.findByEmailAndAttemptedAtAfter(email, tiempo);

        Long intentosFallidos = intentos.stream().filter(i -> !i.isSuccess()).count();
        return intentosFallidos > MAX_FAILED_ATTEMPTS;
    }
}
