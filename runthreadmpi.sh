cp=$HOME/.m2/repository/com/google/guava/guava/15.0/guava-15.0.jar:$HOME/.m2/repository/commons-cli/commons-cli/1.2/commons-cli-1.2.jar:$HOME/.m2/repository/habanero-java-lib/habanero-java-lib/0.1.2/habanero-java-lib-0.1.2.jar:$HOME/.m2/repository/net/java/dev/jna/jna/4.1.0/jna-4.1.0.jar:$HOME/.m2/repository/net/java/dev/jna/jna-platform/4.1.0/jna-platform-4.1.0.jar:$HOME/.m2/repository/net/openhft/affinity/2.1.7-SNAPSHOT/affinity-2.1.7-SNAPSHOT.jar:$HOME/.m2/repository/ompi/ompijavabinding/1.8.1/ompijavabinding-1.8.1.jar:$HOME/.m2/repository/org/jblas/jblas/1.2.3/jblas-1.2.3.jar:$HOME/.m2/repository/org/kohsuke/jetbrains/annotations/9.0/annotations-9.0.jar:$HOME/.m2/repository/org/slf4j/slf4j-api/1.7.6/slf4j-api-1.7.6.jar:$HOME/.m2/repository/org/slf4j/slf4j-simple/1.7.6/slf4j-simple-1.7.6.jar:$HOME/.m2/repository/org/saliya/MiscJava/1.0-SNAPSHOT/MiscJava-1.0-SNAPSHOT.jar

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
$BUILD/bin/mpirun --report-bindings --hostfile $hostfile -np $(($nodes*$ppn)) java $opts -cp $cp org.saliya.threads.basic.PrimerWithMPI $tpp $3 | tee $pat-out.txt
echo "Finished $pat on `date`" >> status.txt

