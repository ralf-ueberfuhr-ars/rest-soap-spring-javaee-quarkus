package de.ars.schulung.samples.services.todo.control;

import de.ars.schulung.samples.services.todo.entity.TodoEntity;
import org.springframework.stereotype.Component;

/**
 * Dieser Mapper kopiert die Informationen zwischen den Schichten.
 */
@Component
class TodoEntityMapper {

    TodoEntity map(Todo todo) {
        if (null == todo) {
            return null;
        } else {
            TodoEntity result = new TodoEntity();
            result.setId(todo.getId());
            result.setTitle(todo.getTitle());
            result.setDescription(todo.getDescription());
            result.setDueDate(todo.getDueDate());
            if (null != todo.getStatus()) {
                result.setStatus(TodoEntity.StatusEntity.valueOf(todo.getStatus().name()));
            }
            return result;
        }
    }

    Todo map(TodoEntity todo) {
        if (null == todo) {
            return null;
        } else {
            Todo result = new Todo();
            result.setId(todo.getId());
            result.setTitle(todo.getTitle());
            result.setDescription(todo.getDescription());
            result.setDueDate(todo.getDueDate());
            if (null != todo.getStatus()) {
                result.setStatus(Todo.Status.valueOf(todo.getStatus().name()));
            }
            return result;
        }
    }

}
