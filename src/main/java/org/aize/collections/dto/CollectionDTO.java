package org.aize.collections.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionDTO extends ComponentDTO {
    private List<ItemDTO> items;
    private List<CollectionDTO> collections;
}
