package idv.jhuang78.hsb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public abstract class AbstractTasklet  implements Tasklet {

	private Logger log = Logger.getLogger(this.getClass());
	@Resource
	protected Map<String, String> taskletConfig;
	
	
	abstract protected String[] getCmd(StepContribution contribution, ChunkContext context) throws Exception;
	protected InputStream getStdin() {
		return null;
	}
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
		
		String name = context.getStepContext().getStepName();
		log.info("=====================================================");
		log.info("|         " + this.getClass().getSimpleName() + ": " + name);
		log.info("=====================================================");
		
		
		String startAt = (String) context.getStepContext().getJobParameters().get("startAt");
		String stopAt = (String) context.getStepContext().getJobParameters().get("stopAt");
		if(taskletConfig.get("run") == null && startAt == null)
			startAt = name;
		if(taskletConfig.get("run") == null)
			taskletConfig.put("run", "false");
		log.info(name + ", " + startAt + ", " + stopAt + ", " + taskletConfig);
		if(name.equals(stopAt)) {
			taskletConfig.put("run", "false");
		} else if(name.equals(startAt)) {
			taskletConfig.put("run", "true");
		} else if(!"true".equals(taskletConfig.get("run"))){
			log.warn("SKIPPING " + name);
			return RepeatStatus.FINISHED;
		}
		
		
		
		String[] commands = getCmd(contribution, context);
		ProcessBuilder ps = new ProcessBuilder(commands);
		ps.redirectErrorStream(true);
		
		Properties sysProps = System.getProperties();
		for (String key : sysProps.stringPropertyNames())
			ps.environment().put(key, sysProps.getProperty(key));
		
		try(PrintWriter out = new PrintWriter(new FileOutputStream(new File("commands.log"), true))) {
			String cmdLine = "";
			for(String cmd : commands)
				cmdLine += cmd + " ";
			out.println(cmdLine);
		}
		
		log.info("Executing: " + Arrays.toString(commands));
		final Process pr = ps.start();
		int code = 0;
		
		final InputStream stdin = getStdin();
		if(stdin != null) {
			new Thread(){
				public void run() {
					
					Scanner in = new Scanner(stdin);
					PrintWriter out = new PrintWriter(pr.getOutputStream());
					while(in.hasNextLine()) {
						String line = in.nextLine();
						//log.info(">> " + line);
						out.println(line);
					}
					in.close();
					out.close();	
				}
			}.run();
		}
		
		try(BufferedReader in = new BufferedReader(new InputStreamReader(
				pr.getInputStream()))) {

			String line;
			while ((line = in.readLine()) != null) {
				log.info("\t" + name + " | " + line);
			}
			code = pr.waitFor();
		}
		if(code != 0)
			throw new UnexpectedJobExecutionException("Script terminated with code " + code);
		
			
			

		
		return RepeatStatus.FINISHED;
	}

	
	

}
