package heroes;

import java.util.Objects;

public final class Boy extends Human {

    public Boy(String name, int age) {
        super(name, age);
    }

    public void think() {
        String thought;

        if (age <= 3) {
            thought = "sits quietly and thinks about where to hide their favorite toy";
        } else if (age <= 7) {
            thought = "sits quietly and thinks about their best friend and their next adventures";
        } else if (age <= 12) {
            thought = "sits quietly and thinking about his friend...";
        } else if (age <= 17) {
            thought = "withdrawn in thoughts, contemplating the future and their friends";
        } else {
            thought = "deeply reflects on the value of friendship and shared memories";
        }

        System.out.println(name + " " + thought);
    }

    public void think(String thought) {
        System.out.println(name + " " + thought);
    }

    @Override
    public void giveNewspaper(Human human) {
        System.out.println(name + " pushed the newspaper towards " + human.name);
        human.newspaper = newspaper;
        newspaper = null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Boy other)) return false;
        return other.name.equals(name) && other.age == age;
    }

    @Override
    public String toString() {
        return name + " " + age + " y.o.";
    }
}