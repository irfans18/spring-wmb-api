package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.UserCredential;
import com.enigma.wmb_api.repo.UserCredentialRepo;
import com.enigma.wmb_api.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserCredentialServiceImpl implements UserCredentialService {
    private final UserCredentialRepo repo;
    @Transactional(readOnly = true)
    @Override
    public UserCredential findOrFail(String id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,("User not found")));
    }

    @Override
    public UserCredential getByContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return repo.findFirstByUsername(authentication.getPrincipal().toString())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findFirstByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
