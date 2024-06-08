package pl.edu.agh.mwo.java1.model;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Klasa reprezentująca pojedyncze zadanie.
 */
public class Task {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
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

    }


    public void setId(Long id) {
        this.id = id;
    }

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
        if (!parts[1].isEmpty()) {
            task.startTask = LocalDateTime.parse(parts[1], DATE_TIME_FORMATTER);
        }
        if (!parts[2].isEmpty()) {
            task.stopTask = LocalDateTime.parse(parts[2], DATE_TIME_FORMATTER);
            task.totalTime = Duration.parse(parts[3]);
        }
        return task;
    }

    public String toCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append(taskName).append(",");
        csv.append(startTask != null ? startTask.format(DATE_TIME_FORMATTER) : "").append(",");
        csv.append(stopTask != null ? stopTask.format(DATE_TIME_FORMATTER) : "").append(",");
        csv.append(totalTime);
        return csv.toString();
    }
}

