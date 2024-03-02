package com.enigma.wmb_api.repo;

import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.model.response.MenuResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepo extends JpaRepository<Menu, String>, JpaSpecificationExecutor<Menu> {
}