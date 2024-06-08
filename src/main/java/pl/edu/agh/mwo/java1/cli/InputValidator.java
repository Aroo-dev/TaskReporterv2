package pl.edu.agh.mwo.java1.cli;



/**
 * Klasa do walidacji argumentów wejściowych.
 */
public class InputValidator {
    public void validateStartArgs(String[] args) {
        if (args.length < 3) {
            throw new IllegalArgumentException("Usage: app start <taskName> <projectName>");
        }
    }

    public void validateContinueArgs(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: app continue <projectName> [taskIndex]");
        }
        if (args.length == 3) {
            try {
                Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Task index must be a number.");
            }
        }
    }

    public void validateArgsLength(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No command provided.");
        }
    }
}

