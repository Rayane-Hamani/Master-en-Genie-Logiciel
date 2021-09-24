package agentflopbox;



public class Main
{
	public static final String FLOPBOX = "http://localhost:8080/flopbox/";
	
	public static Agent agent;
	
	
	
    public static void main(String[] args)
    {
    	agent = new Agent();
    	AgentUtil.loadServers(agent.getServers());
		AgentUtil.loadPortsAndUsers(agent.getServers());
    	agent.importRemoteData();
//    	while(true)
//    	{
//    		agent.updateServer();
//    	}
    }
}
