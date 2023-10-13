import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public interface DataManager {
    List<String> fetchData();
    void deleteData();
    void updateData();
}


class JDBC implements DataManager{
    private Connection connection;

    public JDBC() {
        try {
            String url = "jdbc:mysql://localhost:3306/database";
            String username = "username";
            String password = "password";
            connection = DriverManager.getConnection(url, username, password);
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
