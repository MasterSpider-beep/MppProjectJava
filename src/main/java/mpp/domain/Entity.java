package mpp.domain;

import java.util.Objects;

public class Entity {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return id == entity.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    protected int id;

    public Entity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
