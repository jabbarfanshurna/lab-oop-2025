package kanbanlitegradle.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectDataStore {
    private static final String FILE_PATH = "src/main/resources/data/projects.json";

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setPrettyPrinting()
            .create();

    public static void saveProjects(List<Project> projects) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(projects, writer);
            System.out.println("Proyek berhasil disimpan ke file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Project> loadProjects() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Project[] projectArray = gson.fromJson(reader, Project[].class);
            List<Project> projects = new ArrayList<>(Arrays.asList(projectArray));

            for (Project project : projects) {
                if (project.getTeamMembers() == null) {
                    project.setTeamMembers(new ArrayList<>());
                }
                if (project.getTasks() == null) {
                    project.setTasks(new ArrayList<>());
                }
            }

            return projects;
        } catch (IOException e) {
            System.out.println("Gagal memuat file proyek, membuat daftar kosong.");
            return new ArrayList<>();
        }
    }
}