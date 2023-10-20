import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class initializes and manages all components regarding
 * Java Swing/ User Interface
 * @author Harrold Ngo
 */
public class GUI {
    private JFrame jframe;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private Main main;
    private JPanel userSelection;

    /**
     * Constructor for creating a GUI
     * Creates a JFrame and uses CardLayout to switch between screens
     * @param main
     */
    public GUI(Main main) {
        this.main = main;
        jframe = new JFrame("Nutrifit");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(600, 450);
        jframe.setVisible(true);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        jframe.add(cardPanel);
    }

    /**
     * A method used for creating the UserSelection screen since
     * it requires the list of existing UserProfiles if there is any
     * @param userProfiles
     */
    public void createUserSelectionPanel(List<UserProfile> userProfiles){
        JPanel userSelection = new JPanel();

        //Iterates through the list of UserProfiles and creates a button for each Profile
        for(UserProfile up : userProfiles) {
            JButton button = new JButton(up.getName());
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    main.selectedProfile = up;
                    createMainMenuPanel();
                    createDietLogsPanel(up.getDietLogs());
                    createExerciseLogsPanel(up.getExerciseLogs());
                    cardLayout.show(cardPanel, "MainMenu");
                }
            });
            userSelection.add(button);
        }
        JButton createProfile = new JButton("Create New Profile");
        createProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createCreateUserProfilePanel();
                cardLayout.show(cardPanel, "CreateUserProfile");
            }
        });
        userSelection.add(createProfile);
        this.userSelection = userSelection;
        cardPanel.add(userSelection, "UserSelection");
        cardLayout.show(cardPanel, "UserSelection");
    }

    /**
     * A method used to create the User Profile Creation screen
     */
    public void createCreateUserProfilePanel(){
        JPanel createUserProfile = new JPanel();
        createUserProfile.setLayout(new BoxLayout(createUserProfile, BoxLayout.PAGE_AXIS));

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

        JButton create = new JButton("Create");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.profileList.add(new UserProfile(
                        nameTextField.getText(),
                        sexComboBox.getSelectedItem().toString(),
                        Date.valueOf(dateTextField.getText()),
                        Double.parseDouble(heightTextField.getText()),
                        Double.parseDouble(weightTextField.getText()),
                        unitsComboBox.getSelectedItem().toString()));
                cardPanel.remove(userSelection);
                createUserSelectionPanel(main.profileList);
                cardLayout.show(cardPanel, "UserSelection");

            }
        });

        JButton cancel = addMoveScreenButton("Cancel", "UserSelection");

        createUserProfile.add(name);
        createUserProfile.add(nameTextField);
        createUserProfile.add(sex);
        createUserProfile.add(sexComboBox);
        createUserProfile.add(date);
        createUserProfile.add(dateTextField);
        createUserProfile.add(height);
        createUserProfile.add(heightTextField);
        createUserProfile.add(weight);
        createUserProfile.add(weightTextField);
        createUserProfile.add(units);
        createUserProfile.add(unitsComboBox);
        createUserProfile.add(create);
        createUserProfile.add(cancel);

        cardPanel.add(createUserProfile, "CreateUserProfile");

    }

    /**
     * A method used to create the Main Menu screen
     */
    public void createMainMenuPanel() {
        JPanel mainmenu = new JPanel();
        mainmenu.setLayout(new BoxLayout(mainmenu, BoxLayout.PAGE_AXIS));

        JButton back = addMoveScreenButton("Back","UserSelection");
        mainmenu.add(back);

        JButton showDietLogsButton = addMoveScreenButton("Show Diet Logs", "DietLogs");
        JButton showExerciseLogsButton = addMoveScreenButton("Show Exercise Logs", "ExerciseLogs");


        mainmenu.add(showDietLogsButton);
        mainmenu.add(showExerciseLogsButton);

        cardPanel.add(mainmenu, "MainMenu");
    }

    /**
     * A method used to create the Diet Logs screen using
     * a list of diet logs
     * @param listOfDietLogs
     */
    public void createDietLogsPanel(List<DietLog> listOfDietLogs){
        JPanel dietLogsPanel = new JPanel();
        dietLogsPanel.setLayout(new BoxLayout(dietLogsPanel, BoxLayout.PAGE_AXIS));

        JPanel buttons = new JPanel();

        JButton back = addMoveScreenButton("Back","MainMenu");
        buttons.add(back);

        JButton create = addMoveScreenButton("Create","CreateDietLog");
        buttons.add(create);

        dietLogsPanel.add(buttons);

        String[] columnNames = {"ID","Date", "Meal Type", "Actions"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable jTable = new JTable(tableModel);
        for(DietLog log : listOfDietLogs){
            String[] arr = {Integer.toString(log.getId()), log.getDate().toString(), log.getMealType(), "View"};
            tableModel.addRow(arr);

        }
        jTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable target = (JTable)e.getSource();
                int row = target.getSelectedRow();
                String value = (String) target.getValueAt(row,0);
                createDietLogsDetailsPanel(Integer.parseInt(value));
                cardLayout.show(cardPanel, "DietLogDetails");
            }
        });
        JScrollPane sp = new JScrollPane(jTable);
        dietLogsPanel.add(sp);
        cardPanel.add(dietLogsPanel, "DietLogs");
    }

    /**
     * Method used to create a Details screen for an individual
     * Diet Log using the id
     * @param id
     */
    public void createDietLogsDetailsPanel(int id){
        DietLog currentLog = null;
        for(DietLog log : main.selectedProfile.getDietLogs()){
            if(id == log.getId()){
                currentLog = log;
                break;
            }
        }
        JPanel dietlogdetailsPanel = new JPanel();
        dietlogdetailsPanel.setLayout(new BoxLayout(dietlogdetailsPanel, BoxLayout.PAGE_AXIS));

        JPanel tablesPanel = new JPanel(new GridLayout(1, 2));


        JButton back = addMoveScreenButton("Back","DietLogs");
        dietlogdetailsPanel.add(back);

        DefaultTableModel ingredientTableModel = new DefaultTableModel(new String[]{"Ingredients", "Amount"}, 0);
        JTable ingredientTable = new JTable(ingredientTableModel);
        DefaultTableModel nutrientTableModel = new DefaultTableModel(new String[]{"Nutrients", "Amount"}, 0);
        JTable nutrientTable = new JTable(nutrientTableModel);


        for(Map.Entry<String, Integer> ingredient : currentLog.getIngredients().entrySet()){
            ingredientTableModel.addRow(new String[] {ingredient.getKey(), Integer.toString(ingredient.getValue())});
        }
        for(Map.Entry<String, Double> nutrient : currentLog.getNutrients().entrySet()){
            nutrientTableModel.addRow(new String[] {nutrient.getKey(), Double.toString(nutrient.getValue())});
        }

        JScrollPane sp = new JScrollPane(ingredientTable);
        tablesPanel.add(sp);
        JScrollPane sp2 = new JScrollPane(nutrientTable);
        tablesPanel.add(sp2);

        dietlogdetailsPanel.add(tablesPanel);

        cardPanel.add(dietlogdetailsPanel, "DietLogDetails");

    }

    /**
     * Method used for the creation of the Diet Log Creation screen
     */
    public void createCreateDietLogsPanel(){

        JPanel createDietLog = new JPanel();
        createDietLog.setLayout(new BoxLayout(createDietLog, BoxLayout.PAGE_AXIS));

        JPanel addIngredientsGroup = new JPanel();

        JLabel date = new JLabel("Date of Entry(format: yyyy-mm-dd): ");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter dateFormatter = new DateFormatter(dateFormat);
        JFormattedTextField dateTextField = new JFormattedTextField(dateFormatter);
        dateTextField.setPreferredSize(new Dimension(500, 30));

        JLabel mealtype = new JLabel("Meal Type: ");
        String[] unit = {"Breakfast", "Lunch", "Dinner"};
        JComboBox<String> mealTypeComboBox = new JComboBox<>(unit);

        DefaultTableModel ingredientTableModel = new DefaultTableModel(new String[]{"Ingredients", "Amount"}, 0);
        JTable ingredientTable = new JTable(ingredientTableModel);


        DefaultComboBoxModel<String> ingredientsComboBoxModel = new DefaultComboBoxModel<>();
        JComboBox<String> ingredientsComboBox = new JComboBox<>(ingredientsComboBoxModel);
        ingredientsComboBox.setEditable(true);
        ingredientsComboBoxModel.addElement("test");
        ingredientsComboBoxModel.addElement("test2");
        ingredientsComboBoxModel.addElement("test3");

        JTextField ingredientsAmount = new JTextField(1);

        JButton addIngredientButton = new JButton("Add Ingredient");
        addIngredientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ingredientTableModel.addRow(new String[] {ingredientsComboBox.getSelectedItem().toString(), ingredientsAmount.getText()});
                addIngredientsGroup.revalidate();
            }
        });
        addIngredientsGroup.add(ingredientsComboBox);
        addIngredientsGroup.add(ingredientsAmount);
        addIngredientsGroup.add(addIngredientButton);


        JButton create = new JButton("Create");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JButton cancel = addMoveScreenButton("Cancel", "DietLogs");

        createDietLog.add(date);
        createDietLog.add(dateTextField);
        createDietLog.add(mealtype);
        createDietLog.add(mealTypeComboBox);
        createDietLog.add(addIngredientsGroup);
        createDietLog.add(create);
        createDietLog.add(cancel);

        cardPanel.add(createDietLog, "CreateDietLog");

    }

    /**
     * Method used for the creation of the Exercise Logs screen using
     * the list of exercise logs
     * @param listOfExerciseLogs
     */
    public void createExerciseLogsPanel(List<ExerciseLog> listOfExerciseLogs){
        JPanel exerciseLogsPanel = new JPanel();
        exerciseLogsPanel.setLayout(new BoxLayout(exerciseLogsPanel, BoxLayout.PAGE_AXIS));

        JPanel buttons = new JPanel();

        JButton back = addMoveScreenButton("Back","MainMenu");
        buttons.add(back);

        JButton create = addMoveScreenButton("Create","CreateExerciseLog");
        buttons.add(create);

        exerciseLogsPanel.add(buttons);

        String[] columnNames = {"ID","Date", "Exercise Type", "Duration", "Intensity"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable jTable = new JTable(tableModel);
        for(ExerciseLog log : listOfExerciseLogs){
            String[] arr = {Integer.toString(log.getId()), log.getDate().toString(), log.getExerciseType(), "" +log.getDuration().toMinutes(), log.getIntensity()};
            tableModel.addRow(arr);
        }

        JScrollPane sp = new JScrollPane(jTable);
        exerciseLogsPanel.add(sp);
        cardPanel.add(exerciseLogsPanel, "ExerciseLogs");
    }

    /**
     * Method used for creating the Exercise Logs
     * creation screen
     */
    public void createCreateExerciseLogsPanel(){

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
