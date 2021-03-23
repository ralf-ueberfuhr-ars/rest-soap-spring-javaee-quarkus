package de.ars.schulung.samples.services.todo.control;

import de.ars.schulung.samples.services.todo.TodosApplicationTest;
import de.ars.schulung.samples.services.todo.control.Todo;
import de.ars.schulung.samples.services.todo.control.TodosService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integrationstests des TodosServices. Die Anwendung wird als Black Box behandelt.
 * Somit müssen alle Tests, die einen existierenden Datensatz benötigen, diesen im Test anlegen.
 */
@TodosApplicationTest
public class TodosServiceTest {

    @Autowired
    TodosService service;

    /*
     * Testfall:
     *  - alle Todos auslesen -> kein Fehler
     */
    @Test
    void testFindAllTodos() {
        service.findAll();
        // hier keine Assertions - muss nur ohne Fehler durchlaufen
    }

    @Nested
    @DisplayName("Tests that need an existing todo")
    class TestsThatNeedExistingTodo {

        private Todo existing;

        @BeforeEach
        void setup() {
            // Arrange
            existing = new Todo(null, "todo for test");
            service.create(existing);
        }

        /*
         * Testfall:
         *  - Anlegen eines Todos -> ID besetzt
         *  - Auslesen -> gefunden mit entsprechenden Werten
         */
        @Test
        void testCreateTodoResultsInFindableTodo() {
            // erneutes Auslesen
            final Optional<Todo> result = service.findById(existing.getId());
            assertThat(result) //
                    .isPresent() //
                    .hasValueSatisfying(t -> assertThat(t).extracting(Todo::getTitle).isEqualTo(existing.getTitle()));
        }

        /*
         * Testfall:
         *  - Anlegen eines Todos
         *  - Ändern -> erfolgreich
         *  - Auslesen -> gefunden mit entsprechenden Werten
         */
        @Test
        void testUpdateTodo() {
            // Ändern
            existing.setTitle("anderer Titel");
            final boolean updateSuccessful = service.update(existing);
            assertThat(updateSuccessful).describedAs("update successful").isTrue();
            // Auslesen und Prüfen
            final Optional<Todo> result = service.findById(existing.getId());
            assertThat(result) //
                    .isPresent() //
                    .hasValueSatisfying(t -> assertThat(t).extracting(Todo::getTitle).isEqualTo("anderer Titel"));
        }

        /*
         * Testfall:
         *  - Anlegen eines Todos
         *  - Löschen des Todos -> erfolgreich
         *  - erneutes Löschen -> nicht
         */
        @Test
        void testDeleteTodo() {
            // erstes Löschen
            final boolean deleteSuccessful = service.delete(existing.getId());
            assertThat(deleteSuccessful).describedAs("first delete successful").isTrue();
            // zweites Löschen
            final boolean deleteNotSuccessful = service.delete(existing.getId());
            assertThat(deleteNotSuccessful).describedAs("second delete not successful").isFalse();
        }

        /*
         * Testfall:
         *  - Anlegen+Löschen eines Todos
         *  - Auslesen des Todos -> nicht gefunden
         */
        @Test
        void testGetTodoAfterDeleted() {
            service.delete(existing.getId());
            // Auslesen und Prüfen
            final Optional<Todo> result = service.findById(existing.getId());
            assertThat(result).isNotPresent();
        }

        /*
         * Testfall:
         *  - Anlegen eines Todos
         *  - Löschen des Todos
         *  - Ändern des Todos -> nicht erfolgreich
         */
        @Test
        void testUpdateTodoAfterDeleted() {
            service.delete(existing.getId());
            // Ändern
            final boolean updateSuccessful = service.update(existing);
            assertThat(updateSuccessful).describedAs("update not successful").isFalse();
        }

        /**
         * Nested Classes kapseln Testfälle logisch und erlauben ein gemeinsames Setup zusätzlich zu dem der umgebenden Klasse.
         */
        @Disabled // TODO ist im Service noch nicht implementiert
        @DisplayName("Tests zur Validierung")
        @Nested
        class ValidationTests {

            /*
             * Testfall:
             *  - Anlegen eines Todos mit ID -> Exception
             */
            @Test
            void testCreateTodoWithID() {
                // TODO Testfall implementieren
            }

            /*
             * Testfall:
             *  - Anlegen eines Todos ohne Titel -> Exception
             */
            @Test
            void testCreateTodoWithoutTitle() {
                // TODO Testfall implementieren
            }

            /*
             * Testfall:
             *  - Anlegen eines Todos
             *  - Ändern des Todos mit leerer ID -> Exception
             */
            @Test
            void testUpdateWithEmptyID() {
                // TODO Testfall implementieren
            }

            /*
             * Testfall:
             *  - Anlegen eines Todos
             *  - Ändern des Todos mit leerem Titel -> Exception
             */
            @Test
            void testUpdateWithEmptyTitle() {
                // TODO Testfall implementieren
            }

        }

    }

}