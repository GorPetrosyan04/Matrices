import java.io.*;
import java.util.*;
import java.util.Scanner;

public class PetroHh {
    public static void main(String[] args)throws Exception {
     // declare variables
        int cnt = args.length; // get no. of arguments
        String fname;
        if (cnt < 1) {
            System.out.printf("\n\tOOOPS Invalid No. of arguments!");
            return;
        } // end if
        // get input file name
        fname = args[0];
        System.out.printf("\n\t\tInput filename: %s", fname);

        Scanner scanner = new Scanner(new File(fname));

        //System.out.println("Enter the number of matrices:");
        int n = scanner.nextInt();

        int[] dims = new int[n + 1];

        //System.out.println("Enter the dimensions of each matrix:");
        for (int i = 0; i < n + 1; i++) {
            dims[i] = scanner.nextInt();
        }

        long start = System.nanoTime();
        int minMultRec = recMCM(1, dims.length - 1, dims);
        long end = System.nanoTime();
        System.out.println("Minimum number of multiplications (Recursive): " + minMultRec);
        System.out.println("Time taken for recursive MCM: " + (end - start) + " nanoseconds");

        start = System.nanoTime();
        int[][] M = new int[dims.length][dims.length];
        int[][] F = new int[dims.length][dims.length];
        int minMultDP = dpMCM(dims.length - 1, dims, M, F);
        end = System.nanoTime();
        System.out.printf("\nDynamic Programming Min MPY = %d", minMultDP);
        System.out.printf("    Exec Time:    %d ns\n", end - start);
        prtMCM(dims, M, F, dims.length);
        System.out.println("\nPrint MPY order is as follows:");
        showorder(F, 1, dims.length - 1);
        System.out.printf("\nAuthor: Gor Petrosyan Date: " + java.time.LocalDate.now());

        scanner.close();
    }

    private static int recMCM(int i, int j, int[] d) {
        if (i == j)
            return 0;

        int min = Integer.MAX_VALUE;

        for (int k = i; k < j; k++) {
            int tmp = recMCM(i, k, d) + recMCM(k + 1, j, d) + d[i - 1] * d[k] * d[j];
            if (tmp < min)
                min = tmp;
        }

        return min;
    }

    private static int dpMCM(int n, int[] d, int[][] M, int[][] F) {
        for (int i = 1; i <= n; i++) {
            M[i][i] = 0;
        }

        for (int diag = 1; diag < n; diag++) {
            for (int i = 1; i <= n - diag; i++) {
                int j = i + diag;
                M[i][j] = Integer.MAX_VALUE;
                for (int k = i; k < j; k++) {
                    int tmp = M[i][k] + M[k + 1][j] + d[i - 1] * d[k] * d[j];
                    if (tmp < M[i][j]) {
                        M[i][j] = tmp;
                        F[i][j] = k;
                    }
                }
            }
        }

        return M[1][n];
    }

    private static void prtMCM(int[] dims, int[][] M, int[][] F, int n) {
        System.out.println("\nM Matrix:");
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < n; j++) {
                System.out.print(M[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("\nF Matrix:");
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < n; j++) {
                System.out.print(F[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void showorder(int[][] F, int i, int j) {
        if (i == j) {
            System.out.print("A" + i + " ");
        } else {
            int k = F[i][j];
            System.out.print("(");
            showorder(F, i, k);
            System.out.print("*");
            showorder(F, k + 1, j);
            System.out.print(")");
        }
    }
}
