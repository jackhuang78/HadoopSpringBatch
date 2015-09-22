package idv.jhuang78.hsb;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;

public class PigTasklet extends AbstractTasklet {

	private static Logger log = Logger.getLogger(PigTasklet.class);

	public String script;
	public Map<String, String> params;
	public String queue;
	
	protected String[] getCmd(StepContribution contribution, ChunkContext context) throws Exception {
		if (script == null || script.isEmpty()) {
			throw new UnexpectedJobExecutionException(
					"No script is given to the PigTasklet to execute");
		}
		
		String name = context.getStepContext().getStepName();
		Properties sysProps = System.getProperties();
		String paramFile = String.format("%s/pig_param_%s.properties", 
				sysProps.getProperty("tmp"), name);
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
		
		return new String[]{
			"pig", 
			"-Dmapred.job.queue.name=" + queue, 
			"-useHCatalog", 
			"-m", paramFile, 
			script
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

}
