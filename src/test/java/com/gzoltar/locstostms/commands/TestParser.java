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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import com.gzoltar.locstostms.ResourcesUtil;

public class TestParser {

  private Map<Integer, Set<Integer>> run(final String fileName) throws Exception {
    File javaFile = ResourcesUtil.getFile(fileName);

    LocsToStms parser = new LocsToStms();
    parser.parse(javaFile);

    return parser.getJavaStatements();
  }

  @Test
  public void testClassDefinition() throws Exception {
    Map<Integer, Set<Integer>> stms = this.run("examples/ClassDefinition.java");
    assertEquals(1, stms.size());

    Set<Integer> stms_of_line_1 = stms.get(1);
    assertEquals(2, stms_of_line_1.size());
    assertTrue(stms_of_line_1.contains(1));
    assertTrue(stms_of_line_1.contains(2));
  }

  @Test
  public void testFields() throws Exception {
    Map<Integer, Set<Integer>> stms = this.run("examples/Fields.java");
    assertEquals(3, stms.size());

    Set<Integer> stms_of_line_2 = stms.get(2);
    assertEquals(4, stms_of_line_2.size());
    assertTrue(stms_of_line_2.contains(2));
    assertTrue(stms_of_line_2.contains(3));
    assertTrue(stms_of_line_2.contains(4));
    assertTrue(stms_of_line_2.contains(5));

    Set<Integer> stms_of_line_8 = stms.get(8);
    assertEquals(1, stms_of_line_8.size());
    assertTrue(stms_of_line_8.contains(8));

    Set<Integer> stms_of_line_10 = stms.get(10);
    assertEquals(2, stms_of_line_10.size());
    assertTrue(stms_of_line_10.contains(10));
    assertTrue(stms_of_line_10.contains(11));
  }

  @Test
  public void testMethodArguments() throws Exception {
    Map<Integer, Set<Integer>> stms = this.run("examples/MethodArguments.java");
    assertEquals(1, stms.size());

    Set<Integer> stms_of_line_2 = stms.get(2);
    assertEquals(2, stms_of_line_2.size());
    assertTrue(stms_of_line_2.contains(2));
    assertTrue(stms_of_line_2.contains(3));
  }

  @Test
  public void testIfCondition() throws Exception {
    Map<Integer, Set<Integer>> stms = this.run("examples/IfCondition.java");
    assertEquals(4, stms.size());

    Set<Integer> stms_of_line_3 = stms.get(3);
    assertEquals(2, stms_of_line_3.size());
    assertTrue(stms_of_line_3.contains(3));
    assertTrue(stms_of_line_3.contains(4));
  }

  @Test
  public void testForLoop() throws Exception {
    Map<Integer, Set<Integer>> stms = this.run("examples/ForLoop.java");
    assertEquals(2, stms.size());

    Set<Integer> stms_of_line_3 = stms.get(3);
    assertEquals(3, stms_of_line_3.size());
    assertTrue(stms_of_line_3.contains(3));
    assertTrue(stms_of_line_3.contains(4));
    assertTrue(stms_of_line_3.contains(5));
  }

  @Test
  public void testWhileLoop() throws Exception {
    Map<Integer, Set<Integer>> stms = this.run("examples/WhileLoop.java");
    assertEquals(2, stms.size());

    Set<Integer> stms_of_line_3 = stms.get(3);
    assertEquals(2, stms_of_line_3.size());
    assertTrue(stms_of_line_3.contains(3));
    assertTrue(stms_of_line_3.contains(4));
  }

  @Test
  public void testComments() throws Exception {
    Map<Integer, Set<Integer>> stms = this.run("examples/Comments.java");
    assertEquals(0, stms.size());
  }

  @Test
  public void testEnumDeclaration() throws Exception {
    Map<Integer, Set<Integer>> stms = this.run("examples/EnumDeclaration.java");
    assertEquals(4, stms.size());

    Set<Integer> stms_of_line_12 = stms.get(12);
    assertEquals(2, stms_of_line_12.size());
    assertTrue(stms_of_line_12.contains(12));
    assertTrue(stms_of_line_12.contains(13));

    Set<Integer> stms_of_line_14 = stms.get(14);
    assertEquals(2, stms_of_line_14.size());
    assertTrue(stms_of_line_14.contains(14));
    assertTrue(stms_of_line_14.contains(15));

    Set<Integer> stms_of_line_16 = stms.get(16);
    assertEquals(1, stms_of_line_16.size());
    assertTrue(stms_of_line_16.contains(16));

    Set<Integer> stms_of_line_18 = stms.get(18);
    assertEquals(1, stms_of_line_18.size());
    assertTrue(stms_of_line_18.contains(18));
  }

  @Test
  public void testAnnotations() throws Exception {
    Map<Integer, Set<Integer>> stms = this.run("examples/Annotation.java");
    assertEquals(2, stms.size());

    Set<Integer> stms_of_line_3 = stms.get(3);
    assertEquals(2, stms_of_line_3.size());
    assertTrue(stms_of_line_3.contains(3));
    assertTrue(stms_of_line_3.contains(4));

    Set<Integer> stms_of_line_5 = stms.get(5);
    assertEquals(1, stms_of_line_5.size());
    assertTrue(stms_of_line_5.contains(5));
  }
}
