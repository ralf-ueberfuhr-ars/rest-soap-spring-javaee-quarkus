package de.ars.schulung.sample.boundary;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Type for boundary.
 */
public class TodoDto {

	private Long id;
	@NotNull
	@Size(min = 3)
	private String title;
	private LocalDate dueDate;
	private boolean completed;

	public TodoDto() {
		super();
	}

	public TodoDto(String title, LocalDate dueDate) {
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
