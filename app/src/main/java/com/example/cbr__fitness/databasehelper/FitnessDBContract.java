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
        public static final String COlUMN_NAME_HEIGHT = "height";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_TRAINING_TYPE = "training_type";
        public static final String COLUMN_NAME_IS_ACTIVE = "is_active";
    }

    public static final String SQL_CREATE_USER = "CREATE TABLE " + UserEntry.TABLE_NAME
            + " (" + UserEntry.COLUMN_NAME_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            UserEntry.COLUMN_NAME_NAME + " TEXT NOT NULL DEFAULT 'Nameless'," +
            UserEntry.COLUMN_NAME_PASSWORD + " TEXT NOT NULL DEFAULT 'PWLess'," +
            UserEntry.COLUMN_NAME_AGE + " INTEGER NOT NULL DEFAULT(20)," +
            UserEntry.COlUMN_NAME_WEIGHT + " REAL NOT NULL DEFAULT(75)," +  //weight and height is used to recreate BMI on user creation
            UserEntry.COlUMN_NAME_HEIGHT + " REAL NOT NULL DEFAULT(175)," +
            UserEntry.COLUMN_NAME_GENDER + " INTEGER NOT NULL DEFAULT(1)," +    //0=male 1=female 2=diverse
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
        public static final String COLUMN_NAME_EXERCISE_TYPE ="exercise_type";
        public static final String COLUMN_NAME_ILLUSTRATION_LINK = "illustration_link";
        public static final String COLUMN_NAME_EQUIPMENT = "equipment";
        public static final String COLUMN_NAME_DURATION_REP = "duration_rep";
        public static final String COLUMN_NAME_MOVEMENT_TYPE = "movement_type";
        public static final String COLUMN_NAME_IS_EXPLOSIVE = "is_explosive";
    }

    public static final String SQL_CREATE_EXERCISE = "CREATE TABLE " + ExerciseEntry.TABLE_NAME
            + " (" + ExerciseEntry.COLUMN_NAME_EID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ExerciseEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL DEFAULT 'NamelessExercise'," +
            ExerciseEntry.COLUMN_NAME_PRIME_MUSCLE + " INTEGER NOT NULL DEFAULT (1)," +
            ExerciseEntry.COLUMN_NAME_SECONDARY_MUSCLE + " INTEGER NOT NULL DEFAULT (1)," +
            ExerciseEntry.COLUMN_NAME_EXERCISE_TYPE + " INTEGER NOT NULL DEFAULT (1)," +
            ExerciseEntry.COLUMN_NAME_EXPLANATION + " TEXT," +
            ExerciseEntry.COLUMN_NAME_ILLUSTRATION_LINK + " TEXT," +
            ExerciseEntry.COLUMN_NAME_EQUIPMENT + " INTEGER NOT NULL DEFAULT (1)," +
            ExerciseEntry.COLUMN_NAME_DURATION_REP + " INTEGER NOT NULL DEFAULT (5)," +//as time in mm:ss
            ExerciseEntry.COLUMN_NAME_MOVEMENT_TYPE + " INTEGER NOT NULL DEFAULT (1)," +
            ExerciseEntry.COLUMN_NAME_IS_EXPLOSIVE + " INTEGER NOT NULL DEFAULT (0)," +
            "FOREIGN KEY(" + ExerciseEntry.COLUMN_NAME_EQUIPMENT + ") REFERENCES " + EquipmentEntry.TABLE_NAME + "(" + EquipmentEntry.COLUMN_NAME_EQ_ID + ")"+
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
            PlanExerciseRelationEntry.COLUMN_NAME_REPS + " INTEGER NOT NULL DEFAULT (10)," +
            PlanExerciseRelationEntry.COLUMN_NAME_BREAK + " INTEGER NOT NULL DEFAULT (30)," + //in seconds
            PlanExerciseRelationEntry.COLUMN_NAME_WEIGHT + " INTEGER NOT NULL DEFAULT (0)," + //Integer where 0 represents body weight.
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
        public static final String COLUMN_NAME_MUSCLE_GROUP = "muscle_group";
        public static final String COLUMN_NAME_RATING = "rating";
    }

    public static final String SQL_CREATE_PLAN = "CREATE TABLE " + PlanEntry.TABLE_NAME
            + " (" + PlanEntry.COLUMN_NAME_PID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PlanEntry.COLUMN_NAME_NAME + " TEXT NOT NULL DEFAULT 'NamelessPlan'," +
            PlanEntry.COLUMN_NAME_GOAL + " INTEGER NOT NULL DEFAULT(0)," + //TODO: List of goals as a table to translate int to goals
            PlanEntry.COLUMN_NAME_MUSCLE_GROUP + " INTEGER NOT NULL DEFAULT(0)," +
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
            LimitationsEntry.COLUMN_NAME_LIMITATION + " TEXT NOT NULL UNIQUE" +
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

    public static class RollEntry implements BaseColumns {
        public static final String TABLE_NAME ="roll";
        public static final String COLUMN_NAME_RID ="rid";
        public static final String COLUMN_NAME_ROLL ="roll";
    }

    public static final String SQL_CREATE_ROLL = "CREATE TABLE " + RollEntry.TABLE_NAME
            + " (" +
            RollEntry.COLUMN_NAME_RID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            RollEntry.COLUMN_NAME_ROLL + " TEXT NOT NULL UNIQUE" +
            ")";

    public static final String SQL_DELETE_ROLL = "DROP TABLE IF EXISTS " + LimitationsUserRelation.TABLE_NAME;

    public static class RollUserRelation implements BaseColumns {
        public static final String TABLE_NAME = "roll_user_rel";
        public static final String COLUMN_NAME_RID = "rid";
        public static final String COLUMN_NAME_UID= "uid";
    }

    public static final String SQL_CREATE_ROLL_USER_RELATION = "CREATE TABLE " + RollUserRelation.TABLE_NAME
            + " (" +
            RollUserRelation.COLUMN_NAME_UID + " INTEGER NOT NULL," +
            RollUserRelation.COLUMN_NAME_RID + " INTEGER NOT NULL," +
            "FOREIGN KEY(" + RollUserRelation.COLUMN_NAME_UID + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry.COLUMN_NAME_UID + "), " +
            "FOREIGN KEY(" + RollUserRelation.COLUMN_NAME_RID + ") REFERENCES " + RollEntry.TABLE_NAME + "(" + RollEntry.COLUMN_NAME_RID + "), " +
            "PRIMARY KEY (" + RollUserRelation.COLUMN_NAME_RID + ", " + RollUserRelation.COLUMN_NAME_UID + ")" +
            ")";

    public static final String SQL_DELETE_ROLL_USER_RELATION = "DROP TABLE IF EXISTS " + RollUserRelation.TABLE_NAME;

    public static class EquipmentEntry implements BaseColumns {
        public static final String TABLE_NAME = "equipment";
        public static final String COLUMN_NAME_EQ_ID = "eq_id";
        public static final String COLUMN_NAME_LABEL= "label";
    }

    public static final String SQL_CREATE_EQUIPMENTS = "CREATE TABLE " + EquipmentEntry.TABLE_NAME
            + " (" +
            EquipmentEntry.COLUMN_NAME_EQ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            EquipmentEntry.COLUMN_NAME_LABEL + " TEXT NOT NULL UNIQUE" +
            ")";

    public static final String SQL_DELETE_EQUIPMENT = "DROP TABLE IF EXISTS " + EquipmentEntry.TABLE_NAME;


    public static class UserEquipmentRelation implements BaseColumns {
        public static final String TABLE_NAME = "user_equipment_rel";
        public static final String COLUMN_NAME_UID = "uid";
        public static final String COLUMN_NAME_EQ_ID= "eq_id";
    }

    public static final String SQL_CREATE_USER_EQUIPMENT_RELATION = "CREATE TABLE " + UserEquipmentRelation.TABLE_NAME
            + " (" +
            UserEquipmentRelation.COLUMN_NAME_UID + " INTEGER NOT NULL," +
            UserEquipmentRelation.COLUMN_NAME_EQ_ID + " INTEGER NOT NULL," +
            "FOREIGN KEY(" + UserEquipmentRelation.COLUMN_NAME_UID + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry.COLUMN_NAME_UID + "), " +
            "FOREIGN KEY(" + UserEquipmentRelation.COLUMN_NAME_EQ_ID + ") REFERENCES " + EquipmentEntry.TABLE_NAME + "(" + EquipmentEntry.COLUMN_NAME_EQ_ID + "), " +
            "PRIMARY KEY (" + UserEquipmentRelation.COLUMN_NAME_EQ_ID + ", " + UserEquipmentRelation.COLUMN_NAME_UID + ")" +
            ")";

    public static final String SQL_DELETE_USER_EQUIPMENT_RELATION ="DROP TABLE IF EXISTS " + UserEquipmentRelation.TABLE_NAME;


    //Use only ID 1 to 9 where 1 is none which everyone has
    public static final String SQL_INSERT_USER_EQUIPMENT_RELATION  = "INSERT INTO "
            + UserEquipmentRelation.TABLE_NAME + " " +
            "(" + UserEquipmentRelation.COLUMN_NAME_UID + "," + UserEquipmentRelation.COLUMN_NAME_EQ_ID + ") " +
            "VALUES (1,1), (1,2), (1,3), (1,9)" +
            ",(2,1), (2,9)" +
            ",(5,1), (5,4),  (5,9)" +
            ",(3,1), (3,5), (3,6), (3,8)" +
            ",(4,1), (4,2), (4,7), (4,6)" +
            ";";

    public static final String SQL_INSERT_EQUIPMENT = "INSERT INTO "
            + EquipmentEntry.TABLE_NAME + " (" + EquipmentEntry.COLUMN_NAME_LABEL + ") " +
            "VALUES ('Keins'), ('Fitness Studio Mitgliedschaft'), ('Hanteln'), ('Theraband'), ('Dumbells'), ('Klimmzugstange')" +
            ",('Hantelbank'), ('Langhantel'), ('DipBars');";

    public static final String SQL_INSERT_LIMITATIONS= "INSERT INTO "
            + LimitationsEntry.TABLE_NAME + " (" + LimitationsEntry.COLUMN_NAME_LIMITATION + ") " +
            "VALUES ('Elbows'), ('Wrists'), ('Shoulder'), ('Knees'), ('Spine');";

    public static final String SQL_INSERT_ROLLS ="INSERT INTO "
            + RollEntry.TABLE_NAME + " (" + RollEntry.COLUMN_NAME_ROLL + ") " +
            "VALUES ('User'), ('Expert'), ('Admin');";

    public static final String SQL_INSERT_TEST_USER = "INSERT INTO "
            + UserEntry.TABLE_NAME + "(" + UserEntry.COLUMN_NAME_NAME
            +"," + UserEntry.COLUMN_NAME_PASSWORD
            +"," + UserEntry.COLUMN_NAME_AGE
            +"," + UserEntry.COLUMN_NAME_GENDER
            +"," + UserEntry.COLUMN_NAME_TRAINING_TYPE
            +"," + UserEntry.COlUMN_NAME_WEIGHT
            +"," + UserEntry.COlUMN_NAME_HEIGHT
            + ") VALUES ('Alex', 'Alex', 25, 1, 1, 72, 180)" +
            ",('Tom', 'Tom', 23, 1, 1, 90, 185)" +
            ",('Jenny', 'Jenny', 26, 2, 2, 65, 165)" +
            ",('Jeremy', 'Jeremy', 35, 1, 3, 100, 190)" +
            ",('Markus', 'Markus', 56, 1, 1, 85, 173)" +
            ";";

    public static final String SQL_INSERT_USER_ROLL = "INSERT INTO "
            + RollUserRelation.TABLE_NAME + "(" + RollUserRelation.COLUMN_NAME_UID + ", " + RollUserRelation.COLUMN_NAME_RID +")"
            + "VALUES (1, 1)" +
            ",(1, 2)" +
            ", (1, 3)" +
            ", (2, 1)" +
            ", (3, 1)" +
            ", (4, 1)" +
            ", (5, 1)" +
            ";";

    public static final String SQL_INSERT_USER_LIMITATION_REL = "INSERT INTO "
            + LimitationsUserRelation.TABLE_NAME + "(" + LimitationsUserRelation.COLUMN_NAME_UID
            + ", " + LimitationsUserRelation.COLUMN_NAME_LID + ") " +
            "VALUES (1, 3),(4, 2), (4, 4), (5, 5), (5, 4);";

    public static final String SQL_INSERT_EXERCISES = "INSERT INTO "
            + ExerciseEntry.TABLE_NAME + "(" + ExerciseEntry.COLUMN_NAME_TITLE
            +"," + ExerciseEntry.COLUMN_NAME_DURATION_REP
            +"," + ExerciseEntry.COLUMN_NAME_EXERCISE_TYPE
            +"," + ExerciseEntry.COLUMN_NAME_EQUIPMENT
            +"," + ExerciseEntry.COLUMN_NAME_EXPLANATION
            +"," + ExerciseEntry.COLUMN_NAME_PRIME_MUSCLE
            +"," + ExerciseEntry.COLUMN_NAME_SECONDARY_MUSCLE
            +"," + ExerciseEntry.COLUMN_NAME_ILLUSTRATION_LINK
            +"," + ExerciseEntry.COLUMN_NAME_MOVEMENT_TYPE
            +"," + ExerciseEntry.COLUMN_NAME_IS_EXPLOSIVE
            + ") VALUES ('Push-Up', 5, 2, 1, 'Begeben sie sich in eine liegende Position mit dem " +
            "Gesicht nach unten, platzieren sie ihre Arme schulterbreit unter den Schultern und" +
            " ihre Fuesse huefstbreit. Spannen sie ihre Bauchmuseln und ihre Glutes an. Druecken sie" +
            "sich mit ihren Armen nach oben und lassen sich dann wieder Runter. Achten sie auf einen" +
            "geraden Ruecken.', 4, 6, 'LINK', 1, 0)," +
            "('Clapping Push-Up', 4, 2, 1, 'Begeben sie sich in eine liegende Position mit dem " +
            "Gesicht nach unten, platzieren sie ihre Arme schulterbreit unter den Schultern und" +
            " ihre Fuesse huefstbreit. Spannen sie ihre Bauchmuseln und ihre Glutes an. Druecken sie" +
            "sich schnell nach oben, so dass sie bevor sie wieder absinken in die Haende klatschen" +
            " koennen. Nach dem klatschen die Arme wieder in die Ausgangspositions um den abstieg " +
            "abzufangen. Achten sie auf einen geraden Ruecken.', 4, 6, 'LINK', 1, 0)" +
            ",('Dip', 5, 2, 9, 'Beidseitig die Stangen greifen, eine gerade Haltung einnehmen, dann" +
            "den Koerper langsam und kontrolliert durch das Biegen der Arme senken und anschliessend" +
            "wieder hochdruecken', 4, 6, 'LINK', 1, 0)" +
            ";";

    public static final String SQL_INSERT_PLAN = "INSERT INTO "
            + PlanEntry.TABLE_NAME + "(" + PlanEntry.COLUMN_NAME_NAME
            +"," + PlanEntry.COLUMN_NAME_GOAL
            +"," + PlanEntry.COLUMN_NAME_MUSCLE_GROUP
            +"," + PlanEntry.COLUMN_NAME_RATING
            + ") VALUES ('PushUpPlan', 2, 2, 5)," +
            "('DipPlan', 2, 3, 5)" +
            ",('UpperBody', 2, 3, 2)" +
            ",('Plan', 2, 2, 2)" +
            ",('PushUps', 2, 2, 4)" +
            ",('Dips', 2, 3, 1)" +
            ";";

    public static final String SQL_INSERT_PLAN_EXERCISE_RELATION = "INSERT INTO "
            + PlanExerciseRelationEntry.TABLE_NAME + "(" + PlanExerciseRelationEntry.COLUMN_NAME_EID
            +"," + PlanExerciseRelationEntry.COLUMN_NAME_PID
            +"," + PlanExerciseRelationEntry.COLUMN_NAME_BREAK
            +"," + PlanExerciseRelationEntry.COLUMN_NAME_REPS
            +"," + PlanExerciseRelationEntry.COLUMN_NAME_SETS
            +"," + PlanExerciseRelationEntry.COLUMN_NAME_WEIGHT
            +") VALUES (1, 1, 60, 10, 3, 0)" +
            ",(2, 1, 60, 12, 2, 0)" +
            ",(3, 1, 90, 10, 4, 0)" +
            ",(3, 2, 180, 15, 4, 0)" +
            ",(3, 3, 80, 12, 3, 0)" +
            ",(2, 3, 80, 10, 4, 0)" +
            ",(1, 4, 60, 12, 3, 0)" +
            ",(2, 4, 40, 10, 4, 0)" +
            ",(1, 5, 60, 15, 3, 0)" +
            ",(3, 6, 40, 20, 4, 0)" +
            ";";

    public static final String SQL_INSERT_PLAN_USER_RELATION = "INSERT INTO "
            + UserPlanRelation.TABLE_NAME + "(" + UserPlanRelation.COLUMN_NAME_PID + "," + UserPlanRelation.COLUMN_NAME_UID
            + ") VALUES (1,1),(2,1)" +
            ",(3,2)" +
            ",(4,3)" +
            ",(5,4)" +
            ",(6,5)" +
            ";";
}
