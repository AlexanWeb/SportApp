package com.example.cbr__fitness.databasehelper;

import android.provider.BaseColumns;

/**
 * This class stores the SQL commands for the SQLITE DB used for the local storage of the app
 */
public class FitnessDBContract {
    //to prevent instantiation
    private FitnessDBContract(){}

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_UID = "uid";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_AGE ="age";
        public static final String COlUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_TRAINING_TYPE = "training_type";
        public static final String COLUMN_NAME_IS_ACTIVE = "is_active";
    }

    public static final String SQL_CREATE_USER = "CREATE TABLE " + UserEntry.TABLE_NAME
            + " (" + UserEntry.COLUMN_NAME_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            UserEntry.COLUMN_NAME_NAME + " TEXT NOT NULL DEFAULT 'Nameless'," +
            UserEntry.COLUMN_NAME_PASSWORD + " TEXT NOT NULL DEFAULT 'PWLess'," +
            UserEntry.COLUMN_NAME_AGE + " INTEGER NOT NULL DEFAULT(20)," +
            UserEntry.COlUMN_NAME_WEIGHT + " REAL NOT NULL DEFAULT(75)," +
            UserEntry.COLUMN_NAME_GENDER + " INTEGER NOT NULL DEFAULT(0)," +    //0=male 1=female
            UserEntry.COLUMN_NAME_TRAINING_TYPE + " INTEGER NOT NULL DEFAULT(1)," + //0=amateur 1=intermediate 2=professional
            UserEntry.COLUMN_NAME_IS_ACTIVE + " INTEGER NOT NULL DEFAULT(0)" +
            ")";

    public static final String SQL_DELETE_USER = "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;

    public static class ExerciseEntry implements BaseColumns {
        public static final String TABLE_NAME = "exercise";
        public static final String COLUMN_NAME_EID = "eid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_PRIME_MUSCLE = "prime_muscle";
        public static final String COLUMN_NAME_SECONDARY_MUSCLE = "secondary_muscle";
        public static final String COLUMN_NAME_EXPLANATION = "explanation";
        public static final String COLUMN_NAME_ILLUSTRATION_LINK = "illustration_link";
        public static final String COLUMN_NAME_EQUIPMENT = "equipment";
        public static final String COLUMN_NAME_DURATION_REP = "duration_rep";
    }

    public static final String SQL_CREATE_EXERCISE = "CREATE TABLE " + ExerciseEntry.TABLE_NAME
            + " (" + ExerciseEntry.COLUMN_NAME_EID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ExerciseEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL DEFAULT 'NamelessExercise'," +
            ExerciseEntry.COLUMN_NAME_PRIME_MUSCLE + " INTEGER NOT NULL DEFAULT (0)," +
            ExerciseEntry.COLUMN_NAME_SECONDARY_MUSCLE + " INTEGER NOT NULL DEFAULT (0)," +
            ExerciseEntry.COLUMN_NAME_EXPLANATION + " TEXT," +
            ExerciseEntry.COLUMN_NAME_ILLUSTRATION_LINK + " TEXT," +
            ExerciseEntry.COLUMN_NAME_EQUIPMENT + " INTEGER NOT NULL DEFAULT (0)," +
            ExerciseEntry.COLUMN_NAME_DURATION_REP + " STRING NOT NULL DEFAULT '0:05'" + //as time in mm:ss
            ")";

    public static final String SQL_DELETE_EXERCISE = "DROP TABLE IF EXISTS " + ExerciseEntry.TABLE_NAME;

    public static class PlanExerciseRelationEntry implements BaseColumns {
        public static final String TABLE_NAME = "plan_exercise_rel";
        public static final String COLUMN_NAME_PID = "pid";
        public static final String COLUMN_NAME_EID = "eid";
        public static final String COLUMN_NAME_SETS = "sets";
        public static final String COLUMN_NAME_REPS = "reps";
        public static final String COLUMN_NAME_BREAK = "break";
        public static final String COLUMN_NAME_WEIGHT = "weight";
    }

    public static final String SQL_CREATE_PLAN_EXERCISE_RELATION = "CREATE TABLE " + PlanExerciseRelationEntry.TABLE_NAME
            + " (" +PlanExerciseRelationEntry.COLUMN_NAME_PID + " INTEGER NOT NULL," +
            PlanExerciseRelationEntry.COLUMN_NAME_EID + " INTEGER NOT NULL," +
            PlanExerciseRelationEntry.COLUMN_NAME_SETS + " INTEGER NOT NULL DEFAULT (3)," +
            PlanExerciseRelationEntry.COLUMN_NAME_REPS + " INTEGER NOT NULL DEFAULT (10)," + //TODO maybe STRING for body workout.
            PlanExerciseRelationEntry.COLUMN_NAME_BREAK + " INTEGER NOT NULL DEFAULT (30)," + //in seconds
            PlanExerciseRelationEntry.COLUMN_NAME_WEIGHT + " STRING NOT NULL DEFAULT 'body'," + //String to allow for different weight types
            "FOREIGN KEY(" + PlanExerciseRelationEntry.COLUMN_NAME_PID + ") REFERENCES " + PlanEntry.TABLE_NAME + "(" + PlanEntry.COLUMN_NAME_PID + "), " +
            "FOREIGN KEY(" + PlanExerciseRelationEntry.COLUMN_NAME_EID + ") REFERENCES " + ExerciseEntry.TABLE_NAME + "(" + ExerciseEntry.COLUMN_NAME_EID + "), " +
            "PRIMARY KEY (" + PlanExerciseRelationEntry.COLUMN_NAME_PID + ", " + PlanExerciseRelationEntry.COLUMN_NAME_EID + ")" +
            ")";

    public static final String SQL_DELETE_PLAN_EXERCISE_RELATION = "DROP TABLE IF EXISTS " + PlanExerciseRelationEntry.TABLE_NAME;

    public static class PlanEntry implements BaseColumns {
        public static final String TABLE_NAME = "plan";
        public static final String COLUMN_NAME_PID = "pid";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_GOAL = "goal";
        public static final String COLUMN_NAME_SUB_GOAL = "sub_goal";
        public static final String COLUMN_NAME_RATING = "rating";
    }

    public static final String SQL_CREATE_PLAN = "CREATE TABLE " + PlanEntry.TABLE_NAME
            + " (" + PlanEntry.COLUMN_NAME_PID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PlanEntry.COLUMN_NAME_NAME + " TEXT NOT NULL DEFAULT 'NamelessPlan'," +
            PlanEntry.COLUMN_NAME_GOAL + " INTEGER NOT NULL DEFAULT(0)," + //TODO: List of goals as a table to translate int to goals
            PlanEntry.COLUMN_NAME_SUB_GOAL + " INTEGER NOT NULL DEFAULT(0)," +
            PlanEntry.COLUMN_NAME_RATING + " INTEGER NOT NULL DEFAULT(3)" +
            ")";

    public static final String SQL_DELETE_PLAN = "DROP TABLE IF EXISTS " + PlanEntry.TABLE_NAME;

    public static class UserPlanRelation implements BaseColumns {
        public static final String TABLE_NAME = "user_plan_rel";
        public static final String COLUMN_NAME_UID = "uid";
        public static final String COLUMN_NAME_PID = "pid";
    }

    public static final String SQL_CREATE_USER_PLAN_RELATION = "CREATE TABLE " + UserPlanRelation.TABLE_NAME
            + " (" +
            UserPlanRelation.COLUMN_NAME_UID + " INTEGER NOT NULL," +
            UserPlanRelation.COLUMN_NAME_PID + " INTEGER NOT NULL," +
            "FOREIGN KEY(" + UserPlanRelation.COLUMN_NAME_UID + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry.COLUMN_NAME_UID + "), " +
            "FOREIGN KEY(" + UserPlanRelation.COLUMN_NAME_PID + ") REFERENCES " + PlanEntry.TABLE_NAME + "(" + PlanEntry.COLUMN_NAME_PID + "), " +
            "PRIMARY KEY (" + UserPlanRelation.COLUMN_NAME_PID + ", " + UserPlanRelation.COLUMN_NAME_UID + ")" +
            ")";

    public static final String SQL_DELETE_USER_PLAN_RELATION = "DROP TABLE IF EXISTS " + UserPlanRelation.TABLE_NAME;

    public static class LimitationsEntry implements BaseColumns {
        public static final String TABLE_NAME = "limitations";
        public static final String COLUMN_NAME_LID = "lid";
        public static final String COLUMN_NAME_LIMITATION = "limitation";
    }

    public static final String SQL_CREATE_LIMITATIONS = "CREATE TABLE " + LimitationsEntry.TABLE_NAME
            + " (" +
            LimitationsEntry.COLUMN_NAME_LID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            LimitationsEntry.COLUMN_NAME_LIMITATION + " INTEGER NOT NULL DEFAULT(0)" +
            ")";

    public static final String SQL_DELETE_LIMITATIONS = "DROP TABLE IF EXISTS " + LimitationsEntry.TABLE_NAME;

    public static class LimitationsUserRelation implements BaseColumns {
        public static final String TABLE_NAME = "limitations_user_rel";
        public static final String COLUMN_NAME_LID = "lid";
        public static final String COLUMN_NAME_UID= "uid";
    }

    public static final String SQL_CREATE_LIMITATION_USER_RELATION = "CREATE TABLE " + LimitationsUserRelation.TABLE_NAME
            + " (" +
            LimitationsUserRelation.COLUMN_NAME_UID + " INTEGER NOT NULL," +
            LimitationsUserRelation.COLUMN_NAME_LID + " INTEGER NOT NULL," +
            "FOREIGN KEY(" + LimitationsUserRelation.COLUMN_NAME_UID + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry.COLUMN_NAME_UID + "), " +
            "FOREIGN KEY(" + LimitationsUserRelation.COLUMN_NAME_LID + ") REFERENCES " + LimitationsEntry.TABLE_NAME + "(" + LimitationsEntry.COLUMN_NAME_LID + "), " +
            "PRIMARY KEY (" + LimitationsUserRelation.COLUMN_NAME_LID + ", " + LimitationsUserRelation.COLUMN_NAME_UID + ")" +
            ")";

    public static final String SQL_DELETE_LIMITATION_USER_RELATION = "DROP TABLE IF EXISTS " + LimitationsUserRelation.TABLE_NAME;
}
