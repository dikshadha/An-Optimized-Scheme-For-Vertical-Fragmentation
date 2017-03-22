/**
 * @author vivek(16CS07F) and sourabh(16CS21F)
 */

import java.util.LinkedList;

public class DFS {
	static LinkedList<LinkedList<Integer>> result;
	static boolean visited[];
	static int numberOfVertices;
	static int tree[][];

	public static LinkedList<LinkedList<Integer>> applyDFS(int[][] tree) {
		DFS.tree = tree;
		numberOfVertices = tree.length;
		result = new LinkedList<LinkedList<Integer>>();
		visited = new boolean[numberOfVertices];

		for (int i = 0; i < numberOfVertices; i++) {
			if (!visited[i]) {
				result.add(new LinkedList<Integer>());
				(result.getLast()).add(new Integer(i));
				dfsUtil(i);
			}
		}
		return result;
	}

	public static void dfsUtil(int vertex) {
		visited[vertex] = true;
		for (int i = 0; i < numberOfVertices; i++) {
			if (tree[vertex][i] != 0 && !visited[i]) {
				(result.getLast()).add(new Integer(i));
				dfsUtil(i);
			}
		}
	}
}
