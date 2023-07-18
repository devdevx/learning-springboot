package io.github.devdevx.ds.spec;

import io.github.devdevx.ds.persistence.Genre_;
import io.github.devdevx.ds.persistence.Movie;
import io.github.devdevx.ds.persistence.MovieRepository;
import io.github.devdevx.ds.persistence.Movie_;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.data.jpa.domain.Specification.where;

@SpringBootTest
@Sql(scripts = "classpath:scenario.sql")
class SearchSpecificationTest {

    @Autowired
    MovieRepository movieRepository;

    @Test
    void nested_list_property_filter() {
        var spec = new SearchSpecificationBuilder<Movie>()
                .like(Movie_.GENRES + "." + Genre_.NAME, "rror")
                .build();

        var result = movieRepository.findAll(where(spec));
        assertEquals(1, result.size());
        assertEquals("Get Out", result.get(0).getTitle());
    }

    @Test
    void no_filer() {
        var spec = new SearchSpecificationBuilder<Movie>()
                .build();

        var result = movieRepository.findAll(where(spec));
        assertEquals(6, result.size());
    }

    @Test
    void paged_and_sorted() {
        var spec = new SearchSpecificationBuilder<Movie>()
                .build();

        var sort = Sort.by(Sort.Direction.DESC, Movie_.MAIN_GENRE + "." + Genre_.NAME, Movie_.ID);
        var page = PageRequest.of(0, 2, sort);

        var result = movieRepository.findAll(where(spec), page).toList();
        assertEquals(2, result.size());
        assertEquals("Interstellar", result.get(0).getTitle());
        assertEquals("Get Out", result.get(1).getTitle());
    }

    @Test
    void filter_greater_than() {
        var spec = new SearchSpecificationBuilder<Movie>()
                .greaterThan(Movie_.ID, 5)
                .build();

        var result = movieRepository.findAll(where(spec));
        assertEquals(1, result.size());
        assertEquals("The Hangover", result.get(0).getTitle());
    }

    @Test
    void filter_greater_than_or_equal() {
        var spec = new SearchSpecificationBuilder<Movie>()
                .greaterThanOrEqual(Movie_.ID, 5)
                .build();

        var result = movieRepository.findAll(where(spec));
        assertEquals(2, result.size());
        assertEquals("Interstellar", result.get(0).getTitle());
        assertEquals("The Hangover", result.get(1).getTitle());
    }

    @Test
    void filter_in() {
        var spec = new SearchSpecificationBuilder<Movie>()
                .in(Movie_.ID, List.of(5, 6))
                .build();

        var result = movieRepository.findAll(where(spec));
        assertEquals(2, result.size());
        assertEquals("Interstellar", result.get(0).getTitle());
        assertEquals("The Hangover", result.get(1).getTitle());
    }

    @Test
    void filter_is_null() {
        var spec = new SearchSpecificationBuilder<Movie>()
                .isNull(Movie_.RELEASE_DATE)
                .build();

        var result = movieRepository.findAll(where(spec));
        assertEquals(1, result.size());
        assertEquals("The Hangover", result.get(0).getTitle());
    }

    @Test
    void filter_equals() {
        var spec = new SearchSpecificationBuilder<Movie>()
                .equal(Movie_.TITLE, "Interstellar")
                .build();

        var result = movieRepository.findAll(where(spec));
        assertEquals(1, result.size());
        assertEquals("Interstellar", result.get(0).getTitle());
    }

    @Test
    void filter_less_than() {
        var spec = new SearchSpecificationBuilder<Movie>()
                .lessThan(Movie_.RELEASE_DATE, LocalDate.of(1994, 10, 14))
                .build();

        var result = movieRepository.findAll(where(spec));
        assertEquals(1, result.size());
        assertEquals("The Shawshank Redemption", result.get(0).getTitle());
    }

    @Test
    void filter_less_than_or_equal() {
        var spec = new SearchSpecificationBuilder<Movie>()
                .lessThanOrEqual(Movie_.RELEASE_DATE, LocalDate.of(1994, 10, 14))
                .build();

        var result = movieRepository.findAll(where(spec));
        assertEquals(2, result.size());
        assertEquals("Pulp Fiction", result.get(0).getTitle());
        assertEquals("The Shawshank Redemption", result.get(1).getTitle());
    }

    @Test
    void wrong_property_filter() {
        var ex = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            var spec = new SearchSpecificationBuilder<Movie>()
                    .like("wrong", "rror")
                    .build();

            movieRepository.findAll(where(spec));
        });
        var field = ex.getMessage().split("'")[1];
        assertEquals("wrong", field);
    }

    @Test
    void wrong_nested_property_filter() {
        var ex = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            var spec = new SearchSpecificationBuilder<Movie>()
                    .like("mainGenre.wrong", "rror")
                    .build();

            movieRepository.findAll(where(spec));
        });
        var field = ex.getMessage().split("'")[1];
        assertEquals("wrong", field);
    }

    @Test
    void wrong_all_path_property_filter() {
        var ex = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            var spec = new SearchSpecificationBuilder<Movie>()
                    .like("wrong.subwrong", "rror")
                    .build();

            movieRepository.findAll(where(spec));
        });
        var field = ex.getMessage().split("'")[1];
        assertEquals("wrong", field);
    }

    @Test
    void single_type_conversion() {
        var spec = new SearchSpecificationBuilder<Movie>()
                .lessThan(Movie_.RELEASE_DATE, "1994-10-14")
                .build();

        var result = movieRepository.findAll(where(spec));
        assertEquals(1, result.size());
        assertEquals("The Shawshank Redemption", result.get(0).getTitle());
    }

    @Test
    void list_type_conversion() {
        var spec = new SearchSpecificationBuilder<Movie>()
                .in(Movie_.RELEASE_DATE, List.of("2014-11-07", "2017-02-24"))
                .build();

        var result = movieRepository.findAll(where(spec));
        assertEquals(2, result.size());
        assertEquals("Get Out", result.get(0).getTitle());
        assertEquals("Interstellar", result.get(1).getTitle());
    }
}
