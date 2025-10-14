package ru.ifmo.lab2.moves;

interface Run {
    default void run() {
        // todo: implement it...
    }

    private void ask() {
        dump();
    }

    private void dump() {
        ask();
        final int cF = c + 10;
        System.out.println(cF);
    }

    int c = 10;
}

public abstract class Animal {
    private int age;
    private String name;

    public Animal(int age, String name) {
        this.age = age;
        this.name = name;
    }

    protected void eat() {
        System.out.println("Animal is eating");
    }
}

//
//    /*package-private*/  public void eat() {}
//
//    /*package-private*/  public  void sleep(int durationHours) {}
//}
//
class Cat extends Animal {

    public Cat(int age, String name) {
        super(age, name);
    }

    public void eat() {
        System.out.println("Cat is eating");
    }
}
//    public void sleep(int durationHours) {
//        System.out.println("Cat is sleeping " + durationHours + " hours");
//    }
//}
//
//class Dog extends Animal {
//
//
//    public Dog(int age, String name) {
//        super(age, name);
//    }
//
//        System.out.println("Dog is eating");
//    }

//    public void sleep(int durationHours) {
//        System.out.println("Dog is sleeping " + durationHours + " hours");
//    }
//
//}
//
class Runner {
    public static void main(String[] args) {
        Animal cat = new Cat(10, "Tom");

        cat.eat();
//        Animal a = new Animal();
//        a.eat();
//        Animal dog = new Dog();

//        new Runner().dump(cat);
//        new Runner().dump(dog);
    }
}