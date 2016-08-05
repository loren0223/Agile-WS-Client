package business.checkprivilege;


import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.business.v1.jaxws.AgileCheckPrivilegeRequestType;
import com.agile.ws.schema.business.v1.jaxws.AgileCheckPrivilegeResponseType;
import com.agile.ws.schema.business.v1.jaxws.CheckPrivilegeRequestType;
import com.agile.ws.schema.business.v1.jaxws.CheckPrivilegeResponseType;
import com.agile.ws.schema.business.v1.jaxws.CheckPrivilegeType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileUserIdentifierType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectPortType;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectService;

import run.DataPrepare;
import run.RunAllSamples;



 /**
 * Sample      : GetFileFF
 * Category    : BusinessObject webservice
 *
 * Description : This sample demonstrates the usage of 'getFileFF' webservice
 * for a specific case where all the file of an Agile File Folder object 
 * must be retrieved. The request object contains the specifications that
 * identify the attachment to be downloaded an array of bytes is obtained in the
 * response object.
 */
public class CheckPrivilege {
    private static final String COMMAND_NAME = "CheckPrivilege";
    
	public static String clsName;
    public static String serviceName   =  "BusinessObject";    
    public static BusinessObjectPortType agileStub;        
    
    public static String partNumber;

    public CheckPrivilege() {
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

        checkArguments(args);        

        // Comment this method out if you intend to use your own data
        // or scenario by editing the static variables at the top of this code
        prepareData(args);

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");
     
        try{
            
            setupServerLogin(args);
            
            // Create the request object CheckPrivilegeRequestType for the checkPrivilege operation
            // Create an array of requests of type AgileCheckPrivilegeRequestType . Batch operations may be
            // performed by populating as many request objects as required to check for several privileges
            // at once
            CheckPrivilegeRequestType checkPrivilegeRequestType = new CheckPrivilegeRequestType();
            AgileCheckPrivilegeRequestType agileCheckPrivilegeRequestType[] = new AgileCheckPrivilegeRequestType[1];             
            agileCheckPrivilegeRequestType[0] = new AgileCheckPrivilegeRequestType();

            // For each request, specify the user whose privileges are to be checked.
            // Users are specified as objects of AgileUserIdentifierType as shown:
            AgileUserIdentifierType user = new AgileUserIdentifierType();
            user.setUserIdentifier(args[1]);
            agileCheckPrivilegeRequestType[0].setUserIdentification(user);
            
            // Set the privilege which is to be queried for among privileges available to
            // the specified user. AgilePrivilegeType is used to specify a privilege.
            // AgilePrivilegeType.value1 refers to the 'Comment' privilege
            
               
            agileCheckPrivilegeRequestType[0].setPrivilege(1);
            
            
            // As an optional specification, the class identifier and object number
            // may also be specified to identify a particular Agile Object for
            // whose specific privileges the user details are queried.
            agileCheckPrivilegeRequestType[0].setClassIdentifier("Part");
            agileCheckPrivilegeRequestType[0].setObjectNumber( partNumber );            
            System.out.println("Checking for the 'comment' privilege on the object '" + partNumber + "'" );
            System.out.println("with the user '" + user.getUserIdentifier() + "'...\n");


            // The request objects are set and the agile Stub is used to make the CheckPrivilege
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the checkPrivilege operation.             
            checkPrivilegeRequestType.getRequests().addAll(Arrays.asList(agileCheckPrivilegeRequestType));
            CheckPrivilegeResponseType checkPrivilegeResponseType = agileStub.checkPrivilege(checkPrivilegeRequestType);            
            System.out.println("STATUS CODE: " +  checkPrivilegeResponseType.getStatusCode() );
                         
                         
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !checkPrivilegeResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = checkPrivilegeResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
            }
            else{
                
                // Obtain the object CheckPrivilegeType from the response object
                // The getCheckPrivilege() method on this object will yield a boolean
                // value indicating whether the user has the specified privilege.
                AgileCheckPrivilegeResponseType response = checkPrivilegeResponseType.getResponses().get(0);
                CheckPrivilegeType checkPrivilegeType  = response.getPrivilege();
                System.out.println("Privilege: " + checkPrivilegeType.isCheckPrivilege() );
                
                
            }
            
            
            }
            
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            } 
        
    }
    
}
