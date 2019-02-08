package org.singam.camel.component.maven;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class MavenComponentTest extends CamelTestSupport {

    @Test
    public void testMaven() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(2);       
        
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("jetty:http://localhost:8080/test")
                .setHeader(MavenConstants.MAVEN_PROJECT_PATH, constant("D:\\MavenProjectExample\\"))
                .setHeader(MavenConstants.MAVEN_GOALS, constant("archetype:generate"))
                .setHeader(MavenConstants.MAVEN_HOME, constant("D:\\LearnSoftwares\\apache-maven-3.3.9\\"))
                .setHeader(MavenConstants.MAVEN_SYSTEM_PROPERTIES, constant("-DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DarchetypeCatalog=internal"))
                  .to("maven://bar?projectPath=D:/MavenProjectExample/")
                  .setHeader(MavenConstants.MAVEN_SYSTEM_PROPERTIES, constant("-DgroupId=com.mycompany.app -DartifactId=my-app1 -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DarchetypeCatalog=internal"))
                  .to("maven://bar?projectPath=D:/MavenProjectExample/")
                  .to("seda:myapp")
                  .to("seda:myapp1")
                  .to("mock:result");
                
                from("seda:myapp")
                .setHeader(MavenConstants.MAVEN_SYSTEM_PROPERTIES, constant("-Dmaven.test.skip=true"))
                .setHeader(MavenConstants.MAVEN_GOALS, constant("clean,install"))
                .to("maven://bar?projectPath=D:/MavenProjectExample/my-app/")
                  .to("mock:result");
                
                from("seda:myapp1")
                .setHeader(MavenConstants.MAVEN_SYSTEM_PROPERTIES, constant("-Dmaven.test.skip=true"))
                .setHeader(MavenConstants.MAVEN_GOALS, constant("clean,install"))
                .to("maven://bar1?projectPath=D:/MavenProjectExample/my-app1/")
                  .to("mock:result");
            }
        };
    }
    @Override
	public int getShutdownTimeout() {
    	return 50000;
    }
}
