package collaboration.commentrobject;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.api.UserConstants;
import com.agile.ws.schema.collaboration.v1.jaxws.AgileCommentRObjectRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.CommentRObjectRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.CommentRObjectResponseType;
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
* Sample      : CommentRObject
* Category    : Collaboration webservice
*
* Description : This sample demonstrates the webservice 'commentROObject' using
* which a routable object is commented upon. The request contains specifications
* relating to the same.
*/
public class CommentRObject {
    private static final String COMMAND_NAME = "CommentRObject";
    
	public static String clsName;
    public static String serviceName   =  "Collaboration";    
    public static CollaborationPortType agileStub;        

    public static String changeNumber;

    public CommentRObject() {
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
            
            changeNumber = dataPrepare.getNextAutoNumber("ECO");
            dataPrepare.createObjectByTags("ECO", new String[]{"number"}, new String[]{changeNumber} );
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
            
            // Create the request object CommentRObjectRequestType for the commentRObject operation
            // Create an array of requests of type AgileCommentRObjectRequestType. Batch operations may be
            // performed by populating as many request objects as required to comment several
            // routable objects with a single operation.

            CommentRObjectRequestType commentRObjectRequestType = new CommentRObjectRequestType();                         
            AgileCommentRObjectRequestType agileCommentRObjectRequestType[] = new AgileCommentRObjectRequestType[1];                         
            agileCommentRObjectRequestType[0] = new AgileCommentRObjectRequestType();           

           
            // For each batched request, specify the type of object and unique object
            // number of the routable object which has to be commented upon.
            // Use boolean variables to denote whether the orginators, change analysts
            // and CCB need to be notified. 'true' or 'false' may be used for the same.
            
            agileCommentRObjectRequestType[0].setClassIdentifier("ECO");             
            agileCommentRObjectRequestType[0].setObjectNumber( changeNumber );
            agileCommentRObjectRequestType[0].setComment("Comment");            
            agileCommentRObjectRequestType[0].setNotifyOriginator(true);
            agileCommentRObjectRequestType[0].setNotifyChangeAnalyst(true);
            agileCommentRObjectRequestType[0].setNotifyCCB(true);
            
            System.out.println("Commenting on the change object '" + changeNumber + "'...\n");
            

            // The request objects are set and the agile Stub is used to make the commentRObject
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the commentRObject operation.                         

            commentRObjectRequestType.getCommentRObjectRequest().addAll(Arrays.asList(agileCommentRObjectRequestType));             
            CommentRObjectResponseType commentRObjectResponseType = agileStub.commentRObject(commentRObjectRequestType);
            System.out.println("STATUS CODE: " +  commentRObjectResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !commentRObjectResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = commentRObjectResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println("Exception Id:" + exceptions[j].getExceptionId() + "\nMessage: " + exceptions[j].getMessage() );
                }
                
                AgileWarningListType agileWarningListType[] = commentRObjectResponseType.getWarnings().toArray(new AgileWarningListType[0]);
                if(agileWarningListType!=null)
                for(int i=0; i<agileWarningListType.length; i++){
                    AgileWarningType warnings[] = agileWarningListType[i].getWarning().toArray(new AgileWarningType[0]);
                    for(int j=0; j<warnings.length; j++)
                        System.out.println("Warning Id: " + warnings[j].getWarningId() + "\nMessage: " + warnings[j].getMessage() );
                    
                }                    
                
            }
                else{
                        // If the webservice call was successful, confirm the same                
                        System.out.print("The object " + agileCommentRObjectRequestType[0].getObjectNumber());
                        System.out.println(" was successfully commented upon using the webservice call ");
                    }                
                    

            }
           
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }

}
