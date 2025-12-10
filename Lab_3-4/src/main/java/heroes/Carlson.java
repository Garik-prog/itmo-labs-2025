package heroes;

import objects.Food;
import objects.Porridge;

import java.util.Objects;


public final class Carlson extends Human implements Flyable {
    private static final int FIVE_KILOGRAMS_PER_HOUR = 5;

    public Carlson(String name, int age) {
        super(name, age);
    }

    @Override
    public void startEating(Food food) throws NoFoodException {
        super.startEating(food);
    }

    public void startEating(Food food, int speed) throws NoFoodException {
        super.startEating(food);
        if (speed >= FIVE_KILOGRAMS_PER_HOUR) {
            System.out.println("It is cracking behind ears!");
        }
    }

    @Override
    public void endEating(Food food) throws NoFoodException {
        super.endEating(food);
        if (food.getClass().equals(Porridge.class)) {
            System.out.println(name + " is chewing loudly... As always...");
        }
    }

    @Override
    public void fly() {
        System.out.println(name + " started flying!");
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Carlson other)) return false;

        return other.name.equals(name) && other.age == age;
    }
}
