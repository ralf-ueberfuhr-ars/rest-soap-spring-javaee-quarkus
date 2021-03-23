package de.ars.schulung.samples.services.todo.boundary;

import de.ars.schulung.samples.services.todo.TodosApplicationTest;
import de.ars.schulung.samples.services.todo.boundary.NotFoundException;
import de.ars.schulung.samples.services.todo.boundary.TodoDto;
import de.ars.schulung.samples.services.todo.boundary.TodosController;
import de.ars.schulung.samples.services.todo.control.Todo;
import de.ars.schulung.samples.services.todo.control.TodosService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Integrationstests des TodosControllers auf Java API Layer.
 * Dabei wird der Service gemockt, was den Vorteil hat, dass nicht wie bisher umständlich Daten erstellt
 * werden müssen. Durch das Instruieren des Mocks lässt sich so besonderes Verhalten im Test reproduzieren.
 */
@TodosApplicationTest
public class TodosControllerWithMockServiceTest {

    // dieses Objekt wird als Mock instruiert
    @MockBean
    TodosService service;
    // dieses Objekt wird getestet
    @Autowired
    TodosController controller;

    /*
     * Testfall:
     *  - alle Todos auslesen -> kein Fehler
     */
    @Test
    void testFindAllTodos() {
        when(service.findAll()).thenReturn(Arrays.asList(new Todo(1L, "test")));
        final Collection<TodoDto> result = this.controller.findAll();
        assertThat(result).hasSize(1) //
                .element(0).extracting(TodoDto::getId, TodoDto::getTitle).containsExactly(1L, "test");
    }

    /*
     * Testfall:
     *  - Anlegen eines Todos -> service muss bestätigen und ID zuweisen
     *  - ID muss an Dto zugewiesen werden
     */
    @Test
    void testCreateTodo() {
        final TodoDto newTodo = new TodoDto(null, "test-todo");
        // etwas umständlich, die ID zu besetzen, wenn auf dem Service create() aufgerufen wird
        doAnswer(inv -> {
            inv.getArgument(0, Todo.class).setId(5L);
            return null;
        }).when(service).create(any()); // Methode muss aufgerufen werden, sondern Fehler
        // jetzt der Test
        this.controller.create(newTodo);
        // assert
        assertThat(newTodo).extracting(TodoDto::getId).isEqualTo(5L);
    }

    /*
     * Testfall:
     *  - Anlegen eines Todos ohne Titel -> service darf nicht aufgerufen worden sein
     */
    @Test
    // das geht jetzt so nicht mehr, wenn BeanVal durch den Container stattfindet
    @Disabled
    // @see TodosControllerAPITest
    void testCreateTodoWithoutTitle() {
        assertThatThrownBy(() -> this.controller.create(new TodoDto())) //
                .isInstanceOf(ValidationException.class);
        verifyNoInteractions(service);
    }

    /*
     * Testfall:
     *  - Ändern eines bestehenden Todos
     *  - Aufruf der Service-Methode und Rückgabewert prüfen
     */
    @Test
    void testUpdateExisting() {
        final TodoDto todo = new TodoDto(5L, "test-todo");
        when(service.update(any())).thenReturn(true); // existent
        // Test
        this.controller.update(5L, todo);
        // no assertions, should just run without exception
    }

    /*
     * Testfall:
     *  - Ändern eines nicht existenten Todos
     *  - Aufruf der Service-Methode und Rückgabewert prüfen
     */
    @Test
    void testUpdateNotExisting() {
        final TodoDto todo = new TodoDto(5L, "test-todo");
        when(service.update(any())).thenReturn(false); // NICHT existent
        // Test+Assert
        assertThatThrownBy(() -> this.controller.update(5L, todo)).isInstanceOf(NotFoundException.class);
    }

    /*
     * Testfall:
     *  - Löschen eines existenten Todos
     *  - Aufruf der Service-Methode und Rückgabewert prüfen
     */
    @Test
    void testDeleteExisting() {
        when(service.delete(5L)).thenReturn(true); // existent
        // Test
        this.controller.delete(5L);
        // no assertions, should just run without exception
    }

    /*
     * Testfall:
     *  - Löschen eines nicht existenten Todos
     *  - Aufruf der Service-Methode und Rückgabewert prüfen
     */
    @Test
    void testDeleteNotExisting() {
        when(service.delete(5L)).thenReturn(false); // NICHT existent
        // Test+Assert
        assertThatThrownBy(() -> this.controller.delete(5L)).isInstanceOf(NotFoundException.class);
    }

    /*
     * Testfall:
     *  - Ändern des Todos mit anderer ID
     *  - service darf nicht aufgerufen werden
     */
    @Test
    void testUpdateWithNonMatchingID() {
        // Versuch zu ändern
        assertThatThrownBy(() -> controller.update(5L, new TodoDto(6L, "any title"))) //
                .isInstanceOf(ValidationException.class);
        // assert
        verifyNoInteractions(service);
    }

    /*
     * Testfall:
     *  - Ändern des Todos mit leerer ID
     *  - service darf nicht aufgerufen werden
     */
    @Test
    void testUpdateWithEmptyID() {
        // Versuch zu ändern
        assertThatThrownBy(() -> controller.update(null, new TodoDto(null, "any title"))) //
                .isInstanceOf(ValidationException.class);
        // assert
        verifyNoInteractions(service);
    }

    /*
     * Testfall:
     *  - Ändern des Todos mit leerem Titel
     *  - service darf nicht aufgerufen werden
     */
    @Test
    // das geht jetzt so nicht mehr, wenn BeanVal durch den Container stattfindet
    @Disabled
    // @see TodosControllerAPITest
    void testUpdateWithEmptyTitle() {
        // Versuch zu ändern
        assertThatThrownBy(() -> controller.update(5L, new TodoDto(5L, null))) //
                .isInstanceOf(ValidationException.class);
        // assert
        verifyNoInteractions(service);
    }

    // simulate exceptions

    /*
     * Testfall:
     *  - Erzeugen eines Todos -> service verursacht ValidationException
     *  - Exception wird durchgereicht
     */
    @Test
    void testValidationExceptionOfServiceOnCreate() {
        doThrow(ValidationException.class).when(service).create(any());
        // Test
        assertThatThrownBy(() -> this.controller.create(new TodoDto(null, "test-todo"))) //
                .isInstanceOf(ValidationException.class);
    }

    /*
     * Testfall:
     *  - Ändern eines Todos -> service verursacht ValidationException
     *  - Exception wird durchgereicht
     */
    @Test
    void testValidationExceptionOfServiceOnUpdate() {
        doThrow(ValidationException.class).when(service).update(any());
        // Test
        assertThatThrownBy(() -> this.controller.update(5L, new TodoDto(5L, "test-todo"))) //
                .isInstanceOf(ValidationException.class);
    }

}