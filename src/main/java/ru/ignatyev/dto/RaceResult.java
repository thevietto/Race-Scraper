package ru.ignatyev.dto;

public class RaceResult {

    private String raceId;
    private String raceName;
    private String date;
    private String bibNumber;
    private String name;
    private String category;
    private String rank;
    private String categoryPlace;
    private String genderPlace;
    private String swim;
    private String bike;
    private String run;
    private String finish;

    public String getRaceId() {
        return raceId;
    }

    public void setRaceId(String raceId) {
        this.raceId = raceId;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBibNumber() {
        return bibNumber;
    }

    public void setBibNumber(String bibNumber) {
        this.bibNumber = bibNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getCategoryPlace() {
        return categoryPlace;
    }

    public void setCategoryPlace(String categoryPlace) {
        this.categoryPlace = categoryPlace;
    }

    public String getGenderPlace() {
        return genderPlace;
    }

    public void setGenderPlace(String genderPlace) {
        this.genderPlace = genderPlace;
    }

    public String getSwim() {
        return swim;
    }

    public void setSwim(String swim) {
        this.swim = swim;
    }

    public String getBike() {
        return bike;
    }

    public void setBike(String bike) {
        this.bike = bike;
    }

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    @Override
    public String toString() {
        return raceId + "," +
                getRaceName() +
                "," +
                getDate() +
                "," +
                getBibNumber() +
                "," +
                getName() +
                "," +
                getCategory() +
                "," +
                getRank() +
                "," +
                getCategoryPlace() +
                "," +
                getGenderPlace() +
                "," +
                getSwim() +
                "," +
                getBike() +
                "," +
                getRun() +
                "," +
                getFinish();
    }
}
