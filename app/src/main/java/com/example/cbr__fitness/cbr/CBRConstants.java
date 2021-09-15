package com.example.cbr__fitness.cbr;

import com.example.cbr__fitness.enums.EquipmentEnum;
import com.example.cbr__fitness.enums.LimitationEnum;
import com.example.cbr__fitness.enums.MuscleEnum;
import com.example.cbr__fitness.enums.MuscleGroupEnum;

import java.util.List;

public class CBRConstants {

    public static final double LIMITATION_SIM_WEIGHT = 0.5;

    public static final double MATCHING_MUSCLE_GROUPS = 1.0;

    public static final double LEG_OTHER_MUSCLE_GROUP = 0.0;

    public static final double STOMACH_OTHER_MUSCLE_GROUP = 0.0;

    public static final double ARM_CHEST_MUSCLE_GROUP = 0.4;

    public static final double CHEST_SHOULDER_MUSCLE_GROUP = 0.6;

    public static final double ARM_SHOULDER_MUSCLE_GROUP = 0.5;
    //Assuming roughly 7 min for an exercise and representation in seconds.
    //Double is used to get the result into a Double space as needed for similarity
    public static final double MAX_DURATION_DIFFERENCE = 900.0;

    public static final double WEIGHT_DURATION = 1;

    public static final double WEIGHT_AGE = 1;

    public static final double WEIGHT_TRAINING_TYPE = 1;

    public static final double WEIGHT_BMI = 1;

    public static final double WEIGHT_GENDER = 1;

    public static final double WEIGHT_HEIGHT = 1;

    public static final double WEIGHT_WEIGHT = 1;

    public static final double WEIGHT_EQUIPMENT = 3;

    public static final double WEIGHT_EXERCISE_TYPE = 3;

    public static final double WEIGHT_IS_EXPLOSIVE = 2;

    public static final double WEIGHT_MOVEMENT_TYPE = 2;

    public static final double WEIGHT_PRIMARY_MUSCLE = 2;

    public static final double WEIGHT_SECONDARY_MUSCLE = 2;

    public static final double EQUIPMENT_SIM_WEIGHT = 0.5;

    public static double getMuscleGroupSimilarityMultiplier(MuscleGroupEnum request, MuscleGroupEnum query){
        if (request == query) {
            return MATCHING_MUSCLE_GROUPS;
        } else if (request == MuscleGroupEnum.LEGS || request == MuscleGroupEnum.ABS
                || query == MuscleGroupEnum.LEGS || query == MuscleGroupEnum.ABS
                || request == MuscleGroupEnum.BACK || query == MuscleGroupEnum.BACK
                || request == MuscleGroupEnum.WHOLE || query == MuscleGroupEnum.WHOLE) {
            //Returning this as it is has the same value as the other relations. This can be subject to change.
            return LEG_OTHER_MUSCLE_GROUP;
        } else if (request == MuscleGroupEnum.ARMS) {
            if (query == MuscleGroupEnum.SHOULDER) {
                return ARM_SHOULDER_MUSCLE_GROUP;
            } else {
                return ARM_CHEST_MUSCLE_GROUP;
            }
        } else if (request == MuscleGroupEnum.CHEST) {
            if (query == MuscleGroupEnum.SHOULDER) {
                return CHEST_SHOULDER_MUSCLE_GROUP;
            } else {
                return ARM_CHEST_MUSCLE_GROUP;
            }
        } else {
            if (query == MuscleGroupEnum.ARMS) {
                return ARM_SHOULDER_MUSCLE_GROUP;
            } else {
                return CHEST_SHOULDER_MUSCLE_GROUP;
            }
        }
    }

    public static double getSimilarityForDuration (double targetDuration, double queriedDuration) {
        double sim = 0.0;
        //Done to prevent a division of 0
        if (!(targetDuration == queriedDuration)) {
            //Assuming a linear reduction in similarity reaching 0 as MAX_DURATION_DIFFERENCE is reached.
            double temp = (MAX_DURATION_DIFFERENCE - (Math.abs(targetDuration - queriedDuration)));
            if (temp > 0) {
                sim = temp /MAX_DURATION_DIFFERENCE;
            }
        }

        return  sim;
    }

    public static double getCompleteWeightSum () {
        return WEIGHT_AGE + WEIGHT_WEIGHT + WEIGHT_BMI + WEIGHT_GENDER + WEIGHT_HEIGHT
                + WEIGHT_DURATION + WEIGHT_DURATION;
    }

    public static double LimitationSimilarity(List<LimitationEnum> logged, List<LimitationEnum> queried) {
        double result = (LimitationEnum.values().length * 1.0);
        for (LimitationEnum e : logged) {
            if (!queried.contains(e)) {
                result -=1;
            }
        }
        for (LimitationEnum e : queried) {
            if (!logged.contains(e)) {
                result -= 1;
            }
        }
        return  result/LimitationEnum.values().length;
    }

    public static double getEquipmentSimilarity(List<EquipmentEnum> logged, List<EquipmentEnum> queried) {
        double result = (EquipmentEnum.values().length * 1.0);
        for (EquipmentEnum e : logged) {
            if (!queried.contains(e)) {
                result -=1;
            }
        }
        for (EquipmentEnum e : queried) {
            if (!logged.contains(e)) {
                result -= 1;
            }
        }
        return  result/EquipmentEnum.values().length;
    }
}
