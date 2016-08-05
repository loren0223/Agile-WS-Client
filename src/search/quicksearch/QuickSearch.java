package search.quicksearch;

import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileListEntryType;
import com.agile.ws.schema.common.v1.jaxws.AgileRowType;
import com.agile.ws.schema.common.v1.jaxws.AgileTableType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.common.v1.jaxws.SelectionType;
import com.agile.ws.schema.search.v1.jaxws.QuickSearchRequestType;
import com.agile.ws.schema.search.v1.jaxws.QuickSearchResponseType;
import com.agile.ws.service.search.v1.jaxws.SearchService;
import com.agile.ws.service.search.v1.jaxws.SearchService_Service;
import common.util.WSUtil;

import run.DataPrepare;
import run.RunAllSamples;


/**
* Sample      : QuickSearch
* Category    : Search webservice
*
* Description : This sample demonstrates the execution of a quick search
* using Agilw webservices. The fields necessary for the quick search are specified
* through the request object and the search results are obtained through the 
* response object.
*/
public class QuickSearch {
    private static final String COMMAND_NAME = "QuickSearch";
    
	public static String clsName;
    public static String serviceName   =  "Search";       
    public static SearchService agileStub;        

    public QuickSearch() {
             clsName = this.getClass().getName();

    }
      

    /**
     * The method setupServerLogin is used to login to the server by providing
     * the server url details and authentication credentials.
     * @param args 
     */
    
    public static void setupServerLogin(String[] args) throws Exception {       

         
    	   
    	//The AdminMetadataServiceLocator is used to obtain a AdminMetadata_BindingStub
    	URL url = new URL(args[0] + "/" + serviceName + "?WSDL"); 
    	SearchService_Service locator = 
             new SearchService_Service(url);

        //A stub of type AdminMetadata_BindingStub is obtained by specifying the server URL
        //Eventually, the convertCurrency webservice will be invoked by using this agileStub.
          agileStub = locator.getSearch();
                
          
        // Username and password details are specified for the agileStub
          Map<String, Object> reqContext = ((BindingProvider)agileStub).getRequestContext();
          reqContext.put(BindingProvider.USERNAME_PROPERTY, args[1]);
          reqContext.put(BindingProvider.PASSWORD_PROPERTY, args[2]);
     }

 	/**
	 * This sample can be configured by passing server url, user name and
	 * password as program arguments in the same order. This method checks for
	 * these values.
	 * 
	 * @param args
	 */
	private static void checkArguments(String[] args) {
		RunAllSamples.runCount++;
		
		if (args.length < 3) {
			// should pass arguments through the command line
			printUsage();
			System.exit(-1);
		}
	}

	/**
	 * print usage message to the standard error
	 */
	private static void printUsage() {
		System.err.println("Usage: " + COMMAND_NAME + " server_url user_name password");
		System.err.println("\t" + "server_url: the server URL");
		System.err.println("\t" + "user_name: the user name");
		System.err.println("\t" + "password: the password");
	}

    public static void prepareData(String[] args){
        
        try{
            System.out.println("<<< Preparing Data... >>>");
            DataPrepare dataPrepare = new DataPrepare(args[0], args[1], args[2]);
            dataPrepare.setupAdminMetadata(); 
            dataPrepare.setupBusiness();
            
            dataPrepare.createNewObject("Part");
            System.out.println("<<< DataPrepare successful >>>");
        }
        catch(Exception e){            
            System.out.println("<<< ALERT: DataPrepare failed. Your server settings may be invalid, this sample might not run successfully.");
            System.out.println("To run the sample without the assitance of prepared data, you may edit the static variables after");
            System.out.println("creating the necessary prerequisites through the Agile web client and then execute the sample >>>");
        }
        
        
    }

