package com.cmv.vetclinic.modules.blog.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cmv.vetclinic.config.cloudinary.CloudinaryService;
import com.cmv.vetclinic.exceptions.PostExceptions.PostNotFoundException;
import com.cmv.vetclinic.exceptions.UserExceptions.UserNotFoundException;
import com.cmv.vetclinic.modules.blog.dto.CloudinaryUploadResult;
import com.cmv.vetclinic.modules.blog.dto.PostRequest;
import com.cmv.vetclinic.modules.blog.dto.PostResponse;
import com.cmv.vetclinic.modules.blog.mapper.PostImageMapper;
import com.cmv.vetclinic.modules.blog.mapper.PostMapper;
import com.cmv.vetclinic.modules.blog.model.Post;
import com.cmv.vetclinic.modules.blog.model.PostImage;
import com.cmv.vetclinic.modules.blog.repository.PostRepository;
import com.cmv.vetclinic.modules.user.model.User;
import com.cmv.vetclinic.modules.user.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private static final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final PostImageMapper postImageMapper;
    private final CloudinaryService cloudinaryService;
    private final com.cmv.vetclinic.modules.blog.repository.PostImageRepository postImageRepository;

    @Override
    @Transactional
    public PostResponse createPost(PostRequest request, MultipartFile[] images, String username) {
        User author = userRepository.findByUsername(username);
        if (author == null) {
            throw new UserNotFoundException(username);
        }

        List<CloudinaryUploadResult> uploads = new ArrayList<>();

        try {
            if (images != null) {
                for (MultipartFile file : images) {
                    uploads.add(cloudinaryService.uploadFile(file));
                }
            }

            Post post = postMapper.toEntity(request);
            post.setAuthor(author);

            List<PostImage> imageEntities = new ArrayList<>();
            if (!uploads.isEmpty()) {
                List<String> descriptions = request.getImageDescriptions() == null
                        ? Collections.emptyList()
                        : request.getImageDescriptions();

                for (int i = 0; i < uploads.size(); i++) {
                    CloudinaryUploadResult upload = uploads.get(i);
                    String desc = (i < descriptions.size()) ? descriptions.get(i) : null;

                    imageEntities.add(PostImage.builder()
                            .post(post)
                            .url(upload.getUrl())
                            .publicId(upload.getPublicId())
                            .description(desc)
                            .build());
                }

                post.setImages(imageEntities);
            }

            Post saved = postRepository.save(post);
            return postMapper.toResponse(saved);

        } catch (Exception e) {
            log.error("Error creating post: {}", e.getMessage());

            for (CloudinaryUploadResult upload : uploads) {
                try {
                    cloudinaryService.deleteFile(upload.getPublicId());
                } catch (IOException ex) {
                    log.warn("Failed to delete image {} after error: {}", upload.getPublicId(), ex.getMessage());
                }
            }
            throw new RuntimeException("Error creating post: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getAllPosts(
            Long authorId,
            String status,
            String keyword,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            int page,
            int size) {
        Pageable pageable = PageRequest.of(page, size,
                org.springframework.data.domain.Sort.by("publicationDate").descending());
        Specification<Post> spec = (Specification<Post>) (root, query, criteriaBuilder) -> null;

        if (authorId != null)
            spec = spec.and(PostSpecification.hasAuthor(authorId));
        if (keyword != null && !keyword.isEmpty())
            spec = spec.and(PostSpecification.textContains(keyword));
        if (fromDate != null && toDate != null)
            spec = spec.and(PostSpecification.dateBetween(fromDate, toDate));

        @SuppressWarnings("unchecked")
        Page<Post> postsPage = postRepository.findAll(spec, pageable);
        return postsPage.map(postMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        return postMapper.toResponse(post);
    }

    @Override
    @Transactional
    public PostResponse updatePost(Long id, PostRequest request, MultipartFile[] newImages, String username) {
        Post existing = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        existing.setTitle(request.getTitle());
        existing.setSubtitle(request.getSubtitle());
        existing.setContent(request.getContent());

        List<CloudinaryUploadResult> uploads = new ArrayList<>();

        try {

            if (newImages != null && newImages.length > 0) {

                for (PostImage oldImg : existing.getImages()) {
                    try {
                        cloudinaryService.deleteFile(oldImg.getPublicId());
                    } catch (IOException e) {
                        log.warn("Could not delete old image: {}", oldImg.getPublicId());
                    }
                }

                existing.getImages().clear();

                for (MultipartFile file : newImages) {
                    uploads.add(cloudinaryService.uploadFile(file));
                }

                List<String> descriptions = request.getImageDescriptions() == null
                        ? Collections.emptyList()
                        : request.getImageDescriptions();

                List<PostImage> newImageEntities = new ArrayList<>();

                for (int i = 0; i < uploads.size(); i++) {
                    CloudinaryUploadResult upload = uploads.get(i);
                    String desc = (i < descriptions.size()) ? descriptions.get(i) : null;

                    newImageEntities.add(PostImage.builder()
                            .post(existing)
                            .url(upload.getUrl())
                            .publicId(upload.getPublicId())
                            .description(desc)
                            .build());
                }

                existing.getImages().addAll(newImageEntities);
            }

            Post updated = postRepository.save(existing);
            return postMapper.toResponse(updated);

        } catch (Exception e) {

            for (CloudinaryUploadResult upload : uploads) {
                try {
                    cloudinaryService.deleteFile(upload.getPublicId());
                } catch (IOException ex) {
                    log.warn("Error cleaning up image: {}", upload.getPublicId());
                }
            }
            throw new RuntimeException("Error updating post: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deletePost(Long id, String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        for (PostImage img : post.getImages()) {
            try {
                cloudinaryService.deleteFile(img.getPublicId());
            } catch (IOException e) {
                log.warn("No se pudo eliminar imagen de Cloudinary: {}", img.getPublicId());
            }
        }

        postRepository.delete(post);
    }
}