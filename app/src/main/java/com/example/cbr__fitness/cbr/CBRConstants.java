package com.example.cbr__fitness.cbr;

import android.util.Pair;

import com.example.cbr__fitness.enums.EquipmentEnum;
import com.example.cbr__fitness.enums.GenderEnum;
import com.example.cbr__fitness.enums.GoalEnum;
import com.example.cbr__fitness.enums.LimitationEnum;
import com.example.cbr__fitness.enums.MuscleEnum;
import com.example.cbr__fitness.enums.MuscleGroupEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.dfki.mycbr.core.similarity.config.AmalgamationConfig;

public class CBRConstants {

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

    public static final double WEIGHT_LIMITATION = 1;

    public static final double WEIGHT_TRAINING_TYPE = 1;

    public static final double WEIGHT_BMI = 1;

    public static final double WEIGHT_GENDER = 1;

    public static final double WEIGHT_HEIGHT = 1;

    public static final double WEIGHT_WEIGHT = 1;

    public static final double WEIGHT_GOAL = 1;

    public static final double WEIGHT_EQUIPMENT = 3;

    public static final double WEIGHT_EXERCISE_TYPE = 3;

    public static final double WEIGHT_IS_EXPLOSIVE = 2;

    public static final double WEIGHT_MOVEMENT_TYPE = 2;

    public static final double WEIGHT_PRIMARY_MUSCLE = 2;

    public static final double WEIGHT_SECONDARY_MUSCLE = 2;

    public static final double WEIGHT_EQUIPMENT_PLAN = 1;

    public static final double USER_SIMILARITY_THRESHOLD = 0.3;

    public static final double EXERCISE_LIST_SIMILARITY_THRESHOLD = 0.2;

    public static final int MAX_EQUIPMENT_DIFFERENCE = 5;

    public static final double WEIGHT_PERSON = 0.5;

    public static HashMap<Pair<LimitationEnum, LimitationEnum>, Double> LIMITATION_SIMILARITIES;

    public static HashMap<Pair<GoalEnum, GoalEnum>, Double> GOAL_SIMILARITIES;

    public static List<EquipmentEnum> equipmentsOfSportStudios = Arrays.asList(EquipmentEnum.Hantel, EquipmentEnum.Hantelbank
            , EquipmentEnum.KETTELBELL, EquipmentEnum.Klimmzugstange, EquipmentEnum.Langhantel);

    public static final AmalgamationConfig PERSON_AMALGAMATION_TYPE = AmalgamationConfig.WEIGHTED_SUM;

    static {
        LIMITATION_SIMILARITIES = new HashMap<>();
        LIMITATION_SIMILARITIES.put(new Pair<>(LimitationEnum.WRISTS, LimitationEnum.ELBOWS), 0.5);
        LIMITATION_SIMILARITIES.put(new Pair<>(LimitationEnum.ELBOWS, LimitationEnum.WRISTS ), 0.5);
        LIMITATION_SIMILARITIES.put(new Pair<>(LimitationEnum.ELBOWS, LimitationEnum.SHOULDER), 0.5);
        LIMITATION_SIMILARITIES.put(new Pair<>(LimitationEnum.SHOULDER, LimitationEnum.ELBOWS ), 0.5);
        LIMITATION_SIMILARITIES.put(new Pair<>(LimitationEnum.HIPS, LimitationEnum.KNEES), 0.3);
        LIMITATION_SIMILARITIES.put(new Pair<>(LimitationEnum.KNEES, LimitationEnum.HIPS), 0.3);
        //Adding the diagonal of the similarity matrix
        for (LimitationEnum e : LimitationEnum.values()) {
            LIMITATION_SIMILARITIES.put(new Pair<>(e, e), 1.0);
        }
        GOAL_SIMILARITIES = new HashMap<>();
        GOAL_SIMILARITIES.put(new Pair<>(GoalEnum.MAX_STRENGTH, GoalEnum.MUSCLE_MASS), 0.6);
        GOAL_SIMILARITIES.put(new Pair<>(GoalEnum.MUSCLE_MASS, GoalEnum.MAX_STRENGTH), 0.6);
        GOAL_SIMILARITIES.put(new Pair<>(GoalEnum.MAX_STRENGTH, GoalEnum.STRENGTH_ENDURANCE), 0.6);
        GOAL_SIMILARITIES.put(new Pair<>(GoalEnum.STRENGTH_ENDURANCE, GoalEnum.MAX_STRENGTH), 0.6);
        GOAL_SIMILARITIES.put(new Pair<>(GoalEnum.MUSCLE_MASS, GoalEnum.STRENGTH_ENDURANCE), 0.6);
        GOAL_SIMILARITIES.put(new Pair<>(GoalEnum.STRENGTH_ENDURANCE, GoalEnum.MUSCLE_MASS), 0.6);
        for (GoalEnum g : GoalEnum.values()) {
            GOAL_SIMILARITIES.put(new Pair<>(g,g), 1.0);
        }
    }

