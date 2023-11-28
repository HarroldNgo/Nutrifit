import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * A class used to manage/control data received from DataManager
 * Initializes and stores all UserProfiles, DietLogs and ExerciseLogs from DataManager
 * Provides/Manages data across all major use case classes
 * (i.e. Visualizers need Diet Logs, GUI needs Visualizers, etc.)
 * Provides the GUI with all necessary information to display
 *
 * @author Harrold Ngo
 */
public class Main extends Observable {
    private static Main mainInstance;
    DataManager dm;

    List<UserProfile> profileList;

    List<List<String>> foodName;

    UserProfile selectedProfile;

    GUI gui;
    List<ProfileObserver> observers = new ArrayList<>();
    /**
     * Constructor for creating an instance of the Main class
     * Initializes database
     * Fetches all necessary information for app launch
     * @throws ParseException
     */
    private Main () throws ParseException {
        dm = new DataManager(JDBC.getInstance());
        this.profileList = dataToUserProfileList();

        this.foodName = dm.fetchData("food name");

    }
    public static Main getInstance() throws ParseException {
        if(mainInstance==null) mainInstance = new Main();
        return mainInstance;
    }
    /**
     * Method used to initialize an instance of the GUI
     * which will display the screen
     */
    public void startGUI() throws ParseException {
        this.gui = new GUI();
    }
    public List<UserProfile> getProfileList(){
        return this.profileList;
    }
    public void addProfile(String name, String sex, Date date, double height, double weight, String units) throws ParseException {
        UserProfile newProfile = new UserProfile(name, sex, date, height, weight, units);
        this.profileList.add(newProfile);
        this.gui.updateUserSelectionScreen();
        //Add to database
        List<String> data = newProfile.toStringList();
        dm.addData("userprofile", data);
    }
    public void updateProfile(String name, String sex, Date date, double height, double weight, String units){
        selectedProfile.editUserProfile(name, sex, date, height, weight, units);
        //update to database
        List<String> columns = new ArrayList<>();
        columns.add("idUserProfile");
        columns.add("Name");
        columns.add("Sex");
        columns.add("Date");
        columns.add("Height");
        columns.add("Weight");
        columns.add("BMR");
        columns.add("units");
        List<String> data = selectedProfile.toStringList();
        dm.updateData("userprofile",columns, data);
        setChanged();
        notifyObservers();
    }
    public void setSelectedProfile(UserProfile selectedProfile) {
        this.selectedProfile = selectedProfile;
        setChanged();
        notifyObservers();
    }
    public UserProfile getSelectedProfile() {
        return selectedProfile;
    }
    public void addDietLog(Date date, String mealType, Map<String, Integer> ingredients) throws Exception {
        for(DietLog dietLog : selectedProfile.getDietLogs()){
            if(date.equals(dietLog.getDate())){
                if(!mealType.equals("Snack") && mealType.equals(dietLog.getMealType())){
                    throw new Exception("Only 1 of Breakfast, Lunch or Dinner per day");
                }
            }
        }
        selectedProfile.addDietLog(date, mealType, ingredients);
        this.gui.updateDietLogsScreen();
    }

    public void addExerciseLog(Date date, Time time, String exerciseType, int duration, String intensity){
        selectedProfile.addExerciseLog(date, time, exerciseType, duration, intensity);
        this.gui.updateExerciseLogsScreen();
    }
    public void addObserver(ProfileObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        for (ProfileObserver observer : observers) {
            observer.updateOnProfileSelected(selectedProfile);
        }
    }

    /**
     * Fetches the data from the database and formats them in the form of a List
     * of UserProfile as the data from the database is all Strings
     * @return a list of UserProfiles along with their respective DietLogs and ExerciseLogs
     */
    public List<UserProfile> dataToUserProfileList() {
        List<UserProfile> listOfProfiles = new ArrayList<>();
        List<List<String>> profileList = dm.fetchData("UserProfile");
        for(List<String> row : profileList) {
            UserProfile userprofile = new UserProfile(row.get(1), row.get(2), Date.valueOf(row.get(3)), Double.parseDouble(row.get(4)), Double.parseDouble(row.get(5)), row.get(7));
            List<List<String>> dietLogList = dm.fetchSpecificData("dietlogs", "iduserprofile", row.get(0));
            List<List<String>> exerciseLogList = dm.fetchSpecificData("exerciselogs", "iduserprofile", row.get(0));
            for(List<String> dietList : dietLogList){
                DietLog dietlog = new DietLog(Date.valueOf(dietList.get(1)), dietList.get(2));
                userprofile.addDietLog(dietlog);

                List<List<String>> ingredientList = dm.fetchSpecificData("dietlogingredients", "iddietlog", dietList.get(0));
                for(List<String> rowIngredient : ingredientList){
                    Ingredient ingredient = new Ingredient(rowIngredient.get(1), Integer.parseInt(rowIngredient.get(2)));
                    dietlog.addIngredient(ingredient);
                }
                List<List<String>> nutrientList = dm.fetchSpecificData("dietlognutrients", "iddietlog", dietList.get(0));
                for(List<String> rowNutrient : nutrientList){
                    Nutrient nutrient = new Nutrient(rowNutrient.get(1), Double.parseDouble(rowNutrient.get(2)));
                    dietlog.addNutrient(nutrient);
                }
            }
            listOfProfiles.add(userprofile);
            for(List<String> exerciseList : exerciseLogList){
                ExerciseLog exerciselog = new ExerciseLog(Date.valueOf(exerciseList.get(1)), Time.valueOf(exerciseList.get(2)), exerciseList.get(3), Integer.parseInt(exerciseList.get(4)), exerciseList.get(5));
                exerciselog.setCaloriesBurnt(Double.parseDouble(exerciseList.get(6)));
                userprofile.addExerciseLog(exerciselog);
            }
        }
        return listOfProfiles;
    }
    public static void main(String[] arg) throws ParseException {

    }
}

