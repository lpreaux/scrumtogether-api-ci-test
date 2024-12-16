package fr.scrumtogether.scrumtogetherapi.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("EPIC")
public class Epic extends Item {
    @Override
    protected boolean isValidParentType(Item parent) {
        return false; // Epics can't have parents
    }
}