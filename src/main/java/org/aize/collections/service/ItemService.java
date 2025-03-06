package org.aize.collections.service;

import org.aize.collections.dto.CollectionRequestDTO;
import org.aize.collections.dto.CollectionResponseDTO;
import org.aize.collections.dto.ItemDTO;
import org.aize.collections.dto.ItemResponseDTO;
import org.aize.collections.entity.AizeCollection;
import org.aize.collections.entity.Item;
import org.aize.collections.repository.CollectionRepository;
import org.aize.collections.repository.ItemRepository;
import org.aize.collections.utility.CollectionUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService implements ComponentService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Override
    public void create(CollectionRequestDTO requestDTO) {
        List<ItemDTO> itemDTOList = requestDTO.getItemDTOs();
        List<Item> items = new ArrayList<>();
        itemDTOList.forEach(itemDTO -> {
            Item item = new Item();

            // Fetch the corresponding collection using collectionId
            AizeCollection collection = collectionRepository.findById(requestDTO.getCollectionIdForItem())
                    .orElseThrow(() -> new RuntimeException("Collection not found with ID: " + requestDTO.getCollectionIdForItem()));
            item.setCollection(collection);
            item.setCreatedAt(LocalDateTime.now());
            CollectionUtility.convertToItem(itemDTO, item);
            items.add(item);
        });

        itemRepository.saveAll(items);
    }

    @Override
    public CollectionResponseDTO update(Long id, CollectionRequestDTO requestDTO) {
        Item item = itemRepository.findById(id).get();
        if (requestDTO.getItemDTOs() == null || requestDTO.getItemDTOs().isEmpty()) {
            throw new RuntimeException("Item not found with ID: " + id);
        }
        ItemDTO itemDTO = requestDTO.getItemDTOs().get(0);
        CollectionUtility.convertToItem(itemDTO, item);
        return convertToCollectionResponseDTO(Optional.of(CollectionUtility.convertToItemDTO(itemRepository.save(item))));
    }

    private CollectionResponseDTO convertToCollectionResponseDTO(Optional<ItemResponseDTO> itemResponseDTO) {
        CollectionResponseDTO collectionResponseDTO = new CollectionResponseDTO();
        collectionResponseDTO.setItems(List.of(itemResponseDTO.get()));
        return collectionResponseDTO;
    }

    @Override
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public Optional<?> getById(Long id) {
        return Optional.of(convertToCollectionResponseDTO(itemRepository.findById(id)
                .map(CollectionUtility::convertToItemDTO)));
    }
}
