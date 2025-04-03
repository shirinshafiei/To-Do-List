package todo.validator;

import db.Entity;
import db.Validator;
import db.exception.InvalidEntityException;
import example.Human;
import todo.entity.Task;

public class TaskValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if (!(entity instanceof Task)) {
            throw new IllegalArgumentException("The input must be of type Task.");
        }

        if (((Task) entity).getTitle() == null) {
            throw new InvalidEntityException("The title field must not be empty.");
        }
    }
}
