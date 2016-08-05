package business.send;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.api.UserConstants;
import com.agile.ws.schema.business.v1.jaxws.AgileSendObjectRequest;
import com.agile.ws.schema.business.v1.jaxws.SendObjectRequestType;
import com.agile.ws.schema.business.v1.jaxws.SendObjectResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileUserUserGroupIdentifierType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectPortType;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectService;

import run.DataPrepare;
import run.RunAllSamples;



 /**
 * Sample      : SendObject
 * Category    : BusinessObject webservice
 *
 * Description : This sample demonstrates the usage of 'SendObject' webservice
 * using which an Agile object may be sent to a particular user. 
 * 
 * Note:- 'Notifications' must be allowed as per your Java client settings.
 * Check "Server Settings -> Database -> Notification Enabled " in your Java client
 * to verify this.
 */
public class SendObject {
    private static final String COMMAND_NAME = "SendObject";
	public static String clsName;
    public static String serviceName   =  "BusinessObject";       
    public static BusinessObjectPortType agileStub;        
    
    public static String partNumber;
    public static String userObj;
    
    public SendObject() {
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
            userObj = "User" + System.currentTimeMillis();
            dataPrepare.saveObjectAs("User", args[1],
                                     new String[]{"userID", UserConstants.ATT_LOGIN_PASSWORD.toString() },
                                     new String[]{userObj, args[2]});
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
            
            // Note:- To send objects through either webservices or the web user interface,
            // the notification feature must first be enabled through the java client via
            // admin --> Server settings --> Database --> Notification Enabled (Set to 'Yes')
            
            // Create the request object SendObjectRequestType for the getObject operation
            // Create an array of requests of type AgileSendObjectRequest . Batch operations may be
            // performed by populating as many request objects as required to send several objects at once
            SendObjectRequestType sendObjectRequestType = new SendObjectRequestType();
            AgileSendObjectRequest agileSendObjectRequest[] = new AgileSendObjectRequest[1];             
            agileSendObjectRequest[0] = new AgileSendObjectRequest();
            
            
            // For each of the requests, set the class identifier to specify the
            // type of object that will be deleted and an object number to identify
            // the unique object whose delete will be repealed.
            agileSendObjectRequest[0].setClassIdentifier("Part");
            agileSendObjectRequest[0].setObjectNumber( partNumber );
            System.out.println("Sending part '" + partNumber + "' to user '" + userObj + "'...\n");
            
            // Define a group of users using AgileUserUserGroupIdentifierType
            AgileUserUserGroupIdentifierType users[] = new AgileUserUserGroupIdentifierType[1];
            users[0] = new AgileUserUserGroupIdentifierType();
            users[0].setClassIdentifier("User");
            users[0].setObjectIdentifier( userObj );
            agileSendObjectRequest[0].getSendTo().addAll(Arrays.asList(users));
            agileSendObjectRequest[0].setComments("Test comments");


            // The request objects are set and the agile Stub is used to make the sendObject
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the sendObject operation.             
            sendObjectRequestType.getRequests().addAll(Arrays.asList(agileSendObjectRequest));
            SendObjectResponseType sendObjectResponseType = agileStub.sendObject(sendObjectRequestType);            
            System.out.println("STATUS CODE: " +  sendObjectResponseType.getStatusCode() );
                         
                         
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !sendObjectResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = sendObjectResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
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

