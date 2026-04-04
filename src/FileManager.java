import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.time.LocalDate;

public class FileManager {

    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .setPrettyPrinting()
        .create();

    public static void saveProject(Project project, String filePath) {
        File file = new File(filePath);

        File parent = file.getParentFile();
        if(parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new RuntimeException("Could not create directory: " + parent.getAbsolutePath());
        }

        try(Writer writer = new FileWriter(file)) {
            gson.toJson(project, writer);
        } catch(IOException e) {
            throw new RuntimeException("Failed to save project to: " + filePath, e);
        }
    }

    public static Project loadProject(String filePath) {
        File file = new File(filePath);
        try(Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, Project.class);
        } catch(FileNotFoundException e) {
            throw new RuntimeException("Project file not found: " + filePath, e);
        } catch(IOException e) {
            throw new RuntimeException("Failed to load project from: " + filePath, e);
        }
    }

}
