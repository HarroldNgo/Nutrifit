import java.util.Date;
public class UserProfile {
    private String sex;
    private Date dateOfBirth;
    private double height;
    private double weight;

    //Maybe we can use boolean, 0 = metric, 1 = imperial?
    private String units = "metric";

    public UserProfile(String sex, Date date, double height, double weight){
        this.sex=sex;
        this.dateOfBirth = date;
        this.height = height;
        this.weight = weight;

        //add to database
    }
    public void deleteUserProfile(){

    }
    public void editUserProfile(UserProfile profile, String newSex, Date newDate, double newHeight, double newWeight, String newUnits){
        profile.sex = newSex;
        profile.dateOfBirth = newDate;
        profile.height = newHeight;
        profile.weight = newWeight;
        profile.units = newUnits;

        //update to database
    }

}
