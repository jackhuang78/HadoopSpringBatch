DEFINE hcat_load (path) RETURNS data {
	$data = LOAD '$path' USING org.apache.hcatalog.pig.HCatLoader();
};

DEFINE hcat_store (data, path) RETURNS void {
	-- rmf $path
	-- mkdir $path
	STORE $data INTO '$path' USING org.apache.hcatalog.pig.HCatStorer();
};