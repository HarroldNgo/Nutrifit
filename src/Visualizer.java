
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.nio.ByteOrder;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.Date;
import java.util.List;

/**
 * An abstract class used to help with the initialization of all types of
 * visualizers and their shared elements.
 *
 * @author Harrold Ngo
 */
abstract class Visualizer {
    protected JFreeChart chart;
    protected JFrame window;
    public abstract void addToDataset() throws Exception;
    public abstract void createChart();
    public abstract void show();
}

/**
 * This class initializes and creates the chart used to
 * show calorie intake and calories burnt
 */
class CalorieExerciseVisualizer extends Visualizer {
    private DefaultCategoryDataset dataset;
    private Date start;
    private Date end;
    private List<DietLog> dietLogs;
    private List<ExerciseLog> exerciseLogs;
    /**
     * Creates a new Calorie Exercise Visualizer chart that
     * displays the calorie intake and calorie outtake overtime
     *
     * @param start
     * @param end
     * @param dietLogs
     * @param exerciseLogs
     */
    public CalorieExerciseVisualizer(Date start, Date end, List<DietLog> dietLogs, List<ExerciseLog> exerciseLogs) throws Exception {
        this.dataset = new DefaultCategoryDataset();
        this.start=start;
        this.end=end;
        this.dietLogs=dietLogs;
        this.exerciseLogs=exerciseLogs;
        addToDataset();
        createChart();
        show();
    }

    @Override
    public void addToDataset() throws Exception {
        //Iterates through each diet log to find logs that correspond to the specified time range
        for(DietLog log : dietLogs) {
            Date s = Date.valueOf(LocalDate.parse(start.toString()).minusDays(1).toString());
            Date e = Date.valueOf(LocalDate.parse(end.toString()).plusDays(1).toString());
            long diff = end.getTime() - start.getTime();
            long numDays = (diff / (1000 * 60 * 60 * 24)) % 365;
            System.out.println(s.toString()+" "+e.toString());
            if(numDays < 0){
                throw new Exception("Start must be before end date");
            }
            if(log.getDate().after(s) && log.getDate().before(e)){
                long difference = log.getDate().getTime() - start.getTime();
                long day = (difference / (1000 * 60 * 60 * 24)) % 365;
                double calories = 0;
                for(Nutrient nutrient : log.getNutrients()){
                    if(nutrient.getNutrient().compareTo("ENERGY (KILOCALORIES)")==0){
                        calories += nutrient.getAmount();
                        break;
                    }
                }
                dataset.addValue(calories, "Calories Gained", "Day " + day);
            }
        }

        //Iterates through each exercise log to find logs that correspond to the specified time range
        for(ExerciseLog log : exerciseLogs) {
            Date s = Date.valueOf(LocalDate.parse(start.toString()).minusDays(1).toString());
            Date e = Date.valueOf(LocalDate.parse(end.toString()).plusDays(1).toString());
            if(log.getDate().after(s) && log.getDate().before(e)){
                long difference = log.getDate().getTime() - start.getTime();
                long day = (difference / (1000 * 60 * 60 * 24)) % 365;

                double calories = log.getCaloriesBurnt();
                dataset.addValue(calories, "Calories Burnt", "Day " + day);
            }
        }
    }

    @Override
    public void createChart(){
        //The actual creation/initialization of the chart used
        this.chart = ChartFactory.createLineChart(
                "Calorie and Exercise",
                "Day",
                "Calories",
                this.dataset
        );
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setDefaultShapesVisible(true);
    }

    @Override
    public void show() {
        window = new JFrame();
        ChartPanel lineChartPanel = new ChartPanel(chart);
        window.add(lineChartPanel);
        window.pack();
        window.setVisible(true);
    }
    public static void main(String[] arg) {

    }
}

