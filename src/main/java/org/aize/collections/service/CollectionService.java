package org.aize.collections.service;

import jakarta.transaction.Transactional;
import org.aize.collections.dto.CollectionDTO;
import org.aize.collections.dto.CollectionRequestDTO;
import org.aize.collections.dto.CollectionResponseDTO;
import org.aize.collections.entity.AizeCollection;
import org.aize.collections.enums.ComponentType;
import org.aize.collections.repository.CollectionRepository;
import org.aize.collections.utility.CollectionUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CollectionService implements ComponentService {

    @Autowired
    private CollectionRepository collectionRepository;

    @Override
    @Transactional
    public void create(CollectionRequestDTO requestDTO) {
        if (requestDTO.getType() != ComponentType.COLLECTION || requestDTO.getCollectionDTOs() == null) {
            throw new IllegalArgumentException("Invalid request data for creating Collection");
        }

        requestDTO.getCollectionDTOs().forEach(dto -> saveCollectionRecursive(dto, null));
    }

    private AizeCollection saveCollectionRecursive(CollectionDTO collectionDTO, AizeCollection parentCollection) {
        // Convert DTO to entity
        AizeCollection collection = mapToCollectionEntity(collectionDTO);
        collection.setParentCollection(parentCollection);

        // Save to get managed entity
        collection = collectionRepository.save(collection);

        // Recursively process sub-collections
        if (collectionDTO.getCollections() != null) {
            AizeCollection finalCollection = collection;
            List<AizeCollection> subCollections = collectionDTO.getCollections().stream()
                    .map(subDTO -> saveCollectionRecursive(subDTO, finalCollection))
                    .collect(Collectors.toList());

            collection.setCollections(subCollections);
        }

        return collectionRepository.save(collection);
    }

    private AizeCollection mapToCollectionEntity(CollectionDTO collectionDTO) {
        AizeCollection collection = new AizeCollection();
        collection.setName(collectionDTO.getName());
        collection.setDescription(collectionDTO.getDescription());
        collection.setCreatedAt(LocalDateTime.now());
        collection.setUpdatedAt(LocalDateTime.now());

        if (collectionDTO.getItems() != null) {
            collection.setItems(collectionDTO.getItems().stream()
                    .map(itemDTO -> CollectionUtility.mapToItemEntity(itemDTO, collection))
                    .collect(Collectors.toList()));
        }

        return collection;
    }

    @Override
    public CollectionResponseDTO update(Long id, CollectionRequestDTO requestDTO) {
        AizeCollection existingCollection = collectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Collection not found with id: " + id));

        updateCollectionEntity(existingCollection, requestDTO.getCollectionDTOs().get(0));

        return CollectionUtility.convertToDTO(collectionRepository.save(existingCollection));
    }

    private void updateCollectionEntity(AizeCollection existingCollection, CollectionDTO collectionDTO) {
        existingCollection.setName(collectionDTO.getName() != null ? collectionDTO.getName() : existingCollection.getName());
        existingCollection.setDescription(collectionDTO.getDescription() != null ? collectionDTO.getDescription() : existingCollection.getDescription());
        existingCollection.setUpdatedAt(LocalDateTime.now());

        if (collectionDTO.getItems() != null) {
            existingCollection.getItems().clear();
            existingCollection.getItems().addAll(collectionDTO.getItems().stream()
                    .map(itemDTO -> CollectionUtility.mapToItemEntity(itemDTO, existingCollection))
                    .toList());
        }

        if (collectionDTO.getCollections() != null) {
            existingCollection.getCollections().clear();
            List<AizeCollection> updatedSubCollections = collectionDTO.getCollections().stream()
                    .map(subDTO -> saveCollectionRecursive(subDTO, existingCollection))
                    .toList();
            existingCollection.getCollections().addAll(updatedSubCollections);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        AizeCollection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Collection not found with id: " + id));

        collectionRepository.delete(collection);
    }

    @Override
    public Optional<?> getById(Long id) {
        return collectionRepository.findById(id)
                .map(CollectionUtility::convertToDTO);
    }
}
