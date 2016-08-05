package collaboration.changestatus;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.api.ChangeConstants;
import com.agile.api.UserConstants;
import com.agile.api.UserGroupConstants;
import com.agile.ws.schema.collaboration.v1.jaxws.AgileChangeStatusRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.ChangeStatusRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.ChangeStatusResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileUserUserGroupIdentifierType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.service.collaboration.v1.jaxws.CollaborationPortType;
import com.agile.ws.service.collaboration.v1.jaxws.CollaborationService;

import run.DataPrepare;
import run.RunAllSamples;



/**
* Sample      : ChangeStatus
* Category    : Collaboration webservice
*
* Description : This sample demonstrates the 'changeStatus' webservice using 
* which the workflow state of a routable object may be expedited to another 
* workflow state. The request object contains specifications identifying the
* routable object and the new workflow state.
*/
public class ChangeStatus {
    private static final String COMMAND_NAME = "ChangeStatus";
    
	public static String clsName;
    public static String serviceName   =  "Collaboration";    
    public static CollaborationPortType agileStub;      
    
    public static String partNumber;
    public static String changeNumber;
    public static String newStatus;
    public static String user1;

    public ChangeStatus() {
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
            dataPrepare.setupCollaboration();
            dataPrepare.setupTable();
            
            partNumber = dataPrepare.createNewObject("Part");
            changeNumber = dataPrepare.getNextAutoNumber("ECO");
            dataPrepare.createObjectByTags("ECO", new String[]{"number"}, new String[]{changeNumber} );
            dataPrepare.addRow("ECO", changeNumber, ChangeConstants.TABLE_AFFECTEDITEMS.toString(),
                               ChangeConstants.ATT_AFFECTED_ITEMS_ITEM_NUMBER.toString(), partNumber);
            
            dataPrepare.setWorkflow(changeNumber, dataPrepare.getWorkFlow(changeNumber).toString() );
            dataPrepare.updateRow("ECO", changeNumber, ChangeConstants.TABLE_AFFECTEDITEMS.toString(),
                                "itemNumber",partNumber, "newRev", "2", "normalMessageElement");
            newStatus = dataPrepare.getnextStatus(changeNumber);
            dataPrepare.changeStatus(changeNumber, newStatus);
            newStatus = "CCB";
            
            user1 = "User" + System.currentTimeMillis();
            dataPrepare.saveObjectAs("User", args[1],
                                     new String[]{"userID", UserConstants.ATT_LOGIN_PASSWORD.toString() },
                                     new String[]{user1, args[2]});
            
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
            
            // Create the request object ChangeStatusRequestType for the changeStatus operation
            // Create an array of requests of type AgileChangeStatusRequestType. Batch operations may be
            // performed by populating as many request objects as required to change the status for 
            // several routable objects using a single webservice call.

            ChangeStatusRequestType changeStatusRequestType = new ChangeStatusRequestType();                         
            AgileChangeStatusRequestType agileChangeStatusRequestType[] = new AgileChangeStatusRequestType[1];                         
            agileChangeStatusRequestType[0] = new AgileChangeStatusRequestType();           

            
            // For each batched request, specify the type of object whose statuses are to
            // be retrieved and its unique object number.
            agileChangeStatusRequestType[0].setClassIdentifier("ECO");             
            agileChangeStatusRequestType[0].setObjectNumber( changeNumber );
            agileChangeStatusRequestType[0].setNewStatusIdentifier( newStatus );
            System.out.println("Changing status of change '" + changeNumber + "' to '" + newStatus + "'...\n");
            
            // An array of either users, usergroups or both, may be used to list the set of approvers
            // that have to added. Such a set of user information is expressed as an object of type
            // AgileUserUserGroupIdentifierType. Object and Class identifiers are set for each of 
            // these objects to denote user information.
            AgileUserUserGroupIdentifierType users[] = new AgileUserUserGroupIdentifierType[1];
            users[0] = new AgileUserUserGroupIdentifierType();                

            users[0].setClassIdentifier(UserConstants.CLASS_USER.toString());
            users[0].setObjectIdentifier( user1 );
            
            agileChangeStatusRequestType[0].getApprovers().addAll(Arrays.asList(users));
            agileChangeStatusRequestType[0].setComment("Comments");
            agileChangeStatusRequestType[0].setPassword(args[2]);
            agileChangeStatusRequestType[0].setAuditRelease(false);
            agileChangeStatusRequestType[0].setUrgent(false);
            agileChangeStatusRequestType[0].setNotifyOriginator(true);
            agileChangeStatusRequestType[0].setNotifyChangeAnalyst(true);
            agileChangeStatusRequestType[0].setNotifyCCB(true);

            // The request objects are set and the agile Stub is used to make the changeStatus
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the changeStatus operation. 
            changeStatusRequestType.getChangeStatusRequest().addAll(Arrays.asList(agileChangeStatusRequestType));             
            ChangeStatusResponseType changeStatusResponseType = agileStub.changeStatus(changeStatusRequestType);
            System.out.println("STATUS CODE: " +  changeStatusResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !changeStatusResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = changeStatusResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
            }
                else{
                        // If the webservice call was successful, state the same                
                        System.out.print("The status of the object " + agileChangeStatusRequestType[0].getObjectNumber());
                        System.out.println(" was successfully changed to " + agileChangeStatusRequestType[0].getNewStatusIdentifier() + ".");
                    }                
                    

            }
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }

}
