package pl.edu.agh.mwo.java1.service;


import pl.edu.agh.mwo.java1.logger.Logger;
import pl.edu.agh.mwo.java1.model.Project;
import pl.edu.agh.mwo.java1.model.Task;
import pl.edu.agh.mwo.java1.repository.TaskRepository;






import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
/**
 * Klasa TaskService zarządza zadaniami i projektami.
 */
public class TaskService {
    private List<Project> projects;
    private Task currentTask;
    private TaskRepository taskRepository;
    private Logger logger;

    public TaskService(TaskRepository taskRepository, Logger logger) {
        this.taskRepository = taskRepository;
        this.projects = taskRepository.loadProjects();
        this.logger = logger;
    }

    public void startTask(String taskName, String projectName) {
        if (currentTask != null) {
            logger.logWarning("Stop the current task before starting a new one.");
            return;
        }
        Project project = findOrCreateProject(projectName);
        currentTask = new Task(taskName);
        project.addTask(currentTask);
        saveProjects();
    }

    public void stopTask() {
        if (currentTask == null) {
            logger.logWarning("No task is currently running.");
            return;
        }
        currentTask.stop();
        currentTask = null;
        saveProjects();
    }

    public void continueTask() {
        if (currentTask != null) {
            logger.logWarning("Stop the current task before continuing another one.");
            return;
        }

        Task previousTask = null;
        Project projectToContinue = null;

        for (Project project : projects) {
            for (Task task : project.getTasks()) {
                if (task.getStopTask() != null) {
                    if (previousTask == null || task.getStopTask().isAfter(previousTask.getStopTask())) {
                        previousTask = task;
                        projectToContinue = project;
                    }
                }
            }
        }

        if (previousTask == null) {
            logger.logWarning("No task to continue.");
            return;
        }

        currentTask = new Task(previousTask.getTaskName());
        projectToContinue.addTask(currentTask);
        saveProjects();
    }

    public void showCurrentTask() {
        if (currentTask == null) {
            logger.logInfo("No task is currently running.");
        } else {
            logger.logInfo("Current task: " + currentTask.getTaskName());
        }
    }

    public void listTasks(String projectName) {
        Optional<Project> projectOpt = projects.stream()
                .filter(p -> p.getName().equals(projectName))
                .findFirst();

        if (projectOpt.isPresent()) {
            projectOpt.get().getTasks().forEach(task -> logger.logInfo(task.toString()));
        } else {
            logger.logWarning("Project not found.");
        }
    }

    public void listProjects() {
        projects.stream()
                .map(Project::getName)
                .distinct()
                .forEach(logger::logInfo);
    }

    public void report() {
        projects.forEach(project -> {
            logger.logInfo("Project: " + project.getName());
            project.getTasks().forEach(task -> {
                String duration = formatDuration(task.getTotalTime());
                logger.logInfo(String.format("Task: %s, Duration: %s", task.getTaskName(), duration));
            });
        });
    }

    private Project findOrCreateProject(String projectName) {
        return projects.stream()
                .filter(p -> p.getName().equals(projectName))
                .findFirst()
                .orElseGet(() -> {
                    Project newProject = new Project(projectName);
                    projects.add(newProject);
                    return newProject;
                });
    }

    private void saveProjects() {
        try {
            taskRepository.saveProjects(projects);
        } catch (Exception e) {
            logger.logSevere("Failed to save projects", e);
        }
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public Task getCurrentTask() {
        return currentTask;
    }
}