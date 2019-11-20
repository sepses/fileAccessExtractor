package eu.larkc.csparql.readytogopack;

public class QueryProcessWin {
	
//===================CONSTRUCT QUERY==============================
	public static final String login = "REGISTER QUERY login AS " +
			"PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			"PREFIX file: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX ue: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX we: <http://w3id.org/sepses/vocab/windows-event#> " +
			"PREFIX se: <http://w3id.org/sepses/event/systemEvent#> " +
			"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
            "CONSTRUCT {"
                    + "?subject se:hasSystemEventType se:Login;"
            		+ "         rdf:type se:SystemEvent;"
            		+ "         se:timestamp ?timestamp;"
            		+ "         se:hasHost ?host;"
            		+ "         se:hasSourceIp ?sourceIp;"
               		+ "         se:hasPid ?pid;"	
            		+ "         se:hasUser ?user ."	
            		+ "?host   		 se:hostName ?hostname ."
            		+ "?user   		 se:userName ?username"
            		+ "}"+
            "FROM STREAM <ws://localhost:8124/tw/stream> [RANGE 3s STEP 2s] " +
            "WHERE { "+
	             "?s file:hasHost/file:hostName ?hostname . " +
	             "?s file:sourceIp ?sourceIp . " +
	             "?s file:timestamp ?timestamp . " +
	             "?s file:hasEvent/file:eventName ?event ."+
	             "?s file:hasUser/file:userName ?username ."+
	             "?s file:hasProcess/file:processName ?programname . " +
		         "?s file:hasProcess/file:processID ?pid . " +
	            "FILTER regex(str(?event),\"user_login\") "
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"event\")) AS ?subject)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"host\")) AS ?host)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"user\")) AS ?user)"+
            "}";
	
	public static final String logout = "REGISTER QUERY logout AS " +
			"PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			"PREFIX file: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX ue: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX we: <http://w3id.org/sepses/vocab/windows-event#> " +
			"PREFIX se: <http://w3id.org/sepses/event/systemEvent#> " +
			"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
            "CONSTRUCT {"
                    + "?subject se:hasSystemEventType se:Logout;"
            		+ "         rdf:type se:SystemEvent;"
            		+ "         se:timestamp ?timestamp;"
            		+ "         se:hasHost ?host;"
            		+ "         se:hasSourceIp ?sourceIp;"
               		+ "         se:hasPid ?pid;"	
            		+ "         se:hasUser ?user ."	
            		+ "?host   		 se:hostName ?hostname ."
            		+ "?user   		 se:userName ?username"
            		+ "}"+
            "FROM STREAM <ws://localhost:8124/tw/stream> [RANGE 3s STEP 2s] " +
            "WHERE { "+
	             "?s file:hasHost/file:hostName ?hostname . " +
	             "?s file:sourceIp ?sourceIp . " +
	             "?s file:timestamp ?timestamp . " +
	             "?s file:hasEvent/file:eventName ?event ."+
	             "?s file:hasUser/file:userName ?username ."+
	             "?s file:hasProcess/file:processName ?programname . " +
		         "?s file:hasProcess/file:processID ?pid . " +
	            "FILTER regex(str(?event),\"user_logout\") "
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"event\")) AS ?subject)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"host\")) AS ?host)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"user\")) AS ?user)"+
            "}";
	
	public static final String processStarted = "REGISTER QUERY processStarted AS " +
			"PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			"PREFIX file: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX ue: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX we: <http://w3id.org/sepses/vocab/windows-event#> " +
			"PREFIX se: <http://w3id.org/sepses/event/systemEvent#> " +
			"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
            "CONSTRUCT {"
                    + "?subject se:hasSystemEventType se:ProcessStarted;"
            		+ "         rdf:type se:SystemEvent;"
            		+ "         se:timestamp ?timestamp;"
            		+ "         se:hasHost ?host;"
             		+ "         se:hasProgram ?program;"
            		+ "         se:hasUser ?user ."	
            		+ "?host   	  se:hostName ?hostname ."
              		+ "?program   se:programName ?programname ."
            		+ "?program   se:pid ?pid ."
            		+ "?user   		 se:userName ?username"
            		+ "}"+
            "FROM STREAM <ws://localhost:8124/tw/stream> [RANGE 3s STEP 2s] " +
            "WHERE { "+
	             "?s file:hasHost/file:hostName ?hostname . " +
	             "?s file:timestamp ?timestamp . " +
	             "?s file:hasEvent/file:eventName ?event ."+
	             "?s file:hasUser/file:userName ?username ."+
	             "?s file:hasProcess/file:processName ?programname . " +
		         "?s file:hasProcess/file:processID ?pid . " +
	            "FILTER regex(str(?event),\"process_started\") "
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"event\")) AS ?subject)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"host\")) AS ?host)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"program\")) AS ?program)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"user\")) AS ?user)"+
            "}";
	
	public static final String processStopped = "REGISTER QUERY processStopped AS " +
			"PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			"PREFIX file: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX ue: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX we: <http://w3id.org/sepses/vocab/windows-event#> " +
			"PREFIX se: <http://w3id.org/sepses/event/systemEvent#> " +
			"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
            "CONSTRUCT {"
                    + "?subject se:hasSystemEventType se:ProcessStopped;"
            		+ "         rdf:type se:SystemEvent;"
            		+ "         se:timestamp ?timestamp;"
            		+ "         se:hasHost ?host;"
             		+ "         se:hasProgram ?program;"
            		+ "         se:hasUser ?user ."	
            		+ "?host   	  se:hostName ?hostname ."
              		+ "?program   se:programName ?programname ."
            		+ "?program   se:pid ?pid ."
            		+ "?user   		 se:userName ?username"
            		+ "}"+
            "FROM STREAM <ws://localhost:8124/tw/stream> [RANGE 3s STEP 2s] " +
            "WHERE { "+
	             "?s file:hasHost/file:hostName ?hostname . " +
	             "?s file:timestamp ?timestamp . " +
	             "?s file:hasEvent/file:eventName ?event ."+
	             "?s file:hasUser/file:userName ?username ."+
	             "?s file:hasProcess/file:processName ?programname . " +
		         "?s file:hasProcess/file:processID ?pid . " +
	            "FILTER regex(str(?event),\"process_stopped\") "
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"event\")) AS ?subject)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"host\")) AS ?host)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"program\")) AS ?program)"
            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"user\")) AS ?user)"+
            "}";
	
	
	
	

}
