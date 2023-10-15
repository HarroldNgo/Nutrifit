import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface DataManager {
    List<List<String>> fetchData(String table);
    void addData();
    void deleteData();
    void updateData();
}


class JDBC implements DataManager{
    private Connection connection;

    /*
    public static void main(String[] arg) {
        JDBC jdbc = new JDBC();
        List<List<String>> listOfData = jdbc.fetchData("food group");
        for(List<String> row : listOfData) {
            for(String value : row) {
                System.out.println(value);
            }
            System.out.println();
        }
    }
     */
    public JDBC(){
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

    @Override
    public void addData() {

    }

    @Override
    public void deleteData() {

    }

    @Override
    public void updateData() {

    }
}
