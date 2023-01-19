package co.id.dansmultipro.test.service;

import co.id.dansmultipro.test.entity.AppUserDetail;
import co.id.dansmultipro.test.entity.User;
import co.id.dansmultipro.test.model.request.AuthRequest;
import co.id.dansmultipro.test.model.response.AuthResponse;
import co.id.dansmultipro.test.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class AuthService {

    private UserRepository userRepository;
    private AppJwtTokenService appJwtTokenService;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "incorrect username or password");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = new AppUserDetail(user);

        return AuthResponse
                .builder()
                .token(appJwtTokenService.generateAccessToken(request.getUsername(), userDetails))
                .build();
    }
}
