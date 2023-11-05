/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3;

import fi.tuni.prog3.sisu.*;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Aleksi
 */
public class StudyModuleTest {

    public StudyModuleTest() {
    }

    /**
     * Test of studyModule method and class as a whole, of class StudyModule.
     */
    @Test
    public void testNoCoursesOnlyModules() {
        System.out.println("build module");
        StudyModule mod = new StudyModule("name", "123444", "otm-9712d341-1c46-439b-a7a0-3233cbbcd68d", 0);
        assertTrue(mod.getCourses().size() == 0);
        assertTrue(mod.getSubModules().size() == 2);
    }

    /**
     * Test of studyModule method and class as a whole, of class StudyModule.
     */
    @Test
    public void testNoModulesOnlyCourses() {
        System.out.println("build module");
        StudyModule mod = new StudyModule("name", "123444", "otm-33ccb038-0a47-4a15-8877-7143e949ac2c", 0);

        assertTrue(mod.getCourses().size() == 8);
        assertTrue(mod.getSubModules().size() == 0);

    }

    /**
     * Test of studyModule method and class as a whole, of class StudyModule.
     */
    @Test
    public void testInvalidModule() {
        System.out.println("build module");
        StudyModule mod = new StudyModule("name", "123444", "12344", 0);

        assertTrue(mod.getCourses().size() == 0);
        assertTrue(mod.getSubModules().size() == 0);

    }

    /**
     * Test of addrecords and getstatefulcourses method, of class StudyModule.
     */
    @Test
    public void testaddRecordsAndGetStatefulCourses() {
        StudyModule mod = new StudyModule("name", "123444", "otm-33ccb038-0a47-4a15-8877-7143e949ac2c", 0);
        JsonObject toStore = new JsonObject();
        toStore.addProperty("id", "otm-9dea0dfa-9475-4f20-8212-9e8c4df718cf");
        toStore.addProperty("state", "COMPLETED");
        JsonArray rec = new JsonArray();
        rec.add(toStore);
        mod.addRecords(rec);
        ArrayList<Course> res = mod.getStatefulCourses(new ArrayList<Course>());
        Course resCourse = res.get(0);
        assertTrue(resCourse.getId().equals("otm-9dea0dfa-9475-4f20-8212-9e8c4df718cf"));

    }

    /**
     * Test of addrecords and getstatefulcourses method, of class StudyModule.
     */
    @Test
    public void testaddRecordsGetStatefulInvalid() {
        StudyModule mod = new StudyModule("name", "123444", "otm-33ccb038-0a47-4a15-8877-7143e949ac2c", 0);
        JsonObject toStore = new JsonObject();
        toStore.addProperty("id", "otm-9dea0dfa-9475-4f20-8212-9e8c4df718cf");
        toStore.addProperty("state", "COMPLETE");
        JsonArray rec = new JsonArray();
        rec.add(toStore);
        mod.addRecords(rec);
        ArrayList<Course> res = mod.getStatefulCourses(new ArrayList<Course>());
        // this makes sure that no courses state was altered, and thus no stateful
        // courses were returned
        assertTrue(res.size() == 0);

    }

}
