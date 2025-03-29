package db;

import db.exception.EntityNotFoundException;

import java.util.ArrayList;

public class Database {
    private static ArrayList<Entity> entities = new ArrayList<>();
    public static int ID = 1;

    public static void add(Entity e) {
        if (e == null) {
            throw new IllegalArgumentException("Entity cannot be null");
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

    public static void delete(int id) {
        Entity entity = get(id);
        entities.remove(entity);
    }

    public static synchronized void update(Entity e) {
        if (e == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        Entity existingEntity = get(e.id);

        int index = entities.indexOf(existingEntity);
        entities.set(index, e.copy());
    }

}