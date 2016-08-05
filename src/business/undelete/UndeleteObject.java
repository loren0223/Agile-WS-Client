package business.undelete;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import business.delete.DeleteObject;

import com.agile.ws.schema.business.v1.jaxws.AgileUndeleteObjectRequest;
import com.agile.ws.schema.business.v1.jaxws.UndeleteObjectRequestType;
import com.agile.ws.schema.business.v1.jaxws.UndeleteObjectResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectPortType;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectService;

import run.DataPrepare;
import run.RunAllSamples;


 /**
 * Sample      : UndeleteObject
 * Category    : BusinessObject webservice
 *
 * Description : This sample demonstrates the usage of 'UndeleteObject' webservice
 * using which the deletion of an Agile object may be rescinded so that the state
 * of deletion of the object is reverted back to the 'undeleted' state. The request
 * object contains specifications identifying the object upon which this action
 * needs to be performed.
 */
public class UndeleteObject {
    private static final String COMMAND_NAME = "UndeleteObject";
    
	public static String clsName;
    public static String serviceName   =  "BusinessObject";       
    public static BusinessObjectPortType agileStub;        
    
    public static String partNumber;

    public UndeleteObject() {
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
            dataPrepare.deleteObject("Part", partNumber);
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
            
            // Create the request object UndeleteObjectRequestType for the getObject operation
            // Create an array of requests of type AgileUndeleteObjectRequest . Batch operations may be
            // performed by populating as many request objects as required to 'undelete' several  objects.
            UndeleteObjectRequestType undeleteObjectRequestType = new UndeleteObjectRequestType();
            AgileUndeleteObjectRequest agileUndeleteObjectRequest[] = new AgileUndeleteObjectRequest[1];             
            agileUndeleteObjectRequest[0] = new AgileUndeleteObjectRequest();
            
            
            // For each of the requests, set the class identifier to specify the
            // type of object that will be deleted and an object number to identify
            // the unique object whose delete will be repealed.
            agileUndeleteObjectRequest[0].setClassIdentifier("Part");
            agileUndeleteObjectRequest[0].setObjectNumber(partNumber);
            
            System.out.println("Undeleting part '" + partNumber + "'...\n");
            

            // The request objects are set and the agile Stub is used to make the undeleteObject
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the undeleteObject operation.             
            undeleteObjectRequestType.getRequests().addAll(Arrays.asList(agileUndeleteObjectRequest));
            UndeleteObjectResponseType undeleteObjectResponseType = agileStub.undeleteObject(undeleteObjectRequestType);            
            System.out.println("STATUS CODE: " +  undeleteObjectResponseType.getStatusCode() );
                         
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !undeleteObjectResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = undeleteObjectResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
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
