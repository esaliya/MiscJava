package org.saliya.reu2014;
import java.util.Scanner;
import java.util.ArrayList;
public class KMeans {
    public static void main(String[] args) {
        //The Points
        Scanner point = new Scanner(System.in);
        int N = point.nextInt();
        int m = point.nextInt();
        double[][] points = new double[N][m];
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < m; y++) {
                points[x][y] = Math.random();
            }
        }
        //The Centers
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < m; y++) {
                System.out.print(points[x][y] + " ");
            }
            System.out.println();
        }
        System.out.println();
        int K = point.nextInt();
        double[][] centers = new double[K][m];
        for (int x = 0; x < K; x++) {
            for (int y = 0; y < m; y++) {
                centers[x][y] = Math.random();
            }
        }
        for (int x = 0; x < K; x++) {
            for (int y = 0; y < m; y++) {
                System.out.print(centers[x][y]);
            }
            System.out.println();
        }
        System.out.println();

        /* This has to be defined up here. If you put this line
           inside a loop then for each iteration you are creating
           a new set of array lists, which is not what we want
        */
        ArrayList[] pointIdsForCenters = new ArrayList[K];
        //The Distance
        double a = Double.MAX_VALUE;
        int b = 0;
        double distance = 0.0;
        for (int x = 0; x < N; x++) {
            for (int z = 0; z < K; z++) {
                for (int y = 0; y < m; y++) {
                    distance += Math.pow(points[x][y] - centers[z][y], 2);
                }
                System.out.println(distance);
                System.out.println(Math.sqrt(distance));
                if (distance < a) {
                    a = distance;
                    b = z;
                }
                distance = 0.0;
                System.out.println(a);
                System.out.println(b);
                System.out.println();
            }
            //Holding The Distance
            pointIdsForCenters[b].add(x);
            /* You can't directly print the contents of an ArrayList.
               If you need to do that you'll have to write a loop that
               goes through the array and print items. See the for loop
               I've written below
             */
            /*System.out.println(pointIdsForCenters[b]);*/
            for (int i = 0; i < pointIdsForCenters[b].size(); ++i){
                System.out.print(pointIdsForCenters[b].get(i) + " ");
            }
            System.out.println();
        }
        //Adding the new centers
        double[][] updatedCenters = new double[K][m];
        for (int x = 0; x < K; x++) {
            for (int y = 0; y < m; y++) {
                /*updatedCenters[x][y] represents a double value, so if you want to assign something
                  to it, then it has to be a double value. However, pointIdsForCenters[y] produces an array list,
                  so this assignment cannot be done.
                 */
                /*updatedCenters[x][y] = pointIdsForCenters[y];*/
            }
        }
    }
}
