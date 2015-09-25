package idv.jhuang78.hsb.example.udf.pig;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

public class ToString extends EvalFunc<String> {
	
	public String exec(Tuple input) throws IOException {
		if(input == null || input.size() == 0) {
			return null;
		}
		
		
		try {
			String s = "";
			for(int i = 0; i < input.size(); i++) {
				Object o = input.get(i);
				s += o.getClass().getSimpleName() + "(" + o.toString() + ")";
				
			}
			return s;
		} catch(IOException e) {
			throw new IOException("Caught exception processing input row " + input, e);
		}
	}

}


