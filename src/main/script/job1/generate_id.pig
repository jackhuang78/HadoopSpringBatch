REGISTER $udf USING jython AS udf;

data = LOAD '$db.input' USING org.apache.hcatalog.pig.HCatLoader();

data_with_id = FOREACH data GENERATE
	udf.uid() AS id, *;


rmf $warehouse/data_with_id/
mkdir $warehouse/data_with_id/
STORE data_with_id INTO '$db.data_with_id' USING org.apache.hcatalog.pig.HCatStorer();
