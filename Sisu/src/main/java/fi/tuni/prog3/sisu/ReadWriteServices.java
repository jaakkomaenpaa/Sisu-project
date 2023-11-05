package fi.tuni.prog3.sisu;

import com.google.gson.JsonElement;

/**
 * Implementation of the interface, provides simple reading and writing static
 * methods for data classes to use.
 */
public class ReadWriteServices implements iReadAndWriteToFile {

    private ReadWriteServices() throws Exception {
        throw new Exception("Service class is not to be initialized");
    }

    /**
     * Method for reading the stored information. To be sure tries with two
     * different paths to read the files
     * 
     * @param studentNum works as a filename
     * @return
     * @throws Exception
     */
    public static JsonElement readInfo(String studentNum) throws Exception {
        try {
            JsonElement res;

            res = iReadAndWriteToFile
                    .readFromFile(
                            String.format("group2709/Sisu/src/main/java/fi/tuni/prog3/sisu/data/%s.json",
                                    studentNum));
            if (res == null) {
                res = iReadAndWriteToFile
                        .readFromFile(
                                String.format("Sisu/src/main/java/fi/tuni/prog3/sisu/data/%s.json", studentNum));
            }

            return res;
        } catch (Exception e) {
            System.out.println("fail" + e);
            return null;
        }

    }

    /**
     * Mehtod for writing made course state changes to file
     * 
     * @param infoToSave readymade jsonelement to be saved
     * @return
     * @throws Exception
     */
    public static boolean writeToFile(JsonElement infoToSave) throws Exception {

        try {
            String studentNum = infoToSave.getAsJsonObject().get("studentNumber").getAsString();
            boolean res = false;

            res = iReadAndWriteToFile.writeToFile(
                    String.format("group2709/Sisu/src/main/java/fi/tuni/prog3/sisu/data/%s.json", studentNum),
                    infoToSave);
            if (!res) {
                res = iReadAndWriteToFile.writeToFile(
                        String.format("Sisu/src/main/java/fi/tuni/prog3/sisu/data/%s.json", studentNum),
                        infoToSave);
            }

            return res;
        } catch (Exception e) {
            return false;
        }
    }

}
