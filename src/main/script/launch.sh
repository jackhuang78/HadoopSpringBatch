#!/bin/sh

echo 
echo '   __ __        __                 ____         _             ___       __      __ '
echo '  / // /__ ____/ /__  ___  ___    / __/__  ____(_)__  ___ _  / _ )___ _/ /_____/ / '
echo ' / _  / _ `/ _  / _ \/ _ \/ _ \  _\ \/ _ \/ __/ / _ \/ _ `/ / _  / _ `/ __/ __/ _ \'
echo '/_//_/\_,_/\_,_/\___/\___/ .__/ /___/ .__/_/ /_/_//_/\_, / /____/\_,_/\__/\__/_//_/'
echo '                        /_/        /_/              /___/                          '
echo 

home=$(cd $(dirname $(dirname $0)) && pwd)
classpath=$home/lib/*
launcher=org.springframework.batch.core.launch.support.CommandLineJobRunner
config=$1
job=$2
params=$3
tmp=/tmp
pylib=$home/lib/py

echo "Home:      $home"
echo "Classpath: $classpath"
echo "Launcher:  $launcher"
echo "Config:    $config"
echo "Job:       $job"
echo "Params:    $params"

echo "Launching job..."
echo
java -cp "$classpath" -Dhome=$home -Dtmp=$tmp -Dpylib=$pylib $launcher $config $job $params