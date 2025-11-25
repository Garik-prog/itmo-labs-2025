package objects;

import java.util.Objects;

public class Grain extends Food {
    GrainType grainType;

    public Grain(GrainType grainType) {
        super("Grain");
        this.grainType = grainType;
    }

    @Override
    public void startEating() {
        System.out.println("\tGrain " + grainType + " is cracking!");
    }

    @Override
    public void endEating() {
        System.out.println("\tGrain " + grainType + " was eating");
    }

    @Override
    public int hashCode() {
        return Objects.hash(grainType);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Grain)) return false;
        Grain other = (Grain) obj;

        return Objects.equals(other.grainType, grainType);
    }

    @Override
    public String toString() {
        return "Grain { grainType = " + grainType + " }";
    }
}