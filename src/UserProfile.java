import java.sql.Date;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This class initializes and manages a UserProfile
 * @author Harrold Ngo, Gabrielle Kossecki
 */
public class UserProfile {
    private static int id = 0;
    private int userId;
    private String name;
    private String sex;
    private Date dateOfBirth;
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
        //add to database

    }

    /**
     * Method for adding an instance of ExerciseLog to the UserProfile's
     * list of Exercise Logs
     * @param log
     */
    public void addExerciseLog(ExerciseLog log){
        exerciselogs.add(log);

        //add to database
    }

    /**
     * A method used in the constructor which uses it's dateofbirth,
     * height, and weight to calculate the BMR of the UserProfile
     * using the Mifflin St Jeor estimation formula
     */
    public void calculateBMR(){
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


    public static void main(String[] arg) throws ParseException {
        UserProfile user1 = new UserProfile("John", "Male", Date.valueOf("2000-10-01"),180, 60, "metric");
        UserProfile user2 = new UserProfile("Bob", "Male", Date.valueOf("2004-07-01"),200, 70, "metric");
        UserProfile user3 = new UserProfile("Lucy", "Female", Date.valueOf("1999-01-01"),170, 50, "metric");
        List<UserProfile> upl = new ArrayList<>();
        upl.add(user1);
        upl.add(user2);
        upl.add(user3);
        System.out.println("Name - Sex - DateOfBirth - Height - Weight - Units - BMR");
        for(UserProfile up : upl){
            System.out.println(up.getName()+" "+up.getSex()+" "+up.getDateOfBirth().toString()+" "+up.getHeight()+" "+up.getWeight()+" "+up.getUnits()+" "+up.getBMR());
            System.out.println();
        }
        System.out.println("Actual BMR calculated from website for these users:\n1615\n1860\n1282");

        DietLog d1 = new DietLog(Date.valueOf("2023-10-01"), "breakfast");
        d1.addIngredient("chicken", 2);
        d1.addNutrientValue("calories", 300);
        DietLog d2 = new DietLog(Date.valueOf("2023-10-02"), "lunch");
        d2.addIngredient("chicken", 1);
        d2.addNutrientValue("calories", 150);
        DietLog d3 = new DietLog(Date.valueOf("2023-10-01"), "dinner");
        d3.addIngredient("chicken", 3);
        d3.addNutrientValue("calories", 450);

        ExerciseLog e1 = new ExerciseLog(Date.valueOf("2023-10-01"),"walking", Duration.ofMinutes(60), "low");
        e1.setCaloriesBurnt(160);
        ExerciseLog e2 = new ExerciseLog(Date.valueOf("2023-10-02"),"running", Duration.ofMinutes(30), "high");
        e2.setCaloriesBurnt(350);
        ExerciseLog e3 = new ExerciseLog(Date.valueOf("2023-10-02"),"running", Duration.ofMinutes(30), "high");
        e3.setCaloriesBurnt(400);

        user1.addDietLog(d1);
        user1.addExerciseLog(e1);
        user2.addDietLog(d2);
        user2.addExerciseLog(e2);
        user3.addDietLog(d3);
        user3.addExerciseLog(e3);

        Main main = new Main();
        main.profileList.add(user1);
        main.profileList.add(user2);
        main.profileList.add(user3);
        main.startGUI();
    }


}
