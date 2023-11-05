package fi.tuni.prog3.sisu;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Class to represent Module instance, implemente somewhat generically since
 * instances if this class can hold subModules, courses or both.
 */
public class StudyModule extends DegreeModule {

    ArrayList<Course> courses = new ArrayList<Course>();
    ArrayList<StudyModule> subModules = new ArrayList<StudyModule>();

    /**
     * A constructor for initializing the member variables.
     * 
     * @param name       name of the Module or Course.
     * @param id         id of the Module or Course.
     * @param groupId    group id of the Module or Course.
     * @param minCredits minimum credits of the Module or Course.
     */
    public StudyModule(String name, String id, String groupId,
            int minCredits) {
        super(name, id, groupId, minCredits);
        addSubContents(groupId);

    }

    /**
     * just for testing
     * 
     * @param name
     * @param id
     * @param groupId
     * @param minCredits
     * @param subModuleIds
     * @param istest
     */
    public StudyModule(String name, String id, String groupId,
            int minCredits, boolean istest) {
        super(name, id, groupId, minCredits);

    }

    /**
     * 
     * @param total if this is called from higher, then the input will have already
     *              a count in it
     *              otherwise this can be called with param 0 if we want just to get
     *              credits earned under certain module.
     * @return
     */
    public int getEarnedCredits(int total) {
        for (Course course : courses) {
            if ((course.getState().equals("COMPLETED"))) {
                total += course.getMinCredits();
            }

        }
        for (StudyModule mod : subModules) {
            mod.getEarnedCredits(total);

        }
        return total;
    }

    public ArrayList<Course> getStatefulCourses(ArrayList<Course> statefulCourses) {

        for (Course course : courses) {
            if (!(course.getState().equals("DEFAULT"))) {
                statefulCourses.add(course);
            }

        }
        for (StudyModule mod : subModules) {
            mod.getStatefulCourses(statefulCourses);

        }
        return statefulCourses;
    }

    /**
     * Method to alter the courses states based on state they were left last time
     * closing the program.
     * 
     * @param records array of the courses which state was something other than
     *                DEFAULT
     */
    public void addRecords(JsonArray records) {

        for (Course course : courses) {
            for (JsonElement el : records) {

                JsonObject recordCourse = el.getAsJsonObject();

                if (course.getId().equals(recordCourse.get("id").getAsString())) {
                    course.setState(recordCourse.get("state").getAsString());
                }
            }

        }
        for (StudyModule mod : subModules) {
            mod.addRecords(records);

        }
    }

    /**
     * Methodwhich is called in initialization and fills the subModules and courses
     * arrays accordingly.
     * 
     * @param moduleid Modules groupId.
     */
    private void addSubContents(String moduleid) {
        Map<String, String> subcontentMap = new TreeMap<String, String>();
        JsonArray contents = getRulesRecursive(ApiServices.getModule(moduleid));
        if (contents == null) {
            return;
        }

        // For readibility these next two loops are split in two, eventhough one would
        // do the
        // trick too. The map size is so small at almost all times(n<20) so no
        // performance losses are taken for "unnecessary" looping.
        for (JsonElement rule : contents) {
            String type = rule.getAsJsonObject().get("type").getAsString();
            if (type.equals("ModuleRule")) {
                String groupId = rule.getAsJsonObject().get("moduleGroupId").getAsString();
                subcontentMap.put(groupId, type);
            } else if (type.equals("CourseUnitRule")) {
                String groupId = rule.getAsJsonObject().get("courseUnitGroupId").getAsString();
                subcontentMap.put(groupId, type);
            }

        }

        for (Map.Entry<String, String> entry : subcontentMap.entrySet()) {
            if (entry.getValue().equals("ModuleRule")) {
                StudyModule studyModule = parseModule(ApiServices.getModule(entry.getKey()));
                this.subModules.add(studyModule);
            } else if (entry.getValue().equals("CourseUnitRule")) {
                Course course = parseCourse(ApiServices.getCourse(entry.getKey()));
                this.courses.add(course);
            }

        }
    }

