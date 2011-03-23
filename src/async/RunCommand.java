package async;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import simpletestgui.MainForm;

/**
 * This class exists to execute a test and output its results to the
 * designated output text box on the MainForm class.
 *
 * This class is designed to be run as part of a CommandRunner and it is
 * not recommended to use it on its own without knowing how it works.
 * 
 * @author Sam Rose <samwho@lbak.co.uk>
 */
public class RunCommand implements Runnable {

    private static Pattern getPassesPattern = Pattern.compile("Passes: ([0-9]+),");
    private static Pattern getFailuresPattern = Pattern.compile("Failures: ([0-9]+),");
    private static Pattern getExceptionsPattern = Pattern.compile("Exceptions: ([0-9]+)");
    private String command;
    private String[] env;
    private MainForm owner;
    private int passes = 0;
    private int failures = 0;
    private int exceptions = 0;
    private String output = "";

    /**
     * Initialises the RunCommand object with the owner form, the command to
     * execute and the environment variables to use.
     *
     * @param owner The owner form.
     * @param command
     * @param env
     */
    public RunCommand(MainForm owner, String command, String[] env) {
        this.owner = owner;
        this.command = command;
        this.env = env;

        System.out.println("Running command: " + this.command);
        System.out.println("With env:");
        for (int i = 0; i < env.length; i++) {
            System.out.println(env[i]);
        }
    }

    public int getFailures() {
        return failures;
    }

    public int getPasses() {
        return passes;
    }

    public int getExceptions() {
        return exceptions;
    }

    public String getOutput() {
        return output;
    }

    /**
     * Gets the command that this RunCommand is going to be executing.
     * @return The command.
     */
    public String getCommand() {
        return command;
    }

    /**
     * Gets the method that this RunCommand is going to be executing.
     * @return The TEST_METHOD environment variable.
     */
    public String getMethod() {
        for (int i = 0; i < env.length; i++) {
            if (env[i].contains("TEST_METHOD=")) {
                return env[i].substring(12);
            }
        }

        return null;
    }

    /**
     * Runs the command and adds the command output to the output box
     * on the owner form.
     */
    public void run() {
        Matcher passesMatcher;
        Matcher failuresMatcher;
        Matcher exceptionsMatcher;

        try {
            Process p = Runtime.getRuntime().exec(this.command, this.env);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = br.readLine();
            while (line != null) {
                output += line + "\n";
                line = br.readLine();
            }

            passesMatcher = getPassesPattern.matcher(output);
            failuresMatcher = getFailuresPattern.matcher(output);
            exceptionsMatcher = getExceptionsPattern.matcher(output);

            if (passesMatcher.find()) {
                passes += Integer.parseInt(passesMatcher.group(1));
            }
            if (failuresMatcher.find()) {
                failures += Integer.parseInt(failuresMatcher.group(1));
            }
            if (exceptionsMatcher.find()) {
                exceptions += Integer.parseInt(exceptionsMatcher.group(1));
            }

            br.close();
        } catch (Exception e) {
            output += "ERROR: There was an error while running the command " + command
                    + " with the following environment variables:\n";
            for (int i = 0; i < env.length; i++) {
                output += env[i] + "\n";
            }
        }
    }
}
