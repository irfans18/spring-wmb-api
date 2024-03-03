package com.enigma.wmb_api.repo;

import com.enigma.wmb_api.entity.User;
import com.enigma.wmb_api.model.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
}