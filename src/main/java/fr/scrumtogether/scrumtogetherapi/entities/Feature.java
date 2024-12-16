package fr.scrumtogether.scrumtogetherapi.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("FEATURE")
public class Feature extends Item {
  @Override
  protected boolean isValidParentType(Item parent) {
    return parent instanceof Epic;
  }
}