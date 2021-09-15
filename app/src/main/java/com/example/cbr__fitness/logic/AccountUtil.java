package com.example.cbr__fitness.logic;

public class AccountUtil {
    /**
     * As the Database stores gender and workout types as integers due to the lack of enum types in
     * sqlite this class codes and decodes the integers from the database.
     * These variables centralize the variables to ease the editing of these values and reduce
     * errors.
     */
    public static final String male = "Male";
    public static final int maleInt = 0;
    public static final String female = "Female";
    public static final int femaleInt = 1;
    public static final String diverse = "Diverse";
    public static final int diverseInt = 2;

    public static int GenderStringToInt (String genderString) {
        if (genderString.equals(male)) {
            return maleInt;
        } else if (genderString.equals(female)) {
            return femaleInt;
        } else { // returns 2 for "diverse"
            return diverseInt;
        }
    }

    public static String GenderIntToString (int genderInt) {
        if (genderInt == maleInt) {
            return male;
        } else if (genderInt == femaleInt) {
            return female;
        } else {
            return diverse;
        }
    }

    public static final String beginner = "Beginner";
    public static final int beginnerInt = 0;
    public static final String advanced = "Advanced";
    public static final int advancedInt = 1;
    public static final String pro = "Pro";
    public static final int proInt = 2;

    /**
     * Method to convert an integer into the corresponding workout type as a String. It is assumed,
     * that only numbers from 0 to 2 are put in as the training is split into three level.
     * @param workoutInt should be an integer >= 0 and <= 2
     * @return The corresponding String to the input integer describing the workout level
     */
    public static String WorkoutIntToString (int workoutInt) {
        if (workoutInt == beginnerInt) {
            return beginner;
        } else if (workoutInt == advancedInt) {
            return advanced;
        } else { // representing the number 2
            return pro;
        }
    }

    public static int WorkoutStringToInt (String workoutString) {
        if (workoutString.equals(beginner)) {
            return beginnerInt;
        } else if (workoutString.equals(advanced)) {
            return advancedInt;
        } else { // returns 2 for "pro"
            return proInt;
        }
    }

    public static final String bicepsString = "Biceps";
    public static final String tricepsString = "Triceps";
    public static final String shoulderString = "Shoulder";
    public static final String forearmsString = "Forearms";
    public static final String latsString = "Lats";
    public static final String upperBackString = "Upper Back";
    public static final String lowerBackString = "Lower Back";

    public static String PrimeMuscleIntToString (int primeMuscleInt) {
        String primeMuscleString = "";
        switch (primeMuscleInt) {
            case 1:
                primeMuscleString = bicepsString;
                break;
            case 2:
                primeMuscleString = tricepsString;
                break;
            case 3:
                primeMuscleString = forearmsString;
                break;
            case 4:
                primeMuscleString = shoulderString;
                break;
            case 5:
                primeMuscleString = latsString;
                break;
            case 6:
                primeMuscleString = upperBackString;
                break;
            case 7:
                primeMuscleString = lowerBackString;
                break;
        }
        return primeMuscleString;
    }

    public static int primeMuscleStringToInt (String primeMuscleString) {
        int primeMuscleInt = -1;



        return primeMuscleInt;
    }
}
