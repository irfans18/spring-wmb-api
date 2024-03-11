package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.enums.UserRole;
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
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AuthServiceImpl implements AuthService {
    private final UserCredentialRepo credentialRepo;
    private final RoleService roleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${wmb_api.username.superadmin}")
    private String superAdminUsername;
    @Value("${wmb_api.password.superadmin}")
    private String superAdminPassword;


    @Transactional(rollbackFor = Exception.class)
    @PostConstruct // berguna untuk mengeksekusi method yg akan dijalankan pada saat aplikasi pertama kali dijalankan
    public void initSuperAdmin() {
        Optional<UserCredential> currentUser = credentialRepo.findFirstByUsername(superAdminUsername);
        if (currentUser.isPresent()) return;

        Role superAdmin = roleService.getOrCreate(UserRole.ROLE_SUPER_ADMIN);
        Role admin = roleService.getOrCreate(UserRole.ROLE_ADMIN);
        Role customer = roleService.getOrCreate(UserRole.ROLE_CUSTOMER);

        UserCredential account = UserCredential.builder()
                .username(superAdminUsername)
                .password(passwordEncoder.encode(superAdminPassword))
                .role(List.of(superAdmin, admin, customer))
                .isEnable(true)
                .build();

        credentialRepo.save(account);
    }



    private RegisterResponse register(AuthRequest request, Role role) {
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
    public RegisterResponse resgisterUser(AuthRequest request) throws DataIntegrityViolationException {
        Role role = roleService.getOrCreate(UserRole.ROLE_CUSTOMER);

        return register(request, role);
    }

    @Override
    public RegisterResponse resgisterAdmin(AuthRequest request) throws DataIntegrityViolationException {
        Role role = roleService.getOrCreate(UserRole.ROLE_ADMIN);

        return register(request, role);
    }


    @Transactional(readOnly = true)
    @Override
    public LoginResponse login(AuthRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );
        Authentication authenticate = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserCredential credential = (UserCredential) authenticate.getPrincipal();
        String token = jwtService.generateToken(credential);
        return LoginResponse.builder()
                .username(credential.getUsername())
                .roles(credential.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .token(token)
                .build();
    }


    @Override
    public UserCredential findOrFail(String id) {
        return credentialRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid credential"));
    }
}
