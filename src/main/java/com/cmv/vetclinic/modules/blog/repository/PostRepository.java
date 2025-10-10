package com.cmv.vetclinic.modules.blog.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.cmv.vetclinic.modules.blog.model.Post;
import com.cmv.vetclinic.modules.user.model.User;


public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor{
    List<Post> findByAuthor(User author);

    Page<Post> findByAuthorUsernameContainingAndTitleContainingOrContentContaining(
            String authorUsername, String titleKeyword, String contentKeyword, Pageable pageable);
}