package pl.edu.agh.mwo.java1.repository;





import pl.edu.agh.mwo.java1.model.Project;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacja repozytorium zadań, która wykorzystuje pliki CSV do przechowywania danych.

 import com.task.model.Project;

 import java.io.*;
 import java.util.ArrayList;
 import java.util.List;


 /**
 * Implementacja repozytorium zadań, która wykorzystuje pliki CSV do przechowywania danych.
 */
public class CsvTaskRepository implements TaskRepository {
    private static final String FILE_NAME = "time_log.csv";

    public CsvTaskRepository() {
        ensureFileExists();
    }

    @Override
    public List<Project> loadProjects() {
        List<Project> projects = new ArrayList<>();
        File file = new File(FILE_NAME);

        // Sprawdzenie, czy plik jest pusty
        if (file.length() == 0) {
            return projects; // Zwracamy pustą listę, jeśli plik jest pusty
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            List<String> currentProjectLines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String projectName = line.split(",")[0];
                if (!currentProjectLines.isEmpty() && !projectName.equals(currentProjectLines.get(0).split(",")[0])) {
                    projects.add(Project.fromCsv(currentProjectLines));
                    currentProjectLines.clear();
                }
                currentProjectLines.add(line);
            }
            if (!currentProjectLines.isEmpty()) {
                projects.add(Project.fromCsv(currentProjectLines));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projects;
    }

    @Override
    public void saveProjects(List<Project> projects) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Project project : projects) {
                writer.write(project.toCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ensureFileExists() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



