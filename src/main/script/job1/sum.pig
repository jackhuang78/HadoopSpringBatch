REGISTER $udf USING jython AS udf;

input_data = LOAD '$db.input' USING org.apache.hcatalog.pig.HCatLoader();

output_data = FOREACH input_data GENERATE
	udf.uid() AS id, 
	f1+f2+f3 AS total;

rmf $warehouse/output/
mkdir $warehouse/output/
STORE output_data INTO '$db.output' USING org.apache.hcatalog.pig.HCatStorer();