class NutrientIntakeVisualizer extends Visualizer {
    private DefaultCategoryDataset dataset;
    private Date start;
    private Date end;
    private List<DietLog> dietLogs;
    private JFreeChart recommendedChart;
    public NutrientIntakeVisualizer(Date start, Date end, List<DietLog> dietLogs) throws Exception {
        this.start=start;
        this.end=end;
        this.dietLogs=dietLogs;
        addToDataset();
        createChart();
        createRecommendedChart();
        show();
    }
    public void createRecommendedChart(){
        DefaultCategoryDataset recommendedDataset = new DefaultCategoryDataset();
        recommendedDataset.addValue((300000/414974.0)*100, "Nutrients", "Carbohydrate");
        recommendedDataset.addValue((65000/414974.0)*100, "Nutrients", "Fat");
        recommendedDataset.addValue((25000/414974.0)*100, "Nutrients", "Fibre");
        recommendedDataset.addValue((20000/414974.0)*100, "Nutrients", "Satu/Trans Fats");
        recommendedDataset.addValue((2400/414974.0)*100, "Nutrients", "Sodium");
        recommendedDataset.addValue((2574/414974.0)*100, "Nutrients", "Other Nutrients");
        this.recommendedChart = ChartFactory.createBarChart(
                "Recommended Daily Nutrients",
                "Nutrients",
                "Amount(%)",
                recommendedDataset
        );
    }

    @Override
    public void addToDataset() throws Exception {
        this.dataset = new DefaultCategoryDataset();
        Map<String, Double> tempMap = new HashMap<>();
        Date s = Date.valueOf(LocalDate.parse(start.toString()).minusDays(1).toString());
        Date e = Date.valueOf(LocalDate.parse(end.toString()).plusDays(1).toString());
        LocalDateTime startDate = LocalDate.parse(start.toString()).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(end.toString()).atStartOfDay();
        long numOfDays = Duration.between(startDate, endDate).toDays() + 1;
        if(numOfDays < 1){
            throw new Exception("Start date is after End date");
        }
        for(DietLog log : dietLogs){
            if(log.getDate().after(s) && log.getDate().before(e)){
                for(Nutrient nutrient : log.getNutrients()){
                    if(tempMap.containsKey(nutrient.getNutrient())){
                        tempMap.put(nutrient.getNutrient(), tempMap.get(nutrient.getNutrient())+nutrient.getAmount());
                    }
                    else {
                        tempMap.put(nutrient.getNutrient(), nutrient.getAmount());
                    }
                    tempMap.put(nutrient.getNutrient(), nutrient.getAmount());
                }
            }
        }
        List<Double> allNutrientsAmt = new ArrayList<>(tempMap.values());
        Collections.sort(allNutrientsAmt);
        Collections.reverse(allNutrientsAmt);
        int count=0;
        double others = 0;
        double total = 0;
        for(Double amt : allNutrientsAmt){
            total += amt;
        }
        for(Double amt : allNutrientsAmt){
            for(Map.Entry<String, Double> entry : tempMap.entrySet()){
                if(count>4) break;
                if(amt==entry.getValue()){
                    double percentValue = ((entry.getValue()) / numOfDays / total)*100;
                    dataset.addValue(percentValue, "Nutrients", entry.getKey());
                    count++;
                }
            }
            double otherPercentValues = ((amt) / numOfDays / total)*100;
            others += otherPercentValues;
        }

        dataset.addValue(others, "Nutrients", "Other Nutrients");
    }

    @Override
    public void createChart() {
        chart = ChartFactory.createBarChart(
                "Average Daily Nutrients",
                "Nutrients",
                "Amount(%)",
                this.dataset
        );
    }

    @Override
    public void show() {
        window = new JFrame();
        ChartPanel lineChartPanel = new ChartPanel(chart);
        ChartPanel lineChartPanel2 = new ChartPanel(recommendedChart);
        lineChartPanel2.setPreferredSize(new Dimension(500, 200));
        window.add(lineChartPanel, BorderLayout.WEST);
        window.add(lineChartPanel2, BorderLayout.EAST);
        window.pack();
        window.setVisible(true);
    }
}

