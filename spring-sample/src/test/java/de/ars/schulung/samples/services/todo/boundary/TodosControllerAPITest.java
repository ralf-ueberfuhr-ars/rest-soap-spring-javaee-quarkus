package de.ars.schulung.samples.services.todo.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ars.schulung.samples.services.todo.TodosApplicationTest;
import de.ars.schulung.samples.services.todo.boundary.TodoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integrationstests der Todos REST API auf HTTP Layer. Die Anwendung wird als Black Box behandelt.
 * Somit müssen alle Tests, die einen existierenden Datensatz benötigen, diesen im Test anlegen.
 * Spring Boot startet den sog. "ApplicationContext" automatisch und bietet Möglichkeiten, die Anwendungsteile vom Test aus aufzurufen.
 */
@TodosApplicationTest
@AutoConfigureMockMvc
public class TodosControllerAPITest {

    private static final String DEFAULT_MEDIA_TYPE = MediaType.APPLICATION_JSON_VALUE;
    private static final String BASEURL = "/api/v1/todos"; // URL to Resource

    @Autowired
    MockMvc mvc; // testing by sending HTTP requests and verifying HTTP responses
    @Autowired
    ObjectMapper mapper; // used to render or parse JSON

    /*
     * Testfall:
     *  - GET auf alle Todos --> 200 OK mit JSON
     */
    @DisplayName("GET auf alle Daten (200 OK)")
    @Test
    void testFindAllTodos() throws Exception {
        mvc //
                .perform(get(BASEURL).accept(DEFAULT_MEDIA_TYPE)) //
                .andExpect(status().isOk()) //
                .andExpect(content().contentType(DEFAULT_MEDIA_TYPE));
    }

    /*
     * Testfall:
     *  - Anlegen eines Todos per POST -> 201 mit Location Header
     */
    @DisplayName("POST liefert 201 mit Location-Header")
    @Test
    void testCreateTodo() throws Exception {
        final String json = "{\"title\":\"test-todo\"}"; // no mapper, just send the required field
        mvc //
                .perform(post(BASEURL).contentType(DEFAULT_MEDIA_TYPE).content(json)) //
                .andExpect(status().isCreated()) //
                .andExpect(header().exists(HttpHeaders.LOCATION));
    }

    /*
     * Testfall:
     *  - Anlegen eines Todos per POST mit ID -> 400
     */
    @DisplayName("POST erzeugt kein Todo, wenn ID bereits vergeben")
    @Test
    void testCreateTodoWithID() throws Exception {
        final TodoDto newTodo = new TodoDto(1L, "test-todo");
        final String json = mapper.writeValueAsString(newTodo);
        mvc //
                .perform(post(BASEURL).contentType(DEFAULT_MEDIA_TYPE).content(json)) //
                .andExpect(status().isBadRequest());
    }

    /*
     * Testfall:
     *  - Anlegen eines Todos per POST ohne Titel -> 400
     */
    @DisplayName("POST erzeugt kein Todo, wenn kein Titel angegeben ist")
    @Test
    void testCreateTodoWithoutTitle() throws Exception {
        final TodoDto newTodo = new TodoDto();
        final String json = mapper.writeValueAsString(newTodo);
        this.mvc //
                .perform(post(BASEURL).contentType(DEFAULT_MEDIA_TYPE).content(json)) //
                .andExpect(status().isBadRequest());
    }

    /*
     * Testfall:
     *  - Anlegen eines Todos per POST mit leerem Titel -> 400
     */
    @DisplayName("POST erzeugt kein Todo, wenn Titel weniger als 1 Zeichen hat")
    @Test
    void testCreateTodoWithEmptyTitle() throws Exception {
        final TodoDto newTodo = new TodoDto(null, "");
        final String json = mapper.writeValueAsString(newTodo);
        this.mvc //
                .perform(post(BASEURL).contentType(DEFAULT_MEDIA_TYPE).content(json)) //
                .andExpect(status().isBadRequest());
    }

    @Nested
    @DisplayName("Tests that need an existing todo")
    class TestsThatNeedExistingTodo {

        private TodoDto existing;
        private String location;

        @BeforeEach
        void setup() throws Exception {
            // Arrange
            final String json = "{\"title\":\"todo for test\"}"; // no mapper, just send the required field
            location = mvc //
                    .perform(post(BASEURL).contentType(DEFAULT_MEDIA_TYPE).content(json)) //
                    .andExpect(status().isCreated()) //
                    .andExpect(header().exists(HttpHeaders.LOCATION)) //
                    .andReturn().getResponse().getHeader(HttpHeaders.LOCATION);
            assertThat(location).isNotNull();
            // fetch using get
            existing = mapper.readValue( //
                    mvc //
                            .perform(get(location).accept(DEFAULT_MEDIA_TYPE)) //
                            .andExpect(status().isOk()) //
                            .andExpect(content().contentType(DEFAULT_MEDIA_TYPE)) //
                            .andReturn().getResponse().getContentAsString()
                    , TodoDto.class);
            assertThat(existing).isNotNull();
        }

