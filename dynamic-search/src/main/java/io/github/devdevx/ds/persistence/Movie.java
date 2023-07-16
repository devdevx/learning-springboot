package io.github.devdevx.ds.persistence;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    private Long id;
    private String title;
    private LocalDate releaseDate;
    @ManyToOne
    @JoinColumn(name = "main_genre_id")
    private Genre mainGenre;
    @ManyToMany
    @JoinTable(
            name = "movies_x_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;
}
