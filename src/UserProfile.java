import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class initializes and manages a UserProfile
 * @author Harrold Ngo, Gabrielle Kossecki
 */
public class UserProfile {
    private static int id = 1;
    private int userId;
    private String name;
    private String sex;
    private Date dateOfBirth;
    private int age;
    private double height;
    private double weight;
    private String units;
    private double BMR;
    private List<DietLog> dietlogs;
    private List<ExerciseLog> exerciselogs;

    /**
     * Constructor for creating a UserProfile
     * @param name
     * @param sex
     * @param date
     * @param height
     * @param weight
     * @param units
     */
    public UserProfile(String name, String sex, Date date, double height, double weight, String units){
        this.userId = id;
        this.id++;
        this.name = name;
        this.sex=sex;
        this.dateOfBirth = date;
        this.height = height;
        this.weight = weight;
        this.units = units;
        calculateBMR();
        dietlogs = new ArrayList<>();
        exerciselogs = new ArrayList<>();
        //add to database
    }

    /**
     * Method that takes in new values and edits the current UserProfile
     * @param newName
     * @param newSex
     * @param newDate
     * @param newHeight
     * @param newWeight
     * @param newUnits
     */
    public void editUserProfile(String newName, String newSex, Date newDate, double newHeight, double newWeight, String newUnits){
        this.name = newName;
        this.sex = newSex;
        this.dateOfBirth = newDate;
        this.height = newHeight;
        this.weight = newWeight;
        this.units = newUnits;
        this.calculateBMR();
        //update to database
    }

    public int getId(){return this.userId;}
    /**
     * Accessor method for the name
     * @return a String of the name of the UserProfile
     */
    public String getName(){
        return this.name;
    }

    /**
     * Accessor method for the sex
     * @return a String of the sex of the UserProfile
     */
    public String getSex(){
        return this.sex;
    }

    /**
     * Accessor method for the date of birth
     * @return a Date of the date of birth of the UserProfile
     */
    public Date getDateOfBirth(){
        return this.dateOfBirth;
    }

    /**
     * Accessor method for the height
     * @return a double of the height of the UserProfile
     */
    public double getHeight(){
        return this.height;
    }

    /**
     * Accessor method for the weight
     * @return a double of the weight of the UserProfile
     */
    public double getWeight(){
        return this.weight;
    }

    /**
     * Accessor method for the units
     * @return a String of the units of the UserProfile
     */
    public String getUnits(){return this.units;}

    /**
     * Accessor method for the BMR
     * @return a double of the BMR of the UserProfile
     */
    public double getBMR(){return this.BMR;}

    /**
     * Accessor method for the list of diet logs
     * @return a List of DietLogs of the UserProfile
     */
    public List<DietLog> getDietLogs(){
        return this.dietlogs;
    }

    /**
     * Accessor method for the list of exercise logs
     * @return a List of ExerciseLogs of the UserProfile
     */
    public List<ExerciseLog> getExerciseLogs(){
        return this.exerciselogs;
    }

    /**
     * Method for adding an instance of DietLog to the UserProfile's
     * list of Diet Logs
     * @param log
     */
    public void addDietLog(DietLog log){
        dietlogs.add(log);
    }
    public void addDietLog(Date date, String meal, Map<String, Integer> ingredients){
        DietLog dietLog = new DietLog(date, meal);
        dietlogs.add(dietLog);
        //add to database
        DataManager dm = JDBC.getInstance();
        List<String> data = dietLog.toStringList();
        data.add(""+this.getId());
        dm.addData("dietlogs", data);
        dietLog.setIngredients(ingredients);
    }

    /**
     * Method for adding an instance of ExerciseLog to the UserProfile's
     * list of Exercise Logs
     * @param log
     */
    public void addExerciseLog(ExerciseLog log){
        exerciselogs.add(log);
    }
    public void addExerciseLog(Date date, Time time, String exerciseType, int duration, String intensity){
        ExerciseLog exerciseLog = new ExerciseLog(date, time, exerciseType, duration, intensity);
        exerciseLog.calculateCaloriesBurnt(this.BMR);
        exerciselogs.add(exerciseLog);
        //add to database
        DataManager dm = JDBC.getInstance();
        List<String> data = exerciseLog.toStringList();
        data.add(""+this.getId());
        dm.addData("exerciselogs", data);
    }

    /**
     * A method used in the constructor which uses it's dateofbirth,
     * height, and weight to calculate the BMR of the UserProfile
     * using the Mifflin St Jeor estimation formula
     */
    public void calculateBMR(){
        setAge();
        double bmr;

        //Male and female use slightly different formulas
        if(this.sex=="Female") {
            bmr = (10*this.weight) + (6.25*this.height) - (5*age) - 161;
        }
        else{
            bmr = (10*this.weight) + (6.25*this.height) - (5*age) + 5;
        }

        this.BMR = bmr;
    }
    public void setAge(){
        //Gets the date of birth in the form of integers for year, month, day
        LocalDate localDate = dateOfBirth.toLocalDate();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();

        //Gets the current date in the form of integers for year, month, day
        LocalDate currentDate = LocalDate.now();
        int cyear = currentDate.getYear();
        int cmonth = currentDate.getMonthValue();
        int cday = currentDate.getDayOfMonth();

        //Calculates the age by subtracting the current year and the date of birth year
        //It also factors in the month and day**
        int age = (((cyear*365) + (cmonth*31) + cday)/365)-(((year*365) + (month*31) + day)/365);
        this.age = age;
    }
    public int getAge(){
        return this.age;
    }
    public List<String> toStringList(){
        List<String> stringList = new ArrayList<>();
        stringList.add(""+userId);
        stringList.add(""+name);
        stringList.add(""+sex);
        stringList.add(""+dateOfBirth.toString());
        stringList.add(""+height);
        stringList.add(""+weight);
        stringList.add(""+BMR);
        stringList.add(""+units);
        return stringList;
    }


    public static void main(String[] arg) throws ParseException {

    }


}