        /*
         * Testfall:
         *  - PUT auf die URL vom Location Header -> 204
         *  - GET auf die URL vom Location Header -> 200 OK mit geänderten Daten
         */
        @DisplayName("PUT ändert Todo")
        @Test
        void testUpdateTodo() throws Exception {
            // change title of the item
            existing.setTitle("anderer titel");
            // send PUT to store the changes
            final String putJson = mapper.writeValueAsString(existing);
            mvc //
                    .perform(put(location).contentType(DEFAULT_MEDIA_TYPE).content(putJson)) //
                    .andExpect(status().isNoContent());
            // read again
            mvc //
                    .perform(get(location).accept(DEFAULT_MEDIA_TYPE)) //
                    .andExpect(status().isOk()) //
                    .andExpect(content().contentType(DEFAULT_MEDIA_TYPE)) //
                    .andExpect(jsonPath("$.title").value("anderer titel"));
        }

        /*
         * Testfall:
         *  - DELETE auf die URL vom Location Header -> 204
         *  - DELETE erneut auf die URL vom Location Header -> 404
         */
        @DisplayName("DELETE löscht Todo")
        @Test
        void testDeleteTodo() throws Exception {
            // send DELETE to delete the item
            mvc //
                    .perform(delete(location)) //
                    .andExpect(status().isNoContent());
            // try to DELETE again
            mvc //
                    .perform(delete(location)) //
                    .andExpect(status().isNotFound());
        }

        /*
         * Testfall:
         *  - DELETE auf die URL vom Location Header -> 204
         *  - GET erneut auf die URL vom Location Header -> 404
         */
        @DisplayName("GET auf nicht existierendes Todo")
        @Test
        void testGetTodoAfterDeleted() throws Exception {
            // send DELETE to delete it
            mvc //
                    .perform(delete(location)) //
                    .andExpect(status().isNoContent());
            // try to GET again
            mvc //
                    .perform(get(location).accept(DEFAULT_MEDIA_TYPE)) //
                    .andExpect(status().isNotFound());
        }

        /*
         * Testfall:
         *  - DELETE auf die URL vom Location Header -> 204
         *  - PUT erneut auf die URL vom Location Header -> 404
         */
        @DisplayName("PUT auf nicht existierendes Todo")
        @Test
        void testUpdateTodoAfterDeleted() throws Exception {
            // send DELETE to delete it
            mvc //
                    .perform(delete(location)) //
                    .andExpect(status().isNoContent());
            // try to PUT
            final String putJson = mapper.writeValueAsString(existing);
            mvc //
                    .perform(put(location).contentType(DEFAULT_MEDIA_TYPE).content(putJson)) //
                    .andExpect(status().isNotFound());
        }

        /*
         * Testfall:
         *  - PUT erneut auf die URL vom Location Header mit geänderter ID -> 400
         */
        @DisplayName("PUT mit geänderter ID")
        @Test
        void testUpdateWithNonMatchingID() throws Exception {
            // manipulate ID
            existing.setId(existing.getId() + 1);
            // try to PUT
            final String putJson = mapper.writeValueAsString(existing);
            mvc //
                    .perform(put(location).contentType(DEFAULT_MEDIA_TYPE).content(putJson)) //
                    .andExpect(status().isBadRequest());
        }

        /*
         * Testfall:
         *  - PUT erneut auf die URL vom Location Header mit gelöschter ID -> 400
         */
        @DisplayName("PUT mit gelöschter ID")
        @Test
        void testUpdateWithEmptyID() throws Exception {
            // manipulate ID
            existing.setId(null);
            // try to PUT
            final String putJson = mapper.writeValueAsString(existing);
            mvc //
                    .perform(put(location).contentType(DEFAULT_MEDIA_TYPE).content(putJson)) //
                    .andExpect(status().isBadRequest());
        }

        /*
         * Testfall:
         *  - PUT erneut auf die URL vom Location Header mit leerem Titel -> 400
         */
        @DisplayName("PUT mit leerem Titel")
        @Test
        void testUpdateWithEmptyTitle() throws Exception {
            // manipulate ID
            existing.setTitle(null);
            // try to PUT
            final String putJson = mapper.writeValueAsString(existing);
            mvc //
                    .perform(put(location).contentType(DEFAULT_MEDIA_TYPE).content(putJson)) //
                    .andExpect(status().isBadRequest());
        }

    }

}