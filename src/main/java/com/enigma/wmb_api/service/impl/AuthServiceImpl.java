package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.UserRole;
import com.enigma.wmb_api.entity.Role;
import com.enigma.wmb_api.entity.User;
import com.enigma.wmb_api.entity.UserCredential;
import com.enigma.wmb_api.model.request.AuthRequest;
import com.enigma.wmb_api.model.response.LoginResponse;
import com.enigma.wmb_api.model.response.RegisterResponse;
import com.enigma.wmb_api.repo.UserCredentialRepo;
import com.enigma.wmb_api.service.AuthService;
import com.enigma.wmb_api.service.JwtService;
import com.enigma.wmb_api.service.RoleService;
import com.enigma.wmb_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AuthServiceImpl implements AuthService {
    private final UserCredentialRepo credentialRepo;
    private final RoleService roleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Override
    public RegisterResponse resgisterUser(AuthRequest request) throws DataIntegrityViolationException {
        Role role = roleService.getOrCreate(UserRole.ROLE_CUSTOMER);

        UserCredential credential = UserCredential.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(List.of(role))
                .isEnable(true)
                .build();
        credentialRepo.saveAndFlush(credential);

        User user = User.builder()
                .status(true)
                .credential(credential)
                .build();

        userService.create(user);

        List<String> roles = credential.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return RegisterResponse.builder()
                .username(credential.getUsername())
                .roles(roles)
                .build();
    }

    @Override
    public RegisterResponse resgisterAdmin(AuthRequest request) {
        return null;
    }

    @Override
    public LoginResponse login(AuthRequest request) {
        UserCredential credential = credentialRepo.findFirstByUsername(request.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid credential"));
        boolean matches = passwordEncoder.matches(request.getPassword(), credential.getPassword());
        if (matches) {
            List<String> roles = credential.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            return LoginResponse.builder()
                    .username(credential.getUsername())
                    .roles(roles)
                    .token(jwtService.generateToken())
                    .build();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid credentials");
    }

    @Override
    public UserCredential findOrFail(String id) {
        return credentialRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid credential"));
    }
}
