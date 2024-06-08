package pl.edu.agh.mwo.java1.model;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Klasa reprezentująca pojedyncze zadanie.
 */
public class Task {
    private static final AtomicLong counter = new AtomicLong(0);
    private Long id;
    private String taskName;
    private LocalDateTime startTask;
    private LocalDateTime stopTask;
    private Duration totalTime;

    /**
     * Konstruktor inicjalizujący zadanie z podaną nazwą.
     *
     * @param taskName nazwa zadania
     */
    public Task(String taskName) {
        this.taskName = taskName;
        this.startTask = LocalDateTime.now();
        this.id = counter.incrementAndGet();
        this.totalTime = Duration.ZERO;
    }

    /**
     * Konstruktor bezparametrowy.
     */
    public Task() {
        // Konstruktor bezparametrowy wymagany do deserializacji, jeśli to konieczne
    }

    // Gettery i Settery

    public Long getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public LocalDateTime getStartTask() {
        return startTask;
    }

    public LocalDateTime getStopTask() {
        return stopTask;
    }

    public Duration getTotalTime() {
        return totalTime;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setStartTask(LocalDateTime startTask) {
        this.startTask = startTask;
    }

    public void setStopTask(LocalDateTime stopTask) {
        this.stopTask = stopTask;
    }

    public void setTotalTime(Duration totalTime) {
        this.totalTime = totalTime;
    }

    /**
     * Zatrzymuje zadanie i oblicza łączny czas trwania.
     */
    public void stop() {
        if (this.startTask != null) {
            this.stopTask = LocalDateTime.now();
            this.totalTime = this.totalTime.plus(Duration.between(this.startTask, this.stopTask));
            this.startTask = null;
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", startTask=" + startTask +
                ", stopTask=" + stopTask +
                ", totalTime=" + totalTime +
                '}';
    }

    /**
     * Tworzy zadanie z CSV.
     *
     * @param csvLine linia CSV
     * @return zadanie
     */
    public static Task fromCsv(String csvLine) {
        String[] parts = csvLine.split(",");
        Task task = new Task(parts[0]);
        task.setStartTask(LocalDateTime.parse(parts[1]));
        if (!parts[2].isEmpty()) {
            task.setStopTask(LocalDateTime.parse(parts[2]));
            task.setTotalTime(Duration.parse(parts[3]));
        }
        return task;
    }
}

