package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import math.WeightedChoice;

public class RandomOperation {
	public static ArrayList<String> fileBucket = new ArrayList<String>();
	 public static String chooseCommand(String code, String tf, String agent, int nmc, String location, int i, String executionlogdir) {
	    	String ac= "";
	    	switch(code) {
	    	  case "1":
	    		  String[] rn = AccessType.renameFile(tf,executionlogdir);
	    		  removeFilefromBucket(tf);
	    		  storeFiletoBucket(rn[1]);
	    		  ac= rn[0];
	    		  
	    	    // rename
	    	    break;
	    	  case "2":
	    		  String[] cp = AccessType.copyFile(tf,executionlogdir);
	    		  storeFiletoBucket(cp[1]);
	    		  ac = cp[0];
	    		  //copy
	    		break;
	    	  case "3":
	    		  String rm = AccessType.removeFile(tf,executionlogdir);
	    		  removeFilefromBucket(tf);
	    		  ac = rm;
	    		  //delete
	    		break;
	    	  case "4":
	    		  String[] md = AccessType.modifyFile(tf,executionlogdir);
	    		  ac = md[0];
	    		  //modify
	    		break;
	    	  default:
	    		  int ran = randomMachine(nmc,i);
	    		  String agentname = agent+"_"+ran;
	    		  String[] sc = AccessType.secureCopyFileOtherMachine(tf, agentname, location,executionlogdir);
	    		  ac = sc[0];
	    		  
	    		//scp
	    	}
	    
	    	return ac;
	    }
	    
	    private static void removeFilefromBucket(String tf) {
			fileBucket.remove(tf);
		}
	    
	    public static void storeFiletoBucket(String fileName) {
			  fileBucket.add(fileName);
		  }
	    
	    public static String generateRandomOperation(String agent, int nmc, String agentdir, int i, String executionlogdir) throws IOException
	    {
	    	
	    String operation="";
	    	//check if a collection is empty
	    	  if(checkIsBucketEmpty()) {
	    		//if yes
	    	       //create a file (nothing else)
	    		  operation = createFile(executionlogdir);
	    	  }else {
	    		  //if yes
	    		  WeightedChoice action; 
	    		//weighted random action (create, modify, rename, copy, move, delete, scp etc.)
	    		 /* if (nmc>1) {
	    		    action = weightedChooseRandomAction();
	    		  }else {
	    		    action = weightedChooseRandomActionWoSCP();	  
	    		  }*/
	    		  action = weightedChooseRandomActionWoSCP();	
	    		  String curAction=action.nextItem().toString();
	    		  //if action is not create
	    		      if(!(curAction.equals("6"))) {
	    		    	 String fileName = getRandomExistingFileName();	    	
	    		    	  operation = chooseCommand(curAction,fileName,agent,nmc,agentdir,i,executionlogdir);		    	 
	    		      }else {
	    		    	  operation = createFile(executionlogdir);    	    		  
	    		      }          
	    	  }
	    	  return operation;
	    }
	    	  public static boolean checkIsBucketEmpty() {
	    		  int s = fileBucket.size();
	    		  if(s<=0) {
	    			  return true;
	    		  } else {
	    		      return false;
	    		 }
	    	  }
	    	  
	    	  private static WeightedChoice weightedChooseRandomActionWoSCP() {
	    			HashMap<Integer, Double> item_weights = new HashMap<Integer, Double>();
	    			item_weights.put(1,0.5d); //rename
	    			item_weights.put(2,0.5d); //copy
	    			item_weights.put(3,0.5d); //remove
	    			item_weights.put(4,0.5d); //modify
	    			//item_weights.put(5,0.2d); //securecopy
	    			item_weights.put(6,0.5d); //create
	    		    WeightedChoice weightedChoice = new WeightedChoice<>(item_weights);    
	    		   return weightedChoice;
	    		}
	    	  
	    	  private static WeightedChoice weightedChooseRandomAction() {
	    			HashMap<Integer, Double> item_weights = new HashMap<Integer, Double>();
	    			item_weights.put(1,0.7d); //rename
	    			item_weights.put(2,0.5d); //copy
	    			item_weights.put(3,0.2d); //remove
	    			item_weights.put(4,0.5d); //modify
	    			item_weights.put(5,0.2d); //securecopy
	    			item_weights.put(6,0.2d); //create
	    		    WeightedChoice weightedChoice = new WeightedChoice<>(item_weights);    
	    		   return weightedChoice;
	    		}
	    	  
	    private static String getRandomExistingFileName() {
	  		   Random rn = new Random();
	  	 	   int ran = rn.nextInt(fileBucket.size() - 1 + 1) + 1;
	  	 	   String fileName = getFileBucket(ran-1);
	  		   return fileName;
	  		}
	    public static String createFile(String executionlogdir) {
	    			   String[] filex = AccessType.createInitialFile(executionlogdir);
	    			   storeFiletoBucket(filex[0]);
	    			   return filex[1];
	    }
	    		
	    public static String getFileBucket(int i) {
	    			String fileName = fileBucket.get(i);
	    			return fileName;
	    }
	    
	    public static int randomMachine(int nmc, int i) {
	    	Random rn = new Random();
	    	 int ran = rn.nextInt(nmc - 1 + 1) + 1;
	    	 if(ran==i) {
	    		ran = randomMachine(nmc,i);
	    	 }
	    	 return ran;
	    }
	    
	    public static void clearBucket() {
	    	fileBucket.clear();
	    }
}
