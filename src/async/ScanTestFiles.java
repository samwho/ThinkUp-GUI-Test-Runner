/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package async;

import config.Config;
import io.ThinkUpTestsDirectory;
import simpletestgui.MainForm;

/**
 *
 * @author sam
 */
public class ScanTestFiles implements Runnable {
    private MainForm owner;
    private ThinkUpTestsDirectory testFiles;
    private Config config = Config.getInstance();

    public ScanTestFiles(MainForm owner, ThinkUpTestsDirectory testDirectory) {
        this.owner = owner;
        this.testFiles = testDirectory;
    }

    public void run() {
        if (!Config.getConfigFile().isFile()) {
            this.owner.getTestOutput().append("Failed to load config file from: " +
                    Config.getConfigFile().getAbsolutePath());
            return;
        }
        
        owner.getProgressBar().setIndeterminate(true);
        owner.getProgressBar().setString("Scanning ThinkUp dir...");
        this.owner.getProgressBar().setStringPainted(true);

        testFiles.parseDirectory(config.getValue("thinkup_root_dir"));

        owner.getTestFileList().setListData(testFiles.getTestFiles().toArray());
        owner.getProgressBar().setIndeterminate(false);
        owner.getProgressBar().setString("Finished scanning ThinkUp dir.");
        this.owner.getProgressBar().setStringPainted(true);
        owner.getRunButton().setEnabled(true);
    }

}
