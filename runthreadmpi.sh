
cp=$HOME/.m2/repository/com/google/guava/guava/15.0/guava-15.0.jar:$HOME/.m2/repository/commons-cli/commons-cli/1.2/commons-cli-1.2.jar:$HOME/.m2/repository/habanero-java-lib/habanero-java-lib/0.1.2-SNAPSHOT/habanero-java-lib-0.1.2-SNAPSHOT.jar:$HOME/.m2/repository/ompi/ompijavabinding/1.8.1/ompijavabinding-1.8.1.jar:$HOME/.m2/repository/org/jblas/jblas/1.2.3/jblas-1.2.3.jar:$HOME/.m2/repository/org/saliya/MiscJava/1.0-SNAPSHOT/MiscJava-1.0-SNAPSHOT.jar

wd=`pwd`
x='x'
#opts="-XX:+UseConcMarkSweepGC -XX:ParallelCMSThreads=4 -Xms2G -Xmx2G"
opts="-XX:+UseG1GC -Xms256m -Xmx256m"
cps=4
spn=1
cpn=$(($cps*$spn))

allhosts=nodes.txt

nodes=1
ppn=$1
tpp=$2

pat=$tpp$x$ppn$x$nodes
hostfile=$wd/$pat.hosts
cat $allhosts | head -$nodes > $hostfile
echo "Running $pat on `date`" >> status.txt
$BUILD/bin/mpirun --report-bindings --hostfile $hostfile -np $(($nodes*$ppn)) java $opts -cp $cp org.saliya.threads.basic.PrimerWithMPI $tpp | tee $pat-out.txt
echo "Finished $pat on `date`" >> status.txt

mpirun -np $1 java -cp $cp org.saliya.threads.basic.PrimerWithMPI $2
