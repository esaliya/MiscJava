package org.saliya.reu2014;

import java.util.Scanner;
import java.util.ArrayList;

public class KMeans {
    public static void main(String[] args) {
        // Print a decorative header for the program
        printHeader();

        // Retrieve user input
        Scanner sysIn = new Scanner(System.in);
        System.out.print("Enter number of points: ");
        int n = sysIn.nextInt();
        System.out.print("\nEnter number of dimensions: ");
        int m = sysIn.nextInt();
        System.out.print("\nEnter number of centers: ");
        int k = sysIn.nextInt();
        System.out.print("\nEnter threshold: ");
        double threshold = sysIn.nextDouble();
        System.out.print("\nEnter maximum iterations: ");
        int maxIterations = sysIn.nextInt();
        System.out.print("\nVerbose output [true|false]?: ");
        boolean verbose = sysIn.nextBoolean();

        // Number of centers should be less than or equal to the number of points
        if (k > n){
            System.out.println("Error! Number of centers should be less than or equal to the number of points");
            return;
        }

        // Data structure to hold points - a point is an m dimensional double array and we have n such arrays
        double[][] points = new double[n][m];
        // Data structure to hold centers - a center is an m dimensional double array and we have k such arrays
        double[][] centers = new double[k][m];

        // Initialize points to random values
        initializeToRandoms(points);

        // Instead of initializing k centers to random values, we'll pick the first k points as centers
        System.arraycopy(points, 0, centers, 0, k);

        // Flag to indicate if computation should be stopped
        boolean converged;
        // Variable to count the iteration number
        int iteration = 0;
        do {
            converged = true; // Initially assume algorithm has converged

            // Data structure to hold set of associated points for each center
            ArrayList[] pointIdsForCenters = new ArrayList[k];
            for (int i = 0; i < pointIdsForCenters.length; i++) {
                pointIdsForCenters[i] = new ArrayList();
            }

            // Finding the closest center for each point
            for (int p = 0; p < n; ++p) {
                double [] point = points[p];
                double minDistance = Double.MAX_VALUE;
                int centerForMinDistance = -1;
                for (int c = 0; c < k; ++c) {
                    double [] center = centers[c];
                    double distance = getEuclideanDistance(point, center, m);
                    if (distance < minDistance) {
                        minDistance = distance;
                        centerForMinDistance = c;
                    }
                }
                // Holding The Distance
                pointIdsForCenters[centerForMinDistance].add(p);
            }

            if (verbose){
                System.out.println("Closest points at iteration " + iteration);
                printClosestPoints(pointIdsForCenters);
            }

            /* Updating centers */
            // Data structure to hold updated centers
            double[][] updatedCenters = new double[k][m];
            for (int i = 0; i < k; ++i) {
                ArrayList closestPointNumbers = pointIdsForCenters[i]; // associated points for center i
                double[] avgComponentValues = new double[m];
                for (int j = 0; j < closestPointNumbers.size(); ++j) {
                    for (int h = 0; h < m; ++h) {
                        avgComponentValues[h] += points[(Integer) closestPointNumbers.get(j)][h];
                    }
                }
                // take the averages
                for (int h = 0; h < m; ++h) {
                    avgComponentValues[h] /= closestPointNumbers.size();
                }
                updatedCenters[i] = avgComponentValues;
            }


            // What you need to do: compute distance for each updated center and old center
            // if any of them is greater than a given threshold value then redo computation else stop

            for (int c = 0; c < k; ++c) {
                double [] center = centers[c];
                double [] updatedCenter = updatedCenters[c];
                double distance = getEuclideanDistance(center, updatedCenter, m);
                if (distance > threshold) {
                    converged = false;
                    break;
                }
            }

            if (!converged){
                // Copy updated centers to old centers for next iteration if not converged
                for (int c = 0; c < k; ++c) {
                    System.arraycopy(updatedCenters[c], 0, centers[c], 0, m);
                }
            } else {
                System.out.println("KMeans converged to the given threshold at iteration " + iteration);
                printClosestPoints(pointIdsForCenters);
            }
            ++iteration;
        } while (!converged && iteration < maxIterations);

        if (!converged){
            System.out.println("KMeans did not converge to the given threshold, but stopping as maximum iteration number is reached");
        }
    }

    public static void printClosestPoints(ArrayList[] pointIdsForCenters) {
        for (int i = 0; i < pointIdsForCenters.length; ++i) {
            System.out.print(i + " --> ");
            for (int j = 0; j < pointIdsForCenters[i].size(); ++j) {
                System.out.print(pointIdsForCenters[i].get(j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static double getEuclideanDistance(double [] p1, double [] p2, int dims){
        if (p1.length != dims || p2.length != dims){
            System.out.println("Error! Point 1 is of " + p1.length + " dimensions and point 2 is of " + p2.length + " dimensions, but expected is " + dims);
            return -1.0;
        }

        double distance = 0.0;
        double tmp;
        for (int i = 0; i < dims; ++i){
            tmp = p1[i] - p2[i];
            distance += tmp * tmp;
        }
        return Math.sqrt(distance);
    }

    public static void printHeader() {
        System.out.println("*****************************");
        System.out.println("**KMeans Clustering Program**");
        System.out.println("*****************************\n");
    }

    public static void initializeToRandoms(double [][] points){
        if (points.length < 1) {
            return;
        }
        for (double[] point : points) {
            for (int j = 0; j < point.length; j++) {
                point[j] = Math.random();
            }
        }
    }
}