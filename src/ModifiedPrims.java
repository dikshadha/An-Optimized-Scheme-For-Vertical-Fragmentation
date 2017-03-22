import java.util.Arrays;

public class ModifiedPrims {
	static int key[];
	static int adjacencyMatrix[][];
	static int numberOfVertices;
	static boolean isVisited[];
	static int parent[];

	public static int extractMax() {
		int max = 0, index = 0;
		for (int i = 0; i < numberOfVertices; i++) {
			if (!isVisited[i]) {
				if (max < key[i]) {
					max = key[i];
					index = i;
				}
			}
		}
		return index;
	}

	public static int[][] prims(int[][] adjacencyMatrix, int numberOfClusters) {
		ModifiedPrims.adjacencyMatrix = adjacencyMatrix;
		int[][] tree = new int[adjacencyMatrix.length][adjacencyMatrix.length];
		numberOfVertices = adjacencyMatrix.length;
		isVisited = new boolean[numberOfVertices];
		key = new int[numberOfVertices];
		parent = new int[numberOfVertices];

		isVisited[0] = true;
		parent[0] = -1;
		key[0] = 0;

		for (int i = 0; i < numberOfVertices - 1; i++) {
			int index = extractMax();
			for (int j = 0; j < numberOfVertices; j++) {
				if (adjacencyMatrix[index][j] != 0) {
					if (!isVisited[j] && adjacencyMatrix[index][j] > key[j]) {
						key[j] = adjacencyMatrix[index][j];
						parent[j] = index;
					}
				}
			}
			isVisited[index] = true;
		}
		for (int i = 0; i < numberOfVertices; i++)
			System.out.println(i + "->" + parent[i]);
		int[] tempArray = new int[numberOfVertices - 1];
		int counter = 0;
		for (int i = 0; i < numberOfVertices; i++) {
			if (parent[i] != -1) {
				tree[i][parent[i]] = adjacencyMatrix[i][parent[i]];
				tree[parent[i]][i] = adjacencyMatrix[i][parent[i]];
				tempArray[counter++] = adjacencyMatrix[i][parent[i]];
			}
		}
		Arrays.sort(tempArray);
		System.out.println("The old Maximum Spanning Tree is as follows : ");
		for (int i = 0; i < numberOfVertices; i++) {
			for (int j = 0; j < numberOfVertices; j++)
				System.out.print(tree[i][j] + " ");
			System.out.println();
		}
		for (int x = 0; x < numberOfClusters - 1; x++) {
			outerloop: for (int i = 0; i < numberOfVertices; i++) {
				for (int j = 0; j < numberOfVertices; j++) {
					if (tempArray[x] == tree[i][j]) {
						tree[i][j] = 0;
						tree[j][i] = 0;
						break outerloop;
					}
				}
			}
		}
		System.out.println("The new Maximum Spanning Tree is as follows : ");
		for (int i = 0; i < numberOfVertices; i++) {
			for (int j = 0; j < numberOfVertices; j++)
				System.out.print(tree[i][j] + " ");
			System.out.println();
		}

		return tree;
	}
}
