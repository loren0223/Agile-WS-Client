package adminmetadata.getlists;


import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.common.v1.jaxws.AdminListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.metadata.v1.jaxws.AgileGetListsRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.AgileGetListsResponseType;
import com.agile.ws.schema.metadata.v1.jaxws.GetListsRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.GetListsResponseType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataPortType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataService;

import run.RunAllSamples;



/**
* Sample      : GetLists  
* Type        : AdminMetadata webservice 
* 
* Description : This sample retrieves a list of values corresponding to the 
* list identifier specified in the request object. The ApiNames of the list
* values are obtained through the response object.
*/
public class GetLists {

    private static final String COMMAND_NAME = "GetLists";
    
	public static String clsName;
    public static String serviceName   =  "AdminMetadata";    
    public static AdminMetadataPortType agileStub;        

    public GetLists() {
             clsName = this.getClass().getName();
    }
      

    /**
     * The method setupServerLogin is used to login to the server by providing
     * the server url details and authentication credentials.
     * @param args command line arguments
     */
    
    public static void setupServerLogin(String[] args) throws Exception {
       
    	 //The AdminMetadataServiceLocator is used to obtain a AdminMetadata_BindingStub
    	URL url = new URL(args[0] + "/" + serviceName + "?WSDL"); 
    	AdminMetadataService locator = 
             new AdminMetadataService(url);

        //A stub of type AdminMetadata_BindingStub is obtained by specifying the server URL
        //Eventually, the convertCurrency webservice will be invoked by using this agileStub.
          agileStub = locator.getAdminMetadata();
                


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
		System.err.println("\t" + "user_name: user name");
		System.err.println("\t" + "password: password");
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
            
            // Create the request object GetListsRequestType for the getLists operation
            // Create an array of requests of type AgileGetSubClassesRequestType. Batch operations may be
            // performed by populating as many request objects as required to retrieve lists

            GetListsRequestType getListsRequestType = new GetListsRequestType();                         
            AgileGetListsRequestType agileGetListsRequestType[] = new AgileGetListsRequestType[2];             
            for(int i=0; i<agileGetListsRequestType.length; i++)             
               agileGetListsRequestType[i] = new AgileGetListsRequestType();           

            
            // For each batched request, specify the type of object whose lists will be retrieved.
            agileGetListsRequestType[0].setListIdentifier("ActionStatus");
            
            // Setting the 'allLists' field to true will retrieve all available lists 
            //agileGetListsRequestType[1].setAllLists(true);
            
            agileGetListsRequestType[1].setListIdentifier("Country");
             

            // The request objects are set and the agile Stub is used to make the getLists
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the getLists operation. 
            getListsRequestType.getListsRequest().addAll(Arrays.asList(agileGetListsRequestType));           
            GetListsResponseType getListsResponseType = agileStub.getLists(getListsRequestType);
            System.out.println("STATUS CODE: " +  getListsResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !getListsResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = getListsResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
            }
                else{
                // If the webservice call was successful, then display the lists retrieved
                    
                    AgileGetListsResponseType responses[] = getListsResponseType.getListsResponse().toArray(new AgileGetListsResponseType[0]);
                
                    if(responses!=null) 
                    for(int j=0; j<responses.length; j++){
                    
                        System.out.println("-------------------------------------------------------");                                                    
                        AdminListType list[] = responses[j].getList().toArray(new AdminListType[0]);
                        System.out.print("Lists retrieved for ");
                        System.out.println( agileGetListsRequestType[j].getListIdentifier() + ": " );                        
                        if(list!=null)
                            for(int i=0; i<list.length; i++){
                                AdminListType values[] = list[i].getEntry().toArray(new AdminListType[0]);
                                if(values!=null)
                                for(int jj=0; jj<values.length; jj++)
                                    System.out.println( (jj + 1) + ". " +  values[jj].getApiName() );                                    
                            }
                    }                
                    System.out.println("-------------------------------------------------------");
                }


            }
           
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }




}
