package search.advancedsearch;

import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;

import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileListEntryType;
import com.agile.ws.schema.common.v1.jaxws.AgileRowType;
import com.agile.ws.schema.common.v1.jaxws.AgileTableType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.common.v1.jaxws.SelectionType;
import com.agile.ws.schema.search.v1.jaxws.AdvancedSearchRequestType;
import com.agile.ws.schema.search.v1.jaxws.AdvancedSearchResponseType;
import com.agile.ws.service.search.v1.jaxws.SearchService;
import com.agile.ws.service.search.v1.jaxws.SearchService_Service;
import common.util.WSUtil;

import run.RunAllSamples;

/**
* Sample      : AdvancedSearchReferenceObject
* Category    : Search webservice
*
* Description : This sample demonstrates the execution of an advanced search for Reference Object
* using Agile webservices. The fields necessary for the advanced search are
* specified through the request object and the search results are obtained
* in the response object.
*/
public class AdvancedSearchReferenceObject {
    private static final String COMMAND_NAME = "AdvancedSearchReferenceObject";
    
	public static String clsName;
    public static String serviceName   =  "Search";       
    public static SearchService agileStub;        
    public static String refSubclassName = "Part";
    
    public AdvancedSearchReferenceObject() {
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

    

    public static void main(String[] args) {
        
        // This sample may be configured by either directly modifying the string values of
        // 'SERVER_URL', 'USERNAME' and 'PASSWORD' (defined in this class), or by
        // passing them as program arguments in the same order. In this sample, the
        // method 'checkArguments' checks for these values.
        checkArguments(args);

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");
        
        try{
            
            // Login to the server by providing the server url and authentication credentials.
            setupServerLogin(args);             
            
            // Create the request object AdvancedSearchRequestType for the advancedSearch operation
            AdvancedSearchRequestType advancedSearchRequestType = new AdvancedSearchRequestType();                         
            
            // Specify the type of object has to be queried for, by providing the class
            // identifier details. Specify whether the search is to be case sensitive.
            advancedSearchRequestType.setClassIdentifier(refSubclassName);
            advancedSearchRequestType.setCaseSensitive(false);   
            
            // Advanced searches are executed by forming the search criteria 
            // using certain query syntax to construct the query details.
            
            // The query listed here searches for all parts containing the
            // characters 'P0' and whose description holds a legitimate value.
            String criteria = "[Title Block.Name/Number/Desc] contains '250'";
            advancedSearchRequestType.setCriteria(criteria);
            
            // The agile Stub is used to make the advancedSearch webservice call. The status 
            // code obtained from the response object is printed to verify the success of  
            // the advancedSearch operation. 
            System.out.println("Searching.....\n");
            AdvancedSearchResponseType advancedSearchResponseType = agileStub.advancedSearch(advancedSearchRequestType);
            System.out.println("STATUS CODE: " +  advancedSearchResponseType.getStatusCode() );
                       
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !advancedSearchResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = advancedSearchResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
            }
                else{
                // If the webservice call was successful, then display the search results
                    AgileTableType table = advancedSearchResponseType.getTable();
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
