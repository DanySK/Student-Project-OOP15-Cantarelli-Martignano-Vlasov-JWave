package org.jwave.controller.player;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Class FileSystemHandler
 */
public final class FileSystemHandler {
   
    private static final FileSystemHandler SINGLETON = new FileSystemHandler();
  
    private FileSystemHandler() { };
  

  /**
   * @return       String
   * @param        absolutePath
   */
    public String sketchPath(final String absolutePath) {
      return absolutePath;
    }


 
    public InputStream createInput(final String absoluteFileName) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(new File(absoluteFileName)));
    }

    public static FileSystemHandler getFileSystemHandler() {
        return SINGLETON;
    }
}
