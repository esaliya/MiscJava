#!/bin/bash
wd=`pwd`

cp=/home/saliya/.m2/repository/com/google/guava/guava/15.0/guava-15.0.jar:/home/saliya/.m2/repository/commons-cli/commons-cli/1.2/commons-cli-1.2.jar:/home/saliya/.m2/repository/habanero-java-lib/habanero-java-lib/0.1.1/habanero-java-lib-0.1.1.jar:/home/saliya/.m2/repository/ompi/ompijavabinding/1.8.1/ompijavabinding-1.8.1.jar:/home/saliya/.m2/repository/org/jblas/jblas/1.2.3/jblas-1.2.3.jar:/home/saliya/.m2/repository/org/saliya/MiscJava/1.0-SNAPSHOT/MiscJava-1.0-SNAPSHOT.jar

x='x'
opts="-XX:+UseG1GC -Xms512m -Xmx512m"
cpn=4

allhosts=$wd/hosts

points=1000
nodes=1
tpn=4
ppn=2
pat=$tpn$x$ppn$x$nodes

hostfile=$wd/$pat.hosts
cat $allhosts | head -$nodes > $hostfile
echo "Running $pat on `date`" >> status.txt
mpirun --report-bindings --hostfile $hostfile --map-by node:PE=$(($cpn/$ppn)) -np $(($nodes*$ppn)) java $opts -cp $cp org.saliya.threads.pwcanalysis.MimicPartOfPWC -p $points  -n $nodes -t $tpn | tee $pat/pwc-out.txt
#mpirun --report-bindings --hostfile $hostfile -np $(($nodes*$ppn)) java $opts -cp $cp org.saliya.threads.pwcanalysis.MimicPartOfPWC -p $points  -n $nodes -t $tpn | tee $pat/pwc-out.txt
echo "Finished $pat on `date`" >> status.txt

