package org.aize.collections.factory;

import org.aize.collections.enums.ComponentType;
import org.aize.collections.service.CollectionService;
import org.aize.collections.service.ComponentService;
import org.aize.collections.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ComponentFactory {
    private final Map<ComponentType, ComponentService> serviceMap;

    public ComponentFactory(CollectionService collectionService, ItemService itemService) {
        serviceMap = Map.of(
                ComponentType.COLLECTION, collectionService,
                ComponentType.ITEM, itemService
        );
    }

    public ComponentService getService(ComponentType type) {
        return serviceMap.getOrDefault(type, null);
    }
}
