/**
 * Copyright (C) 2018 José Campos and locs-to-stm contributors.
 * 
 * This file is part of locs-to-stm.
 * 
 * locs-to-stm is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * locs-to-stm is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with locs-to-stm.
 * If not, see <https://www.gnu.org/licenses/>.
 */
package com.gzoltar.locstostms;

import org.kohsuke.args4j.CmdLineParser;

/**
 * Parser which remembers the parsed command to have additional context information to produce help
 * output.
 */
public class CommandParser extends CmdLineParser {

  private final Command command;

  public CommandParser(final Command command) {
    super(command);
    this.command = command;
  }

  public Command getCommand() {
    return command;
  }
}
