package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Test {
	//public static HashMap<Integer, String> fileBucket = new HashMap<Integer, String>();
	public static HashMap<String, List<String>> map = new HashMap<String, List<String>>();
	
	public static void main( String[] args ) throws IOException
    {
		
		  
		map.put("1", new ArrayList<String>());
		
		map.get("1").add("test.txt");
		map.get("1").add("test2.txt");
		map.get("1").add("test3.txt");
		
		for(int i=0;i<map.get("1").size();i++) {
			System.out.println(map.get("1").get(i));
		}
		
    }

}
