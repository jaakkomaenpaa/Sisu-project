/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3;

import fi.tuni.prog3.sisu.*;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Aleksi
 */
public class ApiServicesTest {

    public ApiServicesTest() {
    }

    /**
     * Test of getModule method, of class ApiServices.
     */
    @Test
    public void testGetModule() {
        System.out.println("getModule");
        // comp science basics

        try {
            String moduleId = "uta-ok-ykoodi-41176";
            JsonObject result = ApiServices.getModule(moduleId);
            JsonObject expRes = iAPI.getJsonObjectFromApi(
                    "https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=uta-ok-ykoodi-41176&universityId=tuni-university-root-id");

            JSONAssert.assertEquals(expRes.toString(), result.toString(), JSONCompareMode.NON_EXTENSIBLE);
        } catch (Exception e) {
            fail(null, e);
        }

    }

    /**
     * Test of getModule method with invalid call, of class ApiServices.
     */
    @Test
    public void testGetModuleInvalid() {
        System.out.println("getModule invalid");
        // comp science basics

        var res = ApiServices.getModule("abcdefghijklmn");

        assertEquals(res, null);

    }

    /**
     * Test of getCourse method, of class ApiServices.
     */
    @Test
    public void testGetCourse() {
        System.out.println("getCourse");

        try {
            String courseId = "otm-30f7d600-0f92-45af-b339-c235e030c5f0";
            JsonObject result = ApiServices.getCourse(courseId);
            JsonObject expRes = iAPI.getJsonObjectFromApi(
                    "https://sis-tuni.funidata.fi/kori/api/course-units/otm-30f7d600-0f92-45af-b339-c235e030c5f0");

            JSONAssert.assertEquals(expRes.toString(), result.toString(), JSONCompareMode.NON_EXTENSIBLE);
        } catch (Exception e) {
            fail(null, e);
        }
    }

    /**
     * Test of getCourse method, of class ApiServices.
     */
    @Test
    public void testGetCourseInvalid() {
        System.out.println("getCourse invalid");

        JsonObject result = ApiServices.getCourse("abcdefgsjkjl");
        assertEquals(result, null);

    }

    /**
     * Test of getDegreeProgram method, of class ApiServices.
     */
    @Test
    public void testGetDegreeProgram() {
        System.out.println("getDegreeProgram");

        try {
            String degreeId = "otm-bc4f8415-e10e-48b1-b7ec-60ca52528e18";
            JsonObject result = ApiServices.getDegreeProgram(degreeId);
            JsonObject expRes = iAPI.getJsonObjectFromApi(
                    "https://sis-tuni.funidata.fi/kori/api/modules/otm-bc4f8415-e10e-48b1-b7ec-60ca52528e18");

            JSONAssert.assertEquals(expRes.toString(), result.toString(), JSONCompareMode.NON_EXTENSIBLE);
        } catch (Exception e) {
            fail(null, e);
        }
    }

    /**
     * Test of getDegreeProgram method, of class ApiServices.
     */
    @Test
    public void testGetDegreeProgramInvalid() {
        System.out.println("getDegreeProgram invalid");

        JsonObject result = ApiServices.getDegreeProgram("abcdefgsjkjl");
        assertEquals(result, null);
    }

    /**
     * Test of getAllProgramsAsList method, of class ApiServices.
     */
    @Test
    public void testApiServicesProgramsHandling() {
        System.out.println("Programs-related methods");

        Programs test = new Programs();
        assertTrue(test.getAllPrograms().size() > 1);
    }

    /**
     * Test of restricted constructor call, of class ApiServices.
     */
    @Test
    public void testApiServicesConstructor() {
        System.out.println("Constructor init prevention");

        Exception exception = assertThrows(Exception.class, () -> {
            new ApiServices();
        });
        String exp = "Service class is not to be initialized.";
        String actual = exception.getMessage();
        assertTrue(actual.contains(exp));
    }

}
