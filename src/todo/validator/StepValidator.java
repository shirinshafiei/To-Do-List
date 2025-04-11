package todo.validator;

import db.Database;
import db.Entity;
import db.Validator;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;

public class StepValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if (!(entity instanceof Step)) {
            throw new IllegalArgumentException("The input must be of type Step.");
        }

        Step step = (Step) entity;

        if (step.getTitle() == null) {
            throw new InvalidEntityException("The title field must not be empty.");
        }

        try {
            Entity task = Database.get(step.getTaskRef());
            if (!(task instanceof Task)) {
                throw new InvalidEntityException("The taskRef does not link to a valid Task.");
            }
        } catch (EntityNotFoundException e) {
            throw new InvalidEntityException("The taskRef does not exist in the database.");
        }
    }
}