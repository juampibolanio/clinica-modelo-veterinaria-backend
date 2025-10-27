package com.cmv.vetclinic.modules.blog.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cmv.vetclinic.config.cloudinary.CloudinaryService;
import com.cmv.vetclinic.exceptions.PostExceptions.PostNotFoundException;
import com.cmv.vetclinic.exceptions.PostExceptions.UnauthorizedActionException;
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
            String keyword,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by("publicationDate").descending());

        Specification<Post> spec = (Specification<Post>) (root, query, criteriaBuilder) -> null;

        if (authorId != null)
            spec = spec.and(PostSpecification.hasAuthor(authorId));
        if (keyword != null && !keyword.isEmpty())
            spec = spec.and(PostSpecification.textContains(keyword));
        if (fromDate != null && toDate != null)
            spec = spec.and(PostSpecification.dateBetween(fromDate, toDate));

        @SuppressWarnings("unchecked")
        Page<Post> postsPage = postRepository.findAll(spec, pageable);

        List<PostResponse> dtos = postsPage.stream()
                .map(postMapper::toResponse)
                .toList();

        return new PageImpl<>(dtos, pageable, postsPage.getTotalElements());
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
    public PostResponse patchPostUnified(
            Long id,
            PostRequest request,
            MultipartFile[] newImages,
            String username) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new UnauthorizedActionException("Don´t have permission to edit this post");
        }

        if (request == null)
            request = new PostRequest();
        if (request.getKeepImageIds() == null)
            request.setKeepImageIds(Collections.emptyList());
        if (request.getImageDescriptions() == null)
            request.setImageDescriptions(Collections.emptyList());
        if (newImages == null)
            newImages = new MultipartFile[0];

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            post.setTitle(request.getTitle());
        }
        if (request.getSubtitle() != null) {
            post.setSubtitle(request.getSubtitle());
        }
        if (request.getContent() != null && !request.getContent().isBlank()) {
            post.setContent(request.getContent());
        }

        List<Long> keepIds = request.getKeepImageIds();

        List<PostImage> toRemove = post.getImages().stream()
                .filter(img -> !keepIds.contains(img.getId()))
                .toList();

        post.getImages().removeAll(toRemove);

        for (PostImage img : toRemove) {
            try {
                cloudinaryService.deleteFile(img.getPublicId());
            } catch (IOException e) {
                log.warn("Error eliminando imagen de Cloudinary: {}", img.getPublicId());
            }
        }

        List<PostImage> remaining = post.getImages().stream()
                .filter(img -> keepIds.contains(img.getId()))
                .toList();

        for (int i = 0; i < remaining.size() && i < request.getImageDescriptions().size(); i++) {
            remaining.get(i).setDescription(request.getImageDescriptions().get(i));
        }

        for (MultipartFile file : newImages) {
            try {
                var upload = cloudinaryService.uploadFile(file);

                PostImage newImg = PostImage.builder()
                        .url(upload.getUrl())
                        .publicId(upload.getPublicId())
                        .description("") 
                        .post(post)
                        .build();

                post.getImages().add(newImg);

            } catch (IOException e) {
                throw new RuntimeException("Error subiendo imagen a Cloudinary", e);
            }
        }

        Post updated = postRepository.save(post);
        return postMapper.toResponse(updated);
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