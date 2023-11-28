import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.List;

/**
 * This class initializes and manages all components regarding
 * Java Swing/ User Interface
 * @author Harrold Ngo
 */
interface ProfileObserver {
    void updateOnProfileSelected(UserProfile selectedProfile);
}
interface SwitchScreensListener {
    void switchScreens(String location);
}


class UserSelectionScreen extends JPanel{
    private SwitchScreensListener switchScreensListener;
    private Main main;
    public UserSelectionScreen(SwitchScreensListener switchScreensListener, Main main) throws ParseException {

        this.switchScreensListener = switchScreensListener;
        this.main = main;

        //Iterates through the list of UserProfiles and creates a button for each Profile
        for(UserProfile up : main.getProfileList()) {
            NavigateButton button = new NavigateButton(up.getName(), "MainMenu", switchScreensListener);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    main.setSelectedProfile(up);
                }
            });
            this.add(button);
        }
        NavigateButton back = new NavigateButton("Create New Profile", "CreateUserProfile", switchScreensListener);
        this.add(back);

    }

}
class CreateUserProfileScreen extends JPanel{
    public CreateUserProfileScreen(SwitchScreensListener switchScreensListener, Main main){
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel name = new JLabel("Name: ");
        JTextField nameTextField = new JTextField(20);

        JLabel sex = new JLabel("Sex: ");
        String[] malefemale = {"Male", "Female"};
        JComboBox<String> sexComboBox = new JComboBox<>(malefemale);

        JLabel date = new JLabel("Date of Birth(format: yyyy-mm-dd): ");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter dateFormatter = new DateFormatter(dateFormat);
        JFormattedTextField dateTextField = new JFormattedTextField(dateFormatter);

        JLabel height = new JLabel("Height: ");
        JTextField heightTextField = new JTextField(20);

        JLabel weight = new JLabel("Weight: ");
        JTextField weightTextField = new JTextField(20);

        JLabel units = new JLabel("Unit of Measurement: ");
        String[] unit = {"metric", "imperial"};
        JComboBox<String> unitsComboBox = new JComboBox<>(unit);

        NavigateButton create = new NavigateButton("Create", null, switchScreensListener);
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(nameTextField.getText().isBlank()) throw new Exception("Fill out Name text field");
                    if(dateTextField.getText().isBlank()) throw new Exception("Fill out Date text field");
                    if(heightTextField.getText().isBlank()) throw new Exception("Fill out Height text field");
                    if(weightTextField.getText().isBlank()) throw new Exception("Fill out Weight text field");
                    main.addProfile(
                            nameTextField.getText(),
                            sexComboBox.getSelectedItem().toString(),
                            Date.valueOf(dateTextField.getText()),
                            Double.parseDouble(heightTextField.getText()),
                            Double.parseDouble(weightTextField.getText()),
                            unitsComboBox.getSelectedItem().toString()
                    );
                    nameTextField.setText("");
                    sexComboBox.setSelectedIndex(0);
                    dateTextField.setText("");
                    heightTextField.setText("");
                    weightTextField.setText("");
                    unitsComboBox.setSelectedIndex(0);
                } catch (Exception ex) {
                    PopUpWindowMaker popUpWindowMaker = new PopUpWindowMaker("error");
                    PopUpWindow error = popUpWindowMaker.createPopUp();
                    error.show("Please have all fields filled out and\ninput a valid number for height and weight");
                }
            }
        });

        NavigateButton cancel = new NavigateButton("Cancel", "UserSelection", switchScreensListener);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameTextField.setText("");
                sexComboBox.setSelectedIndex(0);
                dateTextField.setText("");
                heightTextField.setText("");
                weightTextField.setText("");
                unitsComboBox.setSelectedIndex(0);
            }
        });

        this.add(name);
        this.add(nameTextField);
        this.add(sex);
        this.add(sexComboBox);
        this.add(date);
        this.add(dateTextField);
        this.add(height);
        this.add(heightTextField);
        this.add(weight);
        this.add(weightTextField);
        this.add(units);
        this.add(unitsComboBox);
        this.add(create);
        this.add(cancel);
    }
    public void updateData(){

    }
}
class MainMenuScreen extends JPanel implements ProfileObserver{
    SwitchScreensListener switchScreensListener;
    Main main;
    public MainMenuScreen(SwitchScreensListener switchScreensListener, Main main) {
        this.switchScreensListener = switchScreensListener;
        this.main = main;
        if(main.getSelectedProfile()!=null){
            build(main.selectedProfile);
        }
    }
    @Override
    public void updateOnProfileSelected(UserProfile selectedProfile) {
        this.removeAll();
        build(selectedProfile);
        this.revalidate();
        this.repaint();
    }
    public void build(UserProfile selectedProfile){
        JPanel userInfo = new JPanel();
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.PAGE_AXIS));
        JPanel navigation = new JPanel();
        navigation.setLayout(new BoxLayout(navigation, BoxLayout.PAGE_AXIS));

        NavigateButton back = new NavigateButton("Back", "UserSelection", switchScreensListener);

        navigation.add(back);
        NavigateButton showDietLogsButton = new NavigateButton("Show Diet Logs", "DietLogs", switchScreensListener);
        NavigateButton showExerciseLogsButton = new NavigateButton("Show Exercise Logs", "ExerciseLogs", switchScreensListener);
        NavigateButton showCaloriesOverTimeButton = new NavigateButton("Show Calories Over Time", "CaloriesOverTime", switchScreensListener);
        NavigateButton showDailyNutrientIntakeButton = new NavigateButton("Show Daily Nutrient Intake", "DailyNutrientIntake", switchScreensListener);
        NavigateButton showFatLossButton = new NavigateButton("Show Future Fat Loss", "FatLoss", switchScreensListener);
        NavigateButton editProfileScreen = new NavigateButton("Edit Profile & Settings", "EditUserProfile", switchScreensListener);

        navigation.add(showDietLogsButton);
        navigation.add(showExerciseLogsButton);

        navigation.add(showCaloriesOverTimeButton);
        navigation.add(showDailyNutrientIntakeButton);
        navigation.add(showFatLossButton);

        JButton showDietAlignsButton = new JButton("Show Diet Alignment");
        showDietAlignsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(selectedProfile.getDietLogs().isEmpty()) throw new Exception("Diet Logs are empty!");
                    Visualizer dietAlignsVisualizer = new DietAlignsVisualizer(selectedProfile);
                } catch (Exception ex){
                    PopUpWindowMaker popUpWindowMaker = new PopUpWindowMaker("error");
                    PopUpWindow error = popUpWindowMaker.createPopUp();
                    error.show("Minimum 1 diet log entry is required.");
                    ex.printStackTrace();
                }

            }
        });
        navigation.add(showDietAlignsButton);

        navigation.add(editProfileScreen);

        JLabel info = new JLabel("User Profile Information");
        JLabel name = new JLabel(selectedProfile.getName());
        name.setFont(info.getFont().deriveFont(info.getFont().getStyle() & ~Font.BOLD));
        JLabel sex = new JLabel(selectedProfile.getSex());
        sex.setFont(info.getFont().deriveFont(info.getFont().getStyle() & ~Font.BOLD));
        JLabel date = new JLabel(selectedProfile.getDateOfBirth().toString());
        date.setFont(info.getFont().deriveFont(info.getFont().getStyle() & ~Font.BOLD));
        JLabel height;
        JLabel weight;
        if(selectedProfile.getUnits().equalsIgnoreCase("imperial")){
            int feet = (int)Math.floor(selectedProfile.getHeight() / 2.54 / 12);
            int inches = (int)Math.round(selectedProfile.getHeight()/2.54 - (12*feet));
            double lbs = selectedProfile.getWeight()*2.205;

            height = new JLabel(feet+"'"+inches+"\"");
            height.setFont(info.getFont().deriveFont(info.getFont().getStyle() & ~Font.BOLD));
            weight = new JLabel(""+lbs);
            weight.setFont(info.getFont().deriveFont(info.getFont().getStyle() & ~Font.BOLD));
        }
        else {
            height = new JLabel(""+selectedProfile.getHeight());
            height.setFont(info.getFont().deriveFont(info.getFont().getStyle() & ~Font.BOLD));
            weight = new JLabel(""+selectedProfile.getWeight());
            weight.setFont(info.getFont().deriveFont(info.getFont().getStyle() & ~Font.BOLD));
        }

        JLabel bmr = new JLabel(""+selectedProfile.getBMR());
        bmr.setFont(info.getFont().deriveFont(info.getFont().getStyle() & ~Font.BOLD));
        JLabel units = new JLabel(""+selectedProfile.getUnits());
        units.setFont(info.getFont().deriveFont(info.getFont().getStyle() & ~Font.BOLD));
        userInfo.add(info);
        userInfo.add(name);
        userInfo.add(sex);
        userInfo.add(date);
        userInfo.add(height);
        userInfo.add(weight);
        userInfo.add(bmr);
        userInfo.add(units);

        this.add(navigation);
        this.add(userInfo);
    }

}
class EditProfileScreen extends JPanel implements ProfileObserver{
    SwitchScreensListener switchScreensListener;
    Main main;
    public EditProfileScreen(SwitchScreensListener switchScreensListener, Main main){
        this.switchScreensListener = switchScreensListener;
        this.main = main;
        if(main.getSelectedProfile()!=null){
            if(main.getSelectedProfile().getUnits().equalsIgnoreCase("imperial")){
                build(main.getSelectedProfile());
            }
            else{
                build(main.getSelectedProfile());
            }
        }
    }
    @Override
    public void updateOnProfileSelected(UserProfile selectedProfile) {
        this.removeAll();
        if(selectedProfile.getUnits().equalsIgnoreCase("imperial")){
            buildImperial(selectedProfile);
        }
        else{
            build(selectedProfile);
        }

        this.revalidate();
        this.repaint();
    }
    public void build(UserProfile selectedProfile){
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel name = new JLabel("Name: ");
        JTextField nameTextField = new JTextField(20);
        nameTextField.setText(selectedProfile.getName());

        JLabel sex = new JLabel("Sex: ");
        String[] malefemale = {"Male", "Female"};
        JComboBox<String> sexComboBox = new JComboBox<>(malefemale);
        sexComboBox.setSelectedItem(selectedProfile.getSex());

        JLabel date = new JLabel("Date of Birth(format: yyyy-mm-dd): ");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter dateFormatter = new DateFormatter(dateFormat);
        JFormattedTextField dateTextField = new JFormattedTextField(dateFormatter);
        dateTextField.setText(selectedProfile.getDateOfBirth().toString());

        JLabel height = new JLabel("Height: ");
        JTextField heightTextField = new JTextField(20);
        heightTextField.setText(""+selectedProfile.getHeight());

        JLabel weight = new JLabel("Weight: ");
        JTextField weightTextField = new JTextField(20);
        weightTextField.setText(""+selectedProfile.getWeight());


        JLabel units = new JLabel("Unit of Measurement: ");
        String[] unit = {"metric", "imperial"};
        JComboBox<String> unitsComboBox = new JComboBox<>(unit);
        unitsComboBox.setSelectedItem(selectedProfile.getUnits());

        NavigateButton update = new NavigateButton("Update", "MainMenu", switchScreensListener);
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(nameTextField.getText().isBlank()) throw new Exception("Fill out Name text field");
                    if(dateTextField.getText().isBlank()) throw new Exception("Fill out Date text field");
                    if(heightTextField.getText().isBlank()) throw new Exception("Fill out Height text field");
                    if(weightTextField.getText().isBlank()) throw new Exception("Fill out Weight text field");
                    main.updateProfile(
                            nameTextField.getText(),
                            sexComboBox.getSelectedItem().toString(),
                            Date.valueOf(dateTextField.getText()),
                            Double.parseDouble(heightTextField.getText()),
                            Double.parseDouble(weightTextField.getText()),
                            unitsComboBox.getSelectedItem().toString()
                    );
                }
                catch(Exception ex) {
                    PopUpWindowMaker popUpWindowMaker = new PopUpWindowMaker("error");
                    PopUpWindow error = popUpWindowMaker.createPopUp();
                    error.show("Please have all fields filled out and\ninput a valid number for height and weight");
                }

            }
        });

        NavigateButton cancel = new NavigateButton("Cancel", "MainMenu", switchScreensListener);

        this.add(name);
        this.add(nameTextField);
        this.add(sex);
        this.add(sexComboBox);
        this.add(date);
        this.add(dateTextField);
        this.add(height);
        this.add(heightTextField);
        this.add(weight);
        this.add(weightTextField);
        this.add(units);
        this.add(unitsComboBox);
        this.add(update);
        this.add(cancel);
    }
    public void buildImperial(UserProfile selectedProfile){
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel name = new JLabel("Name: ");
        JTextField nameTextField = new JTextField(20);
        nameTextField.setText(selectedProfile.getName());

        JLabel sex = new JLabel("Sex: ");
        String[] malefemale = {"Male", "Female"};
        JComboBox<String> sexComboBox = new JComboBox<>(malefemale);
        sexComboBox.setSelectedItem(selectedProfile.getSex());

        JLabel date = new JLabel("Date of Birth(format: yyyy-mm-dd): ");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter dateFormatter = new DateFormatter(dateFormat);
        JFormattedTextField dateTextField = new JFormattedTextField(dateFormatter);
        dateTextField.setText(selectedProfile.getDateOfBirth().toString());
        JPanel height = new JPanel();
        JPanel weight = new JPanel();

        int feet = (int)Math.floor(selectedProfile.getHeight()/2.54/12);
        int inches =(int)Math.round(selectedProfile.getHeight()/2.54 - (feet*12));
        JLabel feetText = new JLabel("Height(ft): ");
        JTextField feetTextField = new JTextField(20);
        feetTextField.setText(""+feet);

        JLabel inchesText = new JLabel("Height(inch): ");
        JTextField inchesTextField = new JTextField(20);
        inchesTextField.setText(""+inches);

        JLabel weightText = new JLabel("Weight(lbs): ");
        JTextField weightTextField = new JTextField(20);
        weightTextField.setText(""+selectedProfile.getWeight()*2.205);
        height.add(feetText);
        height.add(feetTextField);
        height.add(inchesText);
        height.add(inchesTextField);
        weight.add(weightText);
        weight.add(weightTextField);


        JLabel units = new JLabel("Unit of Measurement: ");
        String[] unit = {"metric", "imperial"};
        JComboBox<String> unitsComboBox = new JComboBox<>(unit);
        unitsComboBox.setSelectedItem(selectedProfile.getUnits());

        NavigateButton update = new NavigateButton("Update", "MainMenu", switchScreensListener);
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(nameTextField.getText().isBlank()) throw new Exception("Fill out Name text field");
                    if(dateTextField.getText().isBlank()) throw new Exception("Fill out Date text field");
                    if(feetTextField.getText().isBlank()) throw new Exception("Fill out Feet text field");
                    if(inchesTextField.getText().isBlank()) throw new Exception("Fill out Inches text field");
                    if(weightTextField.getText().isBlank()) throw new Exception("Fill out Weight text field");
                    double heightcm = 0;
                    heightcm += Double.parseDouble(feetTextField.getText())*30.48;
                    heightcm += Double.parseDouble(inchesTextField.getText())*2.54;
                    double kg = Double.parseDouble(weightTextField.getText())/2.205;
                    main.updateProfile(
                            nameTextField.getText(),
                            sexComboBox.getSelectedItem().toString(),
                            Date.valueOf(dateTextField.getText()),
                            heightcm,
                            kg,
                            unitsComboBox.getSelectedItem().toString()
                    );
                }catch(Exception ex) {
                    PopUpWindowMaker popUpWindowMaker = new PopUpWindowMaker("error");
                    PopUpWindow error = popUpWindowMaker.createPopUp();
                    error.show("Please have all fields filled out and\ninput a valid number for height and weight");
                }
            }
        });

        NavigateButton cancel = new NavigateButton("Cancel", "MainMenu", switchScreensListener);

        this.add(name);
        this.add(nameTextField);
        this.add(sex);
        this.add(sexComboBox);
        this.add(date);
        this.add(dateTextField);
        this.add(height);
        this.add(weight);
        this.add(units);
        this.add(unitsComboBox);
        this.add(update);
        this.add(cancel);
    }
}
class DietLogsScreen extends JPanel implements ProfileObserver{
    SwitchScreensListener switchScreensListener;
    Main main;
    JPanel cardPanel;
    public DietLogsScreen(SwitchScreensListener switchScreensListener, Main main, JPanel cardPanel){
        this.switchScreensListener = switchScreensListener;
        this.main = main;
        this.cardPanel = cardPanel;
        if(main.getSelectedProfile()!=null){
            build(main.getSelectedProfile());
        }
    }

