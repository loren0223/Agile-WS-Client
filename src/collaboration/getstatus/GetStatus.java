package collaboration.getstatus;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.collaboration.v1.jaxws.AgileGetStatusRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.AgileGetStatusResponseType;
import com.agile.ws.schema.collaboration.v1.jaxws.AgileStatusType;
import com.agile.ws.schema.collaboration.v1.jaxws.GetStatusRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.GetStatusResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.service.collaboration.v1.jaxws.CollaborationPortType;
import com.agile.ws.service.collaboration.v1.jaxws.CollaborationService;

import run.DataPrepare;
import run.RunAllSamples;



/**
* Sample      : GetStatus
* Category    : Collaboration webservice
*
* Description : This sample demonstrates retrieval of the current status of 
* a routable object using the 'getStatus' webservice.
*/
public class GetStatus {
    private static final String COMMAND_NAME = "GetStatus";
    
	public static String clsName;
    public static String serviceName   =  "Collaboration";    
    public static CollaborationPortType agileStub;        
    
    public static String changeNumber;

    public GetStatus() {
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
            
            changeNumber = dataPrepare.getNextAutoNumber("ECO");
            dataPrepare.createObjectByTags("ECO", new String[]{"number"}, new String[]{changeNumber} );            
            dataPrepare.setWorkflow(changeNumber, dataPrepare.getWorkFlow(changeNumber).toString() );
            
//            dataPrepare.changeStatus(changeNumber, "Submitted");
//            dataPrepare.changeStatus(changeNumber, "CCB");
//            dataPrepare.changeStatus(changeNumber, "Released");
//            dataPrepare.changeStatus(changeNumber, "Implemented");
            
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
            
            // Create the request object GetStatusRequestType for the getStatus operation
            // Create an array of requests of type AgileGetStatusRequestType. Batch operations may be
            // performed by populating as many request objects as required to obtain several
            // workflow status objects.

            GetStatusRequestType getStatusRequestType = new GetStatusRequestType();                         
            AgileGetStatusRequestType agileGetStatusRequestType[] = new AgileGetStatusRequestType[1];                         
            agileGetStatusRequestType[0] = new AgileGetStatusRequestType();           

            
            // For each batched request, specify the type of object whose statuses are to
            // be retrieved and its unique object number.
            agileGetStatusRequestType[0].setClassIdentifier("ECO");             
            agileGetStatusRequestType[0].setObjectNumber( changeNumber );
            System.out.println("Retrieving status information of change '" + changeNumber + "'...\n");

            // The request objects are set and the agile Stub is used to make the getStatus
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the getStatus operation. 
            getStatusRequestType.getStatusRequest().addAll(Arrays.asList(agileGetStatusRequestType));             
            GetStatusResponseType getStatusResponseType = agileStub.getStatus(getStatusRequestType);
            System.out.println("STATUS CODE: " +  getStatusResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !getStatusResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = getStatusResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
            }
                else{
                        // If the webservice call was successful, display the status information
                        // retrieved by the getStatus operation. This shall include the current
                        // workflow state and a list of states prior to and beyond that particular
                        // workflow state. A given workflow state is expressed as on object of type
                        // AgileStatusType. Further information about the status's display name, id
                        // may be obtained through the AgileStatusType status object.
                
                        AgileGetStatusResponseType responses[] =  getStatusResponseType.getStatusResponse().toArray(new AgileGetStatusResponseType[0]);
                        if(responses!=null)
                            for(int i=0; i<responses.length; i++){                                
                                System.out.print("Current status for object " + agileGetStatusRequestType[i].getObjectNumber() );
                                System.out.println(": " + responses[i].getCurrentStatus().getStatusDisplayName() );
                                AgileStatusType nextDefaultStatus = responses[i].getNextDefaultStatus();
                                
                                System.out.println("--------------------------------------------------");
                                System.out.println("The default next status:- ");
                                if(nextDefaultStatus!=null)
                                        System.out.println( nextDefaultStatus.getStatusDisplayName() );
                                
                                System.out.println("--------------------------------------------------");
                                System.out.println("Next valid statuses:- ");
                                AgileStatusType nextValidStatuses[] = responses[i].getNextStatus().toArray(new AgileStatusType[0]);                                
                                if(nextValidStatuses!=null)
                                    for(int j=0; j<nextValidStatuses.length; j++)
                                        System.out.println( (j+1) + ". " + nextValidStatuses[j].getStatusDisplayName() );                                
                                
                                System.out.println("--------------------------------------------------");                                
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
