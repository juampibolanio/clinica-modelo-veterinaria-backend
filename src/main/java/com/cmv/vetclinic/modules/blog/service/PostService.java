package com.cmv.vetclinic.modules.blog.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cmv.vetclinic.modules.blog.dto.PostRequest;
import com.cmv.vetclinic.modules.blog.dto.PostResponse;

public interface PostService {
    
    PostResponse createPost(PostRequest request, MultipartFile[] images, String username);

    List<PostResponse> getAllPosts();

    PostResponse getPostById(Long id);

    PostResponse updatePost(Long id, PostRequest request, MultipartFile[] images, String username);

    void deletePost(Long id, String username);
}
