data = LOAD '$input' USING PigStorage(',') AS (
	f1:int, f2:int, f3:int);

sum = FOREACH data GENERATE
	f1, f2, f3, f1+f2+f3 AS f4;

rmf $output
STORE sum INTO '$output' USING PigStorage(',','-schema');