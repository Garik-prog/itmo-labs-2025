package objects;

import java.util.Objects;

public final class Porridge extends Food {

    public Porridge(String name) {
        super(name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getIngredients());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Porridge)) return false;
        Porridge other = (Porridge) obj;
        return getName().equals(other.getName()) && Objects.equals(getIngredients(), other.getIngredients());
    }

    @Override
    public String toString() {
        return getName() + " consists of " + getIngredients();
    }
}