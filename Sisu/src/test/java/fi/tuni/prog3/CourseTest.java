/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3;

import fi.tuni.prog3.sisu.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Aleksi
 */
public class CourseTest {

    public CourseTest() {
    }

    /**
     * Test of getContent method, of class Course.
     */
    @Test
    public void testGetContent() {
        System.out.println("getContent");
        Course instance = new Course("Programmin 1", "12345", "12345", 0, "Basics of programming in Python.",
                "Basics of GUIS and datastructures");
        String expResult = "Basics of programming in Python.";
        String result = instance.getContent();
        assertEquals(expResult, result);
    }

    /**
     * Test of getOutcomes method, of class Course.
     */
    @Test
    public void testGetOutcomes() {
        System.out.println("getOutcomes");
        Course instance = new Course("Programmin 1", "12345", "12345", 0, "Basics of programming in Python.",
                "Basics of GUIS and datastructures");
        String expResult = "Basics of GUIS and datastructures";
        String result = instance.getOutcomes();
        assertEquals(expResult, result);
    }

    /**
     * Test of getState method, of class Course.
     */
    @Test
    public void testGetState() {
        System.out.println("getState");
        Course instance = new Course("Programmin 1", "12345", "12345", 0, "Basics of programming in Python.",
                "Basics of GUIS and datastructures");
        String expResult = "DEFAULT";
        String result = instance.getState();
        assertEquals(expResult, result);
    }

    /**
     * Test of setState method, of class Course.
     */
    @Test
    public void testSetState() {
        System.out.println("setState");
        Course instance = new Course("Programmin 1", "12345", "12345", 0, "Basics of programming in Python.",
                "Basics of GUIS and datastructures");
        String stateString = "COMPLETED";
        instance.setState(stateString);
        String result = instance.getState();
        assertEquals(stateString, result);
    }

    /**
     * Test of setState method with invalid state, of class Course.
     */
    @Test
    public void testSetStateInvalid() {
        System.out.println("setState doesn't fail with invalid input");
        Course instance = new Course("Programmin 1", "12345", "12345", 0, "Basics of programming in Python.",
                "Basics of GUIS and datastructures");
        String stateString = "COMPLETE";
        instance.setState(stateString);
        String result = instance.getState();
        assertEquals("DEFAULT", result);
    }

    /**
     * Test of setState method with invalid state, of class Course.
     */
    @Test
    public void testSetStateNumberInput() {
        System.out.println("setState doesn't fail with number input");
        Course instance = new Course("Programmin 1", "12345", "12345", 0, "Basics of programming in Python.",
                "Basics of GUIS and datastructures");
        instance.setState("0");
        String result = instance.getState();
        assertEquals("DEFAULT", result);
    }

}
