val hadoopConf = sc.hadoopConfiguration
val hdfs = org.apache.hadoop.fs.FileSystem.get(hadoopConf)
def rm(path:String) {
	hdfs.delete(new org.apache.hadoop.fs.Path(path), true)	
}


val input_data = sc.textFile(inputFile)
val sorted_input_data = input_data.map(s => s + " END")

rm(outputFile)
sorted_input_data.saveAsTextFile(outputFile)
