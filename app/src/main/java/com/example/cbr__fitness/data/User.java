package com.example.cbr__fitness.data;

/**
 * @author Jobst-Julius Bartels
 */

// Datenklasse für den Benutzer (engl. user)
public class User {

    //Membervariablen.
    private String username;
    private String userPassword;
    private String age;
    private String gender;
    private String workType;
    private String duration;
    private String bodyType;
    private String res;
    private PlanList planList;
    private String pathData;

    // Enumerations.
    public enum Gender {
        Male, Female, Diverse
    }
    public enum Worktype {
        Beginner, Advanced, Pro
    }
    public enum Duration {
        VeryShort, Short, Middle, Long, VeryLong
    }
    public enum Age {
        Junior, Teenager, Adult, Senior
    }
    public enum BodyType {
        Ectomorph, Mesomorph, Endomorph
    }
    public enum ResEnum {
        Yes, No
    }
    private Age ageEnum;
    private ResEnum resEnum;
    private Duration DurationEnum;
    private Gender genderEnum;
    private Worktype worktypeEnum;
    private BodyType bodyTypeEnum;

    // Default-Konstruktor.
    public User() {

    }

    //Konstruktor.
    public User(String username, String userPassword, String age, String gender, String worktype, String duration, String res, String pathData, String bodyType) {
        this.username = username;
        this.userPassword = userPassword;
        this.age = age;
        this.gender = gender;
        this.workType = worktype;
        this.duration = duration;
        this.res = res;
        this.pathData = pathData;
        this.bodyType = bodyType;
    }

    // Setter-Methoden.
    public void setUsername(String username) {
        this.username = username;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setPathData(String pathData) {
        this.pathData = pathData;
    }
    public void setWorktype(String workType) {
        this.workType = workType;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public void setRes(String res) {
        this.res = res;
    }
    public void setPlanList(PlanList planList) {
        this.planList = planList;
    }
    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    // Getter-Methoden.
    public String getUsername() {
        return this.username;
    }
    public String getUserPassword() {
        return this.userPassword;
    }
    public String getAge() {
        return this.age;
    }
    public String getGender() {
        return this.gender;
    }
    public String getPathData() {
        return pathData;
    }
    public String getWorktype() {
        return this.workType;
    }
    public String getDuration() {
        return this.duration;
    }
    public String getRes() {
        return this.res;
    }
    public PlanList getPlanList() {
        return planList;
    }
    public Integer getAgeInt() {
        return Integer.parseInt(this.age);
    }
    public Integer getDurationInt() {
        return Integer.parseInt(this.duration);
    }
    public Age getAgeEnum() {
        int foo = Integer.parseInt(this.age);
        if (foo > 11 && foo < 16) {
            this.ageEnum = Age.Junior;
        } else if (foo > 15 && foo < 21) {
            this.ageEnum = Age.Teenager;
        } else if (foo > 20 && foo < 45) {
            this.ageEnum = Age.Adult;
        } else if (foo > 44 && foo < 100) {
            this.ageEnum = Age.Senior;
        }
        return this.ageEnum;
    }
    public Duration getDurationEnum() {
        int foo = Integer.parseInt(this.duration);
        if (foo > 0 && foo <= 15) {
            this.DurationEnum = Duration.VeryShort;
        } else if (foo > 15 && foo <= 45) {
            this.DurationEnum = Duration.Short;
        } else if (foo > 45 && foo <= 75) {
            this.DurationEnum = Duration.Middle;
        } else if (foo > 75 && foo <= 120) {
            this.DurationEnum = Duration.Long;
        } else if (foo > 120) {
            this.DurationEnum = Duration.VeryLong;
        }
        return this.DurationEnum;
    }
    public Gender getGenderEnum() {
        if(this.gender.matches("Male")) {
            this.genderEnum = Gender.Male;
        }  else if(this.gender.matches("Female")) {
            this.genderEnum = Gender.Female;
        } else {
            this.genderEnum = Gender.Diverse;
        }
        return this.genderEnum;
    }
    public Worktype getWorkTypeEnum() {
        if(this.workType.matches("Beginner")) {
            this.worktypeEnum = Worktype.Beginner;
        }  else if(this.workType.matches("Advanced")) {
            this.worktypeEnum = Worktype.Advanced;
        } else {
            this.worktypeEnum = Worktype.Pro;
        }
        return this.worktypeEnum;
    }
    public BodyType getBodyTypeEnum() {
        if(this.bodyType.matches("Ectomorph")) {
            this.bodyTypeEnum = BodyType.Ectomorph;
        }  else if(this.bodyType.matches("Mesomorph")) {
            this.bodyTypeEnum = BodyType.Mesomorph;
        } else {
            this.bodyTypeEnum = BodyType.Endomorph;
        }
        return this.bodyTypeEnum;
    }
    public ResEnum getResEnum() {
        if(this.res.matches("Yes")) {
            this.resEnum = ResEnum.Yes;
        } else {
            this.resEnum = ResEnum.No;
        }
        return this.resEnum;

    }
    public String getBodyType() {
        return bodyType;
    }

    // Print-Methode des Users.
    public String getUserToString(){
        String userString;
        StringBuilder sb = new StringBuilder();
        sb.append("User [username="+ this.username + "]");
        sb.append("[userPassword=" + this.userPassword +"]");
        sb.append("[age=" + this.age +"]");
        sb.append("[gender=" + this.gender +"]");
        sb.append("[workType=" + this.workType +"]");
        sb.append("[bodyType=" + this.bodyType +"]");
        sb.append("[duration=" + this.duration +"]");
        sb.append("[res=" + this.res +"]");
        sb.append("[pPath=" + this.pathData +"]");
        sb.append("\n");
        userString = sb.toString();
        return userString;
    }

    // Print-Methode als Fall (ohne Passwort).
    public String getUserToStringCBR(){
        String userString;
        StringBuilder sb = new StringBuilder();
        sb.append("User [username="+ this.username + "]");
        sb.append("[age=" + this.getAgeEnum() +"]");
        sb.append("[gender=" + this.getGenderEnum() +"]");
        sb.append("[workType=" + this.getWorkTypeEnum() +"]");
        sb.append("[bodyType=" + this.bodyType +"]");
        sb.append("[duration=" + this.getDurationEnum() +"]");
        sb.append("[res=" + this.res +"]");
        sb.append("\n");
        userString = sb.toString();
        return userString;
    }

}
