package objects;

import java.util.ArrayList;

public abstract class Food implements Eatable {
    private final String name;
    private final ArrayList<Food> ingredients;

    public Food(String name) {
        this.name = name;
        this.ingredients = new ArrayList<>();
    }

    public void addIngredient(Food ingredient) {
        this.ingredients.add(ingredient);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Food> getIngredients() {
        return ingredients;
    }

    @Override
    public void startEating() {
        for (Food ingredient : ingredients) {
            ingredient.startEating();
        }
    }

    @Override
    public void endEating() {
        for (Food ingredient : ingredients) {
            ingredient.endEating();
        }
    }
}