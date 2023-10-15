
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

public interface Visualizer {
    CategoryDataset createDataset();
    JFreeChart createChart(CategoryDataset dataset);
}

class CalorieExerciseVisualizer implements Visualizer{
    DefaultCategoryDataset dataset;
    JFreeChart lineChart;

    public CalorieExerciseVisualizer(Date start, Date end, List<DietLog> dietLogs, List<ExerciseLog> exerciseLogs) {
        this.dataset = new DefaultCategoryDataset();
        for(DietLog log : dietLogs) {
            if(log.getDate().after(start)){
                long difference = log.dateOfEntry.getTime() - start.getTime();
                long day = (difference / (1000 * 60 * 60 * 24)) % 365;

                double calories = log.getNutrientValues().get("calories");
                dataset.addValue(calories, "Calories Gained", "Day " + day);
            }
        }
        for(ExerciseLog log : exerciseLogs) {
            if(log.getDate().after(start)){
                long difference = log.dateOfEntry.getTime() - start.getTime();
                long day = (difference / (1000 * 60 * 60 * 24)) % 365;

                double calories = log.getCaloriesBurnt();
                dataset.addValue(calories, "Calories Burnt", "Day " + day);
            }
        }
        lineChart = ChartFactory.createLineChart(
                "Calorie and Exercise",
                "Day",
                "Calories",
                this.dataset
        );


    }
    @Override
    public CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "Category 1", "Series 1");
        dataset.addValue(4.0, "Category 1", "Series 2");
        dataset.addValue(3.0, "Category 2", "Series 3");
        dataset.addValue(7.0, "Category 2", "Series 4");
        return dataset;
    }

    @Override
    public JFreeChart createChart(CategoryDataset dataset) {
        return ChartFactory.createLineChart(
                "Line Chart",
                "Category",
                "Value",
                dataset
        );
    }

    //TESTING IF IT WORKS
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
        ChartPanel lineChartPanel = new ChartPanel(cev.lineChart);
        lineFrame.add(lineChartPanel);

        lineFrame.pack();
        lineFrame.setVisible(true);
    }
}

class NutrientIntakeVisualizer implements Visualizer{

    @Override
    public CategoryDataset createDataset() {
        return null;
    }

    @Override
    public JFreeChart createChart(CategoryDataset dataset) {
        return null;
    }
}