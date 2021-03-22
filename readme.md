Repository with invalid api gateway filter which uses external api. 
Filter is written in blocking manner using RestTemplate.

To check api gateway behaviour during handling request with blocking and non-blocking filter in filter chain:
1. Run api-gateway application with parameter test.delay set to 0-10 value (int)  
2. Load src/test/resources/get.jmx to JMeter. Then enable appropriate sampler.




