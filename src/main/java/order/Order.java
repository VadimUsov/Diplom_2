package order;

import methods.Ingredients;

import java.util.ArrayList;
import java.util.Random;

public class Order {
    static String wrongPart = "qw";
    static Ingredients ingredient = new Ingredients();
    static ArrayList<String> ingredientsList = ingredient.getIngredients();
    static Random random = new Random();
    private String[] ingredients;

    public Order(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public Order() {
    }

    public static String[] getRandomRecipe() {
        return new String[]{ingredientsList.get(random.nextInt(14)), ingredientsList.get(random.nextInt(14))};
    }

    public static String[] getWrongRecipe() {
        return new String[]{ingredientsList.get(random.nextInt(14)) + wrongPart, ingredientsList.get(random.nextInt(14))};
    }

    public String[] getIngredientOfOrder() {
        return ingredients;
    }

    public void setIngredientOfOrder(String[] ingredientOfOrder) {
        this.ingredients = ingredientOfOrder;
    }
}
