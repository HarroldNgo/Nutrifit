import java.sql.Time;
import java.time.Duration;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An abstract class used to help with the initialization of all types of
 * logs and their shared elements.
 *
 * @author Harrold Ngo
 */
public abstract class Log {
    private Date dateOfEntry;

    /**
     * Creates a Log with a given date and assigns an id
     * @param date the date of the log
     */
    public Log(Date date) {
        this.dateOfEntry = date;
    }

    /**
     * Accessor for the date variable
     * @return the date value of the log instance
     */
    public Date getDate(){
        return this.dateOfEntry;
    }

}

/**
 * This class initializes and manages
 * instances of a Diet Log
 *
 * @author Harrold Ngo, Gabrielle Kossecki
 */
class DietLog extends Log {
    private static int id = 1;
    private int dietLogId;
    private String mealType;
    private List<Ingredient> ingredients;
    private List<Nutrient> nutrients;
    private double calories;


    /**
     * Constructor for creating a new Diet Log with the given date and meal
     * @param date the date of the diet log
     * @param meal the specified meal type
     */
    public DietLog(Date date, String meal){
        super(date);
        this.mealType = meal;
        this.ingredients = new ArrayList<>();
        this.nutrients = new ArrayList<>();
        this.dietLogId = id;
        id++;
    }
    public int getDietLogId(){ return this.dietLogId; };

    /**
     * Accessor method for the meal type
     * @return a String of the meal type of the Diet Log instance
     */
    public String getMealType(){
        return this.mealType;
    }

    /**
     * Accessor method for the list of ingredients
     * @return a map of the list of ingredients of the Diet Log instance
     */
    public List<Ingredient> getIngredients(){
        return this.ingredients;
    }

    /**
     * Accessor method for the list of nutrients
     * @return a map of the list of nutrients of the Diet Log instance
     */
    public List<Nutrient> getNutrients(){
        return this.nutrients;
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }
    /**
     * A method used to add an ingredient to this instance of Diet Log
     * @param ingredient the ingredient to be added
     * @param quantity the amount of said ingredient to be added
     */
    public void addIngredient(String ingredient, int quantity){
        Ingredient newIngredient = new Ingredient(ingredient, quantity);
        this.ingredients.add(newIngredient);
        DataManager dm = JDBC.getInstance();
        List<String> stringList = newIngredient.toStringList();
        stringList.add(""+this.getDietLogId());
        dm.addData("dietlogingredients", stringList);
    }
    public void setIngredients(Map<String, Integer> ingredients){
        for(Map.Entry<String, Integer> ingredient : ingredients.entrySet()){
            addIngredient(ingredient.getKey(), ingredient.getValue());
        }
        calculateNutrients();
        setCalories();
    }

    /**
     * Method used to calculate nutrients based on the current list of ingredients
     */
    public void calculateNutrients(){
        DataManager dm = JDBC.getInstance();
        Map<String, Double> newNutrients = new HashMap<>();
        for(Ingredient ingredient : ingredients){
            List<List<String>> nutrients = dm.fetchNutrients(ingredient.getIngredient());
            for(List<String> nutrient : nutrients){
                if(newNutrients.get(nutrient.get(0))==null)
                    newNutrients.put(nutrient.get(0), ingredient.getQuantity()*Double.parseDouble(nutrient.get(1)));
                else
                    newNutrients.put(nutrient.get(0), newNutrients.get(nutrient.get(0)) + ingredient.getQuantity()*Double.parseDouble(nutrient.get(1)));
            }
        }
        setNutrientValues(newNutrients);
    }

