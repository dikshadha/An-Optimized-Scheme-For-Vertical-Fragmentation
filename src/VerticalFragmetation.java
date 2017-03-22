import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.io.File;

public class VerticalFragmetation {
	ECRUDElement[][] matrix;

	int[][] ASUM;
	int[][] ASM;
	int[][] AMM;
	int[][] ARM;
	int[][] tree;
	int[][] MAMM;
	int[][] MARM;
	int numberOfApplications, numberOfSites, numberOfAttributes, numberOfClusters;

	LinkedList<LinkedList<Integer>> clusters;

	// constructor
	public VerticalFragmetation() throws IOException {
		createECRUDMatrix();
		constructASUM();
		constructASM();
		tree = ModifiedPrims.prims(ASM, numberOfClusters);
		constructAMM();
		constructARM();
		clusters = DFS.applyDFS(tree);
		System.out.println("These are the clusters that are formed after Prims : ");
		for (LinkedList<Integer> element : clusters) {
			for (Integer i : element) {
				System.out.print(i + " ");
			}
			System.out.println();
		}
		constructMAMM();
		constructMARM();
		allocate();
	}

	public void allocate() {
		int allocationTable[] = new int[numberOfClusters];
		for (int i = 0; i < numberOfClusters; i++) {
			int maxAlloc = 0, siteAlloc = 0;

			for (int j = 0; j < numberOfSites; j++) {
				if (maxAlloc < MAMM[j][i]) {
					maxAlloc = MAMM[j][i];
					siteAlloc = j;
				}
			}
			allocationTable[i] = siteAlloc;
			System.out.println("Fragment " + i + " is assigned to site " + siteAlloc);
		}
		for (int i = 0; i < numberOfClusters; i++) {
			int maxReplicate = 0, siteReplicate = 0;

			for (int j = 0; j < numberOfSites; j++) {
				if (maxReplicate < MARM[j][i] && allocationTable[i] != j) {
					maxReplicate = MARM[j][i];
					siteReplicate = j;
				}
			}
			System.out.println("Fragment " + i + " is Replicated on site " + siteReplicate);
		}

	}

	public void constructMAMM() {
		MAMM = new int[numberOfSites][numberOfClusters];
		for (int i = 0; i < numberOfSites; i++) {
			for (int j = 0; j < numberOfClusters; j++) {
				LinkedList<Integer> templist = clusters.get(j);
				for (Integer x : templist) {
					MAMM[i][j] += AMM[i][x.intValue()];
				}
			}
		}
		System.out.println("The MAMM is as follows:");
		for (int i = 0; i < numberOfSites; i++) {
			for (int j = 0; j < numberOfClusters; j++) {
				System.out.print(MAMM[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void constructMARM() {
		MARM = new int[numberOfSites][numberOfClusters];
		for (int i = 0; i < numberOfSites; i++) {
			for (int j = 0; j < numberOfClusters; j++) {
				LinkedList<Integer> templist = clusters.get(j);
				for (Integer x : templist) {
					MARM[i][j] += ARM[i][x.intValue()];
				}
			}
		}
		System.out.println("The MARM is as follows:");
		for (int i = 0; i < numberOfSites; i++) {
			for (int j = 0; j < numberOfClusters; j++) {
				System.out.print(MARM[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void constructASUM() {
		ASUM = new int[numberOfSites][numberOfAttributes];
		for (int i = 0; i < numberOfSites; i++) {
			for (int j = 0; j < numberOfAttributes; j++) {
				boolean flag = false;
				for (int k = 0; k < numberOfApplications; k++) {
					String current = matrix[j][i].element[k].toString();
					if (current.charAt(0) != 'N') {
						flag = true;
					}
				}
				if (flag)
					ASUM[i][j] = 1;
				else
					ASUM[i][j] = 0;
			}
		}
		System.out.println("The ASUM is as follows:");
		for (int i = 0; i < numberOfSites; i++) {
			for (int j = 0; j < numberOfAttributes; j++) {
				System.out.print(ASUM[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void constructASM() {
		ASM = new int[numberOfAttributes][numberOfAttributes];
		for (int i = 0; i < numberOfAttributes; i++) {
			for (int j = 0; j < numberOfAttributes; j++) {
				ASM[i][j] = 0;

				for (int k = 0; i != j && k < numberOfSites; k++) {
					ASM[i][j] += ASUM[k][i] * ASUM[k][j];
				}
			}
		}
		System.out.println("\nThe ASM is as follows:");
		for (int i = 0; i < numberOfAttributes; i++) {
			for (int j = 0; j < numberOfAttributes; j++) {
				System.out.print(ASM[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void constructAMM() {
		AMM = new int[numberOfSites][numberOfAttributes];
		for (int i = 0; i < numberOfAttributes; i++) {
			for (int j = 0; j < numberOfSites; j++) {
				AMM[j][i] = matrix[i][j].getManipulateValue();
			}
		}
		System.out.println("\nThe AMM is as follows:");
		for (int i = 0; i < numberOfSites; i++) {
			for (int j = 0; j < numberOfAttributes; j++) {
				System.out.print(AMM[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void constructARM() {
		ARM = new int[numberOfSites][numberOfAttributes];
		for (int i = 0; i < numberOfAttributes; i++) {
			for (int j = 0; j < numberOfSites; j++) {
				ARM[j][i] = matrix[i][j].getReadValue();
			}
		}
		System.out.println("\nThe ARM is as follows:");
		for (int i = 0; i < numberOfSites; i++) {
			for (int j = 0; j < numberOfAttributes; j++) {
				System.out.print(ARM[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void createECRUDMatrix() throws IOException {
		BufferedReader brconsole = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter the input file name : ");
		String fileName = brconsole.readLine();
		
		File file = new File(fileName);		
		if (!file.exists()) {
			System.err.println("File Not found");
			System.exit(-1);
		}

		// reading data from file.
		FileInputStream fis = new FileInputStream(fileName);
		BufferedReader brfile = new BufferedReader(new InputStreamReader(fis));

		numberOfAttributes = Integer.parseInt(brfile.readLine());
		numberOfSites = Integer.parseInt(brfile.readLine());
		numberOfApplications = Integer.parseInt(brfile.readLine());
		numberOfClusters = Integer.parseInt(brfile.readLine());

		// initializing the matrix
		matrix = new ECRUDElement[numberOfAttributes][numberOfSites];

		for (int i = 0; i < numberOfAttributes; i++) {
			String s = brfile.readLine();
			StringTokenizer stz = new StringTokenizer(s, ",");
			if (stz.countTokens() == numberOfSites * numberOfApplications) {
				for (int j = 0; j < numberOfSites; j++) {
					matrix[i][j] = new ECRUDElement(numberOfApplications);

					for (int k = 0; k < numberOfApplications; k++) {
						String current = stz.nextToken();
						for (int l = 0; l < current.length(); l++) {
							if (current.charAt(l) == 'C') {
								matrix[i][j].element[k].c = true;
							} else if (current.charAt(l) == 'R') {
								matrix[i][j].element[k].r = true;
							} else if (current.charAt(l) == 'U') {
								matrix[i][j].element[k].u = true;
							} else if (current.charAt(l) == 'D') {
								matrix[i][j].element[k].d = true;
							}
						}
					}
				}

			} else {
				System.out.println("there is some error in input file");
			}
		}

		fis.close();
		brconsole.close();
		brfile.close();

	}
}