    @Override
    public void updateOnProfileSelected(UserProfile selectedProfile) {
        this.removeAll();
        build(selectedProfile);
        this.revalidate();
        this.repaint();
    }
    public void build(UserProfile selectedProfile){
        List<DietLog> listOfDietLogs = selectedProfile.getDietLogs();

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JPanel buttons = new JPanel();

        NavigateButton back = new NavigateButton("Back", "MainMenu", switchScreensListener);
        buttons.add(back);
        NavigateButton create = new NavigateButton("Create", "CreateDietLog", switchScreensListener);
        buttons.add(create);
        this.add(buttons);

        String[] columnNames = {"ID","Date", "Meal Type", "Calories", "Actions"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable jTable = new JTable(tableModel);
        for(DietLog log : listOfDietLogs){
            String[] arr = {Integer.toString(log.getDietLogId()), log.getDate().toString(), log.getMealType(), Double.toString(log.getCalories()), "View Details"};
            tableModel.addRow(arr);

        }
        jTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable target = (JTable)e.getSource();
                int row = target.getSelectedRow();
                String value = (String) target.getValueAt(row,0);

                DietLogDetailsScreen dietLogDetailsScreen = new DietLogDetailsScreen(switchScreensListener, main, Integer.parseInt(value));
                cardPanel.add(dietLogDetailsScreen, "DietLogDetails");

                switchScreensListener.switchScreens("DietLogDetails");
            }
        });
        JScrollPane sp = new JScrollPane(jTable);
        this.add(sp);
    }
}
class DietLogDetailsScreen extends JPanel{
    public DietLogDetailsScreen(SwitchScreensListener switchScreensListener, Main main, int id){
        DietLog currentLog = null;
        for(DietLog log : main.getSelectedProfile().getDietLogs()){
            if(id == log.getDietLogId()){
                currentLog = log;
                break;
            }
        }
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JPanel tablesPanel = new JPanel(new GridLayout(1, 2));


        NavigateButton back = new NavigateButton("Back", "DietLogs", switchScreensListener);
        this.add(back);

        DefaultTableModel ingredientTableModel = new DefaultTableModel(new String[]{"Ingredients", "Amount"}, 0);
        JTable ingredientTable = new JTable(ingredientTableModel);
        DefaultTableModel nutrientTableModel = new DefaultTableModel(new String[]{"Nutrients", "Amount"}, 0);
        JTable nutrientTable = new JTable(nutrientTableModel);


        for(Ingredient ingredient : currentLog.getIngredients()){
            ingredientTableModel.addRow(new String[] {ingredient.getIngredient(), Integer.toString(ingredient.getQuantity())});
        }
        for(Nutrient nutrient : currentLog.getNutrients()){
            nutrientTableModel.addRow(new String[] {nutrient.getNutrient(), Double.toString(nutrient.getAmount())});
        }

        JScrollPane sp = new JScrollPane(ingredientTable);
        tablesPanel.add(sp);
        JScrollPane sp2 = new JScrollPane(nutrientTable);
        tablesPanel.add(sp2);

        this.add(tablesPanel);
    }
}
class CreateDietLogScreen extends JPanel {
    public CreateDietLogScreen(SwitchScreensListener switchScreensListener, Main main){
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JPanel addIngredientsGroup = new JPanel();
        //addIngredientsGroup.setLayout(new BoxLayout(addIngredientsGroup, BoxLayout.PAGE_AXIS));

        JLabel date = new JLabel("Date of Entry(format: yyyy-mm-dd): ");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter dateFormatter = new DateFormatter(dateFormat);
        JFormattedTextField dateTextField = new JFormattedTextField(dateFormatter);
        dateTextField.setPreferredSize(new Dimension(500, 30));

        JLabel mealtype = new JLabel("Meal Type: ");
        String[] unit = {"Breakfast", "Lunch", "Dinner", "Snack"};
        JComboBox<String> mealTypeComboBox = new JComboBox<>(unit);

        DefaultTableModel ingredientTableModel = new DefaultTableModel(new String[]{"Ingredients", "Amount"}, 0);
        JTable ingredientTable = new JTable(ingredientTableModel);
        JScrollPane sp = new JScrollPane(ingredientTable);

        DefaultComboBoxModel<String> ingredientsComboBoxModel = new DefaultComboBoxModel<>();
        JComboBox<String> ingredientsComboBox = new JComboBox<>(ingredientsComboBoxModel);
        ingredientsComboBox.setEditable(true);
        for(List<String> l : main.foodName){
            ingredientsComboBoxModel.addElement(l.get(4));
        }
        AutoCompletion.enable(ingredientsComboBox);

        JTextField ingredientsAmount = new JTextField(1);

        JButton addIngredientButton = new JButton("Add Ingredient");
        addIngredientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ingredientTableModel.addRow(new String[] {ingredientsComboBox.getSelectedItem().toString(), ingredientsAmount.getText()});
                ingredientTable.repaint();
            }
        });
        addIngredientsGroup.add(ingredientsComboBox);
        addIngredientsGroup.add(ingredientsAmount);
        addIngredientsGroup.add(addIngredientButton);
        //addIngredientsGroup.add(ingredientTable);


        JButton create = new JButton("Create");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, Integer> ingredients = new HashMap<>();
                try {
                    if(ingredientTableModel.getRowCount()<1) throw new Exception("Missing ingredients");
                    for(int i = 0; i<ingredientTableModel.getRowCount(); i++){
                        String foodName = (String)ingredientTableModel.getValueAt(i, 0);
                        int amount = Integer.parseInt((String)ingredientTableModel.getValueAt(i, 1));
                        if(ingredients.containsKey(foodName)){
                            ingredients.put(foodName, ingredients.get(foodName)+amount);
                        }
                        else {
                            ingredients.put((String)ingredientTableModel.getValueAt(i, 0), amount);
                        }

                    }
                    main.addDietLog(
                            Date.valueOf(dateTextField.getText()),
                            mealTypeComboBox.getSelectedItem().toString(),
                            ingredients
                    );
                    ingredientTableModel.setRowCount(0);
                    ingredientsAmount.setText("");
                    dateTextField.setText("");
                    mealTypeComboBox.setSelectedIndex(0);
                } catch(Exception ex){
                    ex.printStackTrace();
                    PopUpWindowMaker popUpWindowMaker = new PopUpWindowMaker("error");
                    PopUpWindow error = popUpWindowMaker.createPopUp();
                    error.show("Please have all fields filled out and have a valid number for ingredient amount\n" +
                            "Please have at least 1 ingredient added to the log\n" +
                            "Please have only 1 of Breakfast, Lunch or Dinner per day");
                }
            }
        });

        NavigateButton cancel = new NavigateButton("Cancel", "DietLogs", switchScreensListener);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ingredientTableModel.setRowCount(0);
                ingredientsAmount.setText("");
                dateTextField.setText("");
                mealTypeComboBox.setSelectedIndex(0);
            }
        });

        this.add(date);
        this.add(dateTextField);
        this.add(mealtype);
        this.add(mealTypeComboBox);
        this.add(sp);
        this.add(addIngredientsGroup);
        this.add(create);
        this.add(cancel);
    }
}

