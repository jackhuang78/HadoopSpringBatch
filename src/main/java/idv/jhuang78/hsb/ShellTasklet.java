package idv.jhuang78.hsb;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

public class ShellTasklet extends AbstractTasklet {

	protected static Logger log = Logger.getLogger(ShellTasklet.class);

	private List<String> commands;
	
	@Override
	protected String[] getCmd(StepContribution contribution, ChunkContext context) {
		return commands.toArray(new String[commands.size()]);
	}
	
	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}
	/*
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext context) throws Exception {

		log.info("=================================");
		log.info("|         ShellTasklet          |");
		log.info("=================================");
		String stepName = context.getStepContext().getStepName();
		if(new CommandExecutor().skip(stepName, config, context)) {
			log.warn("SKIPPING STEP " + stepName);
			return RepeatStatus.FINISHED;
		}
				
//		System.out.println(contribution);
//		System.out.println(context);
//		
		

		
		int ret = new CommandExecutor().execute(stepName, commands.toArray(new String[commands.size()]));
		if(ret != 0)
			throw new UnexpectedJobExecutionException("Script terminated with code " + ret);
		
		return RepeatStatus.FINISHED;

	}



	public Map<String, String> getConfig() {
		return config;
	}

	public void setConfig(Map<String, String> config) {
		this.config = config;
	}*/

	



	
}
