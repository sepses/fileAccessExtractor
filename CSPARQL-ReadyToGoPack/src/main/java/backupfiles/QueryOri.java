package backupfiles;

//import java.util.UUID;

public class QueryOri {
	
	/*public static String getUUID() {
		UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        return randomUUIDString;
	}*/
	
	/*public static ArrayList<String> generateSubject() {
		ArrayList<String> subject = new ArrayList<String>();
		subject.add("eve:event-"+getUUID());
		subject.add("eve:eventT-"+getUUID());
		return subject;
	}*/
	
	public static final String selectAll = "REGISTER QUERY selectall AS " +
            "SELECT * " +
            "FROM STREAM <ws://localhost:8124/tw/stream> [RANGE 3s STEP 1s] " +
            "WHERE { " +
            "?s ?p ?o . " +	
            "}";
	
	public static final String created = "REGISTER QUERY created AS " +
			"PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			"PREFIX file: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX fae: <http://w3id.org/sepses/event/fileAccessEvent#> " +
			"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
			"PREFIX eve: <http://w3id.org/sepses/resource/event#> " +
            "CONSTRUCT {"
                    + "?subject fae:hasFileAccessType sys:Created;"
            		+ "         rdf:type fae:FileAccessEvent;"
            		+ "         fae:logTimestamp ?logtimestamp2;"
            		+ "         fae:hasSourceFile ?sourceFile;"
            		+ "         fae:hasTargetFile ?targetFile;"
            		+ "         fae:hasSourceHost ?sourceHost;"
            		+ "         fae:hasTargetHost ?targetHost;"	
            		+ "         fae:hasUser ?user ."	

            		+ "?sourceFile   fae:fileName ?filename2 ."
            		+ "?targetFile   fae:fileName ?filename2 ."
            		+ "?sourceHost   fae:hostName ?hostname2 ."
            		+ "?targetHost   fae:hostName ?hostname2 ."
            		+ "?user   		 fae:userName ?username ."
            		+ "}"+
                 
            "FROM STREAM <ws://localhost:8124/tw/stream> [RANGE 3s STEP 2s] " +
            "WHERE { {SELECT * WHERE {"+
            "?s file:hasFile/file:pathname ?filename2 . " +
            "?s file:hasHost/file:hostName ?hostname2 . " +
            "?s file:logtimestamp ?logtimestamp2 . " +
            "?s file:hasEvent/file:eventName ?event2."+
            "?s file:hasUser/file:userName ?username"+
	             "{SELECT ?filename ?hostname ?logtimestamp ?event WHERE {" +
		            "?s file:hasFile/file:pathname ?filename . " +
		             "?s file:hasHost/file:hostName ?hostname . " +
		             "?s file:logtimestamp ?logtimestamp . " +
		             "?s file:hasEvent/file:eventName ?event"+
	            " FILTER regex(str(?event),\"IN_CREATE\")}"+
            " }FILTER (regex(str(?event2),\"IN_OPEN\") && ?filename2=?filename && ?logtimestamp2=?logtimestamp && ?hostname2=?hostname )"
            + "BIND (CONCAT(str(?event),\"_\",str(?event2)) AS ?eventall) "
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"event\")) AS ?subject)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"source-file\")) AS ?sourceFile)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"target-file\")) AS ?targetFile)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"source-host\")) AS ?sourceHost)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"target-host\")) AS ?targetHost)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"user\")) AS ?user)"
            + "}} "+
             "FILTER regex(str(?eventall),\"IN_CREATE_IN_OPEN\")}";
          
	public static final String ren ="REGISTER QUERY ren AS " +
			"PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			"PREFIX file: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX fae: <http://w3id.org/sepses/event/fileAccessEvent#> " +
			"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
			"PREFIX eve: <http://w3id.org/sepses/resource/event#> " +
            "CONSTRUCT {"
                    + "?subject fae:hasFileAccessType sys:Renamed;"
            		+ "         rdf:type fae:FileAccessEvent;"
            		+ "         fae:logTimestamp ?logtimestamp2;"
            		+ "         fae:hasSourceFile ?sourceFile;"
            		+ "         fae:hasTargetFile ?targetFile;"
            		+ "         fae:hasSourceHost ?sourceHost;"
            		+ "         fae:hasTargetHost ?targetHost;"	
            		+ "         fae:hasUser ?user ."	

