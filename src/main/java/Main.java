import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.shaded.com.google.common.base.Strings;
import com.github.jsonldjava.utils.JsonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        InputStream inputStream = null;
        Object jsonObject = null;


        inputStream = new FileInputStream("resources/wineventlog1.json");
        jsonObject = JsonUtils.fromInputStream(inputStream);
        


        //Map context = new HashMap();
        //JsonLdOptions options = new JsonLdOptions();
        //Object compact = JsonLdProcessor.compact(jsonObject, context, options);
        //String jsonLdLine = "{\"handleID\":\"0x1ac0\",\"computerName\":\"VirtualWindows8\",\"logonID\":\"0xb74d\",\"processName\":\"C:\\\\Windows\\\\explorer.exe\",\"eventID\":4656,\"level\":\"Information\",\"originatesFrom\":{\"hostName\":{\"name\":\"VirtualWindows8\"},\"@type\":\"rdf:nodeID\",\"@id\":\"http://example.org/sepses#c72594d5-54a5-477d-b972-813a30c3d0e3\"},\"@type\":\"http://purl.org/sepses/vocab/log/wineventlog#WindowsEventLogEntry\",\"logName\":\"Security\",\"@context\":\"http://sepses.ifs.tuwien.ac.at/contexts/wineventlog.jsonld\",\"hasLogType\":\"wineventlog\",\"source\":\"Microsoft-Windows-Security-Auditing\",\"resourceAttributes\":\"-\",\"objectType\":\"File\",\"objectName\":\"C:\\\\Users\\\\VirtualWindows8User\\\\UserDocuments\",\"logMessage\":\"A handle to an object was requested.\\n\\nSubject:\\n\\tSecurity ID:\\t\\tS-1-5-21-2361021202-3253521353-2316283985-1001\\n\\tAccount Name:\\t\\tVirtualWindows8User\\n\\tAccount Domain:\\t\\tVirtualWindows8\\n\\tLogon ID:\\t\\t0xB74D\\n\\nObject:\\n\\tObject Server:\\t\\tSecurity\\n\\tObject Type:\\t\\tFile\\n\\tObject Name:\\t\\tC:\\\\Users\\\\VirtualWindows8User\\\\UserDocuments\\n\\tHandle ID:\\t\\t0x1ac0\\n\\tResource Attributes:\\t-\\n\\nProcess Information:\\n\\tProcess ID:\\t\\t0x3ec\\n\\tProcess Name:\\t\\tC:\\\\Windows\\\\explorer.exe\\n\\nAccess Request Information:\\n\\tTransaction ID:\\t\\t{00000000-0000-0000-0000-000000000000}\\n\\tAccesses:\\t\\tSYNCHRONIZE\\n\\t\\t\\t\\tReadData (or ListDirectory)\\n\\t\\t\\t\\tReadAttributes\\n\\t\\t\\t\\t\\n\\tAccess Reasons:\\t\\tSYNCHRONIZE:\\tGranted by\\tD:(A;OICI;FA;;;S-1-5-21-2361021202-3253521353-2316283985-1001)\\n\\t\\t\\t\\tReadData (or ListDirectory):\\tGranted by\\tD:(A;OICI;FA;;;S-1-5-21-2361021202-3253521353-2316283985-1001)\\n\\t\\t\\t\\tReadAttributes:\\tGranted by\\tD:(A;OICI;FA;;;S-1-5-21-2361021202-3253521353-2316283985-1001)\\n\\t\\t\\t\\t\\n\\tAccess Mask:\\t\\t0x100081\\n\\tPrivileges Used for Access Check:\\t-\\n\\tRestricted SID Count:\\t0\",\"timestamp\":\"2018-08-22T13:33:52.655Z\",\"accesses\":\"%%1541\\n\\t\\t\\t\\t%%4416\\n\\t\\t\\t\\t%%4423\\n\\t\\t\\t\\t\",\"accountDomain\":\"VirtualWindows8\",\"keyword\":[\"Audit Success\"],\"@id\":\"http://example.org/logEntry#logEntry-195fc831-502d-48e9-bcf5-2169c0ddf712\",\"processID\":\"0x3ec\",\"accountName\":\"VirtualWindows8User\",\"version\":1,\"taskCategory\":\"File System\",\"upCode\":\"Info\",\"accessMask\":\"0x100081\",\"@timestamp\":\"2018-08-22T13:33:52.655Z\",\"objectServer\":\"Security\"}";

        //ArrayList c = (ArrayList)((Map) compact).get("@graph");

        ArrayList list = (ArrayList)jsonObject;
        for (Object entry : list) {
            processWinEvent((Map)entry);
        }
        //System.out.println("");
    }

    private static HashMap<String, Map<String, Object>> pendingRemoves = new HashMap<String, Map<String, Object>>();
    private static HashMap<String, Map<String, Object>> handles = new HashMap<String, Map<String, Object>>();
    private static HashMap<String, String> changes = new HashMap<String, String>();

    private static String getFileName(String pathString){
       if(!Strings.isNullOrEmpty(pathString)) {
           File file = new File(pathString);
           return file.getName();
       }
       return "";
    }

    private static String getFolderName(String pathString){
        if(!Strings.isNullOrEmpty(pathString)) {
            File file = new File(pathString);
            return file.getParent();
        }
        return "";
    }

    private static boolean isTemporary(String fileName){
        if(fileName == null || fileName.equals(""))
            return false;

        if(fileName.startsWith("~")
            || fileName.equals("thumbs.db")
            || fileName.endsWith(".dat"))
            return true;

        return false;
    }

    private static boolean isFileName(String fileName) {
        return fileName.contains(".");
    }

    private static void processWinEvent(Map  jsonLdObject) {
        String objectName = (String)jsonLdObject.get("objectName");
        String accessMask = (String)jsonLdObject.get("accessMask");
        Integer eventId = (Integer) jsonLdObject.get("eventID");
        String processName = (String) jsonLdObject.get("processName");
        String accountName = (String)jsonLdObject.get("accountName");
        String fileName = getFileName(objectName);
        String handleId = (String)jsonLdObject.get("handleID");
        
//        System.out.println("\"objectName\":"+objectName+",");
//        System.out.println("\"objectName\":"+objectName+",");
//        System.out.println("\"objectName\":"+objectName+",");
//        System.out.println("\"objectName\":"+objectName+",");

        //if(isFileName((fileName)))
        //  System.out.println("File found: " + objectName + " ID:" + eventId + " Access:" + accessMask + " Handle: " + jsonLdObject.get("handleID"));
        //if(true) return;

        switch(eventId)
        {
            case 4656:
                if(!isTemporary(fileName) && pendingRemoves.containsKey(objectName)) {
                    pendingRemoves.get(objectName).put("alive", true);
                }
                break;

            case 4663:
                if(isTemporary(fileName)) break;

                if(isFileName(fileName)){
                    if(handles.containsKey(handleId)){
                        String handleObjectName = (String) handles.get(handleId).get("objectName");
                        if(!handleObjectName.equals(objectName))
                            changes.put(objectName, handleObjectName);
                            //System.out.println("Change: " + handleObjectName + " - " + objectName);
                    } else{
                        //System.out.println("Access: " + objectName);
                        handles.put(handleId, jsonLdObject);
                    }
                }

                if(accessMask.equals("0x10000")){
                    if(pendingRemoves.containsKey(objectName)){
                        pendingRemoves.replace(objectName, jsonLdObject);
                    }
                    else
                        pendingRemoves.put(objectName, jsonLdObject);
                }
                else if(accessMask.startsWith("0x2") && isFileName(fileName)){
                    System.out.println("Create/Modified file " + objectName + " process: " + processName + " accountName: " + accountName + " handle: " + handleId + " from(" + changes.get(objectName) + ")");
                    pendingRemoves.remove(objectName);
                }
                else if(accessMask.startsWith("0x80")){
                    for (String key : pendingRemoves.keySet()) {
                        Map pendingRemove = pendingRemoves.get(key);
                        String pendingRemoveHandleId =  (String) pendingRemove.get("handleID");
                        Boolean confirmed = false;
                        if(pendingRemove.get("confirmed") != null)
                            confirmed = (Boolean)pendingRemove.get("confirmed");

                        if(pendingRemoveHandleId.equals(handleId) && !key.equals(objectName) && !confirmed && accountName.equals(pendingRemove.get("accountName"))){

                            if(getFileName(objectName).equals(getFileName(key))){
                                System.out.println("User moved " + key + " to " + objectName);
                            }
                            else if(objectName.contains("$Recycle.Bin")){
                                System.out.println("Moved to recycle bin " + key + " to " + objectName);
                                pendingRemoves.remove(key);
                            }
                            else if(key.contains("$Recycle.Bin")){
                                System.out.println("Moved out of recycle bin " + key + " to " + objectName);
                                pendingRemoves.remove(key);
                            }
                            else if(getFolderName(key).equalsIgnoreCase(getFolderName(objectName))){
                                if(getFileName(key).equals("New Folder"))
                                    System.out.println("Created " + objectName);
                                else
                                     System.out.println("Renamed " + key + " to " + objectName + " handle: " + handleId);

                                pendingRemoves.remove(key);
                            }
                            break;
                        }
                    }
                }

                break;

            case 4659: break;

            case 4660:
                for (String key : pendingRemoves.keySet()) {
                    Map pendingRemove = pendingRemoves.get(key);
                    String pendingRemoveHandleId = (String) pendingRemove.get("handleID");
                    //User equals User

                    if (pendingRemoveHandleId.equals(handleId) && accountName.equals(pendingRemove.get("accountName"))) {
                        pendingRemoves.get(key).put("confirmed", true);
                    }
                }

                break;

            default: break;
        }
    }
}