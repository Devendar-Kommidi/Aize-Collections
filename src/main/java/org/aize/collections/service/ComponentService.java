package org.aize.collections.service;

import org.aize.collections.dto.CollectionRequestDTO;
import org.aize.collections.dto.CollectionResponseDTO;

import java.util.Optional;

public interface ComponentService {
    void create(CollectionRequestDTO requestDTO);
    CollectionResponseDTO update(Long id, CollectionRequestDTO requestDTO);
    void delete(Long id);
    Optional<?> getById(Long id);
}
