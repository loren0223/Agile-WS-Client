package business.isdeleted;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.business.v1.jaxws.AgileIsDeletedObjectRequest;
import com.agile.ws.schema.business.v1.jaxws.AgileIsDeletedObjectResponse;
import com.agile.ws.schema.business.v1.jaxws.IsDeletedObjectRequestType;
import com.agile.ws.schema.business.v1.jaxws.IsDeletedObjectResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectPortType;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectService;

import run.DataPrepare;
import run.RunAllSamples;



 /**
 * Sample      : IsDeleted
 * Category    : BusinessObject webservice
 *
 * Description : This sample demonstrates the usage of 'IsDeleted' webservice
 * using the user can ascertain whether an Agile Object has been deleted.
 * The request object contains specifications that identify the Agile object
 * and the response contains a boolean that specifies the state of deletion.
 */
public class IsDeleted {
    private static final String COMMAND_NAME = "IsDeleted";
    
	public static String clsName;
    public static String serviceName   =  "BusinessObject";       
    public static BusinessObjectPortType agileStub;        
    
    public static String partNumber;

    public IsDeleted() {
             clsName = this.getClass().getName();

    }
      

    /**
     * The method setupServerLogin is used to login to the server by providing
     * the server url details and authentication credentials.
     * @param args 
     */
    public static void setupServerLogin(String[] args) throws Exception {
        
    	//The BusinessObjectServiceLocator is used to obtain a BusinessObject_BindingStub
    	URL url = new URL(args[0] + "/" + serviceName + "?WSDL"); 
    	BusinessObjectService locator = new BusinessObjectService(url);

        agileStub = (BusinessObjectPortType)locator.getBusinessObject();


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
            
            partNumber = dataPrepare.createNewObject("Part");
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
            
            setupServerLogin(args);
            
            // Create the request object IsDeletedObjectRequestType for the getObject operation
            // Create an array of requests of type AgileIsDeletedObjectRequest . Batch operations may be
            // performed by populating as many request objects as required to query the deletion status
            // of several objects at once.
            IsDeletedObjectRequestType isDeletedObjectRequestType = new IsDeletedObjectRequestType();
            AgileIsDeletedObjectRequest agileIsDeletedObjectRequest[] = new AgileIsDeletedObjectRequest[1];             
            agileIsDeletedObjectRequest[0] = new AgileIsDeletedObjectRequest();
            
            
            // For each of the requests, set the class identifier to specify the
            // type of object that will be deleted and an object number to identify
            // the unique object whose delete will be repealed.
            agileIsDeletedObjectRequest[0].setClassIdentifier("Part");
            agileIsDeletedObjectRequest[0].setObjectNumber(partNumber);
            System.out.println("Checking if '" + partNumber + "' is deleted....\n");
            

            // The request objects are set and the agile Stub is used to make the isDeletedObject
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the isDeletedObject operation.             
            isDeletedObjectRequestType.getRequests().addAll(Arrays.asList(agileIsDeletedObjectRequest));
            IsDeletedObjectResponseType isDeletedObjectResponseType = agileStub.isDeletedObject(isDeletedObjectRequestType);            
            System.out.println("STATUS CODE: " +  isDeletedObjectResponseType.getStatusCode() );
                         
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !isDeletedObjectResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = isDeletedObjectResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
            }
            else{
            // If the web service call was successfull, then determine the status of deletion 
                AgileIsDeletedObjectResponse responses[] = isDeletedObjectResponseType.getResponses().toArray(new AgileIsDeletedObjectResponse[0]);
                if(responses!=null)
                for(int i=0; i<responses.length; i++)
                    if(responses[i]!=null)
                            System.out.println("isDeleted: " + responses[i].isIsDeleted() );
                
            }
            
            
            }
          
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            } 
        
    }
}
