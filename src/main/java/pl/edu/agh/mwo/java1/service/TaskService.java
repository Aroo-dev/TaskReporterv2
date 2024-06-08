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

    /**
     * Konstruktor klasy TaskService.
     *
     * @param taskRepository repozytorium projektów
     * @param logger         logger do logowania wiadomości
     */
    public TaskService(TaskRepository taskRepository, Logger logger) {
        this.taskRepository = taskRepository;
        this.projects = taskRepository.loadProjects();
        this.logger = logger;
    }

    /**
     * Rozpoczyna nowe zadanie.
     *
     * @param taskName    nazwa zadania
     * @param projectName nazwa projektu
     */
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

    /**
     * Kończy aktualne zadanie.
     */
    public void stopTask() {
        if (currentTask == null) {
            logger.logWarning("No task is currently running.");
            return;
        }
        currentTask.stop();
        currentTask = null;
        saveProjects();
    }

    /**
     * Wznawia poprzednie zadanie w podanym projekcie lub ostatnie zastopowane zadanie.
     *
     * @param projectName nazwa projektu
     * @param index       indeks zadania, opcjonalnie
     */
    public void continueTask(String projectName, Integer index) {
        Optional<Project> projectOpt = projects.stream()
                .filter(p -> p.getName().equals(projectName))
                .findFirst();

        if (!projectOpt.isPresent()) {
            logger.logWarning("Invalid project name.");
            return;
        }
        if (currentTask != null) {
            logger.logWarning("Stop the current task before continuing another one.");
            return;
        }

        Project project = projectOpt.get();
        Task previousTask;

        if (index != null && index >= 0 && index < project.getTasks().size()) {
            previousTask = project.getTasks().get(index);
        } else {
            previousTask = project.getTasks().stream()
                    .filter(task -> task.getStopTask() != null)
                    .reduce((first, second) -> second).orElse(null);
        }

        if (previousTask == null) {
            logger.logWarning("No task to continue.");
            return;
        }

        currentTask = new Task(previousTask.getTaskName());
        project.addTask(currentTask);
        saveProjects();
    }

    /**
     * Wyświetla aktualnie śledzone zadanie.
     */
    public void showCurrentTask() {
        if (currentTask == null) {
            logger.logInfo("No task is currently running.");
        } else {
            logger.logInfo("Current task: " + currentTask.getTaskName());
        }
    }

    /**
     * Wyświetla zadania w podanym projekcie.
     *
     * @param projectName nazwa projektu
     */
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

    /**
     * Wyświetla wszystkie projekty.
     */
    public void listProjects() {
        projects.stream()
                .map(Project::getName)
                .distinct()
                .forEach(logger::logInfo);
    }

    /**
     * Wyświetla raport z wszystkich projektów.
     */
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

    /**
     * Zwraca aktualne zadanie.
     *
     * @return aktualne zadanie
     */
    public Task getCurrentTask() {
        return currentTask;
    }
}
