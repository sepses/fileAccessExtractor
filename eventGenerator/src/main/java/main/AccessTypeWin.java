package main;

import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

public class AccessTypeWin {
	 public static String generateMachine(int nmc) {
	    	String machine = "docker-compose up --scale agent="+nmc;
	    	 return machine;
	    }
	        
	    public static String setLocation(String location) {
	    	 	String command = "cd "+location+" ; echo 'go to "+location+"'";
	    	 return command;
	    }
	    
	    public static String randomFileName() {
	    /*	Random rn = new Random();
	   	     int ran = rn.nextInt(1000000000 - 10000 + 1) + 10000;
	    	
	    	String generatedString = ran+".txt";
	    	return generatedString; */  	
	    	String generatedString = UUID.randomUUID().toString()+".txt";
	    	return generatedString;  
	    }
	    
	    public static String randomDirectoryName() {
	    	Random rn = new Random();
	   	     int ran = rn.nextInt(1000000000 - 10000 + 1) + 10000;
	    	
	    	String generatedString = ran+"";
	    	return generatedString;   	
	    }
	       
	    public static String createInitialDirectory() {
	    	String ranDir = randomDirectoryName();	
	    	String command = "mkdir "+ranDir+" ; echo 'create directory "+ranDir+"'\r\n"+
	    			         "cd "+ranDir+" ; echo 'go to "+ranDir+"'";
	    	return command;
	    }
	    
	    public static String[] createInitialFile(String executionlogdir) {
	    	String initialFile = randomFileName();
	    	String[] command = {null,null};
	    	//String initialFile = prop.getProperty("init_file_name")+ prop.getProperty("init_file_ext");
	    	command[0]= initialFile;
	    	String createLog = "echo '{\"event\":\"created\",\"sourceFile\":\""+initialFile+"\",\"targetFile\":\""+initialFile+"\",\"date\":\"'$(Get-Date -Format o)'\",\"host\":\"'$(hostname)'\",\"user\":\"'$(whoami)'\"}' | Add-Content "+executionlogdir+"log-$(hostname).txt -NoNewline ; '' | Add-Content "+executionlogdir+"log-$(hostname).txt";
	    	command[1] = "New-Item "+initialFile+" ; "+createLog;
	    	    	 return command;
	    }
	    
	    public static String removeFile(String fileName, String executionlogdir) {
	    	String delLog = "echo '{\"event\":\"deleted\",\"sourceFile\":\""+fileName+"\",\"targetFile\":\""+fileName+"\",\"date\":\"'$(Get-Date -Format o)'\",\"host\":\"'$(hostname)'\",\"user\":\"'$(whoami)'\"}' | Add-Content "+executionlogdir+"log-$(hostname).txt -NoNewline ; '' | Add-Content "+executionlogdir+"log-$(hostname).txt";
	    	String command = "rm "+fileName+" ; "+delLog;
	    	return command;
	    }
	    
	      
	    public static String[] renameFile(String fileName, String executionlogdir) {
	    	String renamed_file=randomFileName();
	    	String renLog = "echo '{\"event\":\"renamed\",\"sourceFile\":\""+fileName+"\",\"targetFile\":\""+renamed_file+"\",\"date\":\"'$(Get-Date -Format o)'\",\"host\":\"'$(hostname)'\",\"user\":\"'$(whoami)'\"}' | Add-Content "+executionlogdir+"log-$(hostname).txt -NoNewline ; '' | Add-Content "+executionlogdir+"log-$(hostname).txt";
	    	String command = "mv "+fileName+" "+renamed_file+" ; "+renLog;
	    	String[] a = {command,renamed_file};
	    	return a;
	    }
	    
	    public static String[] copyFile(String fileName, String executionlogdir) {
	    	String copied_file=randomFileName();
	    	String copyLog = "echo '{\"event\":\"copied\",\"sourceFile\":\""+fileName+"\",\"targetFile\":\""+copied_file+"\",\"date\":\"'$(Get-Date -Format o)'\",\"host\":\"'$(hostname)'\",\"user\":\"'$(whoami)'\"}' | Add-Content "+executionlogdir+"log-$(hostname).txt -NoNewline ; '' | Add-Content "+executionlogdir+"log-$(hostname).txt";
		    
	    	String command = "cp "+fileName+" "+copied_file+" ; "+copyLog;
	    
	    	String[] a = {command,copied_file};
	    	return a;
	    }
	    
	    public static String[] modifyFile(String fileName, String executionlogdir) {
	    	Date date= new Date();
	    	long lt = date.getTime();
	    	String modifLog = "echo '{\"event\":\"modified\",\"sourceFile\":\""+fileName+"\",\"targetFile\":\""+fileName+"\",\"date\":\"'$(Get-Date -Format o)'\",\"host\":\"'$(hostname)'\",\"user\":\"'$(whoami)'\"}' | Add-Content "+executionlogdir+"log-$(hostname).txt -NoNewline ; '' | Add-Content "+executionlogdir+"log-$(hostname).txt";
		    
	    	String command = "echo 'this file has been modified on "+lt+" ' | Add-Content "+fileName+" ; "+modifLog;
	    	String[] a = {command,fileName};
	    	return a;
	    }
	    
	    public static String secureCopyFile(String fileName, String agent, String agentdirectory) {
	    	String scpcommand = "sshpass -p 'root' scp -o 'StrictHostKeyChecking no' "+fileName+" "+agent+":"+agentdirectory;
	    	//String command = "cp "+fileName+" copy_of_"+fileName+" ; echo 'copy "+fileName+" to copy_of_"+fileName+"'\r\n";
	    	return scpcommand;
	    }
	    
		public static String[] secureCopyFileOtherMachine(String fileName, String agent, String agentdirectory, String executionlogdir) {
			//String secCopyLog = "echo '{\"event\":\"securedCopied\",\"sourceFile\":\""+fileName+"\",\"targetFile\":\""+fileName+"\",\"date\":\"'$(Get-Date -Format o)'\",\"sourceHost\":\"'$(hostname)'\",\"targetHost\":\""+agent+"\",\"user\":\"'$(whoami)'\"}' | Add-Content "+executionlogdir+"log-$(hostname).txt -NoNewline ; '' | Add-Content "+executionlogdir+"log-$(hostname).txt";
		   String secCopyLog="";
			String scpcommand = "sshpass -p 'root' scp -o 'StrictHostKeyChecking no' "+fileName+" "+agent+":"+agentdirectory+" ; "+secCopyLog;
	    	String command = scpcommand;
	    	String[] a = {command,fileName};
	    	return a;
	    }
		
	    
	    public static String secureRemoteFileExecution(String agentDirCmd, String fileName, String agent) {
	    	String scpcommand = "sshpass -p 'root' ssh -o 'StrictHostKeyChecking no' "+agent+" "+"'screen -d -m "+agentDirCmd+fileName+"'";
	    	return scpcommand;
	    }
	    
	    public static String secureRemoteFileExecutionWoScreen(String agentDirCmd, String fileName, String agent) {
	    	String scpcommand = "sshpass -p 'root' ssh -o 'StrictHostKeyChecking no' "+agent+" "+"'"+agentDirCmd+fileName+"'";
	    	return scpcommand;
	    }
	    
}
