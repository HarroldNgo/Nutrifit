import java.sql.*;
import java.util.List;

public interface DataManager {
    List<String> fetchData();
    void deleteData();
    void updateData();
}


class JDBC implements DataManager{
    private Connection connection;

    public static void main(String[] arg) {
        Connection connection;
        try {
            String url = "jdbc:mysql://localhost:3306/nutrifit";
            String username = "root";
            String password = "password";
            connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from `food group`");

            while(resultSet.next()) {
                System.out.println(resultSet.getString("FoodGroupName"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<String> fetchData() {
        return null;
    }

    @Override
    public void deleteData() {

    }

    @Override
    public void updateData() {

    }
}
