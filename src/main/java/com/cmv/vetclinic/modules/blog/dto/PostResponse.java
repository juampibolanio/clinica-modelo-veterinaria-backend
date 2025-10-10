package com.cmv.vetclinic.modules.blog.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    
    private Long id;
    private String title;
    private String subtitle;
    private String content;
    private String authorName;
    private LocalDateTime publicationDate;
    private List<PostImageResponse> images;
}
