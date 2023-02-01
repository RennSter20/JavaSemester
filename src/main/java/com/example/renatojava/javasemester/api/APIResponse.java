package com.example.renatojava.javasemester.api;

public class APIResponse {

    private String date;
    private Integer totalCases;
    private Integer totalDeaths;
    private Integer newCasesDay;
    private String lastUpdatedFullTime;

    public APIResponse(String date, Integer totalCases, Integer totalDeaths, Integer newCasesDay, String lastUpdatedFullTime) {
        this.date = date;
        this.totalCases = totalCases;
        this.totalDeaths = totalDeaths;
        this.newCasesDay = newCasesDay;
        this.lastUpdatedFullTime = lastUpdatedFullTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getTotalCases() {
        return totalCases;
    }

    public void setTotalCases(Integer totalCases) {
        this.totalCases = totalCases;
    }

    public Integer getTotalDeaths() {
        return totalDeaths;
    }

    public void setTotalDeaths(Integer totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public Integer getNewCasesDay() {
        return newCasesDay;
    }

    public void setNewCasesDay(Integer newCasesDay) {
        this.newCasesDay = newCasesDay;
    }

    public String getLastUpdatedFullTime() {
        return lastUpdatedFullTime;
    }

    public void setLastUpdatedFullTime(String lastUpdatedFullTime) {
        this.lastUpdatedFullTime = lastUpdatedFullTime;
    }

    public static class Builder {

        private String date;
        private Integer totalCases;
        private Integer totalDeaths;
        private Integer newCasesDay;
        private String lastUpdatedFullTime;

        public APIResponse build() {
            return new APIResponse(date, totalCases, totalDeaths, newCasesDay, lastUpdatedFullTime);
        }
        public Builder withDate(String date){
            this.date = date;
            return this;
        }
        public Builder withTotalCases(Integer totalCases){
            this.totalCases = totalCases;
            return this;
        }
        public Builder withTotalDeaths(Integer totalDeaths){
            this.totalDeaths = totalDeaths;
            return this;
        }
        public Builder withNewCasesDay(Integer newCasesDay){
            this.newCasesDay = newCasesDay;
            return this;
        }
        public Builder lastUpdatedFullTime(String last){
            this.lastUpdatedFullTime = last;
            return this;
        }
    }
}
