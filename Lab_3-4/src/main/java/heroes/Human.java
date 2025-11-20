package heroes;

import objects.Food;
import objects.Newspaper;

public abstract class Human {
    protected Newspaper newspaper;
    protected String name;
    protected int age;

    public Human(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void startEating(Food food) throws NoFoodException {
        if (food == null) throw new NoFoodException("No such food to eat.");

        System.out.println(name + " started eating...");
        food.startEating();
    }

    public void startEatingAll(int speed, Food... food) throws NoFoodException {
        if (food == null || food.length == 0) {
            throw new NoFoodException("No such food to eat.");
        }

        for (Food f : food) {
            startEating(f);
        }
    }

    public void endEating(Food food) throws NoFoodException {
        if (food == null) throw new NoFoodException("No such food to eat.");

        food.endEating();
        System.out.println(food + " was eating full!");
    }

    public void endEatingAll(Food... food) throws NoFoodException {
        if (food == null || food.length == 0) {
            throw new NoFoodException("No such food to eat.");
        }

        for (Food f : food) {
            System.out.println(name + " ended eating...");
            endEating(f);
        }
    }

    public void getNewspaper(Newspaper newspaper) {
        this.newspaper = newspaper;
    }

    public void giveNewspaper(Human human) {
        human.newspaper = newspaper;
        newspaper = null;
    }

    public void readNewspaper() {
        System.out.println("\n" + name + " has started reading newspaper...");
        long before = System.currentTimeMillis();
        newspaper.read();
        long after = System.currentTimeMillis();
        System.out.println(name + " has been reading newspaper in " + (int) ((after - before) / 1000) + " seconds!");
    }
}