    public static double getMuscleGroupSimilarityMultiplier(MuscleGroupEnum request, MuscleGroupEnum query){
        if (request == query) {
            return MATCHING_MUSCLE_GROUPS;
        } else if (request == MuscleGroupEnum.LEGS || request == MuscleGroupEnum.ABS
                || query == MuscleGroupEnum.LEGS || query == MuscleGroupEnum.ABS
                || request == MuscleGroupEnum.BACK || query == MuscleGroupEnum.BACK
                || request == MuscleGroupEnum.WHOLE || query == MuscleGroupEnum.WHOLE
                || request == MuscleGroupEnum.UPPER_BODY || query == MuscleGroupEnum.UPPER_BODY
                || request == MuscleGroupEnum.LOWER_BODY || query == MuscleGroupEnum.LOWER_BODY) {
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
        System.out.println(">>>> Duration SIM: " + sim);
        return  sim;
    }

    public static double getCompleteWeightSumPerson() {
        return getCompleteWeightSumMyCBRPerson() + WEIGHT_LIMITATION;
    }
    public static double getCompleteWeightSumMyCBRPerson() {
        return WEIGHT_AGE + WEIGHT_WEIGHT + WEIGHT_BMI + WEIGHT_GENDER + WEIGHT_HEIGHT + WEIGHT_TRAINING_TYPE;
    }
    public static double getCompleteWeightSumPlan() {
        return WEIGHT_GOAL + WEIGHT_EQUIPMENT_PLAN + WEIGHT_DURATION;
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

    public static double LimitationSimilarityRelativeToLimitationCount(List<LimitationEnum> logged
            , List<LimitationEnum> queried){
        double result = Math.max(logged.size(), queried.size()); //setting result to max length
        List<LimitationEnum> most = logged.size() >= queried.size() ? logged : queried;
        List<LimitationEnum> least = logged.size() < queried.size() ? logged : queried;
        for (LimitationEnum e : most) {
            if (!least.contains(e)) {
                result -= 1;
            }
        }
        System.out.println(">>>> LIMITATION SIM : " + result/Math.max(logged.size(), queried.size()));
        return result/Math.max(logged.size(), queried.size());
    }

    public static double getEquipmentSimilarity(List<EquipmentEnum> logged, List<EquipmentEnum> queried) {
        double result = MAX_EQUIPMENT_DIFFERENCE; //Assume Sim of 1 if each of the Equipments
        List<EquipmentEnum> unsupported = getListOfUnsupportedEquipments(logged, queried);
        System.out.println(">>>> SIZE OF UNSUPPORTED: " + unsupported.size());
        //Return similarity based on number of equipments required of a plan but not owned by a user
        return  unsupported.size() >= result ? 0 : (result - unsupported.size())/MAX_EQUIPMENT_DIFFERENCE;
    }

    public static double getGoalSimilarity(GoalEnum target, GoalEnum query) {
        double goalSim;
        Double goalSimTemp = GOAL_SIMILARITIES.get(new Pair<>(target, query));
        goalSim = goalSimTemp!= null ? goalSimTemp : 0 ;

        return goalSim;
    }

    public static List<EquipmentEnum> getListOfUnsupportedEquipments(List<EquipmentEnum> logged
            , List<EquipmentEnum> queried) {
        List<EquipmentEnum> unsupportedEquipments = new ArrayList<>();
        boolean loggedContainsSportStudio = logged.contains(EquipmentEnum.FitnessStudio);
        for (EquipmentEnum e : queried) {
            if (!logged.contains(e) && !(loggedContainsSportStudio && equipmentsOfSportStudios.contains(e))) {
                unsupportedEquipments.add(e);
            }
        }
        return  unsupportedEquipments;
    }
}
