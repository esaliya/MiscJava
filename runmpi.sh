cp=/home/saliya/.m2/repository/com/google/guava/guava/15.0/guava-15.0.jar:/home/saliya/.m2/repository/commons-cli/commons-cli/1.2/commons-cli-1.2.jar:/home/saliya/.m2/repository/habanero-java-lib/habanero-java-lib/0.1.1/habanero-java-lib-0.1.1.jar:/home/saliya/.m2/repository/ompi/ompijavabinding/1.8.1/ompijavabinding-1.8.1.jar:/home/saliya/.m2/repository/org/jblas/jblas/1.2.3/jblas-1.2.3.jar:/home/saliya/.m2/repository/org/saliya/MiscJava/1.0-SNAPSHOT/MiscJava-1.0-SNAPSHOT.jar

#mpirun -np 4 java -cp $cp org.saliya.ompi.struct.Tester
#mpirun -np $1 java -cp $cp org.saliya.ompi.comm.BroadcastArrayVsBuffer $2 $3
mpirun -np $1 java -cp $cp org.saliya.ompi.comm.UserFunctionTest
