package de.ars.schulung.sample.boundary.soap;

import de.ars.schulung.sample.boundary.TodoDto;
import de.ars.schulung.sample.boundary.TodoMapper;
import de.ars.schulung.sample.control.NotFoundException;
import de.ars.schulung.sample.control.TodoManager;
import de.ars.schulung.sample.entity.Todo;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.PathParam;
import java.util.Collection;
import java.util.stream.Collectors;

@WebService(targetNamespace = "http://www.ars.de/schulung/javaee/todos", serviceName = "TodosService")
public class TodosService {

    @Inject
    TodoManager mgr;
    @Inject
    TodoMapper mapper;

    @WebMethod
    public Collection<TodoDto> findAll() {
        return mgr.getTodos().stream().map(mapper::map).collect(Collectors.toList());
    }

    @WebMethod
    public long createTodo(@Valid @NotNull TodoDto todo) {
        todo.setId(null); // wegen JPA
        Todo entity = mapper.map(todo);
        mgr.add(entity);
        return entity.getId();
    }

    @WebMethod
    public TodoDto findById(@PathParam("id") @NotNull @Min(1) Long id) {
        return mapper.map(mgr.getTodo(id));
    }

    @WebMethod
    public boolean updateTodo(@PathParam("id") @NotNull @Min(1) Long id, @Valid @NotNull TodoDto todo) {
        // Local Validation
        if (!id.equals(todo.getId())) {
            return false;
        } else {
            Todo todoFromDb = mgr.getTodo(id);
            mapper.copy(todo, todoFromDb);
            mgr.update(todoFromDb);
            return true;
        }
    }

    @WebMethod
    public void deleteTodo(@PathParam("id") @NotNull @Min(1) Long id) throws NotFoundException {
        Todo todoFromDb = mgr.getTodo(id);
        mgr.remove(todoFromDb);
    }

}
