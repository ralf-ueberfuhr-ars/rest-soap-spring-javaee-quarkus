package de.ars.schulung.samples.services.todo.control;

import de.ars.schulung.samples.services.todo.entity.TodosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

/**
 * Ein Service ist ein Singleton auf Control Layer, der in der Boundary von mehreren (REST) Controllern gemeinsam genutzt werden kann.
 * Dieser hat keinen Bezug mehr zu HTTP.
 */
@Service
public class TodosService {

    @Autowired
    TodoEntityMapper mapper;
    @Autowired
    TodosRepository repo;

    // TODO: Auslagern (siehe https://www.baeldung.com/running-setup-logic-on-startup-in-spring)
    @PostConstruct
    void init() {
        // create sample data, if empty
        if (repo.count() < 1) {
            this.create(new Todo(null, "Deploy and run the application."));
            this.create(new Todo(null, "Enter some TODO items!"));
        }
    }

    /**
     * Gibt alle Todos zurück.
     *
     * @return eine unveränderliche Collection von Todos
     */
    public Collection<Todo> findAll() {
        return stream(repo.findAll().spliterator(), false).map(mapper::map).collect(toList());
    }

    /**
     * Durchsucht die Todos nach einer ID.
     *
     * @param id die ID
     * @return das Suchergebnis
     */
    public Optional<Todo> findById(long id) {
        return repo.findById(id).map(mapper::map);
    }

    /**
     * Fügt ein Item in den Datenbestand hinzu. Dabei wird eine ID generiert und zugewiesen.
     *
     * @param item das anzulegende Item mit leerer ID, hat nach der Operation eine ID
     * @throws IllegalArgumentException wenn die ID bereits belegt ist
     */
    public void create(@Valid Todo item) {
        Objects.requireNonNull(item);
        if (null != item.getId()) {
            throw new IllegalArgumentException("id must be null!");
        }
        item.setId(repo.save(mapper.map(item)).getId());
    }

    /**
     * Aktualisiert ein Item im Datenbestand.
     *
     * @param item das zu ändernde Item mit ID
     * @return <tt>true</tt>, wenn das Item gefunden und geändert wurde
     * @throws IllegalArgumentException wenn die ID nicht belegt ist
     */
    public boolean update(@Valid Todo item) {
        Objects.requireNonNull(item);
        if (null == item.getId()) {
            throw new IllegalArgumentException("id must not be null!");
        }
        // remove separat, um nicht neue Einträge hinzuzufügen (put allein würde auch ersetzen)
        if (repo.existsById(item.getId())) {
            repo.save(mapper.map(item));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Entfernt ein Item aus dem Datenbestand.
     *
     * @param id die ID des zu löschenden Items
     * @return <tt>true</tt>, wenn das Item gefunden und gelöscht wurde
     */
    public boolean delete(long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
