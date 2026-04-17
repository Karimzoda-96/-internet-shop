package my.shop.azhar.service;

import lombok.RequiredArgsConstructor;
import my.shop.azhar.dto.AuthUserResponse;
import my.shop.azhar.dto.LoginRequest;
import my.shop.azhar.dto.LoginResponse;
import my.shop.azhar.entity.User;
import my.shop.azhar.exception.NotFoundException;
import my.shop.azhar.repository.UserRepository;
import my.shop.azhar.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException("User not found"));
        return new LoginResponse(jwtService.generateToken(user), "Bearer", toAuthUser(user));
    }

    public AuthUserResponse me(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return toAuthUser(user);
    }

    private AuthUserResponse toAuthUser(User user) {
        return new AuthUserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole());
    }
}
