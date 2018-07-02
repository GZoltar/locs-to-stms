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
package com.gzoltar.locstostms.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.gzoltar.locstostms.Command;

/**
 * The <code>locstostms</code> command.
 */
public class LocsToStms extends Command {

  @Argument(usage = "list of classes to parse, e.g., org.foo.Bar", metaVar = "<classes>",
      required = true)
  private List<String> classes = new ArrayList<String>();

  @Option(name = "--srcDirs", usage = "list of directories with .java files", metaVar = "<dir>",
      required = true)
  private List<File> srcDirs = new ArrayList<File>();

  @Option(name = "--outputFile",
      usage = "file to which the parser outcome will be written (default 'parser.txt')",
      metaVar = "<file>", required = false)
  private File outputFile = new File("locstostms.txt");

  private final Map<Integer, Set<Integer>> javaStatements = new HashMap<Integer, Set<Integer>>();

  /**
   * {@inheritDoc}
   */
  @Override
  public String description() {
    return "Parses java files and aggregates lines of code per Java statements.";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String name() {
    return "locstostms";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int execute(final PrintStream out, final PrintStream err) throws Exception {
    out.println("* " + this.description());

    this.outputFile.createNewFile();
    FileWriter fw = new FileWriter(this.outputFile.getAbsoluteFile(), false);
    BufferedWriter bw = new BufferedWriter(fw);

    for (String clazz : this.classes) {
      for (File srcDir : this.srcDirs) {
        String fileName = srcDir.getAbsolutePath() + System.getProperty("file.separator")
            + clazz.replace(".", System.getProperty("file.separator")) + ".java";
        File javaFile = new File(fileName);
        if (!javaFile.exists()) {
          continue;
        }
        out.println("* Parsing: " + fileName);

        this.javaStatements.clear();
        this.parse(javaFile);

        for (Integer statementNumber : this.javaStatements.keySet()) {
          for (Integer lineNumber : this.javaStatements.get(statementNumber)) {
            if (lineNumber.equals(statementNumber)) {
              // minor optimisation: if a line number and a statement number is equal, skip it
              continue;
            }

            bw.write(clazz.replace(".", "/") + ".java#" + statementNumber + ":"
                + clazz.replace(".", "/") + ".java#" + lineNumber + "\n");
          }
        }

        break;
      }
    }

    bw.close();
    return 0;
  }

  /**
   * Returns the list of directories to analyse.
   * 
   * @return a {@link java.util.List} object
   */
  public List<File> getSrcDirs() {
    return this.srcDirs;
  }

  /**
   * Returns the name of all classes to parse.
   * 
   * @return a {@link java.util.List} object
   */
  public List<String> getClasses() {
    return this.classes;
  }

  /**
   * Returns the file to which the outcome of the parse will be written.
   * 
   * @return a {@link java.io.File} object
   */
  public File getOutputFile() {
    return this.outputFile;
  }

  /**
   * Returns a map of all lines that compose a single Java statement, e.g., 2={2,3,4}.
   * 
   * @return a java.util.Map<Integer, java.util.Set<Integer>> object
   */
  public Map<Integer, Set<Integer>> getJavaStatements() {
    return this.javaStatements;
  }

  /**
   * Parses a java file.
   * 
   * @param javaFile the java file to parse
   * @throws Exception
   */
  protected void parse(final File javaFile) throws Exception {
    // creates an input stream for the file to be parsed
    InputStream in = new FileInputStream(javaFile);

    CompilationUnit compilationUnit;
    try {
      // parse the file
      compilationUnit = JavaParser.parse(in);
    } finally {
      in.close();
    }

    // explore tree
    this.explore(compilationUnit);
  }

  private void explore(final Node node) {
    // ignore everything related to comments
    if (node.getClass().getCanonicalName().startsWith("com.github.javaparser.ast.comments.")) {
      return;
    }
    if (node.getClass().getCanonicalName()
        .equals(EnumConstantDeclaration.class.getCanonicalName())) {
      return;
    }

    if (node.getChildrenNodes().isEmpty()) {
      Integer line_number = node.getParentNode().getBeginLine();

      // is it a statement?
      if (node.getClass().getCanonicalName().startsWith("com.github.javaparser.ast.stmt.")
          && node.getBeginLine() == node.getEndLine()) {
        line_number = node.getBeginLine();
      } else if (node.getParentNode().getBeginLine() == node.getParentNode().getEndLine()) {
        Node clone = node;
        Node parent = null;

        // to handle special cases: parameters, binary expressions, etc
        // search for the next 'Declaration' or 'Statement'
        while ((parent = clone.getParentNode()) != null) {
          if ((parent.getClass().getCanonicalName().startsWith("com.github.javaparser.ast.stmt."))
              || (parent.getClass().getCanonicalName()
                  .equals(VariableDeclarator.class.getCanonicalName()))
              || (parent.getClass().getCanonicalName().startsWith("com.github.javaparser.ast.body.")
                  && parent.getClass().getCanonicalName().endsWith("Declaration"))) {
            line_number = parent.getBeginLine();
            break;
          }

          clone = parent;
        }
      }

      Set<Integer> lines = null;

      if (this.javaStatements.containsKey(line_number)) {
        lines = this.javaStatements.get(line_number);
      } else {
        lines = new HashSet<Integer>();
        lines.add(line_number);
      }

      lines.add(node.getBeginLine());
      this.javaStatements.put(line_number, lines);
    } else {
      for (Node child : node.getChildrenNodes()) {
        this.explore(child);
      }
    }
  }
}
