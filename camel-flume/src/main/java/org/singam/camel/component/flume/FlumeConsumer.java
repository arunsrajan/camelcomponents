package org.singam.camel.component.flume;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.Responder;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;
import org.apache.flume.api.RpcClient;
import org.apache.flume.source.avro.AvroFlumeEvent;
import org.apache.flume.source.avro.AvroSourceProtocol;
import org.apache.flume.source.avro.Status;

/**
 * The Flume consumer.
 */
public class FlumeConsumer extends ScheduledPollConsumer {
    private final FlumeEndpoint endpoint;
    private Server server;
    Queue queue = new LinkedBlockingQueue();
    
    public FlumeConsumer(FlumeEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        Responder responder = new SpecificResponder(AvroSourceProtocol.class, new AvroHandler());
    	server = new NettyServer(responder, new InetSocketAddress(endpoint.host, endpoint.port));
        server.start();
    }

    @Override
    protected int poll() throws Exception {
        Exchange exchange = null;
        

        try {
        	if(queue.peek() != null) {
	        	exchange = endpoint.createExchange();
	        	exchange.getIn().setBody(queue.poll());
	            getProcessor().process(exchange);
        	}
            return 1; 
        } finally {
            
            if (exchange != null && exchange.getException() != null) {
                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
        }
    }
    
    @Override
    protected void doStop() throws Exception {
    	if(server != null) {
    		server.close();
    		server.join();
    	}
    }
    class AvroHandler implements AvroSourceProtocol{

		@Override
		public Status append(AvroFlumeEvent evt) throws AvroRemoteException {
			System.out.println(new String(evt.getBody().array(), Charset.forName("UTF8")));
			queue.offer(new String(evt.getBody().array(), Charset.forName("UTF8")));
			return Status.OK;
		}

		@Override
		public Status appendBatch(List<AvroFlumeEvent> evt) throws AvroRemoteException {
			for(AvroFlumeEvent event:evt) {
				System.out.println(new String(event.getBody().array(), Charset.forName("UTF8")));
				queue.offer(new String(event.getBody().array(), Charset.forName("UTF8")));
			}
			return Status.OK;
		}
		
	}
    
}
