package com.example.demo;

import com.example.demo.actors.Actor;
import com.example.demo.actors.ActorRepository;
import com.example.demo.movies.Movie;
import com.example.demo.movies.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.NestedTestConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.NestedTestConfiguration.EnclosingConfiguration.INHERIT;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@NestedTestConfiguration(INHERIT)
class DemoApplicationTests {

    @Autowired
    EntityManager entityManager;

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:5.7");

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto=update", () -> "create");
    }

    @Nested
    class ActorTests {

        @Autowired
        ActorRepository actorRepository;

        @BeforeEach
        void prepareActors() {
            Query query = entityManager.createQuery("DELETE FROM Actor");
            query.executeUpdate();
            entityManager.persist(actor("Emil Eifrem"));
        }

        @Test
        void loads_actors() {
            List<Actor> actors = actorRepository.findAll();

            assertThat(actors).hasSize(1);
            assertThat(actors.iterator().next().getName()).isEqualTo("Emil Eifrem");
        }

        private Actor actor(String name) {
            Actor actor = new Actor();
            actor.setName(name);
            return actor;
        }
    }

    @Nested
    class MovieTests {

        @Autowired
        MovieRepository movieRepository;

        @BeforeEach
        void prepareMovies() {
            Query query = entityManager.createQuery("DELETE FROM Movie");
            query.executeUpdate();
            entityManager.persist(movie("The Matrix"));
        }

        @Test
        void loads_movies() {
            List<Movie> movies = movieRepository.findAll();

            assertThat(movies).hasSize(1);
            assertThat(movies.iterator().next().getTitle()).isEqualTo("The Matrix");
        }

        private Movie movie(String title) {
            Movie movie = new Movie();
            movie.setTitle(title);
            return movie;
        }
    }
}
