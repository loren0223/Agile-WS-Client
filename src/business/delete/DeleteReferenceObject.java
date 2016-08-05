package business.delete;

import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.business.v1.jaxws.AgileDeleteObjectRequest;

import com.agile.ws.schema.business.v1.jaxws.DeleteObjectRequestType;
import com.agile.ws.schema.business.v1.jaxws.DeleteObjectResponseType;

import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectPortType;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectService;

import run.DataPrepare;
import run.RunAllSamples;


/**
* Sample      : DeleteReferenceObject
* Category    : BusinessObject webservice
*
* Description : This sample demonstrates the deletion of a Reference object
* by using the 'deleteObject' webservice.
*/
public class DeleteReferenceObject {
    private static final String COMMAND_NAME = "DeleteReferenceObject";
    
	public static String clsName;
    public static String serviceName   =  "BusinessObject";       
    public static BusinessObjectPortType agileStub;        
    
    public static String refSubclassName = "XxReferenceobject";
    public static String refObjPubKey;
    public static String refObjName;

    public DeleteReferenceObject() {
             clsName = this.getClass().getName();

    }     

    /**
     * The method setupServerLogin is used to login to the server by providing
     * the server url details and authentication credentials.
     * @param args 
     */
    public static void setupServerLogin(String[] args) throws Exception {
        
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
          
          String[] objData = dataPrepare.createNewRefObject(refSubclassName);
          refObjName = objData[0];
          refObjPubKey = objData[1];          
          System.out.println("<<< DataPrepare successful >>>");
      }
      catch(Exception e){            
          System.out.println("<<< ALERT: DataPrepare failed. Your server settings may be invalid, this sample might not run successfully.");
          System.out.println("To run the sample without the assitance of prepared data, you may edit the static variables after");
          System.out.println("creating the necessary prerequisites through the Agile web client and then execute the sample >>>");
      }
  }

    public static void main(String[] args) {

        checkArguments(args);     

        // Comment this method out if you intend to use your own data
        // or scenario by editing the static variables at the top of this code
        prepareData(args);

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");
        
        try{
            
            setupServerLogin(args);
            
            // Create the request object DeleteObjectRequestType for the getObject operation
            // Create an array of requests of type AgileDeleteObjectRequest . Batch operations may be
            // performed by populating as many request objects as required to delete several  objects.
            DeleteObjectRequestType deleteObjectRequestType = new DeleteObjectRequestType();
            AgileDeleteObjectRequest agileDeleteObjectRequest = new AgileDeleteObjectRequest();             
            
            
            // For each of the requests, set the class identifier to specify the
            // type of object that will be deleted and an object number to identify
            // the unique object which will be delete from Agile PLM.
            agileDeleteObjectRequest.setClassIdentifier(refSubclassName);
            agileDeleteObjectRequest.setReferencedObjKey(refObjPubKey);  //optional
            agileDeleteObjectRequest.setObjectNumber(refObjName);  //ignored if refObjPubKey is provided
            
            System.out.println("Deleting object '" + refObjName + "'...\n");            

            // The request objects are set and the agile Stub is used to make the deleteObject
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the deleteObject operation.             
            deleteObjectRequestType.getRequests().add(agileDeleteObjectRequest);
            DeleteObjectResponseType deleteObjectResponseType = agileStub.deleteObject(deleteObjectRequestType);            
            System.out.println("STATUS CODE: " +  deleteObjectResponseType.getStatusCode() );
                         
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !deleteObjectResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                
                List<AgileExceptionListType> agileExceptionListType = deleteObjectResponseType.getExceptions();
                for(int i=0; i<agileExceptionListType.size(); i++){
                    List<AgileExceptionType> exceptions = agileExceptionListType.get(i).getException();
                    for(AgileExceptionType exception:exceptions)
                        System.out.println(exception.getMessage() );
                }
            }
        }
        catch (Exception e) {
			RunAllSamples.reportFailure(clsName);
			System.out.println("Exceptions: ");
			e.printStackTrace();
		}
        
    }
}
