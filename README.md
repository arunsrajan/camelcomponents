# camelcomponents
Camel Components Repository
---------------------------

camel-queue
-----------
This is in memory camel queue component. It can have multiple consumers when poll delay different for each consumers. <BR/>

The spring/blueprint DSL code for queue component is given below. <BR/>
Only one consumer will receive the data exactly once even multiple consumers are allowable. <BR/>
```xml
<route id="timerToLog">
      <from uri="timer:foo?period=20000"/>
      <setBody>
          <constant>Camel Queue Component</constant>
      </setBody>
      <log message="${body}"/>
      <to uri="queue:test"/>
      <to uri="queue:test1"/>
    </route>
    
    <route id="queueLog">
      <from uri="queue:test?polldelay=100"/>
      <log message="The message contains: ${body} test"/>
      <to uri="mock:test"/>
    </route>
    
    
    <route id="queueLog2">
      <from uri="queue:test?polldelay=120"/>
      <log message="The message contains: ${body} test2"/>
      <to uri="mock:test"/>
    </route>
    
    <route id="queueLog1">
      <from uri="queue:test1?polldelay=130"/>
      <log message="The message contains: ${body} test1"/>
      <to uri="mock:test1"/>
    </route>
    
    <route id="queueLog3">
      <from uri="queue:test1?polldelay=140"/>
      <log message="The message contains: ${body} test3"/>
      <to uri="mock:test1"/>
    </route>
 ```
 
 The spring/blueprint DSL code for topic component is given below.  <BR/>
 All the consumers with the current data will be received and then the next data is picked .  <BR/>
 
 ```xml
 <route id="timerToLogTopic">
      <from uri="timer:foo?period=20000"/>
      <setBody>
          <constant>Camel Topic Component</constant>
      </setBody>
      <log message="${body}"/>
      <to uri="topic:test"/>
      <to uri="topic:test1"/>
    </route>
    
    <route id="topicLog">
      <from uri="topic:test?polldelay=100"/>
      <log message="The message contains: ${body} testtopic"/>
      <to uri="mock:test"/>
    </route>
    
    
    <route id="topicLog2">
      <from uri="topic:test?polldelay=120"/>
      <log message="The message contains: ${body} testtopic2"/>
      <to uri="mock:test"/>
    </route>
    
    <route id="topicLog1">
      <from uri="topic:test1?polldelay=130"/>
      <log message="The message contains: ${body} testtopic1"/>
      <to uri="mock:test1"/>
    </route>
    
    <route id="topicLog3">
      <from uri="topic:test1?polldelay=140"/>
      <log message="The message contains: ${body} testtopic3"/>
      <to uri="mock:test1"/>
    </route>
 ```
