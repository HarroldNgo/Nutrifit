
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public interface Visualizer {
    CategoryDataset createDataset();
    JFreeChart createChart(CategoryDataset dataset);
}

class CalorieExerciseVisualizer implements Visualizer{
    @Override
    public CategoryDataset createDataset() {
        return null;
    }

    @Override
    public JFreeChart createChart(CategoryDataset dataset) {
        return null;
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