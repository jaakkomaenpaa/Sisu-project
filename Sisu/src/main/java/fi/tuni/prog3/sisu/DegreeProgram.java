package fi.tuni.prog3.sisu;

import java.util.ArrayList;

import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class DegreeProgram extends DegreeModule {

    private ArrayList<StudyModule> subModules = new ArrayList<StudyModule>();
    protected StoredInfo studentInfo;

    /**
     * A constructor for initializing the member variables.
     * Only for testing and the Programs array initialization in the degree
     * selection phase.
     * 
     * @param name       name of the Module or Course.
     * @param id         id of the Module or Course.
     * @param groupId    group id of the Module or Course.
     * @param minCredits minimum credits of the Module or Course.
     */

    public DegreeProgram(String name, String id, String groupId, int minCredits) {
        super(name, id, groupId, minCredits);
    }

    /**
     * A constructor for initializing the member variables also triggers the
     * logic on the lower levels of hierarchy to populate the data structures such
     * as studymodules and
     * courses.
     * 
     * @param readFromFile condition to specify if the constructor should call
     *                     further methods to retrieve information about student's
     *                     study record
     * @param inputInfo    the students information submitted in the begin pages
     *                     form
     * @param name         name of the Module or Course.
     * @param id           id of the Module or Course.F
     * @throws Exception
     */
    public DegreeProgram(String name, String id, StoredInfo inputInfo, boolean readFromFile) throws Exception {
        //
        super(name, id);
        this.studentInfo = new StoredInfo(inputInfo.getName(), inputInfo.getStudentNumber(), inputInfo.getStartYear(),
                inputInfo.getEndYear());
        parseProgram(ApiServices.getDegreeProgram(id));
        // the degreees and their strctures are always fetched from Sisu api, thus only
        // courses states are read from file if there is one
        addSubModules(id);

        // if there is history data, then only the studentnumber is needed from the
        // Storedinfo object passed
        if (readFromFile) {
            addStudyRecords(ReadWriteServices.readInfo(inputInfo.getStudentNumber()));
        }
        // if not, than the new information inputted in the form of begin page will be
        // stored to live in studentInfo attribute. Somewhat wonky to store the user
        // information in this degreeprogram class but it helps in the UI end when there
        // is only one instance to be passed around

    }

    public int getEarnedCredits() {
        int res = 0;
        for (StudyModule mod : subModules) {
            mod.getEarnedCredits(res);
        }
        return res;
    }

    /**
     * Method that triggers gathering information retrieving from subcontents and
     * triggers the writing event
     * 
     * @throws Exception
     * @returns boolean based on the result if the writingwas successful
     */
    public boolean save() throws Exception {
        JsonObject toBeStored = new JsonObject();
        toBeStored.addProperty("name", studentInfo.getName());
        toBeStored.addProperty("studentNumber", studentInfo.getStudentNumber());
        toBeStored.addProperty("startYear", studentInfo.getStartYear());
        toBeStored.addProperty("endYear", studentInfo.getEndYear());
        ArrayList<Course> statefulCourses = getStatefulCourses();
        JsonArray courseArray = new JsonArray();
        for (Course c : statefulCourses) {
            JsonObject courseInfo = new JsonObject();
            courseInfo.addProperty("id", c.getId());
            courseInfo.addProperty("state", c.getState());
            courseArray.add(courseInfo);
        }
        toBeStored.add("courses", courseArray);
        return ReadWriteServices.writeToFile(toBeStored);
    }

    /**
     * Triggers callqueue downwards and calls lower levels to retrieve all their
     * courses which have state other than DEFAULT
     * 
     * @return ArrayList<Course> list of courses.
     */
    private ArrayList<Course> getStatefulCourses() {
        ArrayList<Course> courses = new ArrayList<Course>();
        for (StudyModule mod : subModules) {
            ArrayList<Course> sublist = mod.getStatefulCourses(courses);
            courses.addAll(sublist);

        }
        return courses;
    }

    /**
     * 
     * @param storedInfo json element of the users previously stored information
     */
    private void addStudyRecords(JsonElement storedInfo) {
        // this should be fairly failproof since the json schema is self defined and
        // won't give any nested surprises
        try {
            String name = storedInfo.getAsJsonObject().get("name").getAsString();
            String studenntNumber = storedInfo.getAsJsonObject().get("studentNumber").getAsString();
            int startYear = storedInfo.getAsJsonObject().get("startYear").getAsInt();
            int endYear = storedInfo.getAsJsonObject().get("endYear").getAsInt();
            JsonArray record = storedInfo.getAsJsonObject().getAsJsonArray("courses");
            this.studentInfo = new StoredInfo(name, studenntNumber, startYear, endYear);

            for (StudyModule mod : this.subModules) {
                mod.addRecords(record);
            }
        } catch (Exception e) {
            System.out.println("studyRecords: " + e);
        }

    }

    /**
     * Helper method for constructor for adding the modules under certain program to
     * the submodules list.
     * 
     * @param id groupId of the selected DegreeProgram
     */
    private void addSubModules(String id) {
        if (this.subModules.size() > 0) {
            return;
        }
        Map<String, String> moduleIds = getModuleIdsAsMap(ApiServices.getDegreeProgram(id));

        for (Map.Entry<String, String> entry : moduleIds.entrySet()) {
            // for preventing special cases like different types of rules in the retrieved
            // IdMap
            if (entry.getValue().equals("ModuleRule")) {
                StudyModule studyModule = parseModule(ApiServices.getModule(entry.getKey()));
                this.subModules.add(studyModule);
            }

        }

    }

    /**
     * Returns submodules array.
     * 
     * @return ArrayList<StudyModule>
     */
    public ArrayList<StudyModule> getSubModules() {
        return subModules;
    }

    /**
     * Retrieves the wanted fields from DegreeProgram type of JSON object.
     * 
     * @param progObj DegreeProgram JSON object.
     */
    private void parseProgram(JsonObject progObj) {
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(progObj, Map.class);
        Map<String, Object> nameMap = (Map<String, Object>) map.get("name");
        this.name = (String) nameMap.get("en");
        this.id = (String) map.get("id");
        this.groupId = (String) map.get("groupId");
        Map<String, Integer> targetCreditsMap = gson.fromJson(
                gson.toJsonTree(map.get("targetCredits")),
                new TypeToken<Map<String, Integer>>() {
                }.getType());
        this.minCredits = targetCreditsMap.get("min");

    }

    /**
     * Retrieves all the ids of modules inside the element's "rules" array.
     * 
     * @param element DegreeProgram JSON
     * @return Map<String, String> where the unique groupId is key and value is the
     *         type.
     */
    private Map<String, String> getModuleIdsAsMap(JsonElement element) {

        Map<String, String> subcontentMap = new TreeMap<String, String>();
        try {
            JsonArray subContentIds = getRulesRecursive(element);

            for (JsonElement rule : subContentIds) {
                String type = rule.getAsJsonObject().get("type").getAsString();
                if (type.equals("ModuleRule")) {
                    String groupId = rule.getAsJsonObject().get("moduleGroupId").getAsString();
                    subcontentMap.put(groupId, type);
                } else if (type.equals("CourseUnitRule")) {
                    String groupId = rule.getAsJsonObject().get("courseUnitGroupId").getAsString();
                    subcontentMap.put(groupId, type);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return subcontentMap;
    }

    private JsonArray getRulesRecursive(JsonElement el) {

        JsonObject obj = el.getAsJsonObject();
        if (obj.has("rule")) {
            JsonObject rule = obj.get("rule").getAsJsonObject();
            return getRulesRecursive(rule);
        } else if (obj.has("rules")) {
            if (obj.getAsJsonArray("rules").size() == 0) {
                return null;
            } else {
                JsonObject rule = obj.getAsJsonArray("rules").get(0).getAsJsonObject();
                if (rule.has("rules")) {
                    return getRulesRecursive(rule);
                } else {
                    return obj.getAsJsonArray("rules");

                }
            }

        } else {
            // this means no rules array found
            return null;
        }

    }

    /**
     * Retrieves the wanted fields from a single module object.
     * 
     * @param moduleObj JSON of a module.
     * @return StudyModule object.
     */
    private StudyModule parseModule(JsonObject moduleObj) {
        String id = "";
        String groupId = "";
        JsonObject name;
        String enName = "";
        JsonObject targetCredits;
        int minCredits = 0;

        try {
            id = moduleObj.get("id").getAsString();
        } catch (Exception e) {
            System.out.println("MODid: " + e);
        }
        try {
            groupId = moduleObj.get("groupId").getAsString();
        } catch (Exception e) {
            System.out.println("MODgroupId: " + e);
        }
        try {
            name = moduleObj.get("name").getAsJsonObject();
            if (name.has("en")) {
                enName = name.get("en").getAsString();

            } else {
                enName = name.get("fi").getAsString();
            }
        } catch (Exception e) {
            System.out.println("MODname: " + e);
        }
        try {
            targetCredits = moduleObj.get("targetCredits").getAsJsonObject();
            minCredits = targetCredits.get("min").getAsInt();
        } catch (Exception e) {
            System.out.println("MODcredits: " + e);
        }

        return new StudyModule(enName, id, groupId, minCredits);

    }

    public void print() {
        for (StudyModule mod : subModules) {
            System.out.println(mod.getName() + "------------------");
            mod.printSubContents();
        }
    }

}