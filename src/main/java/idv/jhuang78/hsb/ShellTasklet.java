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
	public List<String> arguments;
	public Properties config;

	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		
		log.info("=================================");
		log.info("|         ShellTasklet          |");
		log.info("=================================");
//		log.info("StepContribution: " + contribution);
//		log.info("ChunkContext: " + chunkContext);
//		log.info("Command: " + command);
//		log.info("Arguments: " + arguments);
//		log.info(config);
		
		
		if(command == null) {
			throw new UnexpectedJobExecutionException(
					"No command is given to the ShellTasklet to execute");
		}

		arguments.add(0, command);
		log.info("Executing: " + arguments);
		
		
		ProcessBuilder ps = new ProcessBuilder(
				arguments.toArray(new String[arguments.size()]));
		ps.redirectErrorStream(true);
		for(String key : config.stringPropertyNames())
			ps.environment().put("env." + key, config.getProperty(key));

		Process pr = ps.start();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(
				pr.getInputStream()))) {

			String line;
			while ((line = in.readLine()) != null) {
				log.info("\t" + line);
			}
			int code = pr.waitFor();
			if (code != 0) {
				throw new UnexpectedJobExecutionException(
						"Script terminated with error code " + code);
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

	
	
}
