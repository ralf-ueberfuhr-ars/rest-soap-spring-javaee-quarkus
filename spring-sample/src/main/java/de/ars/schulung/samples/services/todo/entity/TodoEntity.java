package de.ars.schulung.samples.services.todo.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/*
 * Auch hier wird eine separate Klasse erstellt.
 * Auch hier geht es wieder um Unabhängigkeit der beiden Layer (Control und Persistence).
 * So kann z.B. die Auflösung von Fremdschlüsseln (assignee, priority, topic) in der Persistence Layer erfolgen (JPA unterstützt das), oder aber auch erst in der Control Layer.
 */
@Entity(name = "de/ars/schulung/samples/services/todo")
@Table(name = "todos")
public class TodoEntity {

    public static enum StatusEntity {
        NEW, PROGRESS, COMPLETED, ARCHIVED;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @NotNull
    @Size(min = 1)
    private String title;
    private String description;
    private LocalDate dueDate;
    @Enumerated()
    @NotNull
    private StatusEntity status = StatusEntity.NEW;

    public TodoEntity() {
    }

    public TodoEntity(Long id, String title) {
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

    public StatusEntity getStatus() {
        return status;
    }

    public void setStatus(StatusEntity status) {
        this.status = status;
    }

    // just to get readable output on the console
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TodoEntity {\n");
        sb.append("  id: ").append(id).append("\n");
        sb.append("  title: ").append(title).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
