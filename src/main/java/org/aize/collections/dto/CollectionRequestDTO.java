package org.aize.collections.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.aize.collections.enums.ComponentType;

import java.util.List;

@Getter
@Setter
public class CollectionRequestDTO {
    @NotNull(message = "Entity type must be specified")
    private ComponentType type;
    private Long collectionIdForItem;
    private List<CollectionDTO> collectionDTOs;
    private List<ItemDTO> itemDTOs;
}
