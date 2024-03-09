package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.entity.User;
import com.enigma.wmb_api.entity.UserCredential;
import com.enigma.wmb_api.model.request.update.UserUpdateRequest;
import com.enigma.wmb_api.model.request.update.statusUpdateRequest;
import com.enigma.wmb_api.model.request.UserRequest;
import com.enigma.wmb_api.model.response.UserResponse;
import com.enigma.wmb_api.repo.UserRepo;
import com.enigma.wmb_api.service.UserCredentialService;
import com.enigma.wmb_api.service.UserService;
import com.enigma.wmb_api.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
    private final UserRepo repo;
    private final UserCredentialService credentialService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public User create(User request) {
        return repo.saveAndFlush(request);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserResponse updateStatusById(statusUpdateRequest request) {
        UserCredential credential = credentialService.getByContext();

        if (!credential.getId().equals(request.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ResponseMessage.ERROR_FORBIDDEN);
        }

        User user = findOrFail(request.getUserId());
        user.setStatus(request.getStatus());
        return mapToResponse(repo.saveAndFlush(user));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserResponse update(UserUpdateRequest request) {
        User user = findOrFail(request.getId());

        user.setName(request.getName());
        user.setPhoneNumber(request.getPhoneNumber());
        repo.saveAndFlush(user);

        return mapToResponse(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User findOrFail(String id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_USER_NOT_FOUND));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        User user = findOrFail(id);
        repo.delete(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserResponse> findAll(UserRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Sort sortBy = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageRequest = PageRequest.of(request.getPage() - 1, request.getSize(), sortBy);
        Specification<User> specification = UserSpecification.getSpecification(request);
        Page<User> userPage = repo.findAll(specification, pageRequest);
        return new PageImpl<>(
                userPage.getContent().stream()
                        .map(this::mapToResponse)
                        .toList(),
                userPage.getPageable(),
                userPage.getTotalElements()
        );
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse
                .builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus())
                .credentialId(user.getCredential() == null ? null : user.getCredential().getId())
                .build();
    }

}
