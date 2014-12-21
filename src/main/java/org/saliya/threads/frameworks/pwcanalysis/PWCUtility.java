package org.saliya.threads.frameworks.pwcanalysis;


import mpi.Intracomm;

public class PWCUtility {

    public static Intracomm MPI_communicator;
    public static int MPI_Rank;
    public static int MPI_Size;
    public static MpiOps mpiOps;
    public static int MPIperNodeCount;
    public static int NodeCount;
    public static String ParallelPattern;
    private static int DebugPrintOption = 1;
    private static boolean ConsoleDebugOutput = true;
    public static int ThreadCount;
    public static int PointCount_Global;
    public static int PointStart_Process;
    public static int PointCount_Process;
    public static int PointCount_Largest;
    public static int[] PointsperProcess;
    public static int[][] PointsperThreadperProcess;
    public static int[] PointsperThread;
    public static int[] StartPointperThread;
    public static String PatternLabel;

    public static void printException(Exception e) {
        System.out.println("SALSA Error " + e.getMessage());
    }

    public static void printAndThrowRuntimeException(RuntimeException e) {
        System.out.println("SALSA Error " + e.getMessage());
        throw e;
    }

    public static void printAndThrowRuntimeException(String message) {
        System.out.println("SALSA Error " + message);
        throw new RuntimeException(message);
    }

    public static void SALSAPrint(int PrintOption, String StufftoPrint) {
        if (MPI_Rank != 0) {
            return;
        }
        if (DebugPrintOption < PrintOption) {
            return;
        }

        if (ConsoleDebugOutput) {
            System.out.println(StufftoPrint);
        }
    }
}
