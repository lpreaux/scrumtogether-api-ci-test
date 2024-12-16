package fr.scrumtogether.scrumtogetherapi.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "item_type")
public abstract class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "code", length = 20)
    private String code;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;


    @Setter(AccessLevel.NONE)
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Item parent;

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "parent")
    private Set<Item> childs = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;

    protected void removeChild(Item item) {
        childs.remove(item);
    }

    protected abstract boolean isValidParentType(Item parent);

    public void setParent(Item parent) {
        if (parent != null && !isValidParentType(parent)) {
            throw new IllegalArgumentException("Invalid parent type for " + this.getClass().getSimpleName());
        }
        this.parent = parent;
    }
}