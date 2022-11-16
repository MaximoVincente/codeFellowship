package com.VincenteCodeFellowship.codeFellowship.repositories;

import com.VincenteCodeFellowship.codeFellowship.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}

