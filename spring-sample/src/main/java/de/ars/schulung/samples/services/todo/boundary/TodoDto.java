package de.ars.schulung.samples.services.todo.boundary;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Todo for HTTP transport.
 */
@ApiModel("Todo")
public class TodoDto {

    @ApiModelProperty("The unique identifier.")
    private Long id;
    @ApiModelProperty(value = "The short title.", required = true)
    @NotNull
    @Size(min = 1)
    private String title;
    @ApiModelProperty("The long description.")
    private String description;
    @ApiModelProperty("The date by which the todo has to be done.")
    @JsonProperty("due_date")
    private LocalDate dueDate;
    @ApiModelProperty(value = "The status of the todo. ", allowableValues = "new,progress,completed,archived")
    @Pattern(regexp = "new|progress|completed|archived")
    private String status = "new";

    public TodoDto() {
    }

    public TodoDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // just to get readable output on the console
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TodoDto {\n");
        sb.append("  id: ").append(id).append("\n");
        sb.append("  title: ").append(title).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
