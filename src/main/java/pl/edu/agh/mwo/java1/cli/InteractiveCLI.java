package pl.edu.agh.mwo.java1.cli;



import pl.edu.agh.mwo.java1.logger.Logger;
import pl.edu.agh.mwo.java1.service.TaskService;

import java.util.Scanner;



/**
 * Prosty interaktywny CLI, który pozwala użytkownikowi wchodzić w interakcję z aplikacją.
 */
public class InteractiveCLI {
    private TaskService taskService;
    private InputValidator inputValidator;
    private Logger logger;
    private Scanner scanner;

    public InteractiveCLI(TaskService taskService, InputValidator inputValidator, Logger logger) {
        this.taskService = taskService;
        this.inputValidator = inputValidator;
        this.logger = logger;
        this.scanner = new Scanner(System.in);
    }

    public void start() {


        while (true) {
            System.out.print("Enter command: ");
            String input = scanner.nextLine();
            String[] args = input.split(" ");
            try {
                inputValidator.validateArgsLength(args);
                handleCommand(args);
            } catch (IllegalArgumentException e) {
                logger.logSevere("Invalid argument: " + e.getMessage());
                printHelp();
            } catch (Exception e) {
                logger.logSevere("An error occurred: " + e.getMessage(), e);
            }
        }
    }

    private void handleCommand(String[] args) {
        switch (args[0]) {
            case "start":
                inputValidator.validateStartArgs(args);
                taskService.startTask(args[1], args[2]);
                break;
            case "stop":
                taskService.stopTask();
                break;
            case "continue":
                taskService.continueTask();
                break;
            case "current":
                taskService.showCurrentTask();
                break;
            case "list":
                if (args.length < 2) {
                    taskService.listProjects();
                } else {
                    taskService.listTasks(args[1]);
                }
                break;
            case "project":
                taskService.listProjects();
                break;
            case "report":
                taskService.report();
                break;
            case "-h":
                printHelp();
                break;
            default:
                logger.logWarning("Unknown command. Use -h to display help.");
        }
    }

    private void printHelp() {
        System.out.println("Komendy:");
        System.out.println("app start <taskName> <projectName> - Rozpoczęcie nowego zadania");
        System.out.println("app stop - Zakończenie aktualnego zadania");
        System.out.println("app continue - kontynuowanie ostatniego zadania ");
        System.out.println("app current - Wyświetlenie aktualnie śledzonego zadania");
        System.out.println("app list [projectName] - Wyświetlenie pięciu ostatnich aktywności lub zadań w projekcie");
        System.out.println("app project - Wyświetlenie wszystkich projektów");
        System.out.println("app report - Wyświetlenie raportu z projektów");
        System.out.println("app -h - Wyświetlenie pomocy");
    }
}
