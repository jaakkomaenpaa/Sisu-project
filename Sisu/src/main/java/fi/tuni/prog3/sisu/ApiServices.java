package fi.tuni.prog3.sisu;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Service class for static use. Provides all the necessary querymethods using
 * the iApis generic interface.
 */
public class ApiServices implements iAPI {

    /**
     * This class is only for static use. No instances are needed. That is why the
     * exception throwing constructor is implemented.
     * 
     * @throws Exception
     */
    public ApiServices() throws Exception {
        throw new Exception("Service class is not to be initialized.");
    }

    public static JsonObject getModule(String moduleId) {
        try {
            return iAPI.getJsonObjectFromApi(
                    String.format(
                            "https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=%s&universityId=tuni-university-root-id",
                            moduleId));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JsonObject getCourse(String courseId) {
        try {
            return iAPI.getJsonObjectFromApi(
                    String.format(
                            "https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=%s&universityId=tuni-university-root-id",
                            courseId));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JsonObject getDegreeProgram(String degreeId) {

        try {
            return iAPI.getJsonObjectFromApi(
                    String.format("https://sis-tuni.funidata.fi/kori/api/modules/%s?type=degreeProgramme", degreeId));

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * Finds and returns all existing degreeprogrammes. Works as a wrapper method
     * for the service of retrieving degrees.
     * 
     * @return List<String> element with the ids of degreeprogrammes
     *         If not found for some reason, returns empty list.
     */
    public static ArrayList<DegreeProgram> getAllProgramsAsList() {
        try {
            var obj = getAllPrograms();
            var list = obj.getAsJsonArray("searchResults");
            return parseDegreeProgramIds(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<DegreeProgram>();
        }
    }

    /**
     * Makes the correct query to retrieve all programs as raw json.
     *
     * @return raw JsonObject.
     */
    private static JsonObject getAllPrograms() {
        try {
            return iAPI.getJsonObjectFromApi(
                    "https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 
     * @param searchResults JsonArray representation of the all programmes
     * @return ArrayList<DegreeProgram> list of all the programs.
     */
    private static ArrayList<DegreeProgram> parseDegreeProgramIds(JsonArray searchResults) {
        ArrayList<DegreeProgram> progList = new ArrayList<DegreeProgram>();

        for (int i = 0; i < searchResults.size(); i++) {
            JsonObject degreeProgram = searchResults.get(i).getAsJsonObject();
            String degreeProgramId = degreeProgram.get("id").getAsString();
            String degreeProgramName = degreeProgram.get("name").getAsString();
            Integer minCredits = degreeProgram.getAsJsonObject("credits").get("min").getAsInt();
            progList.add(new DegreeProgram(degreeProgramName, degreeProgramId, degreeProgramId, minCredits));
        }

        return progList;
    }

}
