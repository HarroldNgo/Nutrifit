
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

/**
 * An abstract class used to help with the initialization of all types of
 * visualizers and their shared elements.
 *
 * @author Harrold Ngo
 */
public abstract class Visualizer {
    DefaultCategoryDataset dataset;
    JFreeChart chart;
}

/**
 * This class initializes and creates the chart used to
 * show calorie intake and calories burnt
 */
class CalorieExerciseVisualizer extends Visualizer{

    /**
     * Creates a new Calorie Exercise Visualizer chart that
     * displays the calorie intake and calorie outtake overtime
     *
     * @param start
     * @param end
     * @param dietLogs
     * @param exerciseLogs
     */
    public CalorieExerciseVisualizer(Date start, Date end, List<DietLog> dietLogs, List<ExerciseLog> exerciseLogs) {
        this.dataset = new DefaultCategoryDataset();

        //Iterates through each diet log to find logs that correspond to the specified time range
        for(DietLog log : dietLogs) {
            if(log.getDate().after(start)){
                long difference = log.getDate().getTime() - start.getTime();
                long day = (difference / (1000 * 60 * 60 * 24)) % 365;

                double calories = log.getNutrientValues().get("calories");
                dataset.addValue(calories, "Calories Gained", "Day " + day);
            }
        }

        //Iterates through each exercise log to find logs that correspond to the specified time range
        for(ExerciseLog log : exerciseLogs) {
            if(log.getDate().after(start)){
                long difference = log.getDate().getTime() - start.getTime();
                long day = (difference / (1000 * 60 * 60 * 24)) % 365;

                double calories = log.getCaloriesBurnt();
                dataset.addValue(calories, "Calories Burnt", "Day " + day);
            }
        }

        //The actual creation/initialization of the chart used
        chart = ChartFactory.createLineChart(
                "Calorie and Exercise",
                "Day",
                "Calories",
                this.dataset
        );


    }

    public static void main(String[] arg) throws ParseException {
        List<DietLog> listOfDietLogs = new ArrayList<>();
        List<ExerciseLog> listOfExerciseLogs = new ArrayList<>();

        DietLog d1 = new DietLog(Date.valueOf("2023-10-01"), "breakfast");
        d1.addIngredient("chicken", 2);
        d1.addNutrientValue("calories", 300);
        DietLog d2 = new DietLog(Date.valueOf("2023-10-02"), "breakfast");
        d2.addIngredient("chicken", 1);
        d2.addNutrientValue("calories", 150);

        ExerciseLog e1 = new ExerciseLog(Date.valueOf("2023-10-01"),"walking", Duration.ofMinutes(60), "low");
        e1.setCaloriesBurnt(160);
        ExerciseLog e2 = new ExerciseLog(Date.valueOf("2023-10-02"),"running", Duration.ofMinutes(30), "high");
        e2.setCaloriesBurnt(350);

        listOfDietLogs.add(d1);
        listOfDietLogs.add(d2);
        listOfExerciseLogs.add(e1);
        listOfExerciseLogs.add(e2);

        CalorieExerciseVisualizer cev = new CalorieExerciseVisualizer(Date.valueOf("2023-09-30"), Date.valueOf("2023-11-01"), listOfDietLogs, listOfExerciseLogs);

        JFrame lineFrame = new JFrame();
        ChartPanel lineChartPanel = new ChartPanel(cev.chart);
        lineFrame.add(lineChartPanel);

        lineFrame.pack();
        lineFrame.setVisible(true);
    }
}

class NutrientIntakeVisualizer extends Visualizer{

}