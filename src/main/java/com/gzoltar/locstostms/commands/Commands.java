package com.gzoltar.locstostms.commands;

import java.util.ArrayList;
import java.util.List;
import com.gzoltar.locstostms.Command;

/**
 * List of all available commands.
 */
public final class Commands {

  private Commands() {
    // no-op
  }

  /**
   * @return list of new instances of all available commands
   */
  public static List<Command> get() {
    List<Command> commands = new ArrayList<Command>();
    commands.add(new Version());
    return commands;
  }

  /**
   * @return String containing all available command names
   */
  public static String names() {
    final StringBuilder sb = new StringBuilder();
    for (final Command c : get()) {
      if (sb.length() > 0) {
        sb.append('|');
      }
      sb.append(c.name());
    }
    return sb.toString();
  }
}
