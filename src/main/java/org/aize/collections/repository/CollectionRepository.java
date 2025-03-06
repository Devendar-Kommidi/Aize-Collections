package org.aize.collections.repository;

import org.aize.collections.entity.AizeCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends JpaRepository<AizeCollection, Long> {
}
