package adminmetadata.getmetadata;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import run.RunAllSamples;

import com.agile.api.ChangeConstants;
import com.agile.api.ItemConstants;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AttributeType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.metadata.v1.jaxws.AgileGetTableMetadataRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.AgileGetTableMetadataResponseType;
import com.agile.ws.schema.metadata.v1.jaxws.GetTableMetadataRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.GetTableMetadataResponseType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataPortType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataService;


/**
* Sample      : GetTableMetaData  
* Type        : AdminMetadata webservice 
* 
* Description : This sample demonstrates usage of the getTableMetadata to retrieve
* metadata information for a particular table. The response consists of AttributeType 
* objects from which comprehensive information about the attributes may be obtained.
*/
public class GetTableMetaData {
    
    private static final String COMMAND_NAME = "GetTableMetaData";
    
	public static String clsName;
    public static String serviceName   =  "AdminMetadata";    
    public static AdminMetadataPortType agileStub;        

    public GetTableMetaData() {
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
            
            // Create the request object GetTableMetadataRequestType for the getTableMetaData operation
            // Create an array of requests of type AgileGetTableMetadataRequestType. Batch operations may be
            // performed by populating several requests to obtain metadata information about several tables at once.

            GetTableMetadataRequestType getTableMetadataRequestType = new GetTableMetadataRequestType();                         
            AgileGetTableMetadataRequestType agileGetTableMetadataRequestType[] = new AgileGetTableMetadataRequestType[2];                         
            
            for(int i=0; i<agileGetTableMetadataRequestType.length; i++)
                agileGetTableMetadataRequestType[i] = new AgileGetTableMetadataRequestType();           
                
            
            // For each batched request, specify the type of object whose attributes will be retrieved.
            // Also specify the attribute which will be retrieved
            agileGetTableMetadataRequestType[0].setClassIdentifier("Part");
            agileGetTableMetadataRequestType[0].setTableIdentifier( ItemConstants.TABLE_ATTACHMENTS.toString() );
            
            agileGetTableMetadataRequestType[1].setClassIdentifier("ECO");
            agileGetTableMetadataRequestType[1].setTableIdentifier( ChangeConstants.TABLE_COVERPAGE.toString() );            

             

            // The request objects are set and the agile Stub is used to make the getTableMetaData
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the getTableMetaData operation. 
            getTableMetadataRequestType.getRequests().addAll(Arrays.asList(agileGetTableMetadataRequestType));             
            GetTableMetadataResponseType getTableMetadataResponseType = agileStub.getTableMetadata(getTableMetadataRequestType);
            System.out.println("STATUS CODE: " +  getTableMetadataResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !getTableMetadataResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = getTableMetadataResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
            }
                else{
                // If the webservice call was successful, then display the attributes retrieved
                    
                    AgileGetTableMetadataResponseType responses[] = getTableMetadataResponseType.getResponses().toArray(new AgileGetTableMetadataResponseType[0]);
                
                    if(responses!=null)
                    for(int j=0; j<responses.length; j++){
                            
                        AttributeType attributes[] = responses[j].getAttributes().toArray(new AttributeType[0]);
                        System.out.println("--------------------------------------------------");
                        System.out.println("Class: " + agileGetTableMetadataRequestType[j].getClassIdentifier() );
                        System.out.print("Attributes for ");
                        System.out.println( responses[j].getTableName() + ": " );                        
                        if(attributes!=null)
                        for(int i=0; i<attributes.length; i++)
                            System.out.println( (i + 1) + "." + attributes[i].getDisplayName() );                   

                    
                    }                
                    System.out.println("--------------------------------------------------");
                    
                }


            }
            
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }

}
