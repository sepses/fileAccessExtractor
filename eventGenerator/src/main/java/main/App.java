package main;
import java.io.FileInputStream;
import main.AccessType;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;


/**
 * Hello world!
 *
 */
public class App 
{
	
    public static void main(String[] args) throws Exception {
    	Properties prop =  new Properties();
    	FileInputStream ip= new FileInputStream("config.properties");
    	prop.load(ip);
    	generateSimpleRandom(prop);
    }
	
    
    public static void generateSimpleRandom(Properties prop) throws IOException
    {
    	//System.out.print(randomFileAccess(1,5));
    	//  FileInputStream ip = new FileInputStream("config.properties");
    	//  Properties prop = new Properties();
    	//  prop.load(ip);
    	 
    	 String nMachine=prop.getProperty("machine");
    	 int nmc =  Integer.parseInt(nMachine);
         String agent_name = prop.getProperty("agent_name");
   		 String output_host_dir = prop.getProperty("output_host_dir");
   		 String outputfile = prop.getProperty("output_file_name");
   		 String outputfileext = prop.getProperty("output_file_ext"); 
   		 String output_agent_dir = prop.getProperty("output_agent_dir"); 
   		 String location = prop.getProperty("location"); 
   		 String agentcmddir = prop.getProperty("agent_cmd_dir"); 
   	     String executionlogdir=prop.getProperty("execution_log_dir");
        	//System.exit(0);   
         String duration=prop.getProperty("duration");
         String interval=prop.getProperty("interval");
         int m = Integer.parseInt(duration);
         int s = Integer.parseInt(interval);
         
         String tempCommand="";
     	//System.out.print(tempCommand);System.exit(0);
     	String con="";
   	     Random rn = new Random();
	     int ran = rn.nextInt(1000000000 - 10000 + 1) + 10000;
         for(int i=1;i<=nmc;i++) {
        	 String gfa = generateFileAccess(prop,m,s,i,location,outputfile,outputfileext,output_agent_dir,ran,executionlogdir);	 
        	 if (tempCommand!="") {
         		con=" && ";
         	 }
         	tempCommand = tempCommand+con+AccessType.secureRemoteFileExecution(agentcmddir,gfa,agent_name+"_"+i);
       	 
         }
         FileWriter fw=new FileWriter(output_host_dir+"start.sh");
         fw.write(tempCommand);    
         fw.close();    
         //generateCentralCommand(nmc,agent_name,outputfile,outputfileext,outputdir, agentcmddir);
         System.out.print(AccessType.generateMachine(nmc));
    	
    }
    
   /* public static String generateCentralCommand(int nmc, String agent_name, String outputfile,String output_file_ext,String outputdir, String agentCmdDir) throws IOException {
    	String tempCommand="";
    	//System.out.print(tempCommand);System.exit(0);
    	String outputfiles="";
    	String con="";
        for(int i=1;i<=nmc;i++) {
        	if (tempCommand!="") {
        		con=" && ";
        	}
        	outputfiles = outputfile+"_"+i+output_file_ext;
        	tempCommand = tempCommand+con+AccessType.secureRemoteFileExecution(agentCmdDir,outputfiles,agent_name+"_"+i);
        }
    	
		FileWriter fw=new FileWriter(outputdir+"start.sh");
        fw.write(tempCommand);    
        fw.close();    
        return tempCommand;
        
    }*/
    
    public static String generateFileAccess(Properties prop,int minute, int second,int i, String location,String outputfile, String outputfileext,String output_agent_dir, int ran, String executionlogdir) throws IOException {

    	String fileName = outputfile+"_"+ran+"_"+i+outputfileext;	
    	String rfa = randomFileAccess(minute,second,location,executionlogdir);
    	FileWriter fw=new FileWriter(output_agent_dir+fileName);
        fw.write(rfa);    
        fw.close();    
        return fileName;
    	
    }
    
    
    public static String randomFileAccess(int minutes, int interval, String location, String executionlogdir) {

    	int nc = minutes*60/interval;
    	//System.out.print(nc);System.exit(0);
    	String dir = AccessType.createInitialDirectory();
    	String SL = "cd "+location+" && echo 'go to "+location+"'\r\n" ;
    	String firstcommand = ""+SL;
    	String endcommand ="\ntail -f";
    	String fileAccess="";
    	String[] tempCommand= {null,null};
    	String tempFile="esteh.txt";
    	for(int i=1;i<=nc;i++) {
    		//random command
    	   tempCommand = randomCommand(tempCommand[0],tempFile,executionlogdir);
    	   //System.out.print(tempFile);
    	   fileAccess=fileAccess+"\n"+tempCommand[1]+"\nsleep "+interval+" && echo 'sleep for "+interval+" second(s)'";  
    	   //System.out.print(tempCommand[2]);
    	   tempFile=tempCommand[2];
    	}
    	String cfileAccess = firstcommand+dir+fileAccess+endcommand;
    	return cfileAccess;
    	//System.exit(0);
    	
    }
    
    public static String[] randomCommand(String tc, String tf, String executionlogdir) {
    	
    	int ran=5;
    	
    	if(tc=="delete") {
    		ran = 5;
    	}else if(tc==null){
    		ran = 5;
    	} else {
    		Random rn = new Random();
        	 ran = rn.nextInt(5 - 1 + 1) + 1;
        	//System.out.print(ran);System.exit(0);
        	 if(ran==5 && tc!="delete") {
        		 ran = rn.nextInt(4 - 1 + 1) + 1;
        	 }
    	}
    		
    	String[] rc = chooseCommand(ran,tf,executionlogdir);
    	return rc;
    }
    
    public static String[] chooseCommand(int code, String tf, String executionlogdir) {
    	String[] ac= {null,null,null};
    	switch(code) {
    	  case 1:
    		  String[] rn = AccessType.renameFile(tf,executionlogdir);
    		  ac[0] = "rename";
    		  ac[1] = rn[0];
    		  ac[2] = rn[1];
    	    // rename
    	    break;
    	  case 2:
    		  String[] cp = AccessType.copyFile(tf,executionlogdir);
    		  ac[0] = "copy";
    		  ac[1] = cp[0];
    		  ac[2] = cp[1];
    		  //copy
    		break;
    	  case 3:
    		  String rm = AccessType.removeFile(tf,executionlogdir);
    		  ac[0] = "delete";
    		  ac[1] = rm;
    		  ac[2]=  "";
    		  //modify
    		break;
    	  case 4:
    		  String[] md = AccessType.modifyFile(tf,executionlogdir);
    		  ac[0] = "modify";
    		  ac[1] = md[0];
    		  ac[2]=  md[1];
    		  //delete
    		break;    		  
    	  default:
    		  String[] cr = AccessType.createInitialFile(executionlogdir);
    		  ac[0] = "create";
    		  ac[1] = cr[1];
    		  ac[2]=  cr[0];
    		  //create
    	}
    	//System.out.print(ac[0]);System.exit(0);
    	return ac;
    }
    
    
}