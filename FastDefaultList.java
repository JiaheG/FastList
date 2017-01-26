package comp2402a3;
import java.lang.reflect.Array;
import java.lang.IllegalStateException;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Implements the List interface as a skiplist so that all the
 * standard operations take O(log n) time
 *
 * TODO: Modify this so that it creates a DefaultList, which is basically
 *       an infinitely long list whose values start out as null
 *
 */
public class FastDefaultList<T> extends AbstractList<T> {
	class Node {
		T x;
		Node[] next;
		int[] length;
		int j; //index of current node
		@SuppressWarnings("unchecked")
		public Node(T ix, int h, int j) {
			x = ix;
			next = (Node[])Array.newInstance(Node.class, h+1);
			length = new int[h+1];
			this.j = j;
		}
		public int height() {
			return next.length - 1;
		}
		/*
		public int getJ() {
			return this.j;
		}
		*/
		//in the case that j must be changed in the future
		public void setJ(int i) {
			this.j = i;
		}
	}

	/**
	 * This node sits on the left side of the skiplist
	 */
	protected Node sentinel;

	/**
	 * The maximum height of any element
	 */
	int h;

	/**
	 * A source of random numbers
	 */
	Random rand;

	public FastDefaultList() {
		sentinel = new Node(null, 32, 0);
		h = 0;
		rand = new Random(0);
	}

	/**
	 * Find the node that precedes list index i in the skiplist.
	 *
	 * @param x - the value to search for
	 * @return the predecessor of the node at index i or the final
	 * node if i exceeds size() - 1.
	 */
	protected Node findPred(int i) {
		Node u = sentinel;
		int r = h;
		int j = -1;   // index of the current node in list 0
		while (r >= 0) {
			while (u.next[r] != null && j + u.length[r] < i) {
				j += u.length[r];
				u = u.next[r];
			}
			r--;
		}
		return u; //now returns u and j (index)
	}

	public T get(int i) {
        // Hint: this is too restrictive any non-negative i is allowed
		//if (i < 0 || i > n-1) throw new IndexOutOfBoundsException();
		if (i > size()-1 || i < 0) throw new IndexOutOfBoundsException(); //can't have a negative index
		Node z = findPred(i);

		if (z.next[0] != null) {
			if (z.next[0].j == i) {
				return z.next[0].x; //found it
			}
		}
		return null;
	}

	public T set(int i, T x) {
        // Hint: this is too restrictive any non-negative i is allowed
		//if (i < 0 || i > n-1) throw new IndexOutOfBoundsException();
		if (i > size()-1 || i < 0) throw new IndexOutOfBoundsException(); //can't have a negative index
		Node z = findPred(i);	T replace = null;

		if (z.next[0] != null) {
			if (z.next[0].j == i) {
				replace = z.next[0].x;
				z.next[0].x = x;
				return replace;
			}
		}
		return null;
	}

	/**
	 * Insert a new node into the skiplist
	 * @param i the index of the new node
	 * @param w the node to insert
	 * @return the node u that precedes v in the skiplist
	 */
	protected Node add(int i, Node w) {
		Node u = sentinel;
		int k = w.height();
		int r = h;
		int j = -1; // index of u
		while (r >= 0) {
			while (u.next[r] != null && j+u.length[r] < i) {
				j += u.length[r];
				u = u.next[r];
			}
			u.length[r]++; // accounts for new node in list 0
			if (r <= k) {
				w.next[r] = u.next[r];
				u.next[r] = w;
				w.length[r] = u.length[r] - (i - j);
				u.length[r] = i - j;
			}
			r--;
		}
		return u;
	}

	/**
	 * Simulate repeatedly tossing a coin until it comes up tails.
	 * Note, this code will never generate a height greater than 32
	 * @return the number of coin tosses - 1
	 */
	protected int pickHeight() {
		int z = rand.nextInt();
		int k = 0;
		int m = 1;
		while ((z & m) != 0) {
			k++;
			m <<= 1;
		}
		return k;
	}

	public void add(int i, T x) {
        // Hint: bounds checking again!
		//if (i < 0 || i > n) throw new IndexOutOfBoundsException();
		if (i > size()-1 || i < 0) throw new IndexOutOfBoundsException(); //can't have a negative index

		Node w = new Node(x, pickHeight(), i); //so we can return j at findPred(i)
		if (w.height() > h)
			h = w.height();
		add(i, w);
	}

	public T remove(int i) {
        // Hint: bounds checking again!
		//if (i < 0 || i > n-1) throw new IndexOutOfBoundsException();
		if (i > size()-1 || i < 0) throw new IndexOutOfBoundsException(); //can't have a negative index

		T x = null;
		Node u = sentinel;
		int r = h;
		int j = -1; // index of node u
		while (r >= 0) {
			while (u.next[r] != null && j+u.length[r] < i) {
				j += u.length[r];
				u = u.next[r];
			}
			u.length[r]--;  // for the node we are removing
			if (j + u.length[r] + 1 == i && u.next[r] != null) {
				x = u.next[r].x;
				u.length[r] += u.next[r].length[r];
				u.next[r] = u.next[r].next[r];
				if (u == sentinel && u.next[r] == null)
					h--;
			}
			r--;
		}
		//reset
		u = sentinel;	r = 0;

		return x;
	}


	public int size() {
		return Integer.MAX_VALUE;
	}

	public String toString() {
        // This is just here to help you a bit with debugging
		StringBuilder sb = new StringBuilder();
			int i = -1;
			Node u = sentinel;
			while (u.next[0] != null) {
				i += u.length[0];
				u = u.next[0];
				sb.append(" " + i + "=>" + u.x);
			}
			return sb.toString();
	}

	public static void main(String[] args) {
		Tester.testDefaultList(new FastDefaultList<Integer>());
	}
}
