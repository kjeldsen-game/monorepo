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
    private static final Double DAY_KJELDSEN = 4.01;
    private static final Double MONTH_KJELDSEN = 30.44;
    private static final Double YEAR_KJELDSEN = 365.0;
    private static final Double DAY = 1.0;
    private static final Double MONTH = 7.58;
    private static final Double YEAR = 91.0;
    private static final Integer MIN_AGE = 15;
    private static final Integer MAX_AGE = 33;
    private static final Range<Integer> RANGE_OF_AGE = Range.between(MIN_AGE, MAX_AGE);

    private Integer years;
    private Double months;
    private Double days;

    public static PlayerAge generateAgeOfAPlayer(){
        double month = RandomUtils.nextDouble(0.0,7.58);
        double days = RandomUtils.nextDouble(0.0,4.00);
        return PlayerAge.builder()
                .years(ageGeneration())
                .months((double) Math.round(month*100)/100)
                .days((double) Math.round(days*100)/100)
                .build();
    }
    public static Integer ageGeneration() {
        return RandomUtils.nextInt(RANGE_OF_AGE.getMinimum(), RANGE_OF_AGE.getMaximum());
    }

    public static PlayerAge gettingOlder(PlayerAge age){
        age.incrementDays();
        if (age.getDays()>MONTH_KJELDSEN){
           age.setDays(0d);
           age.setMonths(age.getMonths()+DAY_KJELDSEN);
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
