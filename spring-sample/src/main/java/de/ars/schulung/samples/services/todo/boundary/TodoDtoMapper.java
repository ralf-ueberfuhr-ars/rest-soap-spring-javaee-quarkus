package de.ars.schulung.samples.services.todo.boundary;

import de.ars.schulung.samples.services.todo.control.Todo;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Dieser Mapper kopiert die Informationen zwischen den Schichten.
 */
@Component
class TodoDtoMapper {

    TodoDto map(Todo todo) {
        if (null == todo) {
            return null;
        } else {
            TodoDto result = new TodoDto();
            result.setId(todo.getId());
            result.setTitle(todo.getTitle());
            result.setDescription(todo.getDescription());
            result.setDueDate(todo.getDueDate());
            if (null != todo.getStatus()) {
                result.setStatus(todo.getStatus().name().toLowerCase(Locale.ROOT));
            }
            return result;
        }
    }

    Todo map(TodoDto todo) {
        if (null == todo) {
            return null;
        } else {
            Todo result = new Todo();
            result.setId(todo.getId());
            result.setTitle(todo.getTitle());
            result.setDescription(todo.getDescription());
            result.setDueDate(todo.getDueDate());
            if (null != todo.getStatus()) {
                result.setStatus(Todo.Status.valueOf(todo.getStatus().toUpperCase(Locale.ROOT)));
            }
            return result;
        }
    }

}
