# Java lines of code to Java statements

`locs-to-stms` is an utility Java program that parses java files to extract
the set of lines of code that are part of the same Java statement.

Considering the following snippet of code from
[JFreeChart](http://www.jfree.org/jfreechart/), class
`org.jfree.chart.renderer.category.LineAndShapeRenderer`:

```java
762.  if (this.useSeriesOffset) {
763.      x0 = domainAxis.getCategorySeriesMiddle(
764.              column - 1, dataset.getColumnCount(),
765.              visibleRow, visibleRowCount,
766.              this.itemMargin, dataArea,
767.              plot.getDomainAxisEdge());
768.  }
```

It can be seen there are 6 lines of code, but only 2 statements (as lines
763-767 are part of the same statement). So, `locs-to-stms` reads a list of
java files and prints to a file, information of each java statement. Each row
of the file contains two columns: First column represents a statement, and
the second column represents a line of code that is part of the statement.
For example:

```
...
org/jfree/chart/renderer/category/LineAndShapeRenderer.java#763:org/jfree/chart/renderer/category/LineAndShapeRenderer.java#764
org/jfree/chart/renderer/category/LineAndShapeRenderer.java#763:org/jfree/chart/renderer/category/LineAndShapeRenderer.java#765
org/jfree/chart/renderer/category/LineAndShapeRenderer.java#763:org/jfree/chart/renderer/category/LineAndShapeRenderer.java#766
org/jfree/chart/renderer/category/LineAndShapeRenderer.java#763:org/jfree/chart/renderer/category/LineAndShapeRenderer.java#767
...
```

I.e., lines #764, #765, #766, #767 are part of statement #763.


### How to compile it?

```
mvn clean package
```

### How to use it?


```
java -jar locs-to-stms-<version>-jar-with-dependencies.jar locstostms \
  <classes> ...
  --srcDirs <dir>
  [--outputFile <file>]
```

Where `<classes>` is the list of classes to parse, `--srcDirs` is the source
directory (more than one can be defined), `--outputFile` is the file to which
the output of `locs-to-stms` is written.

For example:

```
java -jar locs-to-stms-0.0.1-jar-with-dependencies.jar locstostms
  org.jfree.chart.renderer.category.LineAndShapeRenderer \
  --srcDirs src/main/java \
  --outputFile locstostms.txt
```
