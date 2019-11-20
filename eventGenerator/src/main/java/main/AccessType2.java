package main;

import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

public class AccessType2 {
	 public static String generateMachine(int nmc) {
	    	String machine = "docker-compose up --scale agent="+nmc;
	    	 return machine;
	    }
	        
	    public static String setLocation(String location) {
	    	 	String command = "cd "+location+" && echo 'go to "+location+"'";
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
	    	String command = "mkdir "+ranDir+" && echo 'create directory "+ranDir+"'\r\n"+
	    			         "cd "+ranDir+" && echo 'go to "+ranDir+"'";
	    	return command;
	    }
	    
	    public static String[] createInitialFile() {
	    	String initialFile = randomFileName();
	    	String[] command = {null,null};
	    	//String initialFile = prop.getProperty("init_file_name")+ prop.getProperty("init_file_ext");
	    	command[0]= initialFile;
	    	String createLog = "echo '{\"event\":\"create\",\"sourceFile\":\""+initialFile+"\",\"date\":\"$(date --iso-8601=seconds)\",\"host\":\"$(hostname)\",\"user\":\"$(whoami)\"}' >> /var/log/log-$(hostname).txt";
	    	command[1] = "touch "+initialFile+" && "+createLog;
	    	 return command;
	    }
	    
	    public static String removeFile(String fileName) {
	    	String command = "rm "+fileName+" && echo 'remove "+fileName+"'";
	    	return command;
	    }
	    
	      
	    public static String[] renameFile(String fileName) {
	    	String renamed_file=randomFileName();
	    	String command = "mv "+fileName+" "+renamed_file+" && echo 'rename "+fileName+" to "+renamed_file+"'";
	    	String[] a = {command,renamed_file};
	    	return a;
	    }
	    
	    public static String[] copyFile(String fileName) {
	    	String copied_file=randomFileName();
	    	String command = "cp "+fileName+" "+copied_file+" && echo 'copy "+fileName+" to "+copied_file+"'";
	    	String[] a = {command,copied_file};
	    	return a;
	    }
	    
	    public static String[] modifyFile(String fileName) {
	    	Date date= new Date();
	    	long lt = date.getTime();
	    	String command = "echo 'this file has been modified on "+lt+" ' >> "+fileName+" && echo 'modify file "+fileName+"'";
	    	String[] a = {command,fileName};
	    	return a;
	    }
	    
	    public static String secureCopyFile(String fileName, String agent, String agentdirectory) {
	    	String scpcommand = "sshpass -p 'root' scp -o 'StrictHostKeyChecking no' "+fileName+" "+agent+":"+agentdirectory;
	    	//String command = "cp "+fileName+" copy_of_"+fileName+" && echo 'copy "+fileName+" to copy_of_"+fileName+"'\r\n";
	    	return scpcommand;
	    }
	    
		public static String[] secureCopyFileOtherMachine(String fileName, String agent, String agentdirectory) {
	    	String scpcommand = "sshpass -p 'root' scp -o 'StrictHostKeyChecking no' "+fileName+" "+agent+":"+agentdirectory+" && echo 'secure copy "+fileName+" to "+agent+":"+agentdirectory+"'";
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