    /**
     * Method used to add nutrient values to the list of nutrients
     */
    public void setNutrientValues(Map<String, Double> nutrients){
        for(Map.Entry<String, Double> nutrient : nutrients.entrySet()){
            addNutrient(nutrient.getKey(), nutrient.getValue());
        }
    }
    public void addNutrient(Nutrient nutrient){
        this.nutrients.add(nutrient);
        if(nutrient.getNutrient().compareTo("ENERGY (KILOCALORIES)")==0){
            calories = nutrient.getAmount();
        }
    }
    public void addNutrient(String nutrient, double amount){
        Nutrient newNutrient = new Nutrient(nutrient, amount);
        this.nutrients.add(newNutrient);
        DataManager dm = JDBC.getInstance();
        List<String> stringList = newNutrient.toStringList();
        stringList.add(""+this.getDietLogId());
        dm.addData("dietlognutrients", stringList);
    }
    public void setCalories(){
        for(Nutrient nutrient : nutrients){
            if(nutrient.getNutrient().compareTo("ENERGY (KILOCALORIES)")==0){
                calories = nutrient.getAmount();
                break;
            }
        }
    }
    public double getCalories(){
        return this.calories;
    }
    public List<String> toStringList(){
        List<String> stringList = new ArrayList<>();
        stringList.add(""+this.getDietLogId());
        stringList.add(""+this.getDate().toString());
        stringList.add(""+this.getMealType());
        return stringList;
    }

    public static void main(String[] arg) {

    }


}

/**
 * This class initializes and manages
 * instances of a Exercise Log
 *
 * @author Harrold Ngo
 */
class ExerciseLog extends Log {
    private static int id = 1;
    private int exerciseLogId;
    private Time timeOfEntry;
    private String exerciseType;
    private int duration;
    private String intensity;
    private double caloriesBurnt = 0;

    /**
     * Constructor for creating an Exercise Log
     * @param date the date of the log to be created
     * @param exercise the exercise type of the log to be created
     * @param duration the duration of the log to be created
     * @param intensity the intensity of the log to be created
     */
    public ExerciseLog( Date date, Time time, String exercise, int duration, String intensity){
        super(date);
        this.timeOfEntry = time;
        this.exerciseType = exercise;
        this.duration = duration;
        this.intensity = intensity;
        this.exerciseLogId = id;
        id++;
    }
    public int getExerciseLogId(){ return this.exerciseLogId; }

    public Time getTime(){ return this.timeOfEntry; }
    /**
     * Accessor method for the type of exercise
     * @return a String for the type of exercise of the Exercise Log
     */
    public String getExerciseType(){
        return this.exerciseType;
    }

    /**
     * Accessor method for the duration
     * @return a Duration type variable of the Exercise Log
     */
    public int getDuration(){
        return this.duration;
    }

    /**
     * Accessor method for the intensity
     * @return a String of the intensity of the Exercise Log
     */
    public String getIntensity(){
        return this.intensity;
    }

    /**
     * Accessor method for the calories burnt
     * @return a double of the calories burnt of the Exercise Log
     */
    public double getCaloriesBurnt(){
        return this.caloriesBurnt;
    }

    //TEMP METHOD FOR TESTING
    public void setCaloriesBurnt(double caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
    }

    /**
     * Calculates calories burnt based on BMR and the Exercise Log's information
     * @param BMR the bmr of a user
     */
    public void calculateCaloriesBurnt(double BMR){
        double met = 0;
        if(this.getIntensity().equals("Low")) met = 1;
        else if(this.getIntensity().equals("Medium")) met = 3;
        else if(this.getIntensity().equals("High")) met = 6;
        else if(this.getIntensity().equals("Very High")) met = 9;
        double caloriesPer = (met * 3.5 * BMR)/5000;
        double calories = caloriesPer*this.getDuration();
        this.setCaloriesBurnt(calories);
    }

    public List<String> toStringList(){
        List<String> stringList = new ArrayList<>();
        stringList.add(""+this.getExerciseLogId());
        stringList.add(""+this.getDate().toString());
        stringList.add(""+this.getTime().toString());
        stringList.add(""+this.getExerciseType());
        stringList.add(""+this.getDuration());
        stringList.add(""+this.getIntensity());
        stringList.add(""+this.getCaloriesBurnt());
        return stringList;
    }

    public static void main(String[] arg) {

    }
}