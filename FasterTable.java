package comp2402a3;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

/**
 *  This is just a copy of A2Table, your job is to make it faster
 */
public class FasterTable<T> implements Table<T> {
	List<List<T>> tab;
	List<Integer> tabValues;

	int nrows, ncols;

	public FasterTable(Class<T> t) {
		nrows = 0;
		ncols = 0;
		tabValues = new ArrayList<Integer>(); //keeps track of values in tab
		tab = new ArrayList<List<T>>();
	}

	public int rows() {
		return nrows;
	}

	public int cols() {
		return ncols;
	}

	public T get(int i, int j) {
		if (i < 0 || i > rows() - 1 || j < 0 || j > cols()-1)
			throw new IndexOutOfBoundsException();
			//return tab.get(i).get(j);
			return tab.get(i).get(tabValues.get(j));
	}

	public T set(int i, int j, T x) {
		if (i < 0 || i > rows() - 1 || j < 0 || j > cols()-1)
			throw new IndexOutOfBoundsException();
		//return tab.get(i).set(j, x);
		return tab.get(i).set(tabValues.get(j), x);
	}

	public void addRow(int i) {
		if (i < 0 || i > rows()) throw new IndexOutOfBoundsException();
		nrows++;
		List<T> row = new ArrayList<T>();
		for (int j = 0; j < cols(); j++) row.add(null);
		tab.add(i, row);
	}

	public void removeRow(int i) {
		if (i < 0 || i > rows() - 1) throw new IndexOutOfBoundsException();
		nrows--;
		tab.remove(i);
	}

	public void addCol(int j) {
		if (j < 0 || j > cols()) throw new IndexOutOfBoundsException();
		ncols++;

	  for (int i = 0; i < rows() ;i++) {
	  	tab.get(i).add(null);
	  }

  	tabValues.add(j,cols()-1);
	}

	public void removeCol(int j) {
        // this method is too slow!
		if (j < 0 || j > cols() - 1) throw new IndexOutOfBoundsException();
		ncols--;

	  for (int i = 0; i < rows(); i++) {
	  	tab.get(i).set(j,null);
	  }

	  tabValues.remove(j);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rows(); i++) {
			for (int j = 0; j < cols(); j++) {
				sb.append(String.valueOf(get(i, j)));
				sb.append(" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		Tester.testTable(new FasterTable<Integer>(Integer.class));

		System.out.println("\nRunning own test...");

		Table<Integer> t = new FasterTable<Integer>(Integer.class);

		System.out.println("\nAdding 4 rows...");
		t.addRow(0);	t.addRow(1);	t.addRow(2);	t.addRow(3);

		System.out.println("\nAdding 4 cols...\n");
		t.addCol(0);	t.addCol(1);	t.addCol(2);	t.addCol(0);

		//summary
		if (t.rows() == 4 && t.cols() == 4) {
			System.out.println("Success!");
		} else {
			System.out.println("Fail.");
		}
		System.out.println("Num of cols: " + t.cols());
		System.out.println("Num of rows: " + t.rows());

		System.out.println("\nRemoving 3 cols...");
		t.removeCol(2); 	t.removeCol(1); 	t.removeCol(0);

		System.out.println("\nRemoving 2 rows...\n");
		t.removeRow(1); 	t.removeRow(0);

		//summary
		if (t.rows() == 2 && t.cols() == 1) {
			System.out.println("Success!");
		} else {
			System.out.println("Fail.");
		}
		System.out.println("Num of cols: " + t.cols());
		System.out.println("Num of rows: " + t.rows());

	}
}
