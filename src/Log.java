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
    private static int id = 0;
    private int logId;
    private Date dateOfEntry;

    /**
     * Creates a Log with a given date and assigns an id
     * @param date the date of the log
     */
    public Log(Date date) {
        this.dateOfEntry = date;
        logId = id;
        id++;
    }

    /**
     * Accessor for the date variable
     * @return the date value of the log instance
     */
    public Date getDate(){
        return this.dateOfEntry;
    }

    /**
     * Accessor for the id
     * @return the id of the log instance
     */
    public int getId(){
        return this.logId;
    }

}

/**
 * This class initializes and manages
 * instances of a Diet Log
 *
 * @author Harrold Ngo, Gabrielle Kossecki
 */
class DietLog extends Log {
    private String mealType;
    private Map<String, Integer> ingredients;
    private Map<String, Double> nutrientValues;

    /**
     * Constructor for creating a new Diet Log with the given date and meal
     * @param date the date of the diet log
     * @param meal the specified meal type
     */
    public DietLog(Date date, String meal){
        super( date);
        this.mealType = meal;
        this.ingredients = new HashMap<>();
        this.nutrientValues = new HashMap<>();
    }

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
    public Map<String, Integer> getIngredients(){
        return this.ingredients;
    }

    /**
     * Accessor method for the list of nutrients
     * @return a map of the list of nutrients of the Diet Log instance
     */
    public Map<String, Double> getNutrients(){
        return this.nutrientValues;
    }

    /**
     * A method used to add an ingredient to this instance of Diet Log
     * @param ingredient the ingredient to be added
     * @param quantity the amount of said ingredient to be added
     */
    public void addIngredient(String ingredient, int quantity){
        this.ingredients.put(ingredient, quantity);

        //calculateNutrients();
    }

    /**
     * Method used to calculate nutrients based on the current list of ingredients
     * @param nutrientAmount list of nutrients from the database
     */
    public void calculateNutrients(List<List<String>> nutrientAmount){
        //Use nutrient amount, nutrient name, food group?, food name? from database to get the nutrient amounts
        /* Send FOOD NAME.FoodDescription (complete food name in english, equivalent to ingredient) to database
         * Get FOOD NAME.FOODID from FOOD NAME csv file
         * Send FOODID to NUTRIENT AMOUNT to obtain NutrientNameID and its corresponding NutrientValue
         * Send NutrientNameID to NUTRIENT NAME file to get corresponding NutrientName
         * Return (NutrientName, NutrientValue)
         * Maybe? Can revise later
         */
    }

    /**
     * Method used to add nutrient values to the list of nutrients
     * @param nutrient  the nutrient name
     * @param amount the amount of said nutrient to be added
     */
    public void addNutrientValue(String nutrient, double amount){
        this.nutrientValues.put(nutrient, amount);
    }

    /**
     * Accessor method for the list of nutrients of the Diet Log
     * @return a Map of the list of nutrients of the Diet Log
     */
    public Map<String, Double> getNutrientValues() {
        return this.nutrientValues;
    }

    public static void main(String[] arg) {
        DietLog d1 = new DietLog(Date.valueOf("2023-10-01"), "breakfast");
        d1.addIngredient("chicken", 2);
        d1.addNutrientValue("calories", 300);
        DietLog d2 = new DietLog(Date.valueOf("2023-10-02"), "lunch");
        d2.addIngredient("chicken", 1);
        d2.addNutrientValue("calories", 150);
        DietLog d3 = new DietLog(Date.valueOf("2023-10-01"), "dinner");
        d3.addIngredient("chicken", 3);
        d3.addNutrientValue("calories", 450);
        List<DietLog> dl = new ArrayList<>();
        dl.add(d1);
        dl.add(d2);
        dl.add(d3);
        for(DietLog d : dl){
            System.out.println(d.getDate().toString() + " " + d.getMealType());
            System.out.println(" Ingredients: ");
            for(Map.Entry<String,Integer> entry : d.getIngredients().entrySet()){
                System.out.println("  "+entry.getKey()+" "+entry.getValue());
            }
            System.out.println(" Nutrients: ");
            for(Map.Entry<String,Double> entry : d.getNutrients().entrySet()){
                System.out.println("  "+entry.getKey()+" "+entry.getValue());
            }
            System.out.println("");
        }
    }


}

/**
 * This class initializes and manages
 * instances of a Exercise Log
 *
 * @author Harrold Ngo
 */
class ExerciseLog extends Log {
    private String exerciseType;
    private Duration duration;
    private String intensity;
    private double caloriesBurnt = 0;

    /**
     * Constructor for creating an Exercise Log
     * @param date the date of the log to be created
     * @param exercise the exercise type of the log to be created
     * @param duration the duration of the log to be created
     * @param intensity the intensity of the log to be created
     */
    public ExerciseLog( Date date, String exercise, Duration duration, String intensity){
        super(date);
        this.exerciseType = exercise;
        this.duration = duration;
        this.intensity = intensity;
    }

    /**
     * Calculates calories burnt based on BMR and the Exercise Log's information
     * @param BMR the bmr of a user
     */
    public void calculateCaloriesBurnt(int BMR){

    }

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
    public Duration getDuration(){
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

    public static void main(String[] arg) {
        ExerciseLog e1 = new ExerciseLog(Date.valueOf("2023-10-01"),"walking", Duration.ofMinutes(60), "low");
        e1.setCaloriesBurnt(160);
        ExerciseLog e2 = new ExerciseLog(Date.valueOf("2023-10-02"),"running", Duration.ofMinutes(30), "high");
        e2.setCaloriesBurnt(350);
        ExerciseLog e3 = new ExerciseLog(Date.valueOf("2023-10-02"),"running", Duration.ofMinutes(30), "high");
        e3.setCaloriesBurnt(400);

        List<ExerciseLog> el = new ArrayList<>();
        el.add(e1);
        el.add(e2);
        el.add(e3);

        System.out.println("Date - ExerciseType - Duration - Intensity - Calories Burnt");
        for(ExerciseLog log : el){
            System.out.println(log.getDate().toString()+" "+log.getExerciseType()+" "+log.getDuration().toMinutes()+" "+log.getIntensity()+ " "+log.getCaloriesBurnt());
            System.out.println();
        }
    }
}