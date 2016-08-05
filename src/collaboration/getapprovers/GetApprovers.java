package collaboration.getapprovers;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.api.ChangeConstants;
import com.agile.api.UserConstants;
import com.agile.ws.schema.collaboration.v1.jaxws.AgileGetApproversRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.AgileGetApproversResponseType;
import com.agile.ws.schema.collaboration.v1.jaxws.GetApproversRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.GetApproversResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileIdentifierType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.service.collaboration.v1.jaxws.CollaborationPortType;
import com.agile.ws.service.collaboration.v1.jaxws.CollaborationService;

import run.DataPrepare;
import run.RunAllSamples;



/**
* Sample      : GetApprovers
* Category    : Collaboration webservice
*
* Description : This sample demonstrates the retrieval of approvers for a
* particular workflow state of a routable object using the 'getApprovers'
* webservice.
*/
public class GetApprovers {
    private static final String COMMAND_NAME = "GetApprovers";
    
	public static String clsName;
    public static String serviceName   =  "Collaboration";    
    public static CollaborationPortType agileStub;        
    
    public static String changeNumber;
    public static String status;

    public GetApprovers() {
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
            
            changeNumber = dataPrepare.getNextAutoNumber("ECO");
            dataPrepare.createObjectByTags("ECO", new String[]{"number"}, new String[]{changeNumber} );            
            dataPrepare.setWorkflow(changeNumber, dataPrepare.getWorkFlow(changeNumber).toString() );
            
            status = dataPrepare.getnextStatus(changeNumber);
            dataPrepare.changeStatus(changeNumber, status );
            status = "CCB";
            dataPrepare.changeStatus(changeNumber, status );
            
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
            
            // Create the request object GetApproversRequestType for the getApprovers operation
            // Create an array of requests of type AgileGetApproversRequestType. Batch operations may be
            // performed by populating as many request objects as required to obtain several
            // workflow status objects.

            GetApproversRequestType getApproversRequestType = new GetApproversRequestType();                         
            AgileGetApproversRequestType agileGetApproversRequestType[] = new AgileGetApproversRequestType[1];                         
            agileGetApproversRequestType[0] = new AgileGetApproversRequestType();           
            
            // For each batched request, specify the type of object whose statuses are to
            // be retrieved and its unique object number.
            agileGetApproversRequestType[0].setClassIdentifier("ECO");             
            agileGetApproversRequestType[0].setObjectNumber( changeNumber );
            agileGetApproversRequestType[0].setStatusIdentifier( status );
            System.out.println("Retrieving approvers for the '" + status + "' state of the change object '" + changeNumber + "'...\n");

            // The request objects are set and the agile Stub is used to make the getApprovers
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the getApprovers operation. 
            getApproversRequestType.getApproversRequest().addAll(Arrays.asList(agileGetApproversRequestType));             
            GetApproversResponseType getApproversResponseType = agileStub.getApprovers(getApproversRequestType);
            System.out.println("STATUS CODE: " +  getApproversResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !getApproversResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = getApproversResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
            }
                else{
                        // If the webservice call was successful, display the list of
                        // users who are designated as approvers for the particular 
                        // status queried for in the request.
                
                        AgileGetApproversResponseType responses[] =  getApproversResponseType.getApproversResponse().toArray(new AgileGetApproversResponseType[0]);
                        if(responses!=null)
                            for(int i=0; i<responses.length; i++){                                
                                
                                System.out.println("Approvers retrieved for the object " + responses[i].getIdentifier().getObjectName() + ": ");
                                
                                AgileIdentifierType approvers[] = responses[i].getApprovers().toArray(new AgileIdentifierType[0]);
                                if(approvers!=null)
                                for(int j=0; j<approvers.length; j++)    
                                    System.out.println(" " + (j+1) + ". " + approvers[j].getName() );
        
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
