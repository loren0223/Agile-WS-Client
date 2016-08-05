package collaboration.rejectRObject;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.api.ChangeConstants;
import com.agile.api.UserConstants;
import com.agile.api.UserGroupConstants;
import com.agile.ws.schema.collaboration.v1.jaxws.AgileRejectRObjectRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.RejectRObjectRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.RejectRObjectResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileUserUserGroupIdentifierType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningListType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.service.collaboration.v1.jaxws.CollaborationPortType;
import com.agile.ws.service.collaboration.v1.jaxws.CollaborationService;

import run.DataPrepare;
import run.RunAllSamples;



/**
* Sample      : RejectRObject
* Category    : Collaboration webservice
*
* Description : This sample demonstrates the process of rejecting a routable 
* object using webservices. The request contains specifications relating to the
* same.
*/
public class RejectRObject {
    private static final String COMMAND_NAME = "RejectRObject";
    
	public static String clsName;
    public static String serviceName   =  "Collaboration";    
    public static CollaborationPortType agileStub;        
    
    public static String changeNumber;
    public static String notifier1;
    
    public RejectRObject() {
             clsName = this.getClass().getName();

    }
      

    /**
     * The method setupServerLogin is used to login to the server by providing
     * the server url details and authentication credentials.
     * @param args 
     */
    
    public static void setupServerLogin(String[] args) throws Exception {       

         
    	//The CollaborationServiceLocator is used to obtain a Collaboration_BindingStub
       	URL url = new URL(args[0] + "/" + serviceName + "?WSDL"); 
       	CollaborationService locator = 
                new CollaborationService(url);

           //A stub of type Collaboration_BindingStub is obtained by specifying the server URL
           //Eventually, the addApprovers webservice will be invoked by using this agileStub.
             agileStub = locator.getCollaboration();
                    


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
            dataPrepare.setupTable();
            dataPrepare.setupCollaboration();
            
            String partNumber = dataPrepare.createNewObject("Part");
            changeNumber = dataPrepare.getNextAutoNumber("ECO");
            dataPrepare.createObjectByTags("ECO", new String[]{"number"}, new String[]{changeNumber} );
            dataPrepare.addRow("ECO", changeNumber, ChangeConstants.TABLE_AFFECTEDITEMS.toString(),
                               ChangeConstants.ATT_AFFECTED_ITEMS_ITEM_NUMBER.toString(), partNumber);
            
            dataPrepare.setWorkflow(changeNumber, dataPrepare.getWorkFlow(changeNumber).toString() );
            String newStatus = dataPrepare.getnextStatus(changeNumber);
            dataPrepare.changeStatus(changeNumber, newStatus );
            dataPrepare.updateRow("ECO",
            		changeNumber,
            		ChangeConstants.TABLE_AFFECTEDITEMS.toString(),
            		"itemNumber",partNumber,
            		"newRev", "A", "normalMessageElement");
            
            newStatus = "CCB";
            dataPrepare.changeStatus(changeNumber, newStatus );
            dataPrepare.addUserApprovers(changeNumber, newStatus, new String[]{args[1]});
            
            notifier1 = "User1" + System.currentTimeMillis();
            dataPrepare.saveObjectAs("User", args[1],
                                     new String[]{"userID", UserConstants.ATT_LOGIN_PASSWORD.toString() },
                                     new String[]{notifier1, args[2]});
            
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
            
            // Login to the server by providing the server url and authentication credentials.
            setupServerLogin(args);             
            
            // Create the request object RejectRObjectRequestType for the rejectRObject operation
            // Create an array of requests of type AgileRejectRObjectRequestType. Batch operations may be
            // performed by populating as many request objects as required to reject several
            // routable objects with a single operation.

            RejectRObjectRequestType rejectRObjectRequestType = new RejectRObjectRequestType();                         
            AgileRejectRObjectRequestType agileRejectRObjectRequestType[] = new AgileRejectRObjectRequestType[1];                         
            agileRejectRObjectRequestType[0] = new AgileRejectRObjectRequestType();           

            
            // For each batched request, specify the type of object and unique object
            // number of the routable object which has to be rejected. The user with 
            // which the agileStub has been authenticated is assumed to be the approver.
            // The approval password of that user is specified in the request and pertinent
            // comments are added for the rejecting the routable object.
            
            agileRejectRObjectRequestType[0].setClassIdentifier("ECO");             
            agileRejectRObjectRequestType[0].setObjectNumber( changeNumber );
            agileRejectRObjectRequestType[0].setPassword( args[2] );
            agileRejectRObjectRequestType[0].setComment("Comment");
            
            System.out.println("Rejecting ECO '" + changeNumber + "'...\n");
            
            // If the second signature has been configured in your java client
            // then the value of the same shall have to be set into the request 
            // object. The second signature may be either the username or password
            // depending on the Java Client's configuration from  the 'Preferences' tab
            agileRejectRObjectRequestType[0].setSecondSignature(null);
            AgileUserUserGroupIdentifierType user = new AgileUserUserGroupIdentifierType();
            user.setClassIdentifier(UserConstants.CLASS_USER.toString());
            user.setObjectIdentifier(args[1]);
            agileRejectRObjectRequestType[0].getRejectForGroups().add(user);
            
            // Additional options may also be elaborated upon using the request object:
            // Specify a list of notifiers using an object of type AgileUserUserGroupIdentifierType.
            // Escalations, transfers and the facility of approving on behalf of a group may also
            // be achieved by the same method. This may be used in cases when the approval on member
            // of a usergroup is sufficient to expedite the approval process. 
            // In this sample, we specify a list of notifiers as shown:
            
            AgileUserUserGroupIdentifierType notifiers[] = new AgileUserUserGroupIdentifierType[1];
            notifiers[0] = new AgileUserUserGroupIdentifierType();                
            notifiers[0].setClassIdentifier(UserConstants.CLASS_USER.toString());
            notifiers[0].setObjectIdentifier( notifier1 );            
            
            
            agileRejectRObjectRequestType[0].getNotifiers().addAll(Arrays.asList(notifiers));
            agileRejectRObjectRequestType[0].setSignoffForSelf(true);

            // The request objects are set and the agile Stub is used to make the rejectRObject
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the rejectRObject operation.                         

            rejectRObjectRequestType.getRejectRObject().addAll(Arrays.asList(agileRejectRObjectRequestType));             
            RejectRObjectResponseType rejectRObjectResponseType = agileStub.rejectRObject(rejectRObjectRequestType);
            System.out.println("STATUS CODE: " +  rejectRObjectResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !rejectRObjectResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = rejectRObjectResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println("Exception Id:" + exceptions[j].getExceptionId() + "\nMessage: " + exceptions[j].getMessage() );
                }
                
                AgileWarningListType agileWarningListType[] = rejectRObjectResponseType.getWarnings().toArray(new AgileWarningListType[0]);
                if(agileWarningListType!=null)
                for(int i=0; i<agileWarningListType.length; i++){
                    AgileWarningType warnings[] = agileWarningListType[i].getWarning().toArray(new AgileWarningType[0]);
                    for(int j=0; j<warnings.length; j++)
                        System.out.println("Warning Id: " + warnings[j].getWarningId() + "\nMessage: " + warnings[j].getMessage() );
                    
                }                    
                
            }
                else{
                        // If the webservice call was successful, confirm the same                
                        System.out.print("The object " + agileRejectRObjectRequestType[0].getObjectNumber());
                        System.out.println(" was successfully rejected using the webservice call ");
                    }                
                    

            }
            
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }

}
