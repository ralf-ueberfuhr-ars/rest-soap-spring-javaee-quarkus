package de.ars.schulung.samples.services.todo.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface TodosRepository extends CrudRepository<TodoEntity, Long> {

    /*
     * Keine weiteren Methoden notwendig, erst wenn nach Attributen gesucht werden soll, z.B:
     *
     * Iterable<TodoEntity> findByStatus(TodoEntity.Status);
     *
     */

}