    public void printSubContents() {
        for (Course course : courses) {
            course.print();
        }
        for (StudyModule mod : subModules) {
            System.out.println(mod.getName() + "------------");
            mod.printSubContents();

        }
    }

    /**
     * Recursive helper method which finds the innermost "rules" array from Module
     * type of JSON.
     * 
     * @param el the module Json where the recursion begins.
     * @return the rules array.
     */
    private JsonArray getRulesRecursive(JsonElement el) {
        if (el == null)
            return null;
        JsonObject obj = el.getAsJsonObject();
        if (obj.has("rule")) {
            JsonObject rule = obj.get("rule").getAsJsonObject();
            return getRulesRecursive(rule);
        } else if (obj.has("rules")) {
            JsonObject rule = obj.getAsJsonArray("rules").get(0).getAsJsonObject();
            if (rule.has("rules")) {
                return getRulesRecursive(rule);
            } else {
                return obj.getAsJsonArray("rules");

            }

        } else {
            // this means no rules array found
            return null;
        }

    }

    /**
     * returns Courses in the courses array.
     * 
     * @return ArrayList<Course>
     */
    public ArrayList<Course> getCourses() {
        return courses;
    }

    /**
     * Returns StudyModules in subModules array.
     * 
     * @return ArrayList<StudyModule>
     */
    public ArrayList<StudyModule> getSubModules() {
        return subModules;
    }

    /**
     * Parsing method for singular course Json object. Heavily exception handled.
     * Choice was made to search for english texts by default and finnish as
     * secondary option.
     * 
     * @param courseObj the JSON containg information about a single course
     * @return Course object
     */
    private Course parseCourse(JsonObject courseObj) {
        String id = "";
        String name = "";
        String outcomes = "";
        String content = "";
        String groupId = "";
        int credits = 0;

        try {
            // Extracting "id" field
            id = courseObj.get("id").getAsString();
        } catch (Exception e) {
            System.out.println("Cid: " + e);
        }

        try {
            // Extracting "name" field
            if (courseObj.getAsJsonObject("name").has("en")) {
                name = courseObj.getAsJsonObject("name").get("en").getAsString();
            } else {
                name = courseObj.getAsJsonObject("name").get("fi").getAsString();
            }
        } catch (Exception e) {
            System.out.println("Cname: " + e);
        }

        try {
            // Extracting "outcomes" field
            if (courseObj.getAsJsonObject("outcomes").has("en")) {
                outcomes = courseObj.getAsJsonObject("outcomes").get("en").getAsString();
            } else {
                outcomes = courseObj.getAsJsonObject("outcomes").get("fi").getAsString();
            }
        } catch (Exception e) {
            System.out.println("Coutcomes: " + e);
        }

        try {
            // Extracting "content" field
            if (courseObj.has("content")) {
                if (courseObj.getAsJsonObject("content").has("en")) {
                    content = courseObj.getAsJsonObject("content").get("en").getAsString();
                } else {
                    content = courseObj.getAsJsonObject("content").get("fi").getAsString();
                }
            }
        } catch (Exception e) {
            System.out.println("Ccontent: " + e);
        }

        try {
            // Extracting "groupId" field
            groupId = courseObj.get("groupId").getAsString();

        } catch (Exception e) {
            System.out.println("CgroupId: " + e);
        }

        try {
            // Extracting "credits" field
            credits = courseObj.getAsJsonObject("credits").get("min").getAsInt();
        } catch (Exception e) {
            System.out.println("Ccredits: " + e);
        }
        return new Course(name, id, groupId, credits, content, outcomes);
    }

    /**
     * Parsing method for singular module Json objects. Heavily exception handled.
     * Choice was made to search for english texts by default and finnish as
     * secondary option.
     * 
     * @param moduleObj the Json object containing information about single module.
     * @return StudyModule object
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

}
