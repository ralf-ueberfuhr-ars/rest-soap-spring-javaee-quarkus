package de.ars.schulung.samples.services.todo.boundary;

import de.ars.schulung.samples.services.todo.TodosApplicationTest;
import de.ars.schulung.samples.services.todo.boundary.TodoDto;
import de.ars.schulung.samples.services.todo.boundary.TodosController;
import de.ars.schulung.samples.services.todo.entity.TodosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Integrationstests des TodosControllers auf Java API Layer.
 * Dabei wird das Repository gemockt. Hier soll lediglich geprüft werden, ob Datenbankfehler durchgereicht werden.
 */
@TodosApplicationTest
public class TodosControllerWithMockRepositoryTest {

    // dieses Objekt wird als Mock instruiert
    // Answers.RETURNS_MOCKS ist notwendig, da Methoden in Service-Init-Methode bereits aufgerufen werden
    @MockBean(answer = Answers.RETURNS_MOCKS)
    TodosRepository repo;
    // dieses Objekt wird getestet
    @Autowired
    TodosController controller;

    @BeforeEach
    void verifyInitialization() {
        // TODO: Auslagern der Init-Methode im TodosService
        // because save is called twice during initialization of service
        Mockito.clearInvocations(repo);
    }

    @Test
    void testFindAll() {
        final PersistenceException ex = new PersistenceException();
        doThrow(ex).when(repo).findAll();
        assertThatThrownBy(() -> this.controller.findAll()).isSameAs(ex);
    }

    @Test
    void testFindById() {
        final PersistenceException ex = new PersistenceException();
        final Long id = 5L;
        doThrow(ex).when(repo).findById(id);
        assertThatThrownBy(() -> this.controller.findById(id)).isSameAs(ex);
    }

    @Test
    void testCreate() {
        final PersistenceException ex = new PersistenceException();
        doThrow(ex).when(repo).save(any());
        assertThatThrownBy(() -> this.controller.create(new TodoDto(null, "test-todo"))).isSameAs(ex);
    }

    @Test
    void testUpdate() {
        final PersistenceException ex = new PersistenceException();
        final Long id = 5L;
        when(repo.existsById(id)).thenReturn(true);
        doThrow(ex).when(repo).save(any());
        assertThatThrownBy(() -> this.controller.update(id, new TodoDto(id, "test-todo"))).isSameAs(ex);
    }

    @Test
    void testDelete() {
        final PersistenceException ex = new PersistenceException();
        final Long id = 5L;
        when(repo.existsById(id)).thenReturn(true);
        doThrow(ex).when(repo).deleteById(id);
        assertThatThrownBy(() -> this.controller.delete(id)).isSameAs(ex);
    }

    @Test
    void testExistsById() {
        final PersistenceException ex = new PersistenceException();
        final Long id = 5L;
        doThrow(ex).when(repo).existsById(id);
        assertThatThrownBy(() -> this.controller.delete(id)).isSameAs(ex);
    }


}