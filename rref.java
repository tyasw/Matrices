/*
 * rref.java
 * Created by: William Tyas
 * Date: 9/3/17
 * Description: Returns the reduced row echelon form of a matrix
 */
import java.util.*;

public class rref {
	public static int width;
	public static int height;
 
	public static void main(String[] args) {
		System.out.println("RREF: Calculate rref of a matrix");
		System.out.println("--------------------------------");
		float[][] matrix = readMatrix();
		System.out.println("Before:");
		print(matrix);
		rref(matrix);
		System.out.println("rref is:");
		print(matrix);
	}

	public static float[][] readMatrix() {
		Scanner input = new Scanner(System.in);
		boolean valid = false;
		while (!valid) {
			System.out.print("Height: ");
			height = input.nextInt();
			System.out.print("Width: ");
			width = input.nextInt();
			if (height <= width) {
				valid = true;
			} else {
				System.out.println("Height cannot be greater than width.");
				System.out.println("Please enter a new height and width.");
			}
		}
		float[][] matrix = new float[height][width];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				System.out.print("[" + i + "]" + "[" + j + "]" + ": ");
				matrix[i][j] = input.nextFloat();	
			}
		}
		return matrix;
	}

	public static void rref(float[][] matrix) {
		// Forward phase - results in reduced echelon form
		for (int i = 0; i < matrix.length; i++) {
			// Find next leftmost nonzero column from rows below i
			int colIndex = nonZeroCol(matrix, i, matrix.length - 1);
			if (colIndex >= 0.0f) {
				// Bring nonzero entry to top of column
				if (matrix[i][colIndex] == 0.0f) {
					int j = nextNonZeroRow(matrix, i, colIndex);
					rowSwap(matrix, i, j);
				}
				float pivot = matrix[i][colIndex];

				// Multiply row to make pivot a 1
				if (pivot != 1.0f && !closeEnough(pivot, 0.0f)) {
					pivot = 1.0f / matrix[i][colIndex];
					for (int k = 0; k < matrix[0].length; k++) {
						float newValue = matrix[i][k] * pivot;
						if (closeEnough(newValue, Math.round(newValue))) {
							matrix[i][k] = Math.round(newValue);
						} else {
							matrix[i][k] = newValue;
						}
					}
				}

				// Add multiples of row to others below
				for (int k = (i + 1); k < matrix.length; k++) {
					float value = matrix[k][colIndex];
					if (value != 0.0f) {
						float first = matrix[k][colIndex];
						for (int el = 0; el < (matrix[0].length); el++) {
							float newValue = -first * matrix[i][el] + matrix[k][el];
							if (closeEnough(newValue, Math.round(newValue))) {
								matrix[k][el] = Math.round(newValue);
							} else {
								matrix[k][el] = newValue;
							}
						}
					}
				}
			}
		}

		// Backward phase - Add multiples of row to others above
		// Results in rref
		int lastNonZeroRow = lastNonZeroRow(matrix);
		for (int k = lastNonZeroRow; k > 0; k--) {
			int firstNonZeroCol = nonZeroCol(matrix, k, k); 
			for (int i = 0; i < k; i++) {
				float value = matrix[i][firstNonZeroCol];
				if (value != 0.0f) {
					float first = matrix[i][firstNonZeroCol];
					for (int el = 0; el < (matrix[0].length); el++) {
						float newValue = -first * matrix[k][el] + matrix[i][el];
						if (closeEnough(newValue, Math.round(newValue))) {
							matrix[i][el] = Math.round(newValue);
						} else {
							matrix[i][el] = newValue;
						}
					}
				}
			}
		}
	}

	public static boolean closeEnough(float pivot, float limit) {
		return (Math.abs(limit - pivot) < 0.001f);
	}

	// Finds the next nonzero column in a matrix between minRow and maxRow
	public static int nonZeroCol(float[][] matrix, int minRow, int maxRow) {
		for (int i = 0; i < matrix[0].length; i++) {
			for (int j = minRow; j <= maxRow; j++) {
				if (matrix[j][i] != 0.0f) {
					return i;
				}
			}
		}
		return -1;
	}

	// Finds next row below rowIndex with nonzero entry in column colIndex
	public static int nextNonZeroRow(float[][] matrix, int rowIndex, int colIndex) {
		for (int i = (rowIndex + 1); i < matrix.length; i++) {
			if (matrix[i][colIndex] != 0.0f) {
				return i;
			}
		}
		return -1;
	}

	// Finds the last nonzero row in a matrix
	public static int lastNonZeroRow(float[][] matrix) {
		boolean zeroRow = true;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] != 0.0f) {
					zeroRow = false;
				}
			}
			if (zeroRow == true) {
				return (i - 1);
			} else if (i == (matrix.length - 1)) { // last row
				return i;
			}
			zeroRow = true;
		}
		return -1;
	}

	public static void rowSwap(float[][] matrix, int first, int second) {
		for (int i = 0; i < matrix[0].length; i++) {
			float tmp = matrix[first][i];
			matrix[first][i] = matrix[second][i];
			matrix[second][i] = tmp;
		}
	}

	public static void print(float[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			System.out.print("[");
			for (int j = 0; j < (matrix[0].length - 1); j++) {
				System.out.print(matrix[i][j] + "\t");
			}
			System.out.print(matrix[i][matrix[0].length - 1]);
			System.out.print("]\n");
		}
		System.out.println();
	}
}
