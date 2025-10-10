package com.cmv.vetclinic.modules.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cmv.vetclinic.modules.blog.model.PostImage;

public interface PostImageRepository extends JpaRepository<PostImage, Long>{
    
}
