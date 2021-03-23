package de.ars.schulung.sample.boundary;

import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;

import de.ars.schulung.sample.entity.Todo;

@ApplicationScoped
public class TodoMapper {

	public TodoDto map(Todo todo) {
		if (null == todo) {
			return null;
		} else {
			TodoDto result = new TodoDto();
			result.setId(todo.getId());
			result.setTitle(todo.getTitle());
			result.setCompleted(todo.isCompleted());
			result.setDueDate(todo.getDueDate());
			return result;
		}
	}

	public Todo map(TodoDto todo) {
		if (null == todo) {
			return null;
		} else {
			Todo result = new Todo();
			result.setId(todo.getId());
			result.setTitle(todo.getTitle());
			result.setCompleted(todo.isCompleted());
			result.setDueDate(todo.getDueDate());
			return result;
		}
	}

	public void copy(TodoDto source, Todo target) {
		Objects.requireNonNull(source);
		Objects.requireNonNull(target);
		target.setTitle(source.getTitle());
		target.setCompleted(source.isCompleted());
		target.setDueDate(source.getDueDate());
	}

}

/*
 * Mapper mit MapStruct:
 * 
 * @Mapper(componentModel = "cdi") public interface TodoMapper {
 * 
 * TodoDto map(Todo todo);
 * 
 * Todo map(TodoDto todo);
 * 
 * @Mapping(target = "id", ignore = true) void copy(TodoDto
 * source, @MappingTarget Todo target);
 * 
 * }
 * 
 */