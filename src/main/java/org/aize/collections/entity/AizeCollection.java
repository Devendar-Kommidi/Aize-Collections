package org.aize.collections.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "collections")
public class AizeCollection extends Component {

    @OneToMany(mappedBy = "parentCollection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AizeCollection> collections;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_collection_id")
    private AizeCollection parentCollection;

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;
}
