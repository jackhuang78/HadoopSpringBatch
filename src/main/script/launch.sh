#!/bin/sh

echo
echo "                __          __        "
echo " )__/_ _/      (    _ '  _ / _)__/_ / "
echo "/  /(/(/()()/)__)/)/ //)(//(_)(//( /) "
echo "           /    /      _/             "
echo

home=$(dirname $(dirname $0))
classpath=$home/libs/*
launcher=org.springframework.batch.core.launch.support.CommandLineJobRunner
config=$1
job=$2
params=$3

echo "Home:      $home"
echo "Classpath: $classpath"
echo "Launcher:  $launcher"
echo "Config:    $config"
echo "Job:       $job"
echo "Params:    $params"

echo "Launching job..."
echo
java -cp "$classpath" $launcher $config $job $params