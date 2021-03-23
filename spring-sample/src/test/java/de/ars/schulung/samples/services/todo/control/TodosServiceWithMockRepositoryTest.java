package de.ars.schulung.samples.services.todo.control;

import de.ars.schulung.samples.services.todo.TodosApplicationTest;
import de.ars.schulung.samples.services.todo.control.Todo;
import de.ars.schulung.samples.services.todo.control.TodosService;
import de.ars.schulung.samples.services.todo.entity.TodoEntity;
import de.ars.schulung.samples.services.todo.entity.TodosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Integrationstests des TodosServices mit gemocktem Repository.
 * Somit müssen Tests einen Datensatz vorher nicht anlegen.
 */
@TodosApplicationTest
public class TodosServiceWithMockRepositoryTest {

    // dieses Objekt wird als Mock instruiert
    // Answers.RETURNS_MOCKS ist notwendig, da Methoden in Service-Init-Methode bereits aufgerufen werden
    @MockBean(answer = Answers.RETURNS_MOCKS)
    TodosRepository repo;
    // dieses Objekt wird getestet
    @Autowired
    TodosService service;

    @BeforeEach
    void verifyInitialization() {
        // TODO: Auslagern der Init-Methode im TodosService
        // because save is called twice during initialization of service
        Mockito.clearInvocations(repo);
    }

    /*
     * Testfall:
     *  - alle Todos auslesen -> kein Fehler
     */
    @Test
    void testFindAllTodos() {
        when(repo.findAll()).thenReturn(Arrays.asList(new TodoEntity(1L, "test")));
        final Collection<Todo> result = this.service.findAll();
        assertThat(result).hasSize(1) //
                .element(0).extracting(Todo::getId, Todo::getTitle).containsExactly(1L, "test");
    }

    /*
     * Testfall:
     *  - Anlegen eines Todos -> ID besetzt
     *  - Auslesen -> gefunden mit entsprechenden Werten
     */
    @Test
    void testCreateTodo() {
        final Todo newTodo = new Todo(null, "test-todo");
        when(repo.save(any())).thenReturn(new TodoEntity(5L, "test-todo"));
        // create
        this.service.create(newTodo);
        // find out id
        assertThat(newTodo).extracting(Todo::getId).isEqualTo(5L);
        verify(repo).save(refEq(new TodoEntity(null, "test-todo")));
    }

    /*
     * Testfall:
     *  - Ändern eines bestehenden Todos
     *  - Aufruf der Repo-Methode und Rückgabewert prüfen
     */
    @Test
    void testUpdateExisting() {
        final Todo todo = new Todo(5L, "test-todo");
        when(repo.existsById(todo.getId())).thenReturn(true);
        // Test
        final boolean result = this.service.update(todo);
        // Assert
        assertThat(result).describedAs("update successful").isTrue();
        verify(repo).save(refEq(new TodoEntity(5L, "test-todo")));
    }

    /*
     * Testfall:
     *  - Ändern eines nicht existenten Todos
     *  - Aufruf der Repo-Methode und Rückgabewert prüfen
     */
    @Test
    void testUpdateNotExisting() {
        final Todo todo = new Todo(5L, "test-todo");
        when(repo.existsById(todo.getId())).thenReturn(false);
        // Test
        final boolean result = this.service.update(todo);
        // Assert
        assertThat(result).describedAs("update not successful").isFalse();
        verify(repo).existsById(todo.getId());
        verifyNoMoreInteractions(repo);
    }

    /*
     * Testfall:
     *  - Löschen eines existenten Todos
     *  - Aufruf der Repo-Methode und Rückgabewert prüfen
     */
    @Test
    void testDeleteExisting() {
        when(repo.existsById(5L)).thenReturn(true);
        // Test
        final boolean result = this.service.delete(5L);
        // Assert
        assertThat(result).describedAs("delete successful").isTrue();
        verify(repo).deleteById(5L);
    }

    /*
     * Testfall:
     *  - Löschen eines nicht existenten Todos
     *  - Aufruf der Repo-Methode und Rückgabewert prüfen
     */
    @Test
    void testDeleteNotExisting() {
        when(repo.existsById(5L)).thenReturn(false);
        // Test
        final boolean result = this.service.delete(5L);
        // Assert
        assertThat(result).describedAs("delete not successful").isFalse();
        verify(repo).existsById(5L);
        verifyNoMoreInteractions(repo);
    }

    // TODO test IllegalArgumentException/ValidationException on create or update
    // TODO simulate exceptions

}