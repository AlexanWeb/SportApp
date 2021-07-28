package com.example.cbr__fitness.data;

import java.util.ArrayList;

/**
 * @author Jobst-Julius Bartels
 */

// Datenklasse für die Exercise-Liste.
public class ExerciseList extends ArrayList<Exercise> {

    // Default-Konstruktor.
    public ExerciseList() {

    }

    // Methode zur Überprüfung, ob eine Exercise existiert.
    public boolean exExists(String exName) {
        boolean exists = false;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getExName().equalsIgnoreCase(exName)) {
                exists = true;
            }
        }
        return exists;
    }

    // Methode zur Anfrage einer bestimmten Exercise.
    public Exercise getCertainExercise(String name) {
        Exercise exercise = new Exercise();
        for(int i = 0; i < this.size(); i++) {
            if(this.get(i).getExName().equalsIgnoreCase(name)) {
                exercise = this.get(i);
            }
        }
        return exercise;
    }

    // Methode zur Bestimmung der gesamten Zeit eines Plans.
    public int getTotalPlanTime () {
        int time = 0;
        for ( int i = 0; i < this.size(); i++) {
            time+= Integer.valueOf(this.get(i).getExTime());
        }
        return time;
    }

    // Methode die eine bestimmte Exercise entfernt.
    public void removeExercise(String pName) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getExName().matches(pName)) {
                this.remove(this.get(i));
            }
        }
    }

    // Print-Methode der exerciseList.
    public String exListToString() {
        String exListString = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.size(); i++) {
            sb.append("Exercise [exName="+ this.get(i).getExName() + "]");
            sb.append("[exSetNumber=" + this.get(i).getExSetNumber() +"]");
            sb.append("[exRep=" + this.get(i).getExRep() +"]");
            sb.append("[exBreak=" + this.get(i).getExBreak() +"]");
            sb.append("[exWeight=" + this.get(i).getExWeight() +"]");
            sb.append("[exMuscle=" + this.get(i).getExMuscle() +"]");
            sb.append("[exTime=" + this.get(i).getExTime() +"]");
            sb.append("[exType=" + this.get(i).getExType() +"]");
            sb.append("[exRating=" + this.get(i).getExRating() +"]");
            sb.append("\n");
        }
        exListString = sb.toString();
        return exListString;
    }
}
