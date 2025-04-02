package db;

import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;



public class Database {
    private static ArrayList<Entity> entities = new ArrayList<>();
    private static HashMap<Integer, Validator> validators = new HashMap<>();
    public static int ID = 1;

    public static void add(Entity e) throws InvalidEntityException {
        if (e == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        Validator validator = validators.get(e.getEntityCode());
        if (validator == null) {
            throw new IllegalArgumentException("No Validator registered for this Entity code");
        }

        if (e instanceof Trackable) {
            Trackable trackableEntity = (Trackable) e;
            Date currentTimestamp = new Date();
            trackableEntity.setCreationDate(currentTimestamp);
            trackableEntity.setLastModificationDate(currentTimestamp);
        }

        validator.validate(e);

        e.id = ID;
        ID++;
        entities.add(e.copy());
    }

    public static Entity get(int id) {
        for (Entity e : entities) {
            if (e.id == id) {
                return e.copy();
            }
        }
        throw new EntityNotFoundException("Entity not found with id: " + id);
    }

    public static void delete(int id) {
        Entity entity = get(id);
        entities.remove(entity);
    }

    public static synchronized void update(Entity e) throws InvalidEntityException {
        if (e == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        Validator validator = validators.get(e.getEntityCode());
        if (validator == null) {
            throw new IllegalArgumentException("No Validator registered for this Entity code");
        }

        if (e instanceof Trackable) {
            Trackable trackableEntity = (Trackable) e;
            Date currentTimestamp = new Date();
            trackableEntity.setLastModificationDate(currentTimestamp);
        }

        validator.validate(e);

        Entity existingEntity = get(e.id);

        int index = entities.indexOf(existingEntity);
        entities.set(index, e.copy());
    }

    public static void registerValidator(int entityCode, Validator validator) {
        if (validators.containsKey(entityCode)) {
            throw new IllegalArgumentException("Validator با این entityCode قبلاً ثبت شده است.");
        }

        validators.put(entityCode, validator);
    }

}