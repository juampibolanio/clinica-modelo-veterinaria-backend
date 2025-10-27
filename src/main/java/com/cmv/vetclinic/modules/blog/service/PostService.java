package com.cmv.vetclinic.modules.blog.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.cmv.vetclinic.modules.blog.dto.PostRequest;
import com.cmv.vetclinic.modules.blog.dto.PostResponse;

public interface PostService {

    PostResponse createPost(PostRequest request, MultipartFile[] images, String username);

    Page<PostResponse> getAllPosts(Long authorId,
            String keyword,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            int page,
            int size);

    PostResponse getPostById(Long id);

    PostResponse updatePost(Long id, PostRequest request, MultipartFile[] images, String username);

    PostResponse patchPost(Long id, PostRequest request, String username);

    PostResponse updateImages(Long id, MultipartFile[] newImages, List<Long> keepImageIds,
            List<String> descriptions, String username);

    void deletePost(Long id, String username);
}