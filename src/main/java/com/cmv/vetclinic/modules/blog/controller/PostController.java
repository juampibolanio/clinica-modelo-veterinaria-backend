package com.cmv.vetclinic.modules.blog.controller;

import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.cmv.vetclinic.modules.blog.dto.PostRequest;
import com.cmv.vetclinic.modules.blog.dto.PostResponse;
import com.cmv.vetclinic.modules.blog.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> createPost(
            @RequestPart("post") @Validated PostRequest postRequest,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @AuthenticationPrincipal UserDetails userDetails) {

        System.out.println("PostRequest recibido: " + postRequest);
        if (images != null) {
            System.out.println("Cantidad de imágenes: " + images.length);
        }
        PostResponse response = postService.createPost(postRequest, images, userDetails.getUsername());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getAllPosts(
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        LocalDateTime from = fromDate != null ? fromDate.atStartOfDay() : null;
        LocalDateTime to = toDate != null ? toDate.atTime(23, 59, 59) : null;

        Page<PostResponse> posts = postService.getAllPosts(authorId, keyword, from, to, page, size);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        PostResponse post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @RequestPart("post") @Validated PostRequest request,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @AuthenticationPrincipal UserDetails userDetails) {

        PostResponse updated = postService.updatePost(id, request, images, userDetails.getUsername());
        return ResponseEntity.ok(updated);
    }

    // PATCH PARCIAL (solo texto y descripciones)
    @PatchMapping("/{id}")
    public ResponseEntity<PostResponse> patchPost(
            @PathVariable Long id,
            @RequestBody PostRequest partialRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        PostResponse updated = postService.patchPost(id, partialRequest, userDetails.getUsername());
        return ResponseEntity.ok(updated);
    }

    // PUT DE IMÁGENES (agregar / eliminar / modificar)
    @PutMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> updateImages(
            @PathVariable Long id,
            @RequestPart(value = "newImages", required = false) MultipartFile[] newImages,
            @RequestParam(value = "keepImageIds", required = false) List<Long> keepImageIds,
            @RequestParam(value = "descriptions", required = false) List<String> descriptions,
            @AuthenticationPrincipal UserDetails userDetails) {

        System.out.println("🟢 updateImages -> ID=" + id);
        System.out.println("keepImageIds: " + keepImageIds);
        System.out.println("descriptions: " + descriptions);
        System.out.println("newImages: " + (newImages != null ? newImages.length : 0));

        // Evitar nulls
        if (keepImageIds == null)
            keepImageIds = List.of();
        if (descriptions == null)
            descriptions = List.of();
        if (newImages == null)
            newImages = new MultipartFile[0];

        PostResponse updated = postService.updateImages(
                id,
                newImages,
                keepImageIds,
                descriptions,
                userDetails.getUsername());

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        postService.deletePost(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}