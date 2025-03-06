package org.aize.collections.controller;

import jakarta.validation.Valid;
import org.aize.collections.dto.CollectionRequestDTO;
import org.aize.collections.dto.CollectionResponseDTO;
import org.aize.collections.enums.ComponentType;
import org.aize.collections.factory.ComponentFactory;
import org.aize.collections.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/collections")
public class CollectionController {

    @Autowired
    private ComponentFactory componentFactory;

    @PostMapping("/create")
    public ResponseEntity<String> createCollection(@Valid @RequestBody CollectionRequestDTO requestDTO) {
        ComponentService service = componentFactory.getService(requestDTO.getType());
        service.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Collection created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollectionResponseDTO> updateCollection(
            @PathVariable Long id,
            @Valid @RequestBody CollectionRequestDTO requestDTO) {
        ComponentService service = componentFactory.getService(requestDTO.getType());
        CollectionResponseDTO updatedCollection = service.update(id, requestDTO);
        return ResponseEntity.ok(updatedCollection);
    }

    @DeleteMapping("/{id}/{type}")
    public ResponseEntity<String> deleteCollection(@PathVariable Long id, @PathVariable String type) {
        ComponentService service = componentFactory.getService(ComponentType.valueOf(type));
        service.delete(id);
        return ResponseEntity.ok("Collection deleted successfully");
    }

    @GetMapping("/{id}/{type}")
    public ResponseEntity<CollectionResponseDTO> getCollectionById(@PathVariable Long id, @PathVariable String type) {
        ComponentService service = componentFactory.getService(ComponentType.valueOf(type));
        return service.getById(id)
                .map(collection -> ResponseEntity.ok((CollectionResponseDTO) collection))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Collection not found"));
    }
}
