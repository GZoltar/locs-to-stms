package com.gzoltar.locstostms;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;

/**
 * Entry point for all command line operations.
 */
public class Main extends Command {

  /**
   * Entry point of any execution
   * 
   * @param args
   */
  public static void main(final String[] args) throws Exception {
    final PrintStream out = new PrintStream(System.out, true);
    final PrintStream err = new PrintStream(System.err, true);
    final int returncode = new Main(args).execute(out, err);
    System.exit(returncode);
  }

  private final String[] args;

  private Main(final String[] args) {
    this.args = args;
  }

  @Argument(handler = CommandHandler.class, required = true)
  private Command command;

  /**
   * {@inheritDoc}
   */
  @Override
  public String description() {
    return "Command line interface for locs-to-stms.";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String usage(final CommandParser parser) {
    return JAVACMD + "--help | <command>";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int execute(PrintStream out, final PrintStream err) throws Exception {

    final CommandParser mainParser = new CommandParser(this);
    try {
      mainParser.parseArgument(this.args);
    } catch (final CmdLineException e) {
      ((CommandParser) e.getParser()).getCommand().printHelp(err);
      err.println();
      err.println(e.getMessage());
      return -1;
    }

    if (this.help) {
      printHelp(out);
      return 0;
    }

    if (this.command.help) {
      this.command.printHelp(out);
      return 0;
    }

    if (this.command.quiet) {
      out = NUL;
    }

    return this.command.execute(out, err);
  }

  private static final PrintStream NUL = new PrintStream(new OutputStream() {
    @Override
    public void write(int b) throws IOException {
      // no-op
    }

    @Override
    public void write(byte[] b) throws IOException {
      // no-op
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
      // no-op
    }

    @Override
    public void flush() throws IOException {
      // no-op
    }

    @Override
    public void close() throws IOException {
      // no-op
    }
  });
}
