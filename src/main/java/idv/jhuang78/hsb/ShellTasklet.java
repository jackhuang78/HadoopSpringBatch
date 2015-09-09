package idv.jhuang78.hsb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class ShellTasklet implements Tasklet {

	private static Logger log = Logger.getLogger(ShellTasklet.class);

	public String command;
	public String script;
	public List<String> arguments;
	public Properties config;

	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {

		log.info("=================================");
		log.info("|         ShellTasklet          |");
		log.info("=================================");

		if (command == null || command.isEmpty()) {
			throw new UnexpectedJobExecutionException(
					"No command is given to the ShellTasklet to execute");
		}
		
		if(arguments == null)
			arguments = new ArrayList<>();
		

		arguments.add(0, command);
		if(script != null && !script.isEmpty())
			arguments.add(1, script);
		

		ProcessBuilder ps = new ProcessBuilder(
				arguments.toArray(new String[arguments.size()]));
		ps.redirectErrorStream(true);
		
		
		Properties sysProps = System.getProperties();
		if(command.equalsIgnoreCase("pig")) {
			for (String key : config.stringPropertyNames())
				arguments.add(2, String.format("-param %s=%s", key, config.getProperty(key)));
			for (String key : sysProps.stringPropertyNames())
				arguments.add(2, String.format("-param %s=%s", key, sysProps.getProperty(key)));
			
		} else {
			for (String key : config.stringPropertyNames())
				ps.environment().put(key, config.getProperty(key));
			for (String key : sysProps.stringPropertyNames())
				ps.environment().put(key, sysProps.getProperty(key));
		}
		
		log.info("Executing: " + arguments);
		Process pr = ps.start();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(
				pr.getInputStream()))) {

			String line;
			while ((line = in.readLine()) != null) {
				log.info("\t" + line);
			}
			int code = pr.waitFor();
			if (code != 0) {
				String msg = "!!!ERROR: Script terminated with error code "
						+ code;
				log.error(msg);
				throw new UnexpectedJobExecutionException(msg);
			}

			return RepeatStatus.FINISHED;
		}

	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}

	public Properties getConfig() {
		return config;
	}

	public void setConfig(Properties config) {
		this.config = config;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	
}
