package pl.edu.agh.mwo.java1.repository;

import pl.edu.agh.mwo.java1.model.Project;

import java.util.List;

public interface TaskRepository {
    List<Project> loadProjects();
    void saveProjects(List<Project> projects);
}