class ExerciseLogsScreen extends JPanel implements ProfileObserver {
    SwitchScreensListener switchScreensListener;
    Main main;
    public ExerciseLogsScreen(SwitchScreensListener switchScreensListener, Main main){
        this.switchScreensListener = switchScreensListener;
        this.main = main;
        if(main.getSelectedProfile()!=null){
            build(main.getSelectedProfile());
        }
    }

    @Override
    public void updateOnProfileSelected(UserProfile selectedProfile) {
        this.removeAll();
        build(selectedProfile);
        this.revalidate();
        this.repaint();
    }
    public void build(UserProfile selectedProfile){
        List<ExerciseLog> listOfExerciseLogs = selectedProfile.getExerciseLogs();

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JPanel buttons = new JPanel();

        NavigateButton back = new NavigateButton("Back", "MainMenu", switchScreensListener);
        buttons.add(back);
        NavigateButton create = new NavigateButton("Create", "CreateExerciseLog", switchScreensListener);
        buttons.add(create);
        this.add(buttons);

        String[] columnNames = {"ID","Date", "Time", "Exercise", "Duration(min)", "Intensity", "Calories Burnt"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable jTable = new JTable(tableModel);
        for(ExerciseLog log : listOfExerciseLogs){
            String[] arr = {Integer.toString(log.getExerciseLogId()), log.getDate().toString(), log.getTime().toString(), log.getExerciseType(), Integer.toString(log.getDuration()), log.getIntensity(), ""+log.getCaloriesBurnt()};
            tableModel.addRow(arr);
        }
        JScrollPane sp = new JScrollPane(jTable);
        this.add(sp);
    }
}
class CreateExerciseLogScreen extends JPanel {
    public CreateExerciseLogScreen(SwitchScreensListener switchScreensListener, Main main){
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel date = new JLabel("Date of Entry(format: yyyy-mm-dd): ");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter dateFormatter = new DateFormatter(dateFormat);
        JFormattedTextField dateTextField = new JFormattedTextField(dateFormatter);
        dateTextField.setPreferredSize(new Dimension(500, 30));

        JLabel time = new JLabel("Time of Entry(format: hh:mm)");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        DateFormatter timeFormatter = new DateFormatter(timeFormat);
        JFormattedTextField timeTextField = new JFormattedTextField(timeFormatter);
        timeTextField.setPreferredSize(new Dimension(500, 30));

        JLabel exerciseType = new JLabel("Exercise Type: ");
        JTextField exerciseTypeTextField = new JTextField(30);

        JLabel duration = new JLabel("Duration(minutes)");
        JTextField durationTextField = new JTextField(30);

        JLabel intensity = new JLabel("Intensity: ");
        String[] intensities = {"Low", "Medium", "High", "Very High"};
        JComboBox<String> intensityComboBox = new JComboBox<>(intensities);

        JButton create = new JButton("Create");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(exerciseTypeTextField.getText().isBlank()) throw new Exception("Missing exercise type");
                    if(dateTextField.getText().isBlank()) throw new Exception("Fill out Date text field");
                    if(timeTextField.getText().isBlank()) throw new Exception("Fill out Time text field");
                    if(durationTextField.getText().isBlank()) throw new Exception("Fill out Duration text field");
                    main.addExerciseLog(
                            Date.valueOf(dateTextField.getText()),
                            Time.valueOf(timeTextField.getText()+":00"),
                            exerciseTypeTextField.getText(),
                            Integer.parseInt(durationTextField.getText()),
                            intensityComboBox.getSelectedItem().toString()
                    );
                    dateTextField.setText("");
                    timeTextField.setText("");
                    exerciseTypeTextField.setText("");
                    durationTextField.setText("");
                    intensityComboBox.setSelectedIndex(0);
                } catch(Exception ex) {
                    PopUpWindowMaker popUpWindowMaker = new PopUpWindowMaker("error");
                    PopUpWindow error = popUpWindowMaker.createPopUp();
                    error.show("Please have all fields filled out and have a valid number for duration\n");
                }
            }
        });

        NavigateButton cancel = new NavigateButton("Cancel", "ExerciseLogs", switchScreensListener);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dateTextField.setText("");
                timeTextField.setText("");
                exerciseTypeTextField.setText("");
                durationTextField.setText("");
                intensityComboBox.setSelectedIndex(0);
            }
        });

        this.add(date);
        this.add(dateTextField);
        this.add(time);
        this.add(timeTextField);
        this.add(exerciseType);
        this.add(exerciseTypeTextField);
        this.add(duration);
        this.add(durationTextField);
        this.add(intensity);
        this.add(intensityComboBox);
        this.add(create);
        this.add(cancel);
    }
}
class ViewCaloriesOverTimeScreen extends JPanel implements ProfileObserver{
    SwitchScreensListener switchScreensListener;
    public ViewCaloriesOverTimeScreen(SwitchScreensListener switchScreensListener, UserProfile selectedProfile){
        this.switchScreensListener =switchScreensListener;
        if(selectedProfile!=null){
            build(selectedProfile);
        }
    }
    public void build(UserProfile selectedProfile){
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel start = new JLabel("Start Date(format: yyyy-mm-dd): ");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter dateFormatter = new DateFormatter(dateFormat);
        JFormattedTextField startTextField = new JFormattedTextField(dateFormatter);
        startTextField.setPreferredSize(new Dimension(500, 30));

        JLabel end = new JLabel("End Date(format: yyyy-mm-dd): ");
        JFormattedTextField endTextField = new JFormattedTextField(dateFormatter);
        endTextField.setPreferredSize(new Dimension(500, 30));

        JButton generate = new JButton("Generate");
        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(selectedProfile.getDietLogs().isEmpty() || selectedProfile.getExerciseLogs().isEmpty()) throw new Exception("No dietlogs/exercise logs available");
                    if(startTextField.getText().isBlank()) throw new Exception("Fill out Start Date text field");
                    if(endTextField.getText().isBlank()) throw new Exception("Fill out End Date text field");
                    Visualizer visualizer = new CalorieExerciseVisualizer(Date.valueOf(startTextField.getText()), Date.valueOf(endTextField.getText()), selectedProfile.getDietLogs(), selectedProfile.getExerciseLogs());
                } catch(Exception ex) {
                    ex.printStackTrace();
                    PopUpWindowMaker popUpWindowMaker = new PopUpWindowMaker("error");
                    PopUpWindow error = popUpWindowMaker.createPopUp();
                    error.show("Make sure you have a minimum of 1 dietlog and exerciselog\n" +
                            "Make sure you have Start and End text fields filled out\n" +
                            "Make sure Start date is before End date");
                }
            }
        });

        NavigateButton cancel = new NavigateButton("Go Back", "MainMenu", switchScreensListener);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTextField.setText("");
                endTextField.setText("");
            }
        });

        JPanel buttonGroup = new JPanel();
        buttonGroup.add(cancel);
        buttonGroup.add(generate);
        this.add(start);
        this.add(startTextField);
        this.add(end);
        this.add(endTextField);
        this.add(buttonGroup);
    }
    @Override
    public void updateOnProfileSelected(UserProfile selectedProfile) {
        this.removeAll();
        build(selectedProfile);
        this.revalidate();
        this.repaint();
    }
}
class ViewDailyNutrientIntakeScreen extends JPanel implements ProfileObserver{
    UserProfile selectedProfile;
    SwitchScreensListener switchScreensListener;
    public ViewDailyNutrientIntakeScreen(SwitchScreensListener switchScreensListener, UserProfile selectedProfile){
        this.selectedProfile=selectedProfile;
        this.switchScreensListener = switchScreensListener;
        if(this.selectedProfile!=null){
            build(this.selectedProfile);
        }
    }
    public void build(UserProfile selectedProfile){
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel start = new JLabel("Start Date(format: yyyy-mm-dd): ");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter dateFormatter = new DateFormatter(dateFormat);
        JFormattedTextField startTextField = new JFormattedTextField(dateFormatter);
        startTextField.setPreferredSize(new Dimension(500, 30));

        JLabel end = new JLabel("End Date(format: yyyy-mm-dd): ");
        JFormattedTextField endTextField = new JFormattedTextField(dateFormatter);
        endTextField.setPreferredSize(new Dimension(500, 30));

        JButton generate = new JButton("Generate");
        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(selectedProfile.getDietLogs().isEmpty()) throw new Exception("No dietlogs/exercise logs available");
                    if(startTextField.getText().isBlank()) throw new Exception("Fill out Start Date text field");
                    if(endTextField.getText().isBlank()) throw new Exception("Fill out End Date text field");
                    Visualizer visualizer = new NutrientIntakeVisualizer(Date.valueOf(startTextField.getText()), Date.valueOf(endTextField.getText()), selectedProfile.getDietLogs());
                } catch(Exception ex) {
                    PopUpWindowMaker popUpWindowMaker = new PopUpWindowMaker("error");
                    PopUpWindow error = popUpWindowMaker.createPopUp();
                    error.show("Make sure you have a minimum of 1 dietlog\n" +
                            "Make sure you have Start and End text fields filled out\n" +
                            "Make sure Start date is before End date");
                }
            }
        });

        NavigateButton cancel = new NavigateButton("Go Back", "MainMenu", switchScreensListener);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTextField.setText("");
                endTextField.setText("");
            }
        });

        JPanel buttonGroup = new JPanel();
        buttonGroup.add(cancel);
        buttonGroup.add(generate);
        this.add(start);
        this.add(startTextField);
        this.add(end);
        this.add(endTextField);
        this.add(buttonGroup);
    }

    @Override
    public void updateOnProfileSelected(UserProfile selectedProfile) {
        this.removeAll();
        build(selectedProfile);
        this.revalidate();
        this.repaint();
    }
}
class ViewFatLossScreen extends JPanel implements ProfileObserver{
    UserProfile selectedProfile;
    SwitchScreensListener switchScreensListener;
    public ViewFatLossScreen(SwitchScreensListener switchScreensListener, UserProfile selectedProfile){
        this.selectedProfile=selectedProfile;
        this.switchScreensListener = switchScreensListener;
        if(this.selectedProfile!=null){
            build(this.selectedProfile);
        }
    }
    public void build(UserProfile selectedProfile){
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLabel future = new JLabel("Enter a Future Date(format: yyyy-mm-dd): ");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter dateFormatter = new DateFormatter(dateFormat);
        JFormattedTextField futureTextField = new JFormattedTextField(dateFormatter);

        JLabel result = new JLabel("Result: ");
        result.setFont(new Font("Serif", Font.PLAIN, 24));
        JButton calculate = new JButton("Calculate");
        NavigateButton cancel = new NavigateButton("Go Back", "MainMenu", switchScreensListener);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                futureTextField.setText("");
                result.setText("");
            }
        });

        calculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(selectedProfile.getDietLogs().isEmpty() || selectedProfile.getExerciseLogs().isEmpty()) throw new Exception("No dietlogs/exercise logs available");
                    if(futureTextField.getText().isBlank()) throw new Exception("Future text field is empty");
                    FatLoss fatLoss = new FatLoss(Date.valueOf(futureTextField.getText()), selectedProfile.getDietLogs(), selectedProfile.getExerciseLogs());

                    if(fatLoss.getFatLossAmount() >= 0) result.setText("Result: "+fatLoss.getFatLossAmount() +"kg gained");
                    else result.setText("Result: "+fatLoss.getFatLossAmount()*-1 +"kg lost");

                } catch(Exception ex) {
                    PopUpWindowMaker popUpWindowMaker = new PopUpWindowMaker("error");
                    PopUpWindow error = popUpWindowMaker.createPopUp();
                    error.show("Make sure you have a minimum of 1 dietlog and 1 exercise log\n" +
                            "Make sure you have Future text field filled out\n" +
                            "Make sure Future date is a date after the newest Diet/Exercise log");
                    ex.printStackTrace();
                }
            }
        });


        JPanel buttonGroup = new JPanel();
        buttonGroup.add(calculate);
        buttonGroup.add(cancel);
        this.add(future);
        this.add(futureTextField);
        this.add(buttonGroup);
        this.add(result);
    }

    @Override
    public void updateOnProfileSelected(UserProfile selectedProfile) {
        this.removeAll();
        build(selectedProfile);
        this.revalidate();
        this.repaint();
    }
}
class NavigateButton extends JButton{
    private SwitchScreensListener switchScreens;
    public NavigateButton(String name, String location, SwitchScreensListener switchScreens){
        this.switchScreens = switchScreens;
        this.setText(name);
        if(location!=null){
            this.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switchScreens.switchScreens(location);
                }
            });
        }

    }
}
class SwitchScreensManager implements SwitchScreensListener {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    public SwitchScreensManager(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
    }
    @Override
    public void switchScreens(String location) {
        cardLayout.show(cardPanel, location);
    }
}


