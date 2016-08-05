package search.getattributes;

import java.net.URL;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AttributeType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.search.v1.jaxws.QueryGetSearchableAttributesRequestType;
import com.agile.ws.schema.search.v1.jaxws.QueryGetSearchableAttributesResponseType;
import com.agile.ws.service.search.v1.jaxws.SearchService;
import com.agile.ws.service.search.v1.jaxws.SearchService_Service;
import run.RunAllSamples;


/**
* Sample      : GetSearchableAttributes
* Category    : Search webservice
*
* Description : This sample demonstrates the webservice 'getSearchableAttributes'
* using which the searchable attributes of an Agile class are queried for.
* The fields necessary for this are specified through the request object
* the list of searchab;e attributes is obtained through the response object. 
*/
public class GetSearchableAttributes {
    private static final String COMMAND_NAME = "GetSearchableAttributes";
    
	public static String clsName;
    public static String serviceName   =  "Search";       
    public static SearchService agileStub;         
    
    public GetSearchableAttributes() {
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

    public static void prepareData(){
        
        try{
            System.out.println("<<< Preparing Data... >>>");

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

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");
        
        try{
            
            // Login to the server by providing the server url and authentication credentials.
            setupServerLogin(args);             
            
            // Create the request object QueryGetSearchableAttributesRequestType
            // to obtain a list of searchable attributes for a given class 
            QueryGetSearchableAttributesRequestType queryGetSearchableAttributesRequestType = new QueryGetSearchableAttributesRequestType();                         
            queryGetSearchableAttributesRequestType.setClassIdentifier("Part");

            // The agile Stub is used to make the getSearchableAttributes webservice
            // call. The status code obtained from the response object is printed
            // to verify the success of the getSearchableAttributes operation. 
            QueryGetSearchableAttributesResponseType queryGetSearchableAttributesResponseType = agileStub.getSearchableAttributes(queryGetSearchableAttributesRequestType);
            System.out.println("STATUS CODE: " +  queryGetSearchableAttributesResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !queryGetSearchableAttributesResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = queryGetSearchableAttributesResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
            }
            else{
            // If the webservice call was successful, then display the attributes retrieved
                System.out.println("\nAttributes for " + queryGetSearchableAttributesRequestType.getClassIdentifier() + ":" );
                AttributeType attributes[] = queryGetSearchableAttributesResponseType.getAttributes().toArray(new AttributeType[0]);
                for(int i=0; i<attributes.length; i++)
                    System.out.println("Attribute " + (i+1) + ". --> " + attributes[i].getApiName() ); 
            }

            }
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }
}
