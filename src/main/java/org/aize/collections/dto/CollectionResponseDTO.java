package org.aize.collections.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CollectionResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ItemResponseDTO> items;
    private List<CollectionResponseDTO> collections;
}

