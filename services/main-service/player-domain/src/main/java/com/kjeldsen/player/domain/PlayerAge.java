package com.kjeldsen.player.domain;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.RandomUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PlayerAge {
    private static final Double DAY_KJELDSEN = 4.01;
    private static final Double MONTH_KJELDSEN = 30.44;
    private static final Double YEAR_KJELDSEN = 365.0;
    private static final Double DAY = 1.0;
    private static final Double MONTH = 7.58;
    private static final Double YEAR = 91.0;
    private static final Integer MIN_AGE_JUNIOR = 15;
    private static final Integer MAX_AGE_JUNIOR = 20;
    private static final Integer MIN_AGE_SENIOR = 21;
    private static final Integer MAX_AGE_SENIOR = 33;
    private static final Range<Integer> RANGE_OF_AGE_JUNIOR = Range.between(MIN_AGE_JUNIOR, MAX_AGE_JUNIOR);
    private static final Range<Integer> RANGE_OF_AGE_SENIOR = Range.between(MIN_AGE_SENIOR, MAX_AGE_SENIOR);
    private Integer years;
    private Double months;
    private Double days;

    public static PlayerAge generateAgeOfAPlayer(PlayerCategory playerCategory){

        Integer years;

        if(PlayerCategory.JUNIOR.name().equals(playerCategory.name())){
            years = RandomUtils.nextInt(RANGE_OF_AGE_JUNIOR.getMinimum(), RANGE_OF_AGE_JUNIOR.getMaximum());
        } else {
            years = RandomUtils.nextInt(RANGE_OF_AGE_SENIOR.getMinimum(), RANGE_OF_AGE_SENIOR.getMaximum());
        }

        double month = RandomUtils.nextDouble(0.0,MONTH_KJELDSEN);
        double days = RandomUtils.nextDouble(0.0,DAY_KJELDSEN);
        return PlayerAge.builder()
                .years(years)
                .months((double) Math.round(month*100)/100)
                .days((double) Math.round(days*100)/100)
                .build();
    }

    public static PlayerAge generateAgeOfAPlayer(){

        double month = RandomUtils.nextDouble(0.0,MONTH_KJELDSEN);
        double days = RandomUtils.nextDouble(0.0,DAY_KJELDSEN);
        return PlayerAge.builder()
                .years(ageGeneration())
                .months((double) Math.round(month*100)/100)
                .days((double) Math.round(days*100)/100)
                .build();
    }

    public static PlayerAge generatePlayerAge(int minAge, int maxAge) {
        return PlayerAge.builder()
            .years(minAge == maxAge ? minAge : ThreadLocalRandom.current().nextInt(minAge, maxAge))
            .months(ThreadLocalRandom.current().nextDouble(0.0,DAY_KJELDSEN))
            .days(ThreadLocalRandom.current().nextDouble(0.0,DAY_KJELDSEN))
            .build();
    }

    public static Integer ageGeneration() {
        return RandomUtils.nextInt(RANGE_OF_AGE_JUNIOR.getMinimum(), RANGE_OF_AGE_SENIOR.getMaximum());
    }

    public static PlayerAge gettingOlder(PlayerAge age) throws IllegalArgumentException {
        if (age == null) { throw  new IllegalArgumentException("Age cannot be null"); }

        age.incrementDays();
        if (age.getDays()>MONTH_KJELDSEN){
           age.setDays(0d);
           age.setMonths(age.getMonths()+MONTH_KJELDSEN);
        } else if (age.getMonths()>YEAR_KJELDSEN) {
           age.setMonths(0d);
           age.setYears(age.getYears()+1);
        }
        return age;
    }

    private void incrementDays(){
        days += DAY_KJELDSEN;
    }
}
