import java.util.HashMap;
import java.util.Map;
 
public class HashMapDemo{
public static void main(String[] args){
HashMap<String, Map<String, Object>> map = new HashMap<String,  Map<String, Object>>();
String data="test";
// mapping beberapa data dalam map
map.get(data).put("alive", true);

 
// membaca data dari map

 

for(String key: map.keySet()){
System.out.println("Map: " + key + " Sama dengan " + map.get(key));
}
}
}