package idv.jhuang78.hsb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Properties;

import org.apache.log4j.Logger;

public class CommandExecutor {
	private static Logger log = Logger.getLogger(CommandExecutor.class);
	
	public int execute(String name, String... commands) throws Exception  {
		
		ProcessBuilder ps = new ProcessBuilder(commands);
		ps.redirectErrorStream(true);
		
		Properties sysProps = System.getProperties();
		for (String key : sysProps.stringPropertyNames())
			ps.environment().put(key, sysProps.getProperty(key));
		
		try(PrintWriter out = new PrintWriter(new FileOutputStream(new File("commands.log"), true))) {
			String cmdLine = "";
			for(String cmd : commands)
				cmdLine += cmd + " ";
			out.println(cmdLine);
		}
		
		log.info("Executing: " + Arrays.toString(commands));
		Process pr = ps.start();
		try(BufferedReader in = new BufferedReader(new InputStreamReader(
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
