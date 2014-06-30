package org.saliya.reu2014;

import java.util.Scanner;
import java.util.ArrayList;

public class KMeans2 {
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
        double sum = 0;
        for (int x = 0; x < centers.length; x++) {
            for (int y = 0; y < centers[0].length; y++) {
                sum = sum + centers[x][y];
            }
        }
        System.out.println(sum);
        System.out.println();
        ArrayList[] pointIdsForCenters = new ArrayList[K];
        for (int i = 0; i < pointIdsForCenters.length; i++) {
            pointIdsForCenters[i] = new ArrayList();
        }
        //do{
        //The Distance
        double a = Double.MAX_VALUE;
        int b = 0;
        double distance = 0.0;
        for (int x = 0; x < N; x++) {
            for (int z = 0; z < K; z++) {
                distance = 0.0;
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
/* for (int i = 0; i<pointIdsForCenters[b].size(); ++i){
System.out.println(pointIdsForCenters[b].get(i)+ " ");
}
System.out.println();
}
double[][] updatedCenters = new double [K][m];
//Inputing the new update centers
for (int x = 0; x<K; x++){
for (int y = 0; y<pointIdsForCenters[b].size(); y++){
//	updatedCenters[x][y] = points[x][y]
}
}
//Taking the sum of the new centers
double sum1=0;
for(int x=0; x<updatedCenters.length; x++){
for(int y=0; y<updatedCenters[0].length; y++){
sum1 = sum1+ updatedCenters[x][y];
}
}
System.out.println(sum1);
System.out.println();
double end = sum1 - sum;
//while */
        }
    }
}
