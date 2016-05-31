package org.jwave.controller.player;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * A FileSystemHandler contains some basic methods needed by the system.
 */
public final class FileSystemHandler {
   
    private static final FileSystemHandler SINGLETON = new FileSystemHandler();
  
    private FileSystemHandler() { };
  

  /**
   * @return       String
   *            
   * @param        absolutePath
   *            the absolute path if the file.
   */
    public String sketchPath(final String absolutePath) {
      return absolutePath;
    }


    /**
     * Creates a stream from the given file.
     * 
     * @param absoluteFileName
     *          file name.
     * @return
     *          {@link}InputStream
     *          
     * @throws FileNotFoundException
     *          if the file can't be found in the file system.
     */
    public InputStream createInput(final String absoluteFileName) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(new File(absoluteFileName)));
    }

    /**
     * 
     * @return
     *          the singleton instance of the file system handler.
     */
    public static FileSystemHandler getFileSystemHandler() {
        return SINGLETON;
    }
}
