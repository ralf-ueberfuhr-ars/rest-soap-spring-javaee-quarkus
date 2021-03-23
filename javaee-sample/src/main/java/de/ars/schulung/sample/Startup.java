package de.ars.schulung.sample;

import de.ars.schulung.sample.control.TodoManager;
import de.ars.schulung.sample.entity.Todo;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class Startup {

    @Inject
    TodoManager manager;

    public void initData(@Observes @Initialized(ApplicationScoped.class) Object init) {
        if(manager.getTodos().isEmpty()) {
            manager.add(new Todo("Eigene Todos anlegen", null));
        }
    }

}