    public static void main(String[] args) {
        
        // This sample may be configured by either directly modifying the string values of
        // 'SERVER_URL', 'USERNAME' and 'PASSWORD' (defined in this class), or by
        // passing them as program arguments in the same order. In this sample, the
        // method 'checkArguments' checks for these values.
        checkArguments(args);    

        // Comment this method out if you intend to use your own data
        // or scenario by editing the static variables at the top of this code
        prepareData(args);

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");
        
        try{
            
            // Login to the server by providing the server url and authentication credentials.
            setupServerLogin(args);             
            
            // Create the request object QuickSearchRequestType for the quickSearch operation
            QuickSearchRequestType quickSearchRequestType = new QuickSearchRequestType();                         
            
            // Specify the type of object has to be queried for, by providing the class
            // identifier details. Keywords are then set into the element 'keywords',
            // these keywords are used to form the search criteria. Wildcards such as '*'
            // may also be used as a part of the keyword.
            
            // Search for all objects belonging to the class 'Part'
            quickSearchRequestType.setClassIdentifier("Part");
            // Search for all objects beginning with the characters 'P0' for eg: 'P00001'
            quickSearchRequestType.setKeywords("P0*");
            // Specify whether the webservice should search for the keyword within files
            // and attachments in addition to searching for objects.
            quickSearchRequestType.setSearchFiles(false);             
            System.out.println("Executing quick search with the keywords '" + quickSearchRequestType.getKeywords() + "'...\n");

            // The agile Stub is used to make the quickSearch webservice call. The status 
            // code obtained from the response object is printed to verify the success of  
            // the quickSearch operation. 
            QuickSearchResponseType quickSearchResponseType = agileStub.quickSearch(quickSearchRequestType);
            System.out.println("STATUS CODE: " +  quickSearchResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !quickSearchResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = quickSearchResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
            }
                else{
                // If the webservice call was successful, then display the search results
                    AgileTableType table = quickSearchResponseType.getTable();
                    displayTableContents(new AgileTableType[] {table});                

                }


            }
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }
    
public static void displayMessageElementValue(Element element){
        
        if(element.getFirstChild()!=null){                                          

            // Important Note: Certain message element values will be obtained 
            // as selections of type "AgileListEntryType". Such values must be
            // accessed as objects of AgileListEntryType by using the 'getObjectValue'
            // method of the message element and by casting the object appropriately.
            
            // The code below checks if the message element is of type AgileListEntryType.
            // If so, it retrieves the value information as a selection and displays the same.
            if(element.getAttribute("xsi:type").contains("AgileListEntryType")){
                
                AgileListEntryType list = (AgileListEntryType) WSUtil.unmarshalToAgileListEntryType(element);;
                SelectionType selection[] = list.getSelection().toArray(new SelectionType[0]);
                for( SelectionType sel: selection){
                    System.out.println(sel.getValue());
                }
            }   
            // If the message element contains an ordinary text value, then print the same
            else
                System.out.println( element.getFirstChild().getNodeValue() );                                        
        }                                            
        else
            System.out.println();
        
    }
    
    public static void displayTableContents(AgileTableType[] tables){
        
        if(tables!=null)
            for(int i=0; i<tables.length; i++){
                        
                System.out.println("---------------------------");
                System.out.print("Table: "); 
                String tableName = tables[i].getTableIdentifier().getTableDisplayName();
                if(tableName!=null)
                    System.out.println(tableName);
                else
                    System.out.println();
                
                AgileRowType[] rows = tables[i].getRow().toArray(new AgileRowType[0]);
                if(rows!=null)
                    for(int j=0; j<rows.length; j++){
                        
                        System.out.println("Row " + (j+1) + ":" );
                        List<Element> messages = rows[j].getAny();
                        for(int m=0; m<messages.size(); m++){  
                        	if(messages.get(m) instanceof Element){
	                            Element element = (Element) messages.get(m);
	                            System.out.print( element.getNodeName() + "--> " );                            
	                            displayMessageElementValue(element);
                        	}else if(messages.get(m) instanceof JAXBElement){
                        		JAXBElement element = (JAXBElement) messages.get(m);
                        		System.out.print( element.getName().getLocalPart() + "--> " ); 
                        		displayMessageElementValue(element);
                        	}
                            
                          }
                        System.out.println("---------------------------");
                        }
                            
                    }
        
        
    }
    
    
 public static void displayMessageElementValue(JAXBElement element){
	Object obj = element.getValue();
	
	if(obj instanceof AgileListEntryType){
		AgileListEntryType agileListEntryType = (AgileListEntryType) obj;
		 SelectionType selection[] = agileListEntryType.getSelection().toArray(new SelectionType[0]);
         for( SelectionType sel: selection){
             System.out.print(sel.getValue()+",");
         }
		
	}
	
	 System.out.println();
 }
}
