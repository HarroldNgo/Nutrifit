import java.util.Date;
public class UserProfile {
    private String name;
    private String sex;
    private Date dateOfBirth;
    private double height;
    private double weight;

    //Maybe we can use boolean, 0 = metric, 1 = imperial?
    private String units = "metric";

    public UserProfile(String name, String sex, Date date, double height, double weight){
        this.name = name;
        this.sex=sex;
        this.dateOfBirth = date;
        this.height = height;
        this.weight = weight;

        //add to database
    }
    public void deleteUserProfile(){

    }
    public void editUserProfile(UserProfile profile, String newName, String newSex, Date newDate, double newHeight, double newWeight, String newUnits){
        profile.name = newName;
        profile.sex = newSex;
        profile.dateOfBirth = newDate;
        profile.height = newHeight;
        profile.weight = newWeight;
        profile.units = newUnits;

        //update to database
    }
    public String getName(){
        return this.name;
    }
    public String getSex(){
        return this.sex;
    }
    public Date getDateOfBirth(){
        return this.dateOfBirth;
    }
    public double getHeight(){
        return this.height;
    }
    public double getWeight(){
        return this.weight;
    }


}
