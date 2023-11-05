/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.sisu;

/**
 * Helper class for storing the information gathered from the user
 * 
 */
public class StoredInfo {
    private String name;
    private String studentNumber;
    private int startYear;
    private int endYear;

    public StoredInfo(String name, String studentNumber, int startYear, int endYear) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.startYear = startYear;
        this.endYear = endYear;

    }

    public String getName() {
        return name;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public String getStudentNumber() {
        return studentNumber;
    }
}
