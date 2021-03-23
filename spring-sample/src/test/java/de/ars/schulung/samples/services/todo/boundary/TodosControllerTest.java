package de.ars.schulung.samples.services.todo.boundary;

import de.ars.schulung.samples.services.todo.TodosApplicationTest;
import de.ars.schulung.samples.services.todo.boundary.NotFoundException;
import de.ars.schulung.samples.services.todo.boundary.TodoDto;
import de.ars.schulung.samples.services.todo.boundary.TodosController;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;

/**
 * Integrationstests des TodosControllers auf Java API Layer. Die Anwendung wird als Black Box behandelt.
 * Somit müssen alle Tests, die einen existierenden Datensatz benötigen, diesen im Test anlegen.
 * Spring Boot startet den sog. "ApplicationContext" automatisch und bietet Möglichkeiten, die Anwendungsteile vom Test aus aufzurufen.
 */
@TodosApplicationTest
public class TodosControllerTest {

    // keine URLS, keine HTTP Methoden, kein HTTP Response Code, kein JSON-Parsen/Rendern...

    @Autowired
    TodosController controller;

    // ... aber gleiche Testfälle

    /*
     * Testfall:
     *  - alle Todos auslesen -> kein Fehler
     */
    @Test
    void testFindAllTodos() {
        controller.findAll();
        // hier keine Assertions - muss nur ohne Fehler durchlaufen
    }

    /*
     * Testfall:
     *  - Anlegen eines Todos -> 201 mit ID
     */
    @Test
    void testCreateTodo() {
        // Arrange
        final TodoDto newTodo = new TodoDto(null, "test-todo");
        // Act
        final ResponseEntity<Void> response = controller.create(newTodo);
        // Assert
        assertAll(
                () -> assertThat(response).extracting(ResponseEntity::getStatusCode).isEqualTo(CREATED),
                () -> assertThat(newTodo.getId()).isNotNull()
        );
    }

    /*
     * Testfall:
     *  - Anlegen eines Todos mit ID -> 400
     */
    @Test
    void testCreateTodoWithID() {
        // Arrange
        final TodoDto newTodo = new TodoDto(1L, "test-todo");
        assertThatThrownBy(() -> controller.create(newTodo)).isInstanceOf(ValidationException.class);
    }

    /*
     * Testfall:
     *  - Anlegen eines Todos ohne Titel -> 400
     */
    @Test
    // das geht jetzt so nicht mehr, wenn BeanVal durch den Container stattfindet
    @Disabled
    // @see TodosControllerAPITest
    void testCreateTodoWithoutTitle() {
        // Arrange
        final TodoDto newTodo = new TodoDto();
        assertThatThrownBy(() -> this.controller.create(newTodo)).isInstanceOf(ValidationException.class);
    }

    @Nested
    @DisplayName("Tests that need an existing todo")
    class TestsThatNeedExistingTodo {

        private TodoDto existing;

        @BeforeEach
        void setup() {
            // Arrange
            existing = new TodoDto(null, "todo for test");
            controller.create(existing);
        }

        /*
         * Testfall:
         *  - Auslesen -> 200 mit entsprechenden Werten
         */
        @Test
        void testCreateTodoResultsInFindableTodo() {
            // Act
            final TodoDto result = controller.findById(existing.getId());
            // Assert
            assertThat(result).extracting(TodoDto::getTitle, TodoDto::getStatus).containsExactly(existing.getTitle(), "new");
        }

        /*
         * Testfall:
         *  - Ändern des Todos mit anderer ID
         */
        @Test
        void testUpdateWithNonMatchingID() {
            // Versuch zu ändern
            assertThatThrownBy(() -> controller.update(existing.getId(), new TodoDto(existing.getId() + 1, "any title"))) //
                    .isInstanceOf(ValidationException.class);
        }

        /*
         * Testfall:
         *  - Anlegen eines Todos -> 201 mit ID
         *  - Ändern des Todos mit leerer ID
         */
        @Test
        void testUpdateWithEmptyID() {
            // Versuch zu ändern
            assertThatThrownBy(() -> controller.update(existing.getId(), new TodoDto(null, "any title"))) //
                    .isInstanceOf(ValidationException.class);
        }

        /*
         * Testfall:
         *  - Ändern des Todos mit leerem Titel
         */
        @Test
        // das geht jetzt so nicht mehr, wenn BeanVal durch den Container stattfindet
        @Disabled
        // @see TodosControllerAPITest
        void testUpdateWithEmptyTitle() {
            // Versuch zu ändern
            assertThatThrownBy(() -> controller.update(existing.getId(), new TodoDto(existing.getId(), null))) //
                    .isInstanceOf(ValidationException.class);
        }

    }

    @Nested
    @DisplayName("Tests that need a non-existing todo")
    class TestsThatNeedNonExistingTodo {

        TodoDto nonExisting;

        @BeforeEach
        void setup() {
            // Arrange
            nonExisting = new TodoDto(null, "todo for test");
            controller.create(nonExisting);
            controller.delete(nonExisting.getId());
        }

        /*
         * Testfall:
         *  - erneutes Löschen -> 404
         */
        @Test
        void testDeleteTodo() {
            assertThatThrownBy(() -> controller.delete(nonExisting.getId())).isInstanceOf(NotFoundException.class);
        }

        /*
         * Testfall:
         *  - Auslesen des Todos -> 404
         */
        @Test
        void testGetTodoAfterDeleted() {
            assertThatThrownBy(() -> controller.findById(nonExisting.getId())).isInstanceOf(NotFoundException.class);
        }

        /*
         * Testfall:
         *  - Ändern des Todos -> 404
         */
        @Test
        void testUpdateTodoAfterDeleted() {
            assertThatThrownBy(() -> controller.update(nonExisting.getId(), new TodoDto(nonExisting.getId(), "any title"))) //
                    .isInstanceOf(NotFoundException.class);
        }

    }
}