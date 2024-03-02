package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.constant.UserRole;
import com.enigma.enigma_shop.dto.request.AuthRequest;
import com.enigma.enigma_shop.dto.response.LoginResponse;
import com.enigma.enigma_shop.dto.response.RegisterResponse;
import com.enigma.enigma_shop.entity.Customer;
import com.enigma.enigma_shop.entity.Role;
import com.enigma.enigma_shop.entity.UserCredential;
import com.enigma.enigma_shop.repo.UserCredentialRepo;
import com.enigma.enigma_shop.service.AuthService;
import com.enigma.enigma_shop.service.CustomerService;
import com.enigma.enigma_shop.service.JwtService;
import com.enigma.enigma_shop.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserCredentialRepo credentialRepo;
    private final RoleService roleService;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Override
    public RegisterResponse resgisterUser(AuthRequest request) throws DataIntegrityViolationException {
        Role role = roleService.getOrSave(UserRole.ROLE_CUSTOMER);

        UserCredential credential = UserCredential.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(List.of(role))
                .isEnable(true)
                .build();
        credentialRepo.saveAndFlush(credential);

        Customer customer = Customer.builder()
                .status(true)
                .credential(credential)
                .build();

        customerService.create(customer);

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
    public LoginResponse loginAdmin(AuthRequest request) {
        return null;
    }

    @Override
    public UserCredential findOrFail(String id) {
        return credentialRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid credential"));
    }
}