            		+ "?sourceFile   fae:fileName ?filename ."
            		+ "?targetFile   fae:fileName ?filename2 ."
            		+ "?sourceHost   fae:hostName ?hostname ."
            		+ "?targetHost   fae:hostName ?hostname2 ."
            		+ "?user   		 fae:userName ?username ."
            		+ "}"+
                 
            "FROM STREAM <ws://localhost:8124/tw/stream> [RANGE 3s STEP 2s] " +
            "WHERE { {SELECT * WHERE {"+
            "?s file:hasFile/file:pathname ?filename2 . " +
            "?s file:hasHost/file:hostName ?hostname2 . " +
            "?s file:logtimestamp ?logtimestamp2 . " +
            "?s file:hasEvent/file:eventName ?event2 ."+
            "?s file:hasUser/file:userName ?username"+
	             "{SELECT ?filename ?hostname ?logtimestamp ?event WHERE {" +
		            "?s file:hasFile/file:pathname ?filename . " +
		             "?s file:hasHost/file:hostName ?hostname . " +
		             "?s file:logtimestamp ?logtimestamp . " +
		             "?s file:hasEvent/file:eventName ?event"+
	            " FILTER regex(str(?event),\"IN_MOVED_FROM\")}"+
            " }FILTER (regex(str(?event2),\"IN_MOVED_TO\") && ?filename2!=?filename && ?logtimestamp2=?logtimestamp && ?hostname2=?hostname )"
            + "BIND (CONCAT(str(?event),\"_\",str(?event2)) AS ?eventall)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"event\")) AS ?subject)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"source-file\")) AS ?sourceFile)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"target-file\")) AS ?targetFile)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"source-host\")) AS ?sourceHost)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"target-host\")) AS ?targetHost)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"user\")) AS ?user)"
            + "}} "+
             "FILTER regex(str(?eventall),\"IN_MOVED_FROM_IN_MOVED_TO\")}";
			
	public static final String modif ="REGISTER QUERY modif AS " +
			"PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			"PREFIX file: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX fae: <http://w3id.org/sepses/event/fileAccessEvent#> " +
			"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
			"PREFIX eve: <http://w3id.org/sepses/resource/event#> " +
            "CONSTRUCT {"
                    + "?subject fae:hasFileAccessType sys:Modified;"
            		+ "         rdf:type fae:FileAccessEvent;"
            		+ "         fae:logTimestamp ?logtimestamp2;"
            		+ "         fae:hasSourceFile ?sourceFile;"
            		+ "         fae:hasTargetFile ?targetFile;"
            		+ "         fae:hasSourceHost ?sourceHost;"
            		+ "         fae:hasTargetHost ?targetHost;"	
            		+ "         fae:hasUser ?user ."	

            		+ "?sourceFile   fae:fileName ?filename ."
            		+ "?targetFile   fae:fileName ?filename2 ."
            		+ "?sourceHost   fae:hostName ?hostname ."
            		+ "?targetHost   fae:hostName ?hostname2 ."
            		+ "?user   		 fae:userName ?username ."
            		+ "}"+
                 
            "FROM STREAM <ws://localhost:8124/tw/stream> [RANGE 3s STEP 2s] " +
            "WHERE { {SELECT * WHERE {"+
            "?s file:hasFile/file:pathname ?filename2 . " +
            "?s file:hasHost/file:hostName ?hostname2 . " +
            "?s file:logtimestamp ?logtimestamp2 . " +
            "?s file:hasEvent/file:eventName ?event2 ."+
            "?s file:hasUser/file:userName ?username"+
	             "{SELECT ?filename ?hostname ?logtimestamp ?event WHERE {" +
		            "?s file:hasFile/file:pathname ?filename . " +
		             "?s file:hasHost/file:hostName ?hostname . " +
		             "?s file:logtimestamp ?logtimestamp . " +
		             "?s file:hasEvent/file:eventName ?event"+
	            " FILTER regex(str(?event),\"IN_OPEN\")}"+
            " }FILTER (regex(str(?event2),\"IN_MODIFY\") && ?filename2=?filename && ?logtimestamp2=?logtimestamp && ?hostname2=?hostname )"
            + "BIND (CONCAT(str(?event),\"_\",str(?event2)) AS ?eventall)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"event\")) AS ?subject)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"source-file\")) AS ?sourceFile)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"target-file\")) AS ?targetFile)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"source-host\")) AS ?sourceHost)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"target-host\")) AS ?targetHost)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"user\")) AS ?user)"
            + "}} "+
             "FILTER regex(str(?eventall),\"IN_OPEN_IN_MODIFY\")}";
	
