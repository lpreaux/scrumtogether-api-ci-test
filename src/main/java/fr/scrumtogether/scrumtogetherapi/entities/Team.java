package fr.scrumtogether.scrumtogetherapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ToString.Include
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ToString.Include
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ToString.Include
    @Column(name = "email", length = 100)
    private String email;

    @OneToMany(mappedBy = "team")
    private Set<TeamUser> teamUsers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "team")
    private Set<Project> projects = new LinkedHashSet<>();

}