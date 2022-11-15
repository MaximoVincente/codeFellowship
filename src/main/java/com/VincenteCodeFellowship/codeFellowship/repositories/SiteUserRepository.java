package com.VincenteCodeFellowship.codeFellowship.repositories;

import com.VincenteCodeFellowship.codeFellowship.models.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteUserRepository extends JpaRepository<SiteUser, Long> {

    public SiteUser findByUsername(String username);
}
