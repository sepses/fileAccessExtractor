package main;
import java.io.FileInputStream;
import main.AccessType;
import math.WeightedChoice;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;


/**
 * Hello world!
 *
 */
public class App5 
    
{
	
	
    public static void main(String[] args) throws Exception {
    	Properties prop =  new Properties();
    	FileInputStream ip= new FileInputStream("config.properties");
    	prop.load(ip);
    	generateSimpleRandom(prop);
    }
	
    
    public static void generateSimpleRandom(Properties prop) throws IOException
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
     	String con="";
   	     Random rn = new Random();
	     int ran = rn.nextInt(1000000000 - 10000 + 1) + 10000;
         for(int i=1;i<=nmc;i++) {
        	 String gfa = generateFileAccess(m,s,i,location,outputfile,outputfileext,output_agent_dir,ran,agent_name,nmc,executionlogdir);	 
        	 if (tempCommand!="") {
         		con=" && ";
         	 }
         	tempCommand = tempCommand+con+AccessType.secureRemoteFileExecution(agentcmddir,gfa,agent_name+"_"+i);
       	 
         }
         FileWriter fw=new FileWriter(output_host_dir+"start.sh");
         //String virtuosoCommand = "sshpass -p 'root' ssh -o 'StrictHostKeyChecking no' dockerfiles_virtuoso_1 'screen -d -m /bin/start.sh'";
         //fw.write(virtuosoCommand+" && " +tempCommand);
       fw.write(tempCommand);
         
         fw.close();    
         System.out.print(AccessType.generateMachine(nmc));
    	
    }
    
    
    public static String generateFileAccess(int minute, int second,int i, String location,String outputfile, String outputfileext,String output_agent_dir, int ran, String agent,int nmc,String executionlogdir) throws IOException {

    	String fileName = outputfile+"_"+ran+"_"+i+outputfileext;	
    	String rfa = randomFileAccess(minute,second,location,agent,nmc,i,executionlogdir);
    	FileWriter fw=new FileWriter(output_agent_dir+fileName);
        fw.write(rfa);    
        fw.close();    
        return fileName;
    	
    }
    
    
    public static String randomFileAccess(int minutes, int interval, String location, String agent, int nmc,int j,String executionlogdir) throws IOException {

    	int nc = minutes*60/interval;
    	String SL = "cd "+location+" && echo 'go to "+location+"'" ;
    	String firstcommand = ""+SL;
    	String endcommand ="\ntail -f";
    	String fileAccess="";
    	String tempCommand= "";
    	for(int i=1;i<=nc;i++) {
    	   tempCommand = RandomOperation.generateRandomOperation(agent,nmc,location,j,executionlogdir);
    	   fileAccess=fileAccess+"\n"+tempCommand+"\nsleep "+interval+" && echo 'sleep for "+interval+" second(s)'";  
    	     	
    	}
    	//clear the bucket, before next using another agent
    	RandomOperation.clearBucket();
    	String cfileAccess = firstcommand+fileAccess+endcommand;
    	return cfileAccess;
    	//System.exit(0);
    	
    }
    
}