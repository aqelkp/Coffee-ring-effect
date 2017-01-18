#! /bin/bash  
#PBS -o logfile.log  
#PBS -e errorfile.err  
#PBS -l cput=04:00:00  
#PBS -l select=1:ncpus=1 
tpdir=`echo $PBS_JOBID | cut -f 1 -d .`  
tempdir=$HOME/work/job$tpdir  
mkdir -p $tempdir  
cd $tempdir  
cp -R $PBS_O_WORKDIR/* .  
java -jar coffee.jar 
mv ../job$tpdir $PBS_O_WORKDIR/. 
