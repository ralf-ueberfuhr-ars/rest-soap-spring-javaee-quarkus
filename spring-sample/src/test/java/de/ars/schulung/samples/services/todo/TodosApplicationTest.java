package de.ars.schulung.samples.services.todo;

import de.ars.schulung.samples.services.todo.TodosApplication;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.*;

/**
 * JUnit/Spring-Annotationen können transitiv genutzt werden.
 * Somit ist es möglich, @SpringBootTest und @AutoConfigureTestDatabase zu kapseln.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootTest(classes = TodosApplication.class)
@AutoConfigureTestDatabase
public @interface TodosApplicationTest {
}