class DietAlignsVisualizer extends Visualizer {
    private DefaultPieDataset dataset;
    private UserProfile selectedProfile;
    private JFreeChart chartCFG;
    public DietAlignsVisualizer(UserProfile selectedProfile){
        this.selectedProfile = selectedProfile;
        addToDataset();
        createChart();
        createCFGChart();
        show();
    }
    public void createCFGChart(){
        DefaultPieDataset cfgDataset = new DefaultPieDataset();
        int age = selectedProfile.getAge();
        String sex = selectedProfile.getSex();
        if(age < 2){
            cfgDataset.setValue("Babyfood & Breastmilk", 1);
        }
        else if(age < 4){
            cfgDataset.setValue("Vegetables and Fruit", 4);
            cfgDataset.setValue("Grain Products", 3);
            cfgDataset.setValue("Milk and Alternatives", 2);
            cfgDataset.setValue("Meat and Alternatives", 1);

        }
        else if(age < 9){
            cfgDataset.setValue("Vegetables and Fruit", 5);
            cfgDataset.setValue("Grain Products", 4);
            cfgDataset.setValue("Milk and Alternatives", 2);
            cfgDataset.setValue("Meat and Alternatives", 1);
        }
        else if(age < 14){
            cfgDataset.setValue("Vegetables and Fruit", 6);
            cfgDataset.setValue("Grain Products", 6);
            cfgDataset.setValue("Milk and Alternatives", 3.5);
            cfgDataset.setValue("Meat and Alternatives", 1.5);
        }
        else if(age < 19){
            if(sex.equals("Male")){
                cfgDataset.setValue("Vegetables and Fruit", 8);
                cfgDataset.setValue("Grain Products", 7);
                cfgDataset.setValue("Milk and Alternatives", 3.5);
                cfgDataset.setValue("Meat and Alternatives", 3);
            }
            else {
                cfgDataset.setValue("Vegetables and Fruit", 7);
                cfgDataset.setValue("Grain Products", 6);
                cfgDataset.setValue("Milk and Alternatives", 3.5);
                cfgDataset.setValue("Meat and Alternatives", 2);
            }
        }
        else if(age < 51){
            if(sex.equals("Male")){
                cfgDataset.setValue("Vegetables and Fruit", 9);
                cfgDataset.setValue("Grain Products", 8);
                cfgDataset.setValue("Milk and Alternatives", 2);
                cfgDataset.setValue("Meat and Alternatives", 3);
            }
            else {
                cfgDataset.setValue("Vegetables and Fruit", 7.5);
                cfgDataset.setValue("Grain Products", 6.5);
                cfgDataset.setValue("Milk and Alternatives", 2);
                cfgDataset.setValue("Meat and Alternatives", 2);
            }
        }
        else {
            if(sex.equals("Male")){
                cfgDataset.setValue("Vegetables and Fruit", 7);
                cfgDataset.setValue("Grain Products", 7);
                cfgDataset.setValue("Milk and Alternatives", 3);
                cfgDataset.setValue("Meat and Alternatives", 3);
            }
            else {
                cfgDataset.setValue("Vegetables and Fruit", 7);
                cfgDataset.setValue("Grain Products", 6);
                cfgDataset.setValue("Milk and Alternatives", 3);
                cfgDataset.setValue("Meat and Alternatives", 2);
            }
        }
        this.chartCFG = ChartFactory.createPieChart(
                "CFG Recommended Diet",
                cfgDataset,
                true,
                true,
                false
        );
    }

    @Override
    public void addToDataset() {
        this.dataset = new DefaultPieDataset<>();
        List<DietLog> dietLogs = selectedProfile.getDietLogs();
        Map<String, Integer> foodGroups = new HashMap<>();
        DataManager dm = new DataManager(JDBC.getInstance());
        for(DietLog dietLog : dietLogs){
            for(Ingredient ingredient : dietLog.getIngredients()){
                String foodGroup = dm.fetchFoodGroups(ingredient.getIngredient()).get(0).get(0);
                if (foodGroups.containsKey(foodGroup)) {
                    foodGroups.put(foodGroup, foodGroups.get(foodGroup)+1);
                }
                else {
                    foodGroups.put(foodGroup, 1);
                }
            }
        }
        for(Map.Entry<String, Integer> foodGroup : foodGroups.entrySet()){
            this.dataset.setValue(foodGroup.getKey(), foodGroup.getValue());
        }
    }

    @Override
    public void createChart() {
        this.chart = ChartFactory.createPieChart(
                "Current Diet",
                dataset,
                true,
                true,
                false
        );
    }

    @Override
    public void show() {
        window = new JFrame();
        ChartPanel pieChartPanel = new ChartPanel(chart);
        ChartPanel cfgPieChartPanel = new ChartPanel(chartCFG);
        pieChartPanel.setPreferredSize(new Dimension(400, 260));
        cfgPieChartPanel.setPreferredSize(new Dimension(400, 260));
        window.add(pieChartPanel, BorderLayout.WEST);
        window.add(cfgPieChartPanel, BorderLayout.EAST);
        window.pack();
        window.setVisible(true);
    }
}