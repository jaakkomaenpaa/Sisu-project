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
public class ProgramsTest {

    public ProgramsTest() {
    }

    /**
     * Test of getAllPrograms method, of class Programs.
     */
    @Test
    public void testGetAllPrograms() {
        System.out.println("getAllPrograms");

        Programs test = new Programs();
        assertTrue(test.getAllPrograms().size() == 273);
    }

}
