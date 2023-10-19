import java.sql.Time;
import java.time.Duration;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Log {
    Date dateOfEntry;
    public Date getDate(){
        return this.dateOfEntry;
    }

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
    public void calculateNutrients(<List<List<String>> nutrientAmount){
        //Use nutrient amount, nutrient name, food group?, food name? from database to get the nutrient amounts
        /* Send FOOD NAME.FoodDescription (complete food name in english, equivalent to ingredient) to database
    	 * Get FOOD NAME.FOODID from FOOD NAME csv file
    	 * Send FOODID to NUTRIENT AMOUNT to obtain NutrientNameID and its corresponding NutrientValue
    	 * Send NutrientNameID to NUTRIENT NAME file to get corresponding NutrientName
    	 * Return (NutrientName, NutrientValue)
    	 * Maybe? Can revise later
    	 */
    }

    public void addNutrientValue(String nutrient, double amount){
        this.nutrientValues.put(nutrient, amount);
    }
    public Map<String, Double> getNutrientValues() {
        return this.nutrientValues;
    }


}

class ExerciseLog extends Log {
    private String exerciseType;
    private Duration duration;
    private String intensity;
    private double caloriesBurnt = 0;
    public ExerciseLog( Date date, String exercise, Duration duration, String intensity){
        this.dateOfEntry = date;
        this.exerciseType = exercise;
        this.duration = duration;
        this.intensity = intensity;
    }
    public void calculateCaloriesBurnt(int BMR){

    }
    public double getCaloriesBurnt(){
        return this.caloriesBurnt;
    }

    //TEMP METHOD FOR TESTING
    public void setCaloriesBurnt(double caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
    }
}
