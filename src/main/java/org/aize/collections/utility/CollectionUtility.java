package org.aize.collections.utility;

import org.aize.collections.dto.CollectionResponseDTO;
import org.aize.collections.dto.ItemDTO;
import org.aize.collections.dto.ItemResponseDTO;
import org.aize.collections.entity.AizeCollection;
import org.aize.collections.entity.Item;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class CollectionUtility {
    public static CollectionResponseDTO convertToDTO(AizeCollection collection) {
        CollectionResponseDTO dto = new CollectionResponseDTO();
        dto.setId(collection.getId());
        dto.setName(collection.getName());
        dto.setDescription(collection.getDescription());
        dto.setCreatedAt(collection.getCreatedAt());
        dto.setUpdatedAt(collection.getUpdatedAt());

        if (collection.getItems() != null) {
            dto.setItems(collection.getItems().stream()
                    .map(CollectionUtility::convertToItemDTO)
                    .collect(Collectors.toList()));
        }

        if (collection.getCollections() != null) {
            dto.setCollections(collection.getCollections().stream()
                    .map(CollectionUtility::convertToDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public static ItemResponseDTO convertToItemDTO(Item item) {
        ItemResponseDTO dto = new ItemResponseDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setExternalLink(item.getExternalLink());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());
        dto.setJsonData(item.getJsonData());
        return dto;
    }

    public static Item mapToItemEntity(ItemDTO itemDTO, AizeCollection collection) {
        Item item = new Item();
        convertToItem(itemDTO, item);
        item.setCollection(collection);
        return item;
    }

    public static void convertToItem(ItemDTO itemDTO, Item item) {
        item.setName(itemDTO.getName() != null ? itemDTO.getName() : item.getName());
        item.setDescription(itemDTO.getDescription() != null ? itemDTO.getDescription() : item.getDescription());
        item.setExternalLink(itemDTO.getExternalLink() != null ? itemDTO.getExternalLink() : item.getExternalLink());
        item.setJsonData(itemDTO.getJsonData() != null ? itemDTO.getJsonData() : item.getJsonData());
        item.setCreatedAt(item.getCreatedAt() != null ? item.getCreatedAt() : LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
    }
}
