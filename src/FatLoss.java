import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class FatLoss {
    private double fatLossAmount;
    public FatLoss(Date future, List<DietLog> dietLogs, List<ExerciseLog> exerciseLogs) throws Exception {
        //sort dietlog
        List<Log> combinedList = new ArrayList<>();
        combinedList.addAll(dietLogs);
        combinedList.addAll(exerciseLogs);
        combinedList.sort((Comparator.comparing(Log::getDate)));

        this.fatLossAmount = calculateFatLoss(combinedList, future);
    }
    public double getFatLossAmount(){ return this.fatLossAmount; }
    public double calculateFatLoss(List<Log> combinedList, Date future) throws Exception {
        double x = 0;
        double y = 0;
        double xx = 0;
        double yy = 0;
        double xy = 0;
        int n = combinedList.size();
        LocalDateTime start = LocalDate.parse(combinedList.get(0).getDate().toString()).atStartOfDay();
        Date end = combinedList.get(combinedList.size()-1).getDate();
        Date e = Date.valueOf(LocalDate.parse(end.toString()).plusDays(1).toString());
        if(future.before(e)){
            throw new Exception("Future date must be a date in the future");
        }
        for(Log log : combinedList){
            LocalDateTime logDate = LocalDate.parse(log.getDate().toString()).atStartOfDay();
            long daysBetween = Duration.between(start, logDate).toDays() + 1;
            x += daysBetween;
            xx += daysBetween*daysBetween;

            if(log instanceof DietLog){
                y += (((DietLog) log).getCalories() / 7700);
                yy += (((DietLog) log).getCalories() / 7700)*(((DietLog) log).getCalories() / 7700);
                xy += daysBetween*(((DietLog) log).getCalories() / 7700);
            }
            else {
                y -= (((ExerciseLog) log).getCaloriesBurnt() / 7700);
                yy -= (((ExerciseLog) log).getCaloriesBurnt() / 7700)*(((ExerciseLog) log).getCaloriesBurnt() / 7700);
                xy -= daysBetween*(((ExerciseLog) log).getCaloriesBurnt() / 7700);
            }
        }
        double a = ((y*xx) - (x*xy)) / ((n*xx) - (x*x));
        double b = ((n*xy) - (x*y)) / ((n*xx) - (x*x));

        LocalDateTime futureDate = LocalDate.parse(future.toString()).atStartOfDay();
        long futureDateDifference = Duration.between(start, futureDate).toDays() + 1;

        double futureFatLossAmount = a + (b*futureDateDifference);

        return futureFatLossAmount;
    }
}
