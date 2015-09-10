package idv.jhuang78.hsb;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class HiveTasklet implements Tasklet {

	private static Logger log = Logger.getLogger(HiveTasklet.class);

	public String script;
	public Map<String, String> params;

	public RepeatStatus execute(StepContribution contribution,
			ChunkContext context) throws Exception {

		log.info("=================================");
		log.info("|         HiveTasklet           |");
		log.info("=================================");
		
		if (script == null || script.isEmpty()) {
			throw new UnexpectedJobExecutionException(
					"No script is given to the HiveTasklet to execute");
		}
		
		Properties sysProps = System.getProperties();
		String paramFile = String.format("%s/hive_param_%s.ini", 
				sysProps.getProperty("tmp"), 
				context.getStepContext().getStepName());
		if(params == null) {
			params = new HashMap<>();
		}
		try(PrintWriter out = new PrintWriter(new File(paramFile))) {
			out.println("-- autogen");
			for(String key : params.keySet()) {
				out.println(String.format("SET %s=%s;", key, params.get(key)));
			}
		}
		
		log.info(String.format("Generating parameter file at %s", paramFile));
		
		int ret = 0;
		try {
			ret = new CommandExecutor().execute("hive", Arrays.asList("-i", paramFile, "-f", script));
		} catch(Exception e) {
			throw new UnexpectedJobExecutionException("", e);
		}
		
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
	
}
