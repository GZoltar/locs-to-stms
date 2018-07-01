package com.gzoltar.locstostms.commands;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import com.gzoltar.locstostms.Command;

/**
 * The <code>version</code> command.
 */
public class Version extends Command {

  @Override
  public String description() {
    return "Print locstostms version information.";
  }

  @Override
  public int execute(final PrintStream out, final PrintStream err) throws IOException {
    // Find the path of the compiled class
    String classPath =
        Version.class.getResource(Version.class.getSimpleName() + ".class").toString();

    // Find the path of the lib which includes the class
    String libPath = classPath.substring(0, classPath.lastIndexOf("!"));

    // Find the path of the file inside the lib jar
    String filePath = libPath + "!/META-INF/MANIFEST.MF";

    // Look at the manifest file and get the 'Implementation-Version' attribute
    Manifest manifest = new Manifest(new URL(filePath).openStream());
    Attributes attr = manifest.getMainAttributes();
    out.println(attr.getValue("Implementation-Version"));

    return 0;
  }
}
