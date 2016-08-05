package adminmetadata.getsubclasses;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.ClassType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.metadata.v1.jaxws.AgileGetSubClassesRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.AgileGetSubClassesResponseType;
import com.agile.ws.schema.metadata.v1.jaxws.GetSubClassesRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.GetSubClassesResponseType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataPortType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataService;

import run.RunAllSamples;


/**
* Sample      : ConvertCurrency  
* Type        : AdminMetadata webservice 
* 
* Description : The sample demonstrates the retrieval of subclasses for a given
* Agile class. The element 'classIdentifier' is specified in the request object
* and the response consists of 'ClassType' objects using which information about
* the subclasses may be obtained.
*/
public class GetSubClasses {
    private static final String COMMAND_NAME = "GetSubClasses";
    
	public static String clsName;
    public static String serviceName   =  "AdminMetadata";    
    public static AdminMetadataPortType agileStub;        

    public GetSubClasses() {
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
            
            // Create the request object GetSubClassesRequestType for the createObject operation
            // Create an array of requests of type AgileGetSubClassesRequestType. Batch operations may be
            // performed by populating as many request objects as required to retrieve several types of 
            // subclasses
            GetSubClassesRequestType getSubClassesRequestType = new GetSubClassesRequestType();                         
            AgileGetSubClassesRequestType agileGetSubClassesRequestType[] = new AgileGetSubClassesRequestType[2];             
            for(int i=0; i<agileGetSubClassesRequestType.length; i++)             
               agileGetSubClassesRequestType[i] = new AgileGetSubClassesRequestType();           

            
            // For each batched request, specify the type of object whose subclasses will be retrieved.
            agileGetSubClassesRequestType[0].setClassIdentifier("Changes");
            agileGetSubClassesRequestType[1].setClassIdentifier("Items");             
             

            // The request objects are set and the agile Stub is used to make the getSubClasses
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the getSubClasses operation.   
            getSubClassesRequestType.getSubClassesRequest().addAll(Arrays.asList(agileGetSubClassesRequestType));
            GetSubClassesResponseType getSubClassesResponseType = agileStub.getSubClasses(getSubClassesRequestType);
            System.out.println("STATUS CODE: " +  getSubClassesResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !getSubClassesResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = getSubClassesResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
            }
                else{
                // If the webservice call was successful, then display the list of classes retrieved
                    
                    AgileGetSubClassesResponseType responses[] = getSubClassesResponseType.getSubClassesResponse().toArray(new AgileGetSubClassesResponseType[0]);
                
                    if(responses!=null)
                    for(int j=0; j<responses.length; j++){

                        System.out.println("-----------------------------------------------");
                        System.out.println("List of subClasses retrieved: ");                                    
                        System.out.println("-----------------------------------------------");
                        ClassType classes[] = responses[j].getClasses().toArray(new ClassType[0]);
                        System.out.println(" \" " + responses[j].getClassIdentifier() + " \" ");

                        if(classes!=null)
                        for(int i=0; i<classes.length; i++)
                            System.out.println( classes[i].getDisplayName() );                   
                    
                    }                
                    
                }


            }
            
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }



}
