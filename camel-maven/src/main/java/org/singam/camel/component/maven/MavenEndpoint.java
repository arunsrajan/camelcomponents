package org.singam.camel.component.maven;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a Maven endpoint.
 */
@UriEndpoint(firstVersion = "1.0.0", scheme = "maven", title = "Maven", syntax="maven:name", 
             consumerClass = MavenConsumer.class, label = "custom")
public class MavenEndpoint extends DefaultEndpoint {
    @UriPath @Metadata(required = "true")
    private String name;
    @UriParam(defaultValue = "10")
    private int option = 10;

    @UriParam
    private String projectPath;
    
    @UriParam
    private String goals;
    
    @UriParam
    private String maveHome;
    
    @UriParam
    private String systemProperties;
    
    @UriParam
    private String pomFile;
    
    public MavenEndpoint() {
    }

    public MavenEndpoint(String uri, MavenComponent component) {
        super(uri, component);
    }

    public MavenEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public Producer createProducer() throws Exception {
        return new MavenProducer(this, projectPath, goals, maveHome, systemProperties, pomFile);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        throw new UnsupportedOperationException("Consumer not supported");
    }

    public boolean isSingleton() {
        return true;
    }

    /**
     * Some description of this option, and what it does
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Some description of this option, and what it does
     */
    public void setOption(int option) {
        this.option = option;
    }

    public int getOption() {
        return option;
    }

	public String getProjectPath() {
		return projectPath;
	}

	/**
	 * Maven Project Path
	 * @param projectPath
	 */
	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getGoals() {
		return goals;
	}

	/**
	 * Maven Goals
	 * @param goals
	 */
	public void setGoals(String goals) {
		this.goals = goals;
	}

	public String getMaveHome() {
		return maveHome;
	}

	/**
	 * Maven Home Path
	 * @param maveHome
	 */
	public void setMaveHome(String maveHome) {
		this.maveHome = maveHome;
	}

	public String getSystemProperties() {
		return systemProperties;
	}

	/**
	 * Maven System Properties
	 * @param systemProperties
	 */
	public void setSystemProperties(String systemProperties) {
		this.systemProperties = systemProperties;
	}

	public String getPomFile() {
		return pomFile;
	}

	/**
	 * POM File Path
	 * @param pomFile
	 */
	public void setPomFile(String pomFile) {
		this.pomFile = pomFile;
	}
    
    
}
