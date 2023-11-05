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
public class DegreeProgramTest {

    public DegreeProgramTest() {
    }

    /**
     * Test of getSubModules method, of class DegreeProgram.
     * 
     * @throws Exception
     */
    @Test
    public void testGetSubModules() throws Exception {
        // This basically also ensures that the datastructure population has started
        // since it has retrieved it's on and only straight subModule
        System.out.println("getSubModules");
        DegreeProgram instance = new DegreeProgram("Anestesiologian ja tehohoidonerikoislääkärikoulutus",
                "otm-6ab4ce4a-4eb7-4c76-8ed7-9ab3a2faa5b6", new StoredInfo("John Smith", "12345", 0, 0), false);
        assertTrue(instance.getSubModules().size() == 1);

    }

    /**
     * Test of getSubModules method, of class DegreeProgram.
     * 
     * @throws Exception
     */
    @Test
    public void testGetName() throws Exception {
        System.out.println("getName");
        DegreeProgram instance = new DegreeProgram("Anestesiologian ja tehohoidonerikoislääkärikoulutus",
                "otm-6ab4ce4a-4eb7-4c76-8ed7-9ab3a2faa5b6", new StoredInfo("John Smith", "12345", 0, 0), false);
        assertEquals("Specialty Training in Anaesthesiology and Intensive Care Medicine (55/2020)", instance.getName());

    }

    /**
     * Test of getId method, of class DegreeProgram.
     * 
     * @throws Exception
     */
    @Test
    public void testGetId() throws Exception {
        System.out.println("getId");
        DegreeProgram instance = new DegreeProgram("Anestesiologian ja tehohoidonerikoislääkärikoulutus",
                "otm-6ab4ce4a-4eb7-4c76-8ed7-9ab3a2faa5b6", new StoredInfo("John Smith", "12345", 0, 0), false);
        assertEquals("otm-6ab4ce4a-4eb7-4c76-8ed7-9ab3a2faa5b6", instance.getId());

    }

    /**
     * Test of getGroupId method, of class DegreeProgram.
     * 
     * @throws Exception
     */
    @Test
    public void testGetGroupId() throws Exception {
        System.out.println("getGroupId");
        DegreeProgram instance = new DegreeProgram("Anestesiologian ja tehohoidonerikoislääkärikoulutus",
                "otm-6ab4ce4a-4eb7-4c76-8ed7-9ab3a2faa5b6", new StoredInfo("John Smith", "12345", 0, 0), false);
        assertEquals("otm-6ab4ce4a-4eb7-4c76-8ed7-9ab3a2faa5b6",
                instance.getGroupId());

    }

    /**
     * Test of getCredits method, of class DegreeProgram.
     * 
     * @throws Exception
     */
    @Test
    public void testGetCredits() throws Exception {
        System.out.println("GetCredits");
        DegreeProgram instance = new DegreeProgram("Anestesiologian ja tehohoidonerikoislääkärikoulutus",
                "otm-6ab4ce4a-4eb7-4c76-8ed7-9ab3a2faa5b6", new StoredInfo("John Smith", "12345", 0, 0), false);
        assertEquals(0, instance.getMinCredits());

    }

}
