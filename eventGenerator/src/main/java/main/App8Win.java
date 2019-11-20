package main;
import java.io.FileInputStream;
import main.AccessTypeWin;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;


/**
 * Hello world!
 *
 */
public class App8Win

    
{
	
	public static HashMap<Integer, List<String>> agentBucket = new HashMap<Integer, List<String>>();
	
    public static void main(String[] args) throws Exception {
    	Properties prop =  new Properties();
    	FileInputStream ip= new FileInputStream("config2.properties");
    	prop.load(ip);
    	generateCrossRandom(prop);
    }
	
    
    public static void generateCrossRandom(Properties prop) throws IOException
    {
    	 
    	 String nMachine=prop.getProperty("machine");
    	 int nmc =  Integer.parseInt(nMachine);
         String agent_name = prop.getProperty("agent_name");
   		 String output_host_dir = prop.getProperty("output_host_dir");
   		 String outputfile = prop.getProperty("output_file_name");
   		 String outputfileext = prop.getProperty("output_file_ext"); 
   		 String output_agent_dir = prop.getProperty("output_agent_dir"); 
   		 String location = prop.getProperty("location"); 
   		 String agentcmddir = prop.getProperty("agent_cmd_dir"); 
         String duration=prop.getProperty("duration");
         String interval=prop.getProperty("interval");
         String executionlogdir=prop.getProperty("execution_log_dir");
         int m = Integer.parseInt(duration);
         int s = Integer.parseInt(interval);
         String tempCommand="";
     	 String SL = "cd "+location+" ; echo 'go to "+location+"'" ;
     	 String firstcommand = ""+SL;
     	 String endcommand ="\ntail -f";
     	 String con="";
     	 
     	    //this is the core, generate random cross file access 
     	    randomFileAccess(m,s,location,agent_name,nmc,executionlogdir);
     	  
     	   
     	 int ran = simpleRandomMinMax(10000,1000000000); //this is for identifying file SH of agent
         for(int i=1;i<=nmc;i++) {
        	 //get command from agent bucket
        	 String tempc="";
        	 for(int k=0;k<agentBucket.get(i).size();k++) {
        		 tempc = tempc+agentBucket.get(i).get(k);
        	 }
        	 tempc = firstcommand+tempc+endcommand;
        	 
        	 //generateSHFile for each agent
        	  String gfa = generateFileAccess(i, outputfile, outputfileext, output_agent_dir, ran, tempc);	 
        	  
        	//generate central command for basehost
        	 if (tempCommand!="") {
         		con=" ; ";
         	 }
         	 tempCommand = tempCommand+con+AccessTypeWin.secureRemoteFileExecution(agentcmddir,gfa,agent_name+"_"+i);
         }
         
         //generate SH File for basehost
         FileWriter fw=new FileWriter(output_host_dir+"start.sh");
         //String virtuosoCommand = "sshpass -p 'root' ssh -o 'StrictHostKeyChecking no' dockerfiles_virtuoso_1 'screen -d -m /bin/start.sh'";
        // fw.write(virtuosoCommand+" ; " +tempCommand);
         fw.write(tempCommand);
         fw.close();    
         System.out.print(AccessTypeWin.generateMachine(nmc));
    	
    }
    
    public static void randomFileAccess(int minutes, int interval, String location, String agent, int nmc, String executionlogdir) throws IOException {
    	
    	//generate bucket for file
    	RandomCrossOperationWin.generateBucketforFile(nmc);
    	//generate bucket for command
    	generateBucketforCommand(nmc);
    	  	
    	//calculate duration
    	int nc = (minutes*60/interval)*nmc;
    	String command= "";
    	for(int i=1;i<=nc;i++) {
    		//choose random agent
    		int ra = simpleRandom(nmc);
    		//generate random command
   	        	command = RandomCrossOperationWin.generateRandomOperation(agent,nmc,location,ra,executionlogdir);
   	        	//populate random command to the respective agent
   	        	agentBucket.get(ra).add("\n"+command+"\nsleep "+interval+" ; echo 'sleep for "+interval+" second(s)'");
    	 }
    	
    }
    
    
    
    private static int simpleRandomMinMax(int min, int max) {
    	Random random = new Random();
	        int ra = random.nextInt(max - min + 1) + min;
		return ra;
	}
    
    private static int simpleRandom(int nmc) {
    	Random random = new Random();
	        int ra = random.nextInt(nmc - 1 + 1) + 1;
		return ra;
	}


	private static void generateBucketforCommand(int nmc) {
    	for(int k=1;k<=nmc;k++) {
    		agentBucket.put(k, new ArrayList<String>());
    	}
		
	}


	public static String generateFileAccess(int i, String outputfile, String outputfileext,String output_agent_dir, int ran, String fullc) throws IOException {
    	String fileName = outputfile+"_"+ran+"_"+i+outputfileext;	
    	FileWriter fw=new FileWriter(output_agent_dir+fileName);
        fw.write(fullc);    
        fw.close();    
        return fileName;
    	
    }
    
}