/*******************************************************************************
 * Copyright 2014 Davide Barbieri, Emanuele Della Valle, Marco Balduini
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Acknowledgements:
 * 
 * This work was partially supported by the European project LarKC (FP7-215535) 
 * and by the European project MODAClouds (FP7-318484)
 ******************************************************************************/
package eu.larkc.csparql.readytogopack;

import java.net.URISyntaxException;
import java.text.ParseException;

import eu.larkc.csparql.core.engine.ConsoleFormatter;
import eu.larkc.csparql.core.engine.CsparqlEngine;
import eu.larkc.csparql.core.engine.CsparqlEngineImpl;
import eu.larkc.csparql.core.engine.CsparqlQueryResultProxy;
import util.RDFFormatter;

public class LinuxExperiment2 {
	 public static void main(String[] args) throws ParseException {

		StreamProcessingService fsStream = new StreamProcessingService("ws://localhost:8124/tw/stream");
		CsparqlEngine engine = new CsparqlEngineImpl();
		
		engine.initialize(true);


		engine.registerStream(fsStream);

		// Start Streaming (this is only needed for the example, normally streams are external
		// C-SPARQL Engine users are supposed to write their own adapters to create RDF streams

		
//=============file integrity===================================================================	
		//CsparqlQueryResultProxy selectProxy = engine.registerQuery(Query.selectAll, false);
		CsparqlQueryResultProxy createdProxy = engine.registerQuery(QueryLinux.created, false);
	    CsparqlQueryResultProxy deletedProxy = engine.registerQuery(QueryLinux.del, false);
		CsparqlQueryResultProxy modifiedProxy = engine.registerQuery(QueryLinux.modif, false);
		CsparqlQueryResultProxy renamedProxy = engine.registerQuery(QueryLinux.ren, false);
	CsparqlQueryResultProxy copiedProxy = engine.registerQuery(QueryLinux.copy, false);
    //  CsparqlQueryResultProxy secCopiedProxy = engine.registerQuery(Query.securedCopied, false);*/

	 //Attach a result consumer to the query result proxy to print the results on the console
   		//selectProxy.addObserver(new ConsoleFormatter());
	      createdProxy.addObserver(new RDFFormatter("created"));
	      deletedProxy.addObserver(new RDFFormatter("deleted"));
	      modifiedProxy.addObserver(new RDFFormatter("modified"));
	      renamedProxy.addObserver(new RDFFormatter("renamed"));
	      copiedProxy.addObserver(new RDFFormatter("copied"));
	     //  secCopiedProxy.addObserver(new RDFFormatter("secCopied"));
	      
//=============system Event===========================================================  
	    //  CsparqlQueryResultProxy loginProxy = engine.registerQuery(QueryProcessWin.login, false);
	    //  CsparqlQueryResultProxy logoutProxy = engine.registerQuery(QueryProcessWin.logout, false);
	    //  CsparqlQueryResultProxy processStarted = engine.registerQuery(QueryProcessWin.processStarted, false);
	    //  CsparqlQueryResultProxy processStopped = engine.registerQuery(QueryProcessWin.processStopped, false);
	
	      
	      //loginProxy.addObserver(new RDFFormatter("login"));
	      //logoutProxy.addObserver(new RDFFormatter("logout"));
	      //processStarted.addObserver(new RDFFormatter("processStarted"));
	      //processStopped.addObserver(new RDFFormatter("processStopped"));
	      
	        //Start the thread that put the triples in the engine
	        try {
	        	//System.out.print("ayo");
	            fsStream.initService();
	            
	           
	        } catch (URISyntaxException e) {
	            e.printStackTrace();
	        }

		//System.exit(0);



	}


}
