import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Main {
    DataManager dm;

    List<UserProfile> profileList;
    List<DietLog> dietLogs;
    List<ExerciseLog> exerciseLogs;

    List<List<String>> foodName;
    List<List<String>> nutrientName;
    List<List<String>> nutrientAmount;

    UserProfile selectedProfile;

    public Main () throws ParseException {
        this.dm = new JDBC();

        this.profileList = dataToUserProfileList();
        this.dietLogs = new ArrayList<>();
        this.exerciseLogs = new ArrayList<>();

        this.foodName = dm.fetchData("food name");
        this.nutrientName = dm.fetchData("nutrient name");
        this.nutrientAmount = dm.fetchData("nutrient amount");

        GUI gui = new GUI(this);
        gui.createUserSelectionPanel(this.profileList);
        gui.createMainMenuPanel();

    }
    public List<UserProfile> dataToUserProfileList() throws ParseException {
        List<UserProfile> listOfProfiles = new ArrayList<>();
        List<List<String>> dataList = dm.fetchData("UserProfile");
        for(List<String> row : dataList) {
            UserProfile userprofile = new UserProfile(row.get(1), row.get(2), new SimpleDateFormat("yyyy-MM-dd").parse(row.get(3)), Double.parseDouble(row.get(4)), Double.parseDouble(row.get(5)));
            listOfProfiles.add(userprofile);
        }
        return listOfProfiles;
    }


    public static void main(String[] args) throws ParseException {
        Main main = new Main();
    }
}