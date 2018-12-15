package org.singam.camel.component.hornetq;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;

/**
 * Represents a Hornetq endpoint.
 */
@UriEndpoint(firstVersion = "1.0.0", scheme = "hornetq", title = "Hornetq", syntax="hornetq:name", 
             consumerClass = HornetqConsumer.class, label = "custom")
public class HornetqEndpoint extends DefaultEndpoint {
    @UriPath @Metadata(required = "true")
    private String name;
    @UriParam(defaultValue = "10")
    private int option = 10;  
    
    @UriParam
    private String type = "queue";
    
    
    @UriParam
    private String prefix = "queue.";
    
    @UriParam
    private String host = "localhost";

    @UriParam
    private int port = 5445;

    ClientSessionFactory sessionFactory = null;
    
    static Map<String,ClientSessionFactory> urlQueueMap = new Hashtable<>();
    
    public HornetqEndpoint() {
    }

    public HornetqEndpoint(String uri, HornetqComponent component) throws Exception {
        super(uri, component);
        int index = uri.indexOf("?");
        String urlpattern;
        if(index!=-1) {
        	urlpattern = uri.substring(0, index);
        }
        else {
        	urlpattern = uri;
        }
        synchronized (this){
        	if(urlQueueMap.get(urlpattern)!=null) {
        		sessionFactory = urlQueueMap.get(urlpattern);
        	}
        	else {
        		Map<String, Object> connectionParams = new HashMap<String, Object>();
                connectionParams.put(TransportConstants.PORT_PROP_NAME, port);
     
                TransportConfiguration transportConfiguration = new TransportConfiguration(
                                                                    NettyConnectorFactory.class.getName(), connectionParams);
     
                
                ServerLocator serverLocator = HornetQClient.createServerLocator(false, transportConfiguration);
     
                ClientSessionFactory sessionFactory = serverLocator.createSessionFactory();
  
                this.sessionFactory = sessionFactory;
              
        		urlQueueMap.put(urlpattern, sessionFactory);
        	}
		}
    }

    public HornetqEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public Producer createProducer() throws Exception {
        return new HornetqProducer(this , sessionFactory.createSession(), prefix + name, type);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new HornetqConsumer(this, processor, sessionFactory.createSession(), prefix + name, type);
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

	public String getType() {
		return type;
	}

	/**
	 * Queue Type 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	public String getHost() {
		return host;
	}

	/**
	 * Host Name of HornetQ server
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	/**
	 * Port of HornetQ Server
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	public String getPrefix() {
		return prefix;
	}

	/**
	 * Queue Prefix of HornetQ Server
	 * @param port
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
    
    
}
