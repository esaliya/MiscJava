package org.saliya.threads.basic;

import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

public class PrimerWithMPI {
    public static void main(String[] args) throws MPIException {
        args = MPI.Init(args);
        Intracomm comm = MPI.COMM_WORLD;
        int rank = comm.getRank();
        int size = comm.getSize();

        int threads = Integer.parseInt(args[0]);

        System.out.println(Runtime.getRuntime().availableProcessors());
        MPI.Finalize();
    }
}
