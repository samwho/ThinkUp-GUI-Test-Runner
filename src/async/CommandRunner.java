package async;

import java.util.ArrayList;
import java.util.Iterator;
import simpletestgui.MainForm;

/**
 * The CommandRunner class is one of the main classes in this program. It handles the
 * running of a group of tests on a separate thread.
 *
 * Pass one of these objects to a thread and run it, like so:
 *
 * Thread t = new Thread(new CommandRunner(<owner form>, <commands>));
 * t.start();
 *
 * for optimum results.
 * 
 * @author Sam Rose <samwho@lbak.co.uk>
 */
public class CommandRunner implements Runnable {

    public static int lineCount = 0;
    private ArrayList<RunCommand> commands;
    private MainForm owner;
    private static final Object lock = new Object();
    private static boolean stop = false;
    private int passes = 0;
    private int failures = 0;
    private int exceptions = 0;
    private int commandsRun = 0;

    /**
     * This is the constructor for the CommandRunner class. The CommandRunner is designed
     * to run a number of test methods from a given file on a separate thread.
     * @param owner The frame that is executing this command.
     * @param commands An ArrayList of RunCommand objects to run.
     */
    public CommandRunner(MainForm owner, ArrayList<RunCommand> commands) {
        this.commands = commands;
        this.owner = owner;
    }

    /**
     * Signals the CommandRunner to stop. The command runner will complete the
     * current test method that it is on and then it will exit. This is to stop
     * any unpredictable results from happening when cancelling a test mid way
     * through.
     */
    public static void stop() {
        stop = true;
    }

    @Override
    public void run() {
        /*
         * This method is synchronized on the static lock in this
         * class. This is to prevent the running of two sets of tests
         * at the same time.
         */
        synchronized (lock) {
            int noCommands = commands.size();

            this.owner.getRunButton().setEnabled(false);
            this.owner.getProgressBar().setMaximum(noCommands);
            this.owner.getProgressBar().setValue(0);

            // get an iterator of RunCommands and loop through them
            Iterator<RunCommand> iter = this.commands.iterator();
            while (iter.hasNext() && !stop) {
                RunCommand r = iter.next();
                this.owner.getProgressBar().setString("Running " + r.getMethod() + "...");
                this.owner.getProgressBar().setStringPainted(true);

                // run the command
                r.run();

                // increment the passes, failure and exceptions from this test
                passes += r.getPasses();
                failures += r.getFailures();
                exceptions += r.getExceptions();
                commandsRun++;

                if (owner.getHideSuccessfulTests().getState() && r.getFailures() == 0 && r.getPasses() > 0) {
                    // user is hiding successful tests and this test is successful
                } else {
                    owner.getTestOutput().append(r.getOutput() + "\n");
                    owner.getTestOutput().setCaretPosition(owner.getTestOutput().getText().length());
                }

                if (!r.getError().isEmpty()) {
                    owner.getTestOutput().append(r.getMethod() + " output to stderr:\n" + r.getError() + "\n");
                    owner.getTestOutput().setCaretPosition(owner.getTestOutput().getText().length());
                }

                // increment the commandsRun variable and set the value of the progress bar to its new value
                this.owner.getProgressBar().setValue(commandsRun);
            }

            // display the cumulative results of this set of tests
            owner.getTestOutput().append("=========================\n");
            owner.getTestOutput().append("Tests run: " + commandsRun + " - Total passes: " + passes +
                    " - Total failures: " + failures + " - Total exceptions: " + exceptions + "\n");
            owner.getTestOutput().setCaretPosition(owner.getTestOutput().getText().length());

            // handle the thread being stopped
            if (stop) {
                this.owner.getProgressBar().setValue(0);
                this.owner.getProgressBar().setString("Cancelled.");
            } else {
                this.owner.getProgressBar().setString("Completed.");
            }

            // reset the stop variable
            stop = false;

            // reset the button states
            this.owner.getRunButton().setEnabled(true);
            this.owner.getCancelButton().setEnabled(false);
        }
    }
}
