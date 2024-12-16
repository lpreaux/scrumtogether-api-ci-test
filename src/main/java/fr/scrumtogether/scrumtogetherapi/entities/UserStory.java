package fr.scrumtogether.scrumtogetherapi.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("USER_STORY")
public class UserStory extends Item {
  @Override
  protected boolean isValidParentType(Item parent) {
    return parent instanceof Feature || parent instanceof Epic;
  }
}