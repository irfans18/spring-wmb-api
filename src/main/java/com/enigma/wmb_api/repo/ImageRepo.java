package com.enigma.wmb_api.repo;

import com.enigma.wmb_api.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ImageRepo extends JpaRepository<Image, String>, JpaSpecificationExecutor<Image> {
}