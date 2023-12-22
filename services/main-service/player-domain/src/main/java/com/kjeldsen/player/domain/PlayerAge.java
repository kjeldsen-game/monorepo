package com.kjeldsen.player.domain;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.RandomUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PlayerAge {
    public static final Double DAY = 4.01;
    public static final Double MONTH = 7.58;
    public static final Double YEAR = 91.0;
    private static final Integer MIN_AGE = 15;
    private static final Integer MAX_AGE = 33;
    private static final Range<Integer> RANGE_OF_AGE = Range.between(MIN_AGE, MAX_AGE);

    private Integer years;
    private Double months;
    private Double days;

    public static PlayerAge generateAgeOfAPlayer(){
        Double month = RandomUtils.nextDouble(0.0,7.58);
        Double days = RandomUtils.nextDouble(0.0,4.00);
        return PlayerAge.builder()
                .years(ageGeneration())
                .months((double) Math.round(month*100)/100)
                .days((double) Math.round(days*100)/100)
                .build();
    }

    public static Integer ageGeneration() {
        return RandomUtils.nextInt(RANGE_OF_AGE.getMinimum(), RANGE_OF_AGE.getMaximum());
    }
    private Integer validateDays(Integer days){
        if (days>DAY){
            days = 0;
            incrementMonths();
        } else {
            ++days;
        }
        return days;
    }

    private Integer validateMonths(Integer months){
        if (months>MONTH){
            months = 0;
            incrementYears();
        } else {
            ++months;
        }
        return months;
    }
    private void incrementYears(){
        years += 1;
    }

    private void incrementMonths(){
        months += 1;
    }
    private Double gettingOlder(){
        return days += DAY;
    }
}
