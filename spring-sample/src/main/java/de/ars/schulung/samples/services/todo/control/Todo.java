package de.ars.schulung.samples.services.todo.control;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class Todo {

    public static enum Status {
        NEW, PROGRESS, COMPLETED, ARCHIVED;
    }

    private Long id;
    @NotNull
    @Size(min = 1)
    private String title;
    private String description;
    private LocalDate dueDate;
    @NotNull
    private Status status = Status.NEW;

    public Todo() {
    }

    public Todo(Long id, String title) {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // just to get readable output on the console
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Todo {\n");
        sb.append("  id: ").append(id).append("\n");
        sb.append("  title: ").append(title).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
