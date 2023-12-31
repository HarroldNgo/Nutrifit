

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

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
        List<Log> combinedList = new ArrayList<>();
        combinedList.addAll(dietLogs);
        combinedList.addAll(exerciseLogs);

        Date s = Date.valueOf(LocalDate.parse(start.toString()).minusDays(1).toString());
        Date e = Date.valueOf(LocalDate.parse(end.toString()).plusDays(1).toString());
        long diff = end.getTime() - start.getTime();
        long numDays = (diff / (1000 * 60 * 60 * 24)) % 365;
        if(numDays < 0){
            throw new Exception("Start must be before end date");
        }

        Map<Date, Double> multipleDietValueDays = new LinkedHashMap<>();
        Map<Date, Double> multipleExerciseValueDays = new LinkedHashMap<>();
        for(Log log : combinedList){
            if(log.getDate().after(s) && log.getDate().before(e)){
                if(log instanceof DietLog){
                    double calories = ((DietLog) log).getCalories();
                    if(multipleDietValueDays.containsKey(log.getDate())){
                        multipleDietValueDays.put(log.getDate(), multipleDietValueDays.get(log.getDate())+calories);
                    }
                    else {
                        multipleDietValueDays.put(log.getDate(), calories);
                    }
                }
                else {
                    double caloriesBurnt = ((ExerciseLog) log).getCaloriesBurnt();
                    if(multipleExerciseValueDays.containsKey(log.getDate())){
                        multipleExerciseValueDays.put(log.getDate(), multipleExerciseValueDays.get(log.getDate())+caloriesBurnt);
                    }
                    else {
                        multipleExerciseValueDays.put(log.getDate(), caloriesBurnt);
                    }
                }
            }
        }
        for(Map.Entry<Date, Double> entry : multipleDietValueDays.entrySet()){
            long difference = entry.getKey().getTime() - start.getTime();
            long day = (difference / (1000 * 60 * 60 * 24)) % 365;
            dataset.addValue(entry.getValue(), "Calories", "Day " + day);
        }
        for(Map.Entry<Date, Double> entry : multipleExerciseValueDays.entrySet()){
            long difference = entry.getKey().getTime() - start.getTime();
            long day = (difference / (1000 * 60 * 60 * 24)) % 365;
            dataset.addValue(entry.getValue(), "Calories Burnt", "Day " + day);
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
    private int showNumber;
    public NutrientIntakeVisualizer(Date start, Date end, List<DietLog> dietLogs, int showNumber) throws Exception {
        this.start=start;
        this.end=end;
        this.dietLogs=dietLogs;
        this.showNumber = showNumber;
        addToDataset();
        createChart();
        createRecommendedChart();
        show();
    }
    public void createRecommendedChart(){
        //Probably a better way to have a dataset of 5 and dataset of 10
        DefaultCategoryDataset recommendedDataset5 = new DefaultCategoryDataset();
    	DefaultCategoryDataset recommendedDataset10 = new DefaultCategoryDataset();
    	
    	recommendedDataset5.addValue((100000000/234984157.4)*100, "Nutrients", "Sugars");
        recommendedDataset5.addValue((75000000/234984157.4)*100, "Nutrients", "Fat");
        recommendedDataset5.addValue((28000000/234984157.4)*100, "Nutrients", "Fibre");
        recommendedDataset5.addValue((20000000/234984157.4)*100, "Nutrients", "Sat/Trans Fats");
        recommendedDataset5.addValue((3400000/234984157.4)*100, "Nutrients", "Potassium");
        
        recommendedDataset10.addValue((100000000/234984157.4)*100, "Nutrients", "Sugars");
        recommendedDataset10.addValue((75000000/234984157.4)*100, "Nutrients", "Fat");
        recommendedDataset10.addValue((28000000/234984157.4)*100, "Nutrients", "Fibre");
        recommendedDataset10.addValue((20000000/234984157.4)*100, "Nutrients", "Sat/Trans Fats");
        recommendedDataset10.addValue((3400000/234984157.4)*100, "Nutrients", "Potassium");
        recommendedDataset10.addValue((2300000/234984157.4)*100, "Nutrients", "Chloride");
        recommendedDataset10.addValue((2300000/234984157.4)*100, "Nutrients", "Sodium");
        recommendedDataset10.addValue((1300000/234984157.4)*100, "Nutrients", "Calcium");
        recommendedDataset10.addValue((1250000/234984157.4)*100, "Nutrients", "Phosphorus");
        recommendedDataset10.addValue((550000/234984157.4)*100, "Nutrients", "Choline");
        
       if (showNumber == 5) {
        	this.recommendedChart = ChartFactory.createBarChart(
                    "Recommended Daily Nutrients",
                    "Nutrients",
                    "Amount(%)",
                    recommendedDataset5
            );
        } else {
        	this.recommendedChart = ChartFactory.createBarChart(
                    "Recommended Daily Nutrients",
                    "Nutrients",
                    "Amount(%)",
                    recommendedDataset10
            );
        }
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
                if(count>showNumber-1) break;
                if(amt==entry.getValue()){
                    double percentValue = ((entry.getValue()) / numOfDays / total)*100;
                    dataset.addValue(percentValue, "Nutrients", entry.getKey());
                    count++;
                    break;
                }
            }
            System.out.println(""+amt/numOfDays);
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
