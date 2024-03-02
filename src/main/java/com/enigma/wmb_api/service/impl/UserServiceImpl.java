package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.User;
import com.enigma.wmb_api.repo.UserRepo;
import com.enigma.wmb_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
    private final UserRepo repo;
    @Override
    public User create(User request) {
        return repo.saveAndFlush(request);
    }

    @Override
    public User update(User request) {
        findOrFail(request.getId());
        return repo.saveAndFlush(request);
    }

    @Override
    public User findOrFail(String id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public void delete(String id) {
        User user = findOrFail(id);
        repo.delete(user);
    }
}
