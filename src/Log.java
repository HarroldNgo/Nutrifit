import java.sql.Time;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Log {
    Date dateOfEntry;

}
class DietLog extends Log {
    private String mealType;
    private Map<String, Integer> ingredients;
    private Map<String, Double> nutrientValues;

    public DietLog(Date date, String meal){
        this.dateOfEntry = date;
        this.mealType = meal;
        this.ingredients = new HashMap<>();
        this.nutrientValues = new HashMap<>();
    }
    public void addIngredient(String ingredient, int quantity){
        this.ingredients.put(ingredient, quantity);
    }
    public void calculateNutrients(){
        //Use nutrient amount, nutrient name, food group?, food name? from database to get the nutrient amounts
    }

    public void addNutrientValue(String nutrient, double amount){
        this.nutrientValues.put(nutrient, amount);
    }


}

class ExerciseLog extends Log {
    private String exerciseType;
    private Duration duration;
    private String intensity;
    private double caloriesBurnt = 0;
    public ExerciseLog(String exercise, Duration duration, String intensity){
        this.exerciseType = exercise;
        this.duration = duration;
        this.intensity = intensity;
    }
    public void calculateCaloriesBurnt(int BMR){

    }

}