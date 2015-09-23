val hadoopConf = sc.hadoopConfiguration
val hdfs = org.apache.hadoop.fs.FileSystem.get(hadoopConf)
def rm(path:String) {
	hdfs.delete(new org.apache.hadoop.fs.Path(path), true)	
}

//val input_data = sc.textFile("/idn/home/yhuan10/HadoopSpringBatch/data/hsb.db/data_with_id")
val input_data = sc.textFile(inputFile)
val sorted_input_data = input_data.map(s => s + " END")

// rm("/idn/home/yhuan10/HadoopSpringBatch/data/test.txt")
// sorted_input_data.saveAsTextFile("/idn/home/yhuan10/HadoopSpringBatch/data/test.txt")
rm(outputFile)
sorted_input_data.saveAsTextFile(outputFile)
