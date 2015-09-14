package idv.jhuang78.hsb;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class ShellTasklet implements Tasklet {

	protected static Logger log = Logger.getLogger(ShellTasklet.class);

	public List<String> commands;

	public RepeatStatus execute(StepContribution contribution,
			ChunkContext context) throws Exception {

		log.info("=================================");
		log.info("|         ShellTasklet          |");
		log.info("=================================");
		
		System.out.println(contribution);
		System.out.println(context);
		
		String stepName = context.getStepContext().getStepName();

		
		int ret = new CommandExecutor().execute(stepName, commands.toArray(new String[commands.size()]));
		if(ret != 0)
			throw new UnexpectedJobExecutionException("Script terminated with code " + ret);
		
		return RepeatStatus.FINISHED;

	}

	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}


	
}
