import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class DataManager {
    private DataManagerStrategy dataManagerStrategy;
    public DataManager(DataManagerStrategy dataManagerStrategy) {
        this.dataManagerStrategy = dataManagerStrategy;
    }
    public void setDataManagerStrategy(DataManagerStrategy dataManagerStrategy){
        this.dataManagerStrategy = dataManagerStrategy;
    }
    List<List<String>> fetchData(String table) {
        return this.dataManagerStrategy.fetchData(table);
    }
    List<List<String>> fetchSpecificData(String table, String col, String id){
        return this.dataManagerStrategy.fetchSpecificData(table, col, id);
    }

    //Cant be bothered to create this sql statement dynamically
    List<List<String>> fetchNutrients(String name){
        return this.dataManagerStrategy.fetchNutrients(name);
    }

    List<List<String>> fetchFoodGroups(String foodName){
        return this.dataManagerStrategy.fetchFoodGroups(foodName);
    }
    void addData(String table, List<String> data){
        this.dataManagerStrategy.addData(table, data);
    }
    void updateData(String table, List<String> columns, List<String> data){
        this.dataManagerStrategy.updateData(table, columns, data);
    }

}

/**
 * Interface used to manage all data sources that the
 * application may or may not use
 *
 * @author Harrold Ngo
 */
interface DataManagerStrategy {
    List<List<String>> fetchData(String table);

    List<List<String>> fetchSpecificData(String table, String col, String id);

    //Cant be bothered to create this sql statement dynamically
    List<List<String>> fetchNutrients(String name);

    List<List<String>> fetchFoodGroups(String foodName);
    void addData(String table, List<String> data);
    void deleteData();
    void updateData(String table, List<String> columns, List<String> data);
}

/**
 * A class that is specifically used for connecting to and
 * retreiving/inputing data from an SQL database
 */
class JDBC implements DataManagerStrategy{
    private Connection connection;
    private static JDBC jdbcInstance;

    /**
     * A private constructor used to initialize an instance
     * of JDBC, conforms to the Singleton design pattern
     */
    private JDBC(){
        try {
            String url = "jdbc:mysql://localhost:3306/nutrifit";
            String username = "root";
            String password = "1234";
            connection = DriverManager.getConnection(url, username, password);
        }
        catch(Exception e){
            PopUpWindowMaker popUpWindowMaker = new PopUpWindowMaker("warning");
            PopUpWindow warning = popUpWindowMaker.createPopUp();
            warning.show("Database connection failed.\n" +
                    "Any creation of UserProfile, DietLog, ExerciseLog will not be stored in a database\n" +
                    "Functions such as Adding ingredients will not work as intended as ingredients are not available\n" +
                    "This also impedes creation of diet logs\n");
            e.printStackTrace();
        }
    }

    /**
     * A static method used to create or get
     * the current instance of JDBC
     * @return an instance of JDBC
     */
    public static JDBC getInstance() {
        if(jdbcInstance == null) {
            jdbcInstance = new JDBC();
        }
        return jdbcInstance;
    }

    /**
     * A method used to fetch data from the SQL database
     * Data is stored in the form of List<List<String>> where
     * each String is a value, Each nested List is a row and the
     * outer List is the table
     * @param table
     * @return a List of a List of Strings from the SQL table
     */
    @Override
    public List<List<String>> fetchData(String table) {
        List<List<String>> listOfRows = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from `"+table+"`");

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while(resultSet.next()) {
                List<String> colData = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    String value = resultSet.getString(i);
                    colData.add(value);
                }
                listOfRows.add(colData);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return listOfRows;
    }

    /**
     * A method used to fetch data which has a foreign key from the SQL database
     * Data is stored in the form of List<List<String>> where
     * each String is a value, Each nested List is a row and the
     * outer List is the table.
     * @param table
     * @param col
     * @param id
     * @return a List of a List of Strings from the SQL table
     */
    @Override
    public List<List<String>> fetchSpecificData(String table, String col, String id) {
        List<List<String>> listOfRows = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from `"+table+"` WHERE "+col+"="+id);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while(resultSet.next()) {
                List<String> colData = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    String value = resultSet.getString(i);
                    colData.add(value);
                }
                listOfRows.add(colData);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return listOfRows;
    }
    public List<List<String>> fetchNutrients(String name){
        List<List<String>> listOfRows = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("" +
                    "select `nutrient name`.NutrientName, `nutrient amount`.NutrientValue " +
                    "from `food name` " +
                    "join `nutrient amount` on `food name`.FoodID = `nutrient amount`.FoodID " +
                    "join `nutrient name` on `nutrient amount`.NutrientID = `nutrient name`.NutrientID " +
                    "WHERE `food name`.FoodDescription = \""+name+"\";");

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while(resultSet.next()) {
                List<String> colData = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    String value = resultSet.getString(i);
                    colData.add(value);
                }
                listOfRows.add(colData);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return listOfRows;
    }

    public List<List<String>> fetchFoodGroups(String foodName) {
        List<List<String>> listOfRows = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("" +
                    "select `food group`.FoodGroupName " +
                    "from `food name` " +
                    "join `food group` on `food name`.FoodGroupID = `food group`.FoodGroupID " +
                    "WHERE `food name`.FoodDescription = \""+foodName+"\";");

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while(resultSet.next()) {
                List<String> colData = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    String value = resultSet.getString(i);
                    colData.add(value);
                }
                listOfRows.add(colData);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return listOfRows;
    }

    /**
     * A method used to insert data into the database
     */
    @Override
    public void addData(String table, List<String> data) {
        try {
            StringBuilder statementBuilder = new StringBuilder("insert into ").append(table).append(" values (");
            for (int i = 0; i < data.size(); i++) {
                statementBuilder.append("?");
                if (i < data.size() - 1) {
                    statementBuilder.append(", ");
                }
            }
            statementBuilder.append(")");

            PreparedStatement preparedStatement = connection.prepareStatement(statementBuilder.toString());
            for(int i=0; i<data.size();i++){
                preparedStatement.setString(i+1, data.get(i));
            }
            preparedStatement.executeUpdate();
            System.out.println("Data inserted to database successfully!");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * A method used to delete data from the database
     */
    @Override
    public void deleteData() {

    }

    /**
     * A method used to update data in the database
     */
    @Override
    public void updateData(String table, List<String> columns, List<String> data) {
        try {StringBuilder statementBuilder = new StringBuilder("update ").append(table).append(" set ");
            for (int i = 1; i < data.size(); i++) {
                statementBuilder.append(columns.get(i)+" = '"+data.get(i)+"'");
                if (i < data.size() - 1) {
                    statementBuilder.append(", ");
                }
            }
            statementBuilder.append(" where "+columns.get(0)+" = "+data.get(0));

            PreparedStatement preparedStatement = connection.prepareStatement(statementBuilder.toString());
            preparedStatement.executeUpdate();
            System.out.println("Data in table updated successfully!");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
