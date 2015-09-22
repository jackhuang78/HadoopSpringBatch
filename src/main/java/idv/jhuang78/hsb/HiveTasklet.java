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
import org.springframework.beans.factory.annotation.Autowired;

public class HiveTasklet extends AbstractTasklet {

	private static Logger log = Logger.getLogger(HiveTasklet.class);

	private String script;
	private Map<String, String> params;
	private String queue;
	private String warehouse;

	
	
	protected String[] getCmd(StepContribution contribution, ChunkContext context) throws Exception {
		if (script == null || script.isEmpty()) {
			throw new UnexpectedJobExecutionException(
					"No script is given to the HiveTasklet to execute");
		}
		
		String name = context.getStepContext().getStepName();
		Properties sysProps = System.getProperties();
		String paramFile = String.format("%s/hive_param_%s.ini", 
				sysProps.getProperty("tmp"), name);
		if(params == null) {
			params = new HashMap<>();
		}
		try(PrintWriter out = new PrintWriter(new File(paramFile))) {
			out.println("-- autogen");
			out.println(String.format("SET mapred.job.queue.name=%s;", queue));
			out.println(String.format("SET hive.metastore.warehouse.dir=%s;", warehouse));
			for(String key : params.keySet()) {
				out.println(String.format("SET %s=%s;", key, params.get(key)));
			}
		}
		
		log.info(String.format("Generating parameter file at %s", paramFile));
		
		return new String[] { 
			"hive", 
			"-i", paramFile, 
			"-f", script
		};
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
	
	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

}
