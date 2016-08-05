package adminmetadata.getAutoNumbers;


import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningListType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningType;
import com.agile.ws.schema.common.v1.jaxws.AutoNumberType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.metadata.v1.jaxws.AgileGetAutoNumbersRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.AgileGetAutoNumbersResponseType;
import com.agile.ws.schema.metadata.v1.jaxws.GetAutoNumbersRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.GetAutoNumbersResponseType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataPortType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataService;

import run.RunAllSamples;



/**
* Sample      : GetAutoNumbers  
* Type        : AdminMetadata webservice 
* 
* Description : This sample demonstrates the retrieval of autonumbers for any
* class using the getAutoNumbers webservice. Given an Agile class and input
* for the number of autonumbers required, the webservice fetches the autonumbers
* which are then obtained from the response object as 'AutoNumberType' objects.
*/
public class GetAutoNumbers {
    private static final String COMMAND_NAME = "GetAutoNumbers";
    
	public static String clsName;
    public static String serviceName   =  "AdminMetadata";    
    public static AdminMetadataPortType agileStub;        

    public GetAutoNumbers() {
             clsName = this.getClass().getName();
    }
      

    /**
     * The method setupServerLogin is used to login to the server by providing
     * the server url details and authentication credentials.
     * @param args 
     */
    
    public static void setupServerLogin(String[] args) throws Exception {
       
    	 //The AdminMetadataServiceLocator is used to obtain a AdminMetadata_BindingStub
    	URL url = new URL(args[0] + "/" + serviceName +"?WSDL"); 
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
        
        checkArguments(args);        

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");
        
        try{
            
            // Login to the server by providing the server url and authentication credentials.
            setupServerLogin(args);             
            
            // Create the request object GetAutoNumbersRequestType for the getAutoNumbers operation
            // Create an array of requests of type AgileGetAutoNumbersRequestType. Batch operations may be
            // performed by populating several requests to obtain several autonumbers at once.

            GetAutoNumbersRequestType getAutoNumbersRequestType = new GetAutoNumbersRequestType();
            AgileGetAutoNumbersRequestType agileGetAutoNumbersRequestType[] = new AgileGetAutoNumbersRequestType[2];
            
            for(int i=0; i<agileGetAutoNumbersRequestType.length; i++)
                agileGetAutoNumbersRequestType[i] = new AgileGetAutoNumbersRequestType();
            
            // For each batched request, specify the type of object for which autonumbers will be obtained.
            // Use the 'includeAllAutoNumberSource' boolean field to use all available autonumber sources.
            // If this boolean is set to 'false', then a unique autoNumberIdentifier will have to be specified.
            // Also specify the the number of autonumbers required by setting the size attribute as show.
            agileGetAutoNumbersRequestType[0].setClassIdentifier("Part");
            agileGetAutoNumbersRequestType[0].setIncludeAllAutoNumberSource(true);
            agileGetAutoNumbersRequestType[0].setSize(3);
            
            agileGetAutoNumbersRequestType[1].setClassIdentifier("ECO");
            agileGetAutoNumbersRequestType[1].setIncludeAllAutoNumberSource(true);
            agileGetAutoNumbersRequestType[1].setSize(2);
            
            getAutoNumbersRequestType.getRequests().addAll(Arrays.asList(agileGetAutoNumbersRequestType));
            
            // The request objects are set and the agile Stub is used to make the getAutoNumbers
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the getAutoNumbers operation.             
            GetAutoNumbersResponseType getAutoNumbersResponseType = agileStub.getAutoNumbers(getAutoNumbersRequestType);
            System.out.println("STATUS CODE: " +  getAutoNumbersResponseType.getStatusCode() );
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !getAutoNumbersResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = getAutoNumbersResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
                AgileWarningListType agileWarningListType[] = getAutoNumbersResponseType.getWarnings().toArray(new AgileWarningListType[0]);
                if(agileWarningListType!=null)
                for(int i=0; i<agileWarningListType.length; i++){
                    AgileWarningType warnings[] = agileWarningListType[i].getWarning().toArray(new AgileWarningType[0]);
                    for(int j=0; j<warnings.length; j++)
                        System.out.println("Warning Id: " + warnings[j].getWarningId() + "\nMessage: " + warnings[j].getMessage() );                    
                }                    
                System.out.println("Failed to obtain an autonumber.");
                
            }
            else{
                // If the webservice call was successful, then display the autonumbers retrieved
                AgileGetAutoNumbersResponseType responses[] = getAutoNumbersResponseType.getAutoNumberResponses().toArray(new AgileGetAutoNumbersResponseType[0]);            
                if(responses!=null)
                for(int i=0; i<responses.length; i++){
                    
                    System.out.println("--------------------------------");
                    System.out.print("Autonumbers obtained " );
                    System.out.println("for " + responses[i].getClassIdentifier() + ": ");
                    // Obtain the list of autonumbers received per response.
                    AutoNumberType[] autonumbers = responses[i].getAutoNumbers().toArray(new AutoNumberType[0]);
                    if(autonumbers!=null)
                    for(int j=0; j< autonumbers.length; j++){                           
                            
                            // Based on the number of autonumbers requested,
                            // iterate through the response and display results
                            for(int s=0; s<agileGetAutoNumbersRequestType[i].getSize(); s++)
                                System.out.println( autonumbers[j].getAutoNumber().get(s) );    
                    }
                    
                }
                System.out.println("--------------------------------");
                    
                
            }
             

            }
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }

    
}