public class GUI {
    private JFrame jframe;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private Main main;
    private SwitchScreensManager switchScreenManager;

    /**
     * Constructor for creating a GUI
     * Creates a JFrame and uses CardLayout to switch between screens
     */
    public GUI() throws ParseException {
        this.main = Main.getInstance();

        jframe = new JFrame("Nutrifit");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(1000, 750);


        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        switchScreenManager = new SwitchScreensManager(cardLayout, cardPanel);

        UserSelectionScreen userSelectionScreen = new UserSelectionScreen(switchScreenManager, main);
        cardPanel.add(userSelectionScreen, "UserSelection");
        MainMenuScreen mainMenuScreen = new MainMenuScreen(switchScreenManager, main);
        cardPanel.add(mainMenuScreen, "MainMenu");
        CreateUserProfileScreen createUserProfileScreen = new CreateUserProfileScreen(switchScreenManager, main);
        cardPanel.add(createUserProfileScreen, "CreateUserProfile");
        EditProfileScreen editProfileScreen = new EditProfileScreen(switchScreenManager, main);
        cardPanel.add(editProfileScreen, "EditUserProfile");
        DietLogsScreen dietLogsScreen = new DietLogsScreen(switchScreenManager, main, cardPanel);
        cardPanel.add(dietLogsScreen, "DietLogs");
        CreateDietLogScreen createDietLogScreen = new CreateDietLogScreen(switchScreenManager, main);
        cardPanel.add(createDietLogScreen, "CreateDietLog");
        ExerciseLogsScreen exerciseLogsScreen = new ExerciseLogsScreen(switchScreenManager, main);
        cardPanel.add(exerciseLogsScreen, "ExerciseLogs");
        CreateExerciseLogScreen createExerciseLogScreen = new CreateExerciseLogScreen(switchScreenManager, main);
        cardPanel.add(createExerciseLogScreen, "CreateExerciseLog");
        ViewCaloriesOverTimeScreen viewCaloriesOverTimeScreen = new ViewCaloriesOverTimeScreen(switchScreenManager, main.getSelectedProfile());
        cardPanel.add(viewCaloriesOverTimeScreen, "CaloriesOverTime");
        ViewDailyNutrientIntakeScreen viewDailyNutrientIntakeScreen = new ViewDailyNutrientIntakeScreen(switchScreenManager, main.getSelectedProfile());
        cardPanel.add(viewDailyNutrientIntakeScreen, "DailyNutrientIntake");
        ViewFatLossScreen viewFatLossScreen = new ViewFatLossScreen(switchScreenManager, main.getSelectedProfile());
        cardPanel.add(viewFatLossScreen, "FatLoss");

        cardLayout.show(cardPanel, "UserSelection");

        main.addObserver(mainMenuScreen);
        main.addObserver(dietLogsScreen);
        main.addObserver(exerciseLogsScreen);
        main.addObserver(editProfileScreen);
        main.addObserver(viewCaloriesOverTimeScreen);
        main.addObserver(viewDailyNutrientIntakeScreen);
        main.addObserver(viewFatLossScreen);

        jframe.add(cardPanel);
        jframe.setVisible(true);
    }
    public void updateUserSelectionScreen() throws ParseException {
        UserSelectionScreen userSelectionScreen = new UserSelectionScreen(switchScreenManager, main);
        cardPanel.add(userSelectionScreen, "UserSelection");
        cardLayout.show(cardPanel,"UserSelection");
    }
    public void updateDietLogsScreen(){
        DietLogsScreen dietLogsScreen = new DietLogsScreen(switchScreenManager, main, cardPanel);
        cardPanel.add(dietLogsScreen, "DietLogs");
        cardLayout.show(cardPanel, "DietLogs");
    }
    public void updateExerciseLogsScreen(){
        ExerciseLogsScreen exerciseLogsScreen = new ExerciseLogsScreen(switchScreenManager, main);
        cardPanel.add(exerciseLogsScreen, "ExerciseLogs");
        cardLayout.show(cardPanel, "ExerciseLogs");
    }

    /**
     * Method used to create a button that navigates to
     * a specific screen
     * @param name
     * @param location
     * @return
     */
    public JButton addMoveScreenButton(String name,String location){
        JButton back = new JButton(name);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, location);
            }
        });
        return back;
    }
    public void refresh(){
        this.jframe.revalidate();
    }

    public static void main(String[] arg)
    {

    }
}

