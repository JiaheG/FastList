package comp2402a3;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Tester {

  // Handy for testing correctness now that we know A2Table works
  public static <T> boolean tableEquals(Table<T> t1, Table<T> t2) {
    if (t1.rows() != t2.rows()) return false;
    if (t1.cols() != t2.cols()) return false;
    for (int i = 0; i < t1.rows(); i++) {
      for (int j = 0; j < t2.cols(); j++) {
        T x1 = t1.get(i, j);
        T x2 = t2.get(i, j);
        if (x1 != null && x2 == null) return false;
        if (x1 == null && x2 != null) return false;
        if (x1 != null && !x1.equals(x2)) return false;
      }
    }
    return true;
  }


  public static boolean testPart1(Table<Integer> t) {
    t = new FasterTable<Integer>(Integer.class);

		t.addRow(0);	t.addRow(1);	t.addRow(2);	t.addRow(3);
		t.addCol(0);	t.addCol(1);	t.addCol(2);	t.addCol(0);

		t.removeCol(2); 	t.removeCol(1); 	t.removeCol(0);
		t.removeRow(1); 	t.removeRow(0);

    if (t.cols() == 1 && t.rows() == 2)
      return true;
    else return false;
  }

  public static void testTable(Table<Integer> tab) {
    long start = System.nanoTime();
    boolean result = Tester.testPart1(tab);
    long stop = System.nanoTime();
    double elapsed = (stop-start)/1e9;
    System.out.println("testPart1 returns " + result + " in " + elapsed + "s"
                       + " when testing a " + tab.getClass().getName());
  }


  public static boolean testPart2(List<Integer> ell) {
    for (int i = 0; i < 10000; i++) {
      ell.add(i, i*2);
        if (ell.get(i) != i*2) {
          return false;
        }
    }

    for (int i = 0; i < 10000; i++) {
      if (ell.get(i) != 0 && i%ell.get(i) == 0) {
        ell.remove(i);
        if (ell.get(i) != null) {
          return false;
        }
      }
    }

    for (int i = 0; i < 10000; i++) {
      ell.set(i, i*3);
      if (ell.get(i) != i*3) {
        return false;
      }
    }

    for (int i = 0; i < 10000; i++) {
      ell.remove(0);
    }
    for (int i = 0; i < 10000; i++) {
      if (ell.get(i) != null) {
        return false;
      }
    }
    return true;
  }

  public static void testDefaultList(List<Integer> ell) {
    long start = System.nanoTime();
    boolean result = Tester.testPart2(ell);
    long stop = System.nanoTime();
    double elapsed = (stop-start)/1e9;
    System.out.println("testPart1 returns " + result + " in " + elapsed + "s"
                       + " when testing a " + ell.getClass().getName());
  }

}
