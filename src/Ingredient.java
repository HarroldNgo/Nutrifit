import java.util.ArrayList;
import java.util.List;

class Ingredient {
    private static int id = 1;
    private int ingredientId;
    private String ingredient;
    private int quantity;
    public Ingredient(String ingredient, int quantity){
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.ingredientId = id;
        id++;
    }
    public String getIngredient(){
        return this.ingredient;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public int getId(){
        return this.ingredientId;
    }
    public List<String> toStringList(){
        List<String> stringList = new ArrayList<>();
        stringList.add(""+this.getId());
        stringList.add(""+this.getIngredient());
        stringList.add(""+this.getQuantity());
        return stringList;
    }
}

class Nutrient {
    private static int id = 1;
    private int nutrientId;
    private String nutrient;
    private double amount;
    public Nutrient(String nutrient, double amount){
        this.nutrient = nutrient;
        this.amount = amount;
        this.nutrientId = id;
        id++;
    }
    public String getNutrient(){
        return this.nutrient;
    }
    public double getAmount(){
        return this.amount;
    }
    public int getId(){
        return this.nutrientId;
    }
    public List<String> toStringList(){
        List<String> stringList = new ArrayList<>();
        stringList.add(""+this.getId());
        stringList.add(""+this.getNutrient());
        stringList.add(""+this.getAmount());
        return stringList;
    }
}

