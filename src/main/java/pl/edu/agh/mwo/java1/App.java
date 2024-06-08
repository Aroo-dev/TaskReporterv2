package pl.edu.agh.mwo.java1;

import pl.edu.agh.mwo.java1.cli.InputValidator;
import pl.edu.agh.mwo.java1.cli.InteractiveCLI;
import pl.edu.agh.mwo.java1.logger.Logger;
import pl.edu.agh.mwo.java1.repository.CsvTaskRepository;
import pl.edu.agh.mwo.java1.service.TaskService;


/**
 * Główna klasa aplikacji.
 */
public class App {
    private static final Logger logger = new Logger();
    private static final InputValidator inputValidator = new InputValidator();

    public static void main(String[] args) {
        CsvTaskRepository taskRepository = new CsvTaskRepository();
        TaskService taskService = new TaskService(taskRepository, logger);
        printHelp();

        if (args.length > 0) {
            try {
                inputValidator.validateArgsLength(args);
                handleCommand(args, taskService);
            } catch (IllegalArgumentException e) {
                logger.logSevere("Invalid argument: " + e.getMessage());
                printHelp();
            } catch (Exception e) {
                logger.logSevere("An error occurred: " + e.getMessage(), e);
            }
        } else {
            InteractiveCLI cli = new InteractiveCLI(taskService, inputValidator, logger);
            cli.start();
        }
    }

    private static void handleCommand(String[] args, TaskService taskService) {
        switch (args[0]) {
            case "start":
                inputValidator.validateStartArgs(args);
                taskService.startTask(args[1], args[2]);
                break;
            case "stop":
                taskService.stopTask();
                break;
            case "continue":
                inputValidator.validateContinueArgs(args);
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
                logger.logWarning("Unknown command. Use app -h to display help.");
        }
    }

    private static void printHelp() {
        System.out.println("Komendy:");
        System.out.println("app start <taskName> <projectName> - Rozpoczęcie nowego zadania");
        System.out.println("app stop - Zakończenie aktualnego zadania");
        System.out.println("app continue <projectName> [taskIndex] - Wznowienie zadania, opcjonalnie z podanym indeksem");
        System.out.println("app current - Wyświetlenie aktualnie śledzonego zadania");
        System.out.println("app list [projectName] - Wyświetlenie pięciu ostatnich aktywności lub zadań w projekcie");
        System.out.println("app project - Wyświetlenie wszystkich projektów");
        System.out.println("app report - Wyświetlenie raportu z projektów");
    }
}