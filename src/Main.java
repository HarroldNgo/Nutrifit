import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A class used to manage/control data received from DataManager
 * Initializes and stores all UserProfiles, DietLogs and ExerciseLogs from DataManager
 * Provides/Manages data across all major use case classes
 * (i.e. Visualizers need Diet Logs, GUI needs Visualizers, etc.)
 * Provides the GUI with all necessary information to display
 *
 * @author Harrold Ngo
 */
public class Main {
    DataManager dm;

    List<UserProfile> profileList;

    List<List<String>> foodName;
    List<List<String>> nutrientName;
    List<List<String>> nutrientAmount;

    UserProfile selectedProfile;

    GUI gui;

    /**
     * Constructor for creating an instance of the Main class
     * Initializes database
     * Fetches all necessary information for app launch
     * @throws ParseException
     */
    public Main () throws ParseException {
        this.dm = JDBC.getInstance();
        this.profileList = dataToUserProfileList();

        this.foodName = dm.fetchData("food name");
        this.nutrientName = dm.fetchData("nutrient name");
        this.nutrientAmount = dm.fetchData("nutrient amount");

    }

    /**
     * Method used to initialize an instance of the GUI
     * which will display the screen
     */
    public void startGUI(){
        gui = new GUI(this);
        gui.createUserSelectionPanel(this.profileList);
    }

    /**
     * Fetches the data from the database and formats them in the form of a List
     * of UserProfile as the data from the database is all Strings
     * @return a list of UserProfiles along with their respective DietLogs and ExerciseLogs
     * @throws ParseException
     */
    public List<UserProfile> dataToUserProfileList() throws ParseException {
        List<UserProfile> listOfProfiles = new ArrayList<>();
        List<List<String>> profileList = dm.fetchData("UserProfile");
        for(List<String> row : profileList) {
            UserProfile userprofile = new UserProfile(row.get(1), row.get(2), Date.valueOf(row.get(3)), Double.parseDouble(row.get(4)), Double.parseDouble(row.get(5)), row.get(6));
            List<List<String>> dietLogList = dm.fetchDataForeignKey("dietlogs", "iduserprofile", Integer.parseInt(row.get(0)));
            for(List<String> dietList : dietLogList){
                DietLog dietlog = new DietLog(Date.valueOf(dietList.get(1)), dietList.get(2));
                userprofile.addDietLog(dietlog);

                List<List<String>> ingredientList = dm.fetchDataForeignKey("dietlogingredients", "iddietlog", Integer.parseInt(dietList.get(0)));
                for(List<String> rowIngredient : ingredientList){
                    dietlog.addIngredient(rowIngredient.get(2), Integer.parseInt(rowIngredient.get(1)));
                }
                List<List<String>> nutrientList = dm.fetchDataForeignKey("dietlognutrients", "iddietlog", Integer.parseInt(dietList.get(0)));
                for(List<String> rowNutrient : nutrientList){
                    dietlog.addNutrientValue(rowNutrient.get(1), Double.parseDouble(rowNutrient.get(2)));
                }
            }
            listOfProfiles.add(userprofile);
        }
        return listOfProfiles;
    }
    public static void main(String[] arg) throws ParseException {
        Main main = new Main();
        main.startGUI();
    }
}

