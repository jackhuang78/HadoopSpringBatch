package idv.jhuang78.hsb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class CommandExecutor {
	private static Logger log = Logger.getLogger(CommandExecutor.class);
	
	public int execute(String name, String command, List<String> arguments) throws Exception  {
		
		List<String> cmd = new ArrayList<>(arguments);
		cmd.add(0,  command);
		
		ProcessBuilder ps = new ProcessBuilder(cmd);
		ps.redirectErrorStream(true);
		
		Properties sysProps = System.getProperties();
		for (String key : sysProps.stringPropertyNames())
			ps.environment().put(key, sysProps.getProperty(key));
		
		
		log.info("Executing: " + cmd);
		Process pr = ps.start();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(
				pr.getInputStream()))) {

			String line;
			while ((line = in.readLine()) != null) {
				log.info("\t" + name + " | " + line);
			}
			int code = pr.waitFor();
			return code;
		}
	}
}
