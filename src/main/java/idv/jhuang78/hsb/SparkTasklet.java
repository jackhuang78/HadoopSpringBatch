package idv.jhuang78.hsb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;

public class SparkTasklet extends AbstractTasklet {

	private static Logger log = Logger.getLogger(HiveTasklet.class);
	
	public String script;
	public Map<String, String> params;
	
	@Override
	protected String[] getCmd(StepContribution contribution, ChunkContext context) throws Exception {
		if (script == null || script.isEmpty()) {
			throw new UnexpectedJobExecutionException(
					"No script is given to the SparkTasklet to execute");
		}
		
		String name = context.getStepContext().getStepName();
		Properties sysProps = System.getProperties();
		String paramFile = String.format("%s/spark_param_%s.scala", 
				sysProps.getProperty("tmp"), name);
		if(params == null) {
			params = new HashMap<>();
		}
		try(PrintWriter out = new PrintWriter(new File(paramFile))) {
			out.println("// autogen");
			//out.println(String.format("SET mapred.job.queue.name=%s;", queue));
			//out.println(String.format("SET hive.metastore.warehouse.dir=%s;", warehouse));
			for(String key : params.keySet()) {
				out.println(String.format("val %s=\"%s\";", key, params.get(key)));
			}
		}
		
		log.info(String.format("Generating parameter file at %s", paramFile));
		
		return new String[] { 
			"spark-shell",
			"-i", paramFile
		};
	}
	
	protected InputStream getStdin() {
		try {
			return new FileInputStream(new File(script));
		} catch (FileNotFoundException e) {
			return null;
		}
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
