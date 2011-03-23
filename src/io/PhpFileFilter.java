package io;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Filters files, accepts .php files only.
 *
 * @author Sam Rose <samwho@lbak.co.uk>
 */
public class PhpFileFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        if (name.endsWith(".php") || !name.contains(".")) return true;
        return false;
    }

}
