package org.aize.collections.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String externalLink;
    private String jsonData;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
