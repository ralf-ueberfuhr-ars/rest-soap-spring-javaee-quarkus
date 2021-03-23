package de.ars.schulung.samples.services.todo.boundary;

import de.ars.schulung.samples.services.todo.control.Todo;
import de.ars.schulung.samples.services.todo.control.TodosService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/todos")
public class TodosController {

    private static final String DEFAULT_MEDIA_TYPE = MediaType.APPLICATION_JSON_VALUE;

    @Autowired
    TodosService service;
    @Autowired
    TodoDtoMapper mapper; // Mapping zwischen den Schichten

    @GetMapping(produces = DEFAULT_MEDIA_TYPE)
    @ApiOperation(value = "Get all items.", response = TodoDto.class, responseContainer = "List")
    @ApiResponses({ //
            @ApiResponse(code = 200, message = "The items were found and returned."), //
    })
    public Collection<TodoDto> findAll() {
        return service.findAll().stream().map(mapper::map).collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}", produces = DEFAULT_MEDIA_TYPE)
    @ApiOperation(value = "Find a single item by id.", response = TodoDto.class)
    @ApiResponses({ //
            @ApiResponse(code = 200, message = "The item was found and returned."), //
            @ApiResponse(code = 400, message = "The item id is empty."), //
            @ApiResponse(code = 404, message = "An item with the given id could not be found."), //
    })
    public TodoDto findById(final @PathVariable("id") Long id) {
        // Validation
        if (null == id) { // should only occur when invoked directly during tests
            throw new ValidationException();
        }
        // Action
        return service.findById(id) //
                .map(mapper::map) // map to dto
                .orElseThrow(NotFoundException::new);
    }

    @PostMapping(consumes = DEFAULT_MEDIA_TYPE)
    @ApiOperation("Create an item.")
    @ApiResponses({ //
            @ApiResponse(code = 201, message = "The item was successfully created."), //
            @ApiResponse(code = 400, message = "The item is empty or has an id or any invalid property."), //
    })
    public ResponseEntity<Void> create(final @Valid @RequestBody TodoDto item) {
        // Validation
        if (null != item.getId()) {
            throw new ValidationException();
        }
        // Action
        final Todo todo = mapper.map(item);
        service.create(todo);
        item.setId(todo.getId()); // just o pass the tests
        // Response
        final URI locationHeader = linkTo(methodOn(TodosController.class).findById(todo.getId())).toUri(); // HATEOAS
        return ResponseEntity.created(locationHeader).build();
    }

    @PutMapping(value = "{id}", consumes = DEFAULT_MEDIA_TYPE)
    @ApiOperation(value = "Update an item.", notes = "This is not a partial update!")
    @ApiResponses({ //
            @ApiResponse(code = 200, message = "The item was successfully updated. No response body is returned."), //
            @ApiResponse(code = 400, message = "The item has validation error. This might occur esp., if the id of the item does not match the id given by the URL path."), //
            @ApiResponse(code = 404, message = "The item could not be found."), //
    })
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable("id") final Long id,
                       @Valid @NotNull @RequestBody final TodoDto item) {
        // Validation
        if (null == id || !id.equals(item.getId())) {
            throw new ValidationException();
        }
        if (!service.update(mapper.map(item))) {
            throw new NotFoundException();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete an item.")
    @ApiResponses({ //
            @ApiResponse(code = 204, message = "The item was successfully deleted."), //
            @ApiResponse(code = 400, message = "The id is invalid."), //
            @ApiResponse(code = 404, message = "The item could not be found, so it was not deleted."), //
    })
    @ResponseStatus(NO_CONTENT)
    public void delete(final @PathVariable("id") Long id) {
        // Validation
        if (null == id) { // should only occur when invoked directly during tests
            throw new ValidationException();
        }
        // Action
        if (!service.delete(id)) {
            throw new NotFoundException();
        }
    }

}
