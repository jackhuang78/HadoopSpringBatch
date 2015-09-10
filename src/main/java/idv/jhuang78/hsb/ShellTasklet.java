package idv.jhuang78.hsb;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class ShellTasklet implements Tasklet {

	protected static Logger log = Logger.getLogger(ShellTasklet.class);

	public String command;
	public String script;
	public List<String> arguments;

	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {

		log.info("=================================");
		log.info("|         ShellTasklet          |");
		log.info("=================================");
		
		System.out.println(contribution);
		System.out.println(chunkContext);
		
		if (command == null || command.isEmpty()) {
			throw new UnexpectedJobExecutionException(
					"No command is given to the ShellTasklet to execute");
		}
		
		if(arguments == null)
			arguments = new ArrayList<>();
		
		if(script != null && !script.isEmpty())
			arguments.add(0, script);
		
		int ret = 0;
		try {
			ret = new CommandExecutor().execute(command, arguments);
		} catch(Exception e) {
			throw new UnexpectedJobExecutionException("", e);
		}
		
		if(ret != 0)
			throw new UnexpectedJobExecutionException("Script terminated with code " + ret);
		
		return RepeatStatus.FINISHED;

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

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	
}
