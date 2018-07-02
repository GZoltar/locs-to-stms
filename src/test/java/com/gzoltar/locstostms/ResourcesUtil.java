package com.gzoltar.locstostms;

import java.io.File;

public final class ResourcesUtil {

  /**
   * Loads the file with the given name.
   * 
   * @param name of the file to load
   * @return a {@link java.io.File} object
   */
  public static File getFile(final String name) {
    return new File(ResourcesUtil.class.getClassLoader().getResource(name).getFile());
  }
}
