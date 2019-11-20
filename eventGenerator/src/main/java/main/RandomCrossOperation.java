package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import math.WeightedChoice;

public class RandomCrossOperation {
	public static HashMap<Integer, List<String>> fileBucket = new HashMap<Integer, List<String>>();
	   
	    
	    public static String generateRandomOperation(String agent, int nmc, String agentdir, int i, String executionlogdir) throws IOException
	    {
	    	
	    String operation="";
	    	//check if a collection on the respective agent (i) is empty
	    	  if(checkIsBucketEmpty(i)) {
	    		//if yes
	    	       //create a file (nothing else to do)
	    		  operation = createFile(i,executionlogdir);
	    	  }else {
	    		  //if yes
	    		  WeightedChoice action; 
	    		//weighted random action (create, modify, rename, copy, move, delete, scp etc.)
	    		  if (nmc>1) {
	    		    action = weightedChooseRandomAction();
	    		  }else {
	    			//nmc <=1, that's why we don't need SCP
	    		    action = weightedChooseRandomActionWoSCP();	  
	    		  }
	    		  String curAction=action.nextItem().toString();
	    		  //if action is not create, 
	    		      if(!(curAction.equals("6"))) {
	    		    	  //get one of random existing filename to do action
	    		    	 String fileName = getRandomExistingFileName(i);	
	    		    	  //choose command
	    		    	 operation = chooseCommand(curAction,fileName,agent,nmc,agentdir,i,executionlogdir);		    	 
	    		      }else {
	    		    	  operation = createFile(i,executionlogdir);    	    		  
	    		      }          
	    	  }
	    	  return operation;
	    }
	    
	    public static String chooseCommand(String code, String tf, String agent, int nmc, String location, int i, String executionlogdir) {
	    	String ac= "";
	    	switch(code) {
	    	  case "1":
	    		  String[] rn = AccessType.renameFile(tf,executionlogdir);
	    		  removeFilefromBucket(i,tf);
	    		  storeFiletoBucket(i,rn[1]);
	    		  ac= rn[0];	  
	    	    // rename
	    	    break;
	    	  case "2":
	    		  String[] cp = AccessType.copyFile(tf,executionlogdir);
	    		  storeFiletoBucket(i,cp[1]);
	    		  ac = cp[0];
	    		  //copy
	    		break;
	    	  case "3":
	    		  String rm = AccessType.removeFile(tf,executionlogdir);
	    		  removeFilefromBucket(i, tf);
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
	    		  storeFiletoBucket(ran,sc[1]);
	    		  ac = sc[0];	  
	    		//scp
	    	}
	    
	    	return ac;
	    }

	  public static boolean checkIsBucketEmpty(int i) {
	    		 
	    		  int s = fileBucket.get(i).size();
	    		  if(s<=0) {
	    			  return true;
	    		  } else {
	    		      return false;
	    		 }
	    	  }
	    	  
	    private static WeightedChoice weightedChooseRandomActionWoSCP() {
	    			HashMap<Integer, Double> item_weights = new HashMap<Integer, Double>();
	    			item_weights.put(1,0.7d); //rename
	    			item_weights.put(2,0.5d); //copy
	    			item_weights.put(3,0.2d); //remove
	    			item_weights.put(4,0.5d); //modify
	    			//item_weights.put(5,0.2d); //securecopy
	    			item_weights.put(6,0.2d); //create
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
	    	  
	    private static String getRandomExistingFileName(int i) {
	  		   Random rn = new Random();
	  	 	   int ran = rn.nextInt(fileBucket.get(i).size() - 1 + 1) + 1;
	  	 	   String fileName = getFileBucket(i,ran-1);
	  		   return fileName;
	  		}
	    
	 
	    public static String createFile(int i, String executionlogdir) {
	    			   String[] filex = AccessType.createInitialFile(executionlogdir);
	    			   storeFiletoBucket(i,filex[0]);
	    			   return filex[1];
	    }
	    		
	    public static String getFileBucket(int i, int j) {
	    			String fileName = fileBucket.get(i).get(j);
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
	    
	    public static void generateBucketforFile(int nmc) {
	    	for(int k=1;k<=nmc;k++) {
	    		fileBucket.put(k, new ArrayList<String>());
	    	}
	    }
	    
	    private static void removeFilefromBucket(int i,String tf) {
			fileBucket.get(i).remove(tf);
		}
	    
	    public static void storeFiletoBucket(int i,String fileName) {
			  fileBucket.get(i).add(fileName);
		  }
	    
}
