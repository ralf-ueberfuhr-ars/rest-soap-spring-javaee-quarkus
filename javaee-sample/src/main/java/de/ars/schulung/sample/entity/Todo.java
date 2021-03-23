package de.ars.schulung.sample.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name="todos")
@NamedQuery(
        name="findAll",
        // JPQL -> nicht SQL!
        // SQL wäre "SELECT t.* FROM todos t"
        query="SELECT t FROM Todo t"
)
@NamedQuery(
        name="findByTitle",
        // JPQL -> nicht SQL!
        // SQL wäre "SELECT t.* FROM todos t"
        query="SELECT t FROM Todo t WHERE LOWER(t.title) LIKE LOWER(:titleparam)"
)
public class Todo {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;	
	@NotNull @Size(min=3)
	@Column(nullable=false)
	private String title;
	// @Future does not support LocalDate?
	// @Future
	private LocalDate dueDate;
	private boolean completed;
	
	public Todo() {
		super();
	}

	public Todo(String title, LocalDate dueDate) {
		super();
		this.title = title;
		this.dueDate = dueDate;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
