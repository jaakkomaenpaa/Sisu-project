package fi.tuni.prog3.sisu;

import java.util.ArrayList;

/**
 * Top level structure to be used mainly on the first time opening the
 * application. Holds all the program Objects in array.
 */
public class Programs {
    ArrayList<DegreeProgram> allPrograms;

    /**
     * Calls immideatly the Apiservices's getAllProgramsAsList and initializes the
     * degreeprogram array to match that return value.
     */
    public Programs() {
        this.allPrograms = ApiServices.getAllProgramsAsList();
    }

    /**
     * Returns the allPrograms array.
     * 
     * @return ArrayList<DegreeProgram>
     */
    public ArrayList<DegreeProgram> getAllPrograms() {
        return allPrograms;
    }

}
