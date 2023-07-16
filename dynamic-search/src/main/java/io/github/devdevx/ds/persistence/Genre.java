package io.github.devdevx.ds.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "genres")
public class Genre {
    @Id
    private Long id;
    private String name;
}
