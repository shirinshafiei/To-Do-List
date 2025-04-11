package db;

import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


public class Database {
    private static ArrayList<Entity> entities = new ArrayList<>();
    private static HashMap<Integer, Validator> validators = new HashMap<>();
    public static int ID = 1;

    public static void add(Entity e) throws InvalidEntityException {
        if (e == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        Validator validator = validators.get(e.getEntityCode());
        if (validator != null) {
            validator.validate(e);
        }

        if (e instanceof Trackable) {
            Trackable trackableEntity = (Trackable) e;
            Date currentTimestamp = new Date();
            trackableEntity.setCreationDate(currentTimestamp);
            trackableEntity.setLastModificationDate(currentTimestamp);
        }

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

    public static boolean delete(int id) {
        synchronized (entities) {
            Iterator<Entity> iterator = entities.iterator();
            while (iterator.hasNext()) {
                Entity e = iterator.next();
                if (e.id == id) {
                    iterator.remove();
                    return true;
                }
            }
        }
        return false;
    }


    public static synchronized void update(Entity e) throws InvalidEntityException {
        if (e == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        Validator validator = validators.get(e.getEntityCode());
        if (validator != null) {
            validator.validate(e);
        }

        if (e instanceof Trackable) {
            Trackable trackableEntity = (Trackable) e;
            Date currentTimestamp = new Date();
            trackableEntity.setLastModificationDate(currentTimestamp);
        }

        boolean found = false;
        for (int i = 0; i < entities.size(); i++) {
            Entity original = entities.get(i);
            if (original.id == e.id) {
                entities.set(i, e.copy());
                found = true;
                break;
            }
        }

        if (!found) {
            throw new EntityNotFoundException("Entity not found with id: " + e.id);
        }
    }

    public static void registerValidator(int entityCode, Validator validator) {
        if (validators.containsKey(entityCode)) {
            throw new IllegalArgumentException("Validator با این entityCode قبلاً ثبت شده است.");
        }

        validators.put(entityCode, validator);
    }

    public static ArrayList<Entity> getAll(int entityCode) {
        ArrayList<Entity> filteredEntities = new ArrayList<>();

        for (Entity entity : entities) {
            if (entity.getEntityCode() == entityCode) {
                filteredEntities.add(entity.copy());
            }
        }

        return filteredEntities;
    }

}