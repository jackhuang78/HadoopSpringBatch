package idv.jhuang78.hsb;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class PigTasklet implements Tasklet {

	private static Logger log = Logger.getLogger(PigTasklet.class);

	public String script;
	public Map<String, String> params;
	public String queue;

	public RepeatStatus execute(StepContribution contribution,
			ChunkContext context) throws Exception {

		log.info("=================================");
		log.info("|         PigTasklet            |");
		log.info("=================================");
		
		if (script == null || script.isEmpty()) {
			throw new UnexpectedJobExecutionException(
					"No script is given to the PigTasklet to execute");
		}
		
		String stepName = context.getStepContext().getStepName();
		Properties sysProps = System.getProperties();
		String paramFile = String.format("%s/pig_param_%s.properties", 
				sysProps.getProperty("tmp"), stepName);
		if(params == null) {
			params = new HashMap<>();
		}
		try(PrintWriter out = new PrintWriter(new File(paramFile))) {
			out.println("# autogen");
			for(String key : params.keySet()) {
				out.println(String.format("%s=%s", key, params.get(key)));
			}
		}
		
		log.info(String.format("Generating parameter file at %s", paramFile));
		
		
		
		int ret = new CommandExecutor().execute(stepName,
				"pig", 
				"-Dmapred.job.queue.name=" + queue, 
				"-useHCatalog", 
				"-m", paramFile, 
				script);
		
		if(ret != 0)
			throw new UnexpectedJobExecutionException("Script terminated with code " + ret);
		
		return RepeatStatus.FINISHED;
				
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}
}