	public static final String del = "REGISTER QUERY del AS " +
			"PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			"PREFIX file: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX fae: <http://w3id.org/sepses/event/fileAccessEvent#> " +
			"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
			"PREFIX eve: <http://w3id.org/sepses/resource/event#> " +
            "CONSTRUCT {"
                    + "?subject fae:hasFileAccessType sys:Deleted;"
            		+ "         rdf:type fae:FileAccessEvent;"
            		+ "         fae:logTimestamp ?logtimestamp;"
            		+ "         fae:hasSourceFile ?sourceFile;"
            		+ "         fae:hasTargetFile ?targetFile;"
            		+ "         fae:hasSourceHost ?sourceHost;"
            		+ "         fae:hasTargetHost ?targetHost;"	
            		+ "         fae:hasUser ?user ."	

            		+ "?sourceFile   fae:fileName ?filename ."
            		+ "?targetFile   fae:fileName ?filename ."
            		+ "?sourceHost   fae:hostName ?hostname ."
            		+ "?targetHost   fae:hostName ?hostname ."
            		+ "?user   		 fae:userName ?username ."
            		+ "}"+
                 
            "FROM STREAM <ws://localhost:8124/tw/stream> [RANGE 3s STEP 2s] " +
            "WHERE { " +
            "?s file:hasEvent/file:eventName ?event ."
            +"?s file:hasFile/file:pathname ?filename . " +
             "?s file:hasHost/file:hostName ?hostname . " +
             "?s file:logtimestamp ?logtimestamp . " +
             "?s file:hasEvent/file:eventName ?event . "+
             "?s file:hasUser/file:userName ?username ."+
            "FILTER regex(str(?event),\"IN_DELETE\")  . " +
            "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"event\")) AS ?subject)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"source-file\")) AS ?sourceFile)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"target-file\")) AS ?targetFile)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"source-host\")) AS ?sourceHost)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"target-host\")) AS ?targetHost)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"user\")) AS ?user)"

            + "}";
	
	
	
	public static final String copy ="REGISTER QUERY copy AS " +
			"PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			"PREFIX file: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX fae: <http://w3id.org/sepses/event/fileAccessEvent#> " +
			"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
			"PREFIX eve: <http://w3id.org/sepses/resource/event#> " +
            "CONSTRUCT {"
                    + "?subject fae:hasFileAccessType sys:Copied;"
            		+ "         rdf:type fae:FileAccessEvent;"
            		+ "         fae:logTimestamp ?logtimestamp2;"
            		+ "         fae:hasSourceFile ?sourceFile;"
            		+ "         fae:hasTargetFile ?targetFile;"
            		+ "         fae:hasSourceHost ?sourceHost;"
            		+ "         fae:hasTargetHost ?targetHost;"	
            		+ "         fae:hasUser ?user ."	

            		+ "?sourceFile   fae:fileName ?filename ."
            		+ "?targetFile   fae:fileName ?filename2 ."
            		+ "?sourceHost   fae:hostName ?hostname ."
            		+ "?targetHost   fae:hostName ?hostname2 ."
            		+ "?user   		 fae:userName ?username ."
            		+ "}"+
                 
            "FROM STREAM <ws://localhost:8124/tw/stream> [RANGE 3s STEP 2s] " +
            "WHERE {"
            + " {SELECT * WHERE {"+
            "?s file:hasFile/file:pathname ?filename3 . " +
            "?s file:hasHost/file:hostName ?hostname3 . " +
            "?s file:logtimestamp ?logtimestamp3 . " +
            "?s file:hasEvent/file:eventName ?event3 ." +
            "?s file:hasUser/file:userName ?username"
	            + " {SELECT ?filename2 ?hostname2 ?logtimestamp2 ?event2 ?eventall  ?filename ?hostname  WHERE {"+
	            "?s file:hasFile/file:pathname ?filename2 . " +
	            "?s file:hasHost/file:hostName ?hostname2 . " +
	            "?s file:logtimestamp ?logtimestamp2 .  " +
	            "?s file:hasEvent/file:eventName ?event2"+
		             "{SELECT ?filename ?hostname ?logtimestamp ?event WHERE {" +
			            "?s file:hasFile/file:pathname ?filename . " +
			             "?s file:hasHost/file:hostName ?hostname . " +
			             "?s file:logtimestamp ?logtimestamp . " +
			             "?s file:hasEvent/file:eventName ?event"+
		            " FILTER regex(str(?event),\"IN_OPEN\")}"+
	            " }FILTER (regex(str(?event2),\"IN_CREATE\") && ?filename2!=?filename && ?logtimestamp2=?logtimestamp && ?hostname2=?hostname ) BIND (CONCAT(str(?event),\"_\",str(?event2)) AS ?eventall)}"
	         + "} FILTER (regex(str(?event3),\"IN_OPEN\") && ?filename3=?filename2 && ?logtimestamp3=?logtimestamp2 && ?hostname3=?hostname2 )"
	            + "BIND (CONCAT(str(?eventall),\"_\",str(?event3)) AS ?eventalll)"
	            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"event\")) AS ?subject)"
	            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"source-file\")) AS ?sourceFile)"
	            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"target-file\")) AS ?targetFile)"
	            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"source-host\")) AS ?sourceHost)"
	            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"target-host\")) AS ?targetHost)"
	            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"user\")) AS ?user)"
	            + "}} "+
             "FILTER regex(str(?eventalll),\"IN_OPEN_IN_CREATE_IN_OPEN\")}";
	
