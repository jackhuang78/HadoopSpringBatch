input_data = LOAD '$db.data_with_id' USING org.apache.hcatalog.pig.HCatLoader();

output_data = FOREACH input_data GENERATE
	id AS id,
	f1*f2*f3 AS product;

rmf $warehouse/product/
mkdir $warehouse/product/
STORE output_data INTO '$db.product' USING org.apache.hcatalog.pig.HCatStorer();