delete from movies_x_genres;
delete from movies;
delete from genres;

INSERT INTO genres(id, name) VALUES (1, 'Action');
INSERT INTO genres(id, name) VALUES (2, 'Comedy');
INSERT INTO genres(id, name) VALUES (3, 'Drama');
INSERT INTO genres(id, name) VALUES (4, 'Horror');
INSERT INTO genres(id, name) VALUES (5, 'Sci-Fi');

-- Insert movie data
INSERT INTO movies(id, title, release_date, main_genre_id) VALUES (1, 'The Dark Knight', '2008-07-18', 1);
INSERT INTO movies_x_genres(movie_id, genre_id) VALUES (1, 1);
INSERT INTO movies_x_genres(movie_id, genre_id) VALUES (1, 3);

INSERT INTO movies(id, title, release_date, main_genre_id) VALUES (2, 'Pulp Fiction', '1994-10-14', 3);
INSERT INTO movies_x_genres(movie_id, genre_id) VALUES (2, 2);
INSERT INTO movies_x_genres(movie_id, genre_id) VALUES (2, 3);

INSERT INTO movies(id, title, release_date, main_genre_id) VALUES (3, 'The Shawshank Redemption', '1994-09-23', 3);
INSERT INTO movies_x_genres(movie_id, genre_id) VALUES (3, 3);

INSERT INTO movies(id, title, release_date, main_genre_id) VALUES (4, 'Get Out', '2017-02-24', 4);
INSERT INTO movies_x_genres(movie_id, genre_id) VALUES (4, 4);

INSERT INTO movies(id, title, release_date, main_genre_id) VALUES (5, 'Interstellar', '2014-11-07', 5);
INSERT INTO movies_x_genres(movie_id, genre_id) VALUES (5, 1);
INSERT INTO movies_x_genres(movie_id, genre_id) VALUES (5, 5);

INSERT INTO movies(id, title, release_date, main_genre_id) VALUES (6, 'The Hangover', null, 2);
INSERT INTO movies_x_genres(movie_id, genre_id) VALUES (6, 2);