	public static final String securedCopied = "REGISTER QUERY secCopy AS " +
			"PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			"PREFIX file: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX fae: <http://w3id.org/sepses/event/fileAccessEvent#> " +
			"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
			"PREFIX eve: <http://w3id.org/sepses/resource/event#> " +
            "CONSTRUCT {"
                    + "?subject fae:hasFileAccessType sys:SecureCopied;"
            		+ "         rdf:type fae:FileAccessEvent;"
            		+ "         fae:logTimestamp ?logtimestamp2;"
            		+ "         fae:hasSourceFile ?sourceFile;"
            		+ "         fae:hasTargetFile ?targetFile;"
            		+ "         fae:hasSourceHost ?sourceHost;"
            		+ "         fae:hasTargetHost ?targetHost;"	
            		+ "         fae:hasUser ?user ."	

            		+ "?sourceFile   fae:fileName ?filename ."
            		+ "?targetFile   fae:fileName ?filename2 ."
            		+ "?sourceHost   fae:hostName ?hostname ."
            		+ "?targetHost   fae:hostName ?hostname2 ."
            		+ "?user   		 fae:userName ?username ."
            		+ "}"+
                 
            "FROM STREAM <ws://localhost:8124/tw/stream> [RANGE 3s STEP 2s] " +
            "WHERE {"
            + " {SELECT * WHERE {"+
            "?s file:hasFile/file:pathname ?filename3 . " +
            "?s file:hasHost/file:hostName ?hostname3 . " +
            "?s file:logtimestamp ?logtimestamp3 . " +
            "?s file:hasEvent/file:eventName ?event3 ." +
            "?s file:hasUser/file:userName ?username"
	            + " {SELECT ?filename2 ?hostname2 ?logtimestamp2 ?event2 ?eventall  ?filename ?hostname  WHERE {"+
	            "?s file:hasFile/file:pathname ?filename2 . " +
	            "?s file:hasHost/file:hostName ?hostname2 . " +
	            "?s file:logtimestamp ?logtimestamp2 . " +
	            "?s file:hasEvent/file:eventName ?event2"+
		             "{SELECT ?filename ?hostname ?logtimestamp ?event WHERE {" +
			            "?s file:hasFile/file:pathname ?filename . " +
			             "?s file:hasHost/file:hostName ?hostname . " +
			             "?s file:logtimestamp ?logtimestamp . " +
			             "?s file:hasEvent/file:eventName ?event"+
		            " FILTER regex(str(?event),\"IN_CREATE\")}"+
	            " }FILTER (regex(str(?event2),\"IN_OPEN\") && ?filename2=?filename && ?logtimestamp2=?logtimestamp && ?hostname2=?hostname ) BIND (CONCAT(str(?event),\"_\",str(?event2)) AS ?eventall)}"
	         + "} FILTER (regex(str(?event3),\"IN_MODIFY\") && ?filename3=?filename2 && ?logtimestamp3=?logtimestamp2 && ?hostname3=?hostname2 )"
	            + "BIND (CONCAT(str(?eventall),\"_\",str(?event3)) AS ?eventalll)"
	            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"event\")) AS ?subject)"
	            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"source-file\")) AS ?sourceFile)"
	            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"target-file\")) AS ?targetFile)"
	            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"source-host\")) AS ?sourceHost)"
	            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"target-host\")) AS ?targetHost)"
	            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"user\")) AS ?user)"
	            + "}} "+
             "FILTER regex(str(?eventalll),\"IN_CREATE_IN_OPEN_IN_MODIFY\")}";
	
}
