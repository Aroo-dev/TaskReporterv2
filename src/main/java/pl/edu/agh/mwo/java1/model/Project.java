package pl.edu.agh.mwo.java1.model;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Klasa reprezentująca projekt, który zarządza listą zadań (Task).
 */
public class Project {
    private static final AtomicLong idCounter = new AtomicLong(0);
    private final Long id;
    private String name;
    private final List<Task> tasks;

    /**
     * Konstruktor bezparametrowy.
     */
    public Project() {
        this.id = idCounter.incrementAndGet();
        this.tasks = new ArrayList<>();
    }

    /**
     * Konstruktor inicjalizujący projekt z podaną nazwą.
     *
     * @param name nazwa projektu
     */
    public Project(String name) {
        this.id = idCounter.incrementAndGet();
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    // Gettery i Settery

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Dodaje zadanie do projektu.
     *
     * @param task zadanie do dodania
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tasks=" + tasks +
                '}';
    }

    /**
     * Konwertuje projekt i jego zadania do formatu CSV.
     *
     * @return reprezentacja CSV projektu
     */
    public String toCsv() {
        StringBuilder csv = new StringBuilder();
        for (Task task : tasks) {
            csv.append(name).append(",").append(task.toString()).append("\n");
        }
        return csv.toString();
    }

    /**
     * Tworzy projekt z CSV.
     *
     * @param csvLines linie CSV
     * @return projekt
     */
    public static Project fromCsv(List<String> csvLines) {
        String projectName = csvLines.get(0).split(",")[0];
        Project project = new Project(projectName);
        for (String line : csvLines) {
            String[] parts = line.split(",", 2);
            project.addTask(Task.fromCsv(parts[1]));
        }
        return project;
    }
}
