package com.cmv.vetclinic.modules.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cmv.vetclinic.modules.blog.model.Post;
import com.cmv.vetclinic.modules.user.model.User;


public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findByAuthor(User author);
}