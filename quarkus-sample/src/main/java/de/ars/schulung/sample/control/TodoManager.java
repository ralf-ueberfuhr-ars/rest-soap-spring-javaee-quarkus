package de.ars.schulung.sample.control;

import de.ars.schulung.sample.entity.Todo;
import de.ars.schulung.sample.entity.TodosRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class TodoManager {

	@Inject
	TodosRepository repository;
	
	public List<Todo> getTodos() {
		return repository.getTodos();
	}

	private Todo ensureExisting(long id) {
		return repository.getTodo(id).orElseThrow(NotFoundException::new);
	}

	private Todo ensureExisting(Todo todo) {
		Objects.requireNonNull(todo);
		Objects.requireNonNull(todo.getId());
		return ensureExisting(todo.getId());
	}

		// Beispiel für ein Update
	public void markCompleted(long id) {
		Todo todo = ensureExisting(id);
		todo.setCompleted(true);
		repository.update(todo);
	}

	public void add(Todo todo) {
		repository.add(todo);
	}

	public void update(Todo todo) {
		ensureExisting(todo);
		repository.update(todo);
	}

	public void remove(Todo todo) {
		ensureExisting(todo);
		repository.remove(todo);
	}

	public List<Todo> getTodos(String title) {
		return repository.getTodos(title);
	}

	public Todo getTodo(long id) {
		return ensureExisting(id);
	}

}
