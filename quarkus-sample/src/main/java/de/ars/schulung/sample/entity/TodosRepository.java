package de.ars.schulung.sample.entity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class TodosRepository {

	@Inject
	EntityManager em;
	
	public List<Todo> getTodos() {
		return em.createNamedQuery("findAll", Todo.class).getResultList();
	}

	public void add(Todo todo) {
		em.persist(todo);
	}

	public void update(Todo todo) {
		em.merge(todo);
	}

	public void remove(Todo todo) {
		em.remove(em.merge(todo));
	}

	public List<Todo> getTodos(String title) {
		TypedQuery<Todo> query = em.createNamedQuery("findByTitle", Todo.class);
		query.setParameter("titleparam", "%" + title + "%");
		return query.getResultList();
	}

	public Optional<Todo> getTodo(long id) {
		return Optional.ofNullable(em.find(Todo.class, id));
	}

}
