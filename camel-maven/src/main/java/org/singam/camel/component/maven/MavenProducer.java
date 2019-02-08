package org.singam.camel.component.maven;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultProducer;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Maven producer.
 */
public class MavenProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(MavenProducer.class);
    private MavenEndpoint endpoint;
    private String projectPath, goals, mavenHome, systemProperties,pomFile;
    
    
    public MavenProducer(MavenEndpoint endpoint, String projectPath, String goals, String mavenHome, String systemProperties, String pomFile) {
        super(endpoint);
        this.endpoint = endpoint;
        this.projectPath = projectPath;
        this.goals = goals;
        this.mavenHome = mavenHome;
        this.systemProperties = systemProperties;
        this.pomFile = pomFile;
    }

    public void process(Exchange exchange) throws Exception {
    	InvocationRequest request = new DefaultInvocationRequest();
    	request.setInteractive(false);
    	request.setBaseDirectory( new File(projectPath));
    	Message message = exchange.getIn();
    	if(pomFile!=null) {
    		request.setPomFile( new File( pomFile ) );
    	}
    	else {
    		if(message.getHeader(MavenConstants.MAVEN_POM_FILE)!=null) {
    			request.setPomFile( new File( (String) message.getHeader(MavenConstants.MAVEN_POM_FILE)) );
    		}
    	}
    	if(goals!=null) {
    		request.setGoals( Arrays.asList(goals.split(",")));
    	}
    	else {
    		if(message.getHeader(MavenConstants.MAVEN_GOALS)!=null) {
    			request.setGoals( Arrays.asList((	(String)message.getHeader(MavenConstants.MAVEN_GOALS)).split(",")));
    		}
    		else {
    			throw new MavenProducerException("Maven Goals Not Set");
    		}
    	}
    	Invoker invoker = new DefaultInvoker();
    	if(mavenHome!=null) {
    		invoker.setMavenHome(new File(mavenHome));
    		invoker.setMavenExecutable(new File(mavenHome+MavenConstants.BACKWARD_SLASH+MavenConstants.MAVEN_EXECUTABLE));
    	}
    	else {
    		if(message.getHeader(MavenConstants.MAVEN_HOME)!=null) {
    			invoker.setMavenHome(new File((String) message.getHeader(MavenConstants.MAVEN_HOME)));
    			invoker.setMavenExecutable(new File((String) message.getHeader(MavenConstants.MAVEN_HOME)+MavenConstants.BACKWARD_SLASH
    					+MavenConstants.MAVEN_BIN_DIR+MavenConstants.BACKWARD_SLASH+MavenConstants.MAVEN_EXECUTABLE));
    		}
    		else {
    			throw new MavenProducerException("Maven Home Not Set");
    		}
    	}
    	Properties properties = new Properties();
    	if(systemProperties!=null) {
    		String[] props=systemProperties.trim().replaceAll(" +", " ").split(" ");
    		for(String prop:props) {
    			if(prop.startsWith("-D")) {
    				properties.setProperty(prop.substring(2,prop.indexOf("=")), prop.substring(prop.indexOf("=")+1));
    			}
    			else {
    				throw new MavenProducerException("Maven Properties Format Incorrect");
    			}
    		}
    	}
    	else {
    		if(message.getHeader(MavenConstants.MAVEN_SYSTEM_PROPERTIES)!=null) {
    			String headerProps = (String) message.getHeader(MavenConstants.MAVEN_SYSTEM_PROPERTIES);
    			String[] props=headerProps.trim().replaceAll(" +", " ").split(" ");
        		for(String prop:props) {
        			if(prop.startsWith("-D")) {
        				properties.setProperty(prop.substring(2,prop.indexOf("=")), prop.substring(prop.indexOf("=")+1));
        			}
        			else {
        				throw new MavenProducerException("Maven Properties Format Incorrect");
        			}
        		}
    		}
    	}
    	request.setProperties(properties);
    	InvocationResult result = invoker.execute( request );
    	if ( result.getExitCode() != 0 )
    	{
    		result.getExecutionException().printStackTrace();
    	    throw new IllegalStateException( "Build failed." );
    	}
    	exchange.getIn().setBody(result);
    }

}
