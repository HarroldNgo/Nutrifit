import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface used to manage all data sources that the
 * application may or may not use
 *
 * @author Harrold Ngo
 */
public interface DataManager {
    List<List<String>> fetchData(String table);

    List<List<String>> fetchDataForeignKey(String table, String col, int id);

    void addData();
    void deleteData();
    void updateData();
}

/**
 * A class that is specifically used for connecting to and
 * retreiving/inputing data from an SQL database
 */
class JDBC implements DataManager{
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
            String password = "LordLucyHN123!";
            connection = DriverManager.getConnection(url, username, password);
        }
        catch(Exception e){
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
    public List<List<String>> fetchDataForeignKey(String table, String col, int id) {
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

    /**
     * A method used to insert data into the database
     */
    @Override
    public void addData() {

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
    public void updateData() {

    }
}
