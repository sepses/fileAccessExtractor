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
public class App3 
{
	
    public static void main(String[] args) throws Exception {
    	Properties prop =  new Properties();
    	FileInputStream ip= new FileInputStream("config.properties");
    	prop.load(ip);
    	generateCrossRandom(prop);
    }
    
    public static void generateCrossRandom(Properties prop) throws IOException
    {
    	//System.out.print(randomFileAccess(1,5));
    	//  FileInputStream ip = new FileInputStream("config.properties");
    	//  Properties prop = new Properties();
    	 // prop.load(ip);
    	 
    	 String nMachine=prop.getProperty("machine");
    	 int nmc =  Integer.parseInt(nMachine);
         String agent_name = prop.getProperty("agent_name");
   		 String output_host_dir = prop.getProperty("output_host_dir");
   		 String outputfile = prop.getProperty("output_file_name");
   		 String outputfileext = prop.getProperty("output_file_ext"); 
   		 String output_agent_dir = prop.getProperty("output_agent_dir"); 
   		 String location = prop.getProperty("location"); 
   		 String agentcmddir = prop.getProperty("agent_cmd_dir"); 
        	//System.exit(0);   
         String duration=prop.getProperty("duration");
         String interval=prop.getProperty("interval");
         String executionlogdir=prop.getProperty("execution_log_dir");
         int m = Integer.parseInt(duration);
         int s = Integer.parseInt(interval);
         
         int ran = randomFileAccess(m, s, location, agent_name, nmc, agentcmddir, output_agent_dir, outputfile,outputfileext,executionlogdir);
         int rm = randomMachine(nmc);
         String gfa = outputfile+"_sub_"+ran+"_1"+outputfileext;
         String tempCommand = AccessType.secureRemoteFileExecutionWoScreen(agentcmddir,gfa,agent_name+"_"+rm);
       	 

         FileWriter fw=new FileWriter(output_host_dir+"start.sh");
         fw.write(tempCommand);    
         fw.close();    
         //generateCentralCommand(nmc,agent_name,outputfile,outputfileext,outputdir, agentcmddir);
         System.out.print(AccessType.generateMachine(nmc));
    	
    }
    
    
    public static void generateFileAccess(String fileAccess,String output_agent_dir,int ran, int j, String of, String ofx) throws IOException {
    	int k = j;
    	FileWriter fw=new FileWriter(output_agent_dir+of+"_sub_"+ran+"_"+k+ofx);
        fw.write(fileAccess);    
        fw.close();    
    	
    }
    
    
    public static int randomFileAccess(int minutes, int interval, String location,String agent, int nmc, String agentcmddir, String output_agent_dir, String outputfile, String outputfileext, String executionlogdir) throws IOException {
          Random rn = new Random();
 	     int ran = rn.nextInt(1000000000 - 10000 + 1) + 10000;
    	int nc = minutes*60/interval;
    	String firstcommand = "cd "+location+" && echo 'go to "+location+"'" ;
    	String fileAccess="";
    	String[] tempCommand= {null,null};
    	String tempFile="esteh.txt";
    	int j=1;
    	   
    	for(int i=1;i<=nc;i++) {
    		//random command
    	   tempCommand = randomCommand(tempCommand[0],tempFile,agent, location, nmc, agentcmddir,ran, j,outputfile,outputfileext,executionlogdir);
    	   //System.out.print(tempFile);
    	   fileAccess=fileAccess+"\nsleep "+interval+" && echo 'sleep for "+interval+" second(s)'"+"\n"+tempCommand[1];  
    	   //System.out.print(tempCommand[2]);
    	   tempFile=tempCommand[2];
    	
    	   if(tempCommand[0]=="seccopy") {
    		   //create sh file
    		   createShFile(firstcommand+fileAccess,output_agent_dir,ran,j,outputfile,outputfileext);
    		   j++;
    		   fileAccess="";
    	   }
    	}
    	String cfileAccess = firstcommand+fileAccess;
    	    generateFileAccess(cfileAccess,output_agent_dir,ran,j,outputfile,outputfileext);
    	
    	return ran;
    }
    
    public static void createShFile(String command,String agentcmddir,int ran, int j, String of, String ofx) throws IOException {
    	FileWriter fw=new FileWriter(agentcmddir+of+"_sub_"+ran+"_"+j+ofx);
        fw.write(command);    
        fw.close();    
    }
    
    public static String[] randomCommand(String tc, String tf,String agent,String location, int nmc, String agentcmddir,int r,int j,String of, String ofx, String executionlogdir) {
    	
    	int ran=6;
    	
    	if(tc=="delete") {
    		ran = 6;
    	}else if(tc==null){
    		ran = 6;
    	} else {
    		Random rn = new Random();
        	 ran = rn.nextInt(6 - 1 + 1) + 1;
        	//System.out.print(ran);System.exit(0);
        	 if(ran==6 && tc!="delete") {
        		 ran = rn.nextInt(5 - 1 + 1) + 1;
        	 }
        	 if(ran==5 && tc=="seccopy") {
        		 ran = rn.nextInt(4 - 1 + 1) + 1;
        	 }
 
    	}
    		
    	String[] rc = chooseCommand(ran,tf,agent,location,nmc,agentcmddir,r,j,of,ofx,executionlogdir);
    	return rc;
    }
    
    public static String[] chooseCommand(int code, String tf,String agent,String location, int nmc, String agentcmddir,int r, int j, String of, String ofx, String executionlogdir) {
    	String[] ac= {null,null,null,null};
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
    	  case 5:
    		  int ran = randomMachine(nmc);
    		  String agentname = agent+"_"+ran;
    		  String[] sc = secureCopyFileOtherMachine(tf, agentname, location, agentcmddir,r,j,of,ofx,executionlogdir);
    		  ac[0] = "seccopy";
    		  ac[1] = sc[0];
    		  ac[2] =  sc[1];
    		  ac[3] = agentname;
    		  //seccopy
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
    
    public static int randomMachine(int nmc) {
    	Random rn = new Random();
    	 int ran = rn.nextInt(nmc - 1 + 1) + 1;
    	 return ran;
    }
    
	public static String[] secureCopyFileOtherMachine(String fileName, String agent, String agentdirectory,String agentcmddir, int r,int j,String of, String ofx, String executionlogdir) {
    	String scpcommand = "sshpass -p 'root' scp -o 'StrictHostKeyChecking no' "+fileName+" "+agent+":"+agentdirectory+" && echo 'secure copy "+fileName+" to "+agent+":"+agentdirectory+"'\n";
    	String sfx = secureRemoteFileExecutionWoDaemon(agentcmddir,fileName,agent,r,j,of,ofx);
    	String command = scpcommand+sfx;
    	String[] a = {command,fileName};
    	return a;
    }
	
    public static String secureRemoteFileExecutionWoDaemon(String agentDirCmd, String fileName, String agent,int r,int j, String of, String ofx) {
    	int k = j+1;
    	String fileExeName = of+"_sub_"+r+"_"+k+ofx;
    	String scpcommand = "sshpass -p 'root' ssh -o 'StrictHostKeyChecking no' "+agent+" "+"'"+agentDirCmd+fileExeName+"' && echo 'fire "+fileExeName+" in "+agent+":"+agentDirCmd+"'";
    	return scpcommand;
    }

}