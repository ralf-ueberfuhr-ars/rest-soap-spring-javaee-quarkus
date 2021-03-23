package de.ars.schulung.sample.boundary.rest;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import de.ars.schulung.sample.boundary.TodoDto;
import de.ars.schulung.sample.boundary.TodoMapper;
import de.ars.schulung.sample.control.NotFoundException;
import de.ars.schulung.sample.control.TodoManager;
import de.ars.schulung.sample.entity.Todo;

@Path("v1/todos")
public class TodosResource {

	@Inject
	TodoManager mgr;
	@Inject
	TodoMapper mapper;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAll() {
		List<Todo> todos = mgr.getTodos();
		List<TodoDto> dtos = todos.stream().map(mapper::map).collect(Collectors.toList());
		return Response.ok(dtos).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTodo(@Valid @NotNull TodoDto todo, @Context UriInfo uriInfo) {
		// Alternative: Validierung über Bean Validation mit
		// Validation Groups mit @ConvertGroup(from=Default.class, to=...) für ID
		todo.setId(null); // wegen JPA
		Todo entity = mapper.map(todo);
		mgr.add(entity);
		// build the Location Header
		UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
		uriBuilder.path(Long.toString(entity.getId()));
		URI locationHeader = uriBuilder.build();
		// response
		return Response.created(locationHeader).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response findById(@PathParam("id") @NotNull @Min(1) Long id) {
		Todo todo = mgr.getTodo(id);
		TodoDto dto = mapper.map(todo);
		return Response.ok(dto).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response updateTodo(@PathParam("id") @NotNull @Min(1) Long id, @Valid @NotNull TodoDto todo) {
		// Local Validation
		if (!id.equals(todo.getId())) {
			return Response.status(Status.BAD_REQUEST.getStatusCode(), "id of todo does not match id in path").build();
		}
		Todo todoFromDb = mgr.getTodo(id);
		mapper.copy(todo, todoFromDb);
		mgr.update(todoFromDb);
		return Response.noContent().build();
	}

	@DELETE
	@Path("{id}")
	public Response deleteTodo(@PathParam("id") @NotNull @Min(1) Long id) throws NotFoundException {
		Todo todoFromDb = mgr.getTodo(id);
		mgr.remove(todoFromDb);
		return Response.noContent().build();
	}

}
