package objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class Brioche extends Food {
    private final List<Grain> grains;
    private final BriocheType type;

    @Override
    public int hashCode() {
        return Objects.hash(grains, type);
    }

    @Override
    public boolean equals(Object obj) {
        if  (obj == null) return false;
        if (!(obj instanceof Brioche other)) return false;
        if (obj.hashCode() != hashCode()) return false;
        if (this == obj) return true;
        return Objects.equals(grains, other.grains) && type == other.type;
    }

    @Override
    public String toString() {
        if (grains.isEmpty()) {
            return "Brioche " + type + " without grains";
        } else {
            return "Brioche " + type + " with grains";
        }
    }

    public Brioche(BriocheType type, Grain... grains) {
        super("Brioche");
        this.grains = new ArrayList<>();
        this.type = type;
        addGrains(grains);
    }

    @Override
    public void endEating() {
        super.endEating();
        if (grains.isEmpty()) {
            System.out.println("Brioche " + type + " without grains was eaten!");
        } else {
            for (Grain grain : grains) {
                grain.endEating();
            }
        }
    }

    @Override
    public void startEating() {
        super.startEating();
        if (grains.isEmpty()) {
            System.out.println("Brioche " + type + " without grains!");
        } else {
            System.out.println("Start eating Brioche " + type);

            for (Grain grain : grains) {
                grain.startEating();
            }
        }
    }

    public void addGrains(Grain... grains) {
        this.grains.addAll(Arrays.asList(grains));
    }
}
