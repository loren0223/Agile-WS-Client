package collaboration.getreviewers;


import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.collaboration.v1.jaxws.AgileGetReviewersRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.AgileGetReviewersResponseType;
import com.agile.ws.schema.collaboration.v1.jaxws.GetReviewersRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.GetReviewersResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileUserUserGroupIdentifierType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.service.collaboration.v1.jaxws.CollaborationPortType;
import com.agile.ws.service.collaboration.v1.jaxws.CollaborationService;

import run.DataPrepare;
import run.RunAllSamples;


/**
 * Sample      : GetReviewers
 * Category    : Collaboration webservice
 *
 * Description : This sample demonstrates the retrieval of reviewers for a
 * particular workflow state of a routable object using the 'getReviewers'
 * webservice.
 */
public class GetReviewers {
  private static final String COMMAND_NAME = "GetReviewers";
  
  public static String clsName;
  public static String serviceName   =  "Collaboration";    
  public static CollaborationPortType agileStub;        

  public static String changeNumber;
  public static String status;

  public GetReviewers() {
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

      String[] jobFunctions = new String[]{"QA","Developer"};
      String approver = dataPrepare.createNewReviewer("User",jobFunctions);
      String observer = dataPrepare.createNewReviewer("User",jobFunctions);
      String acknowledger = dataPrepare.createNewReviewer("User",jobFunctions);
      dataPrepare.addReviewers(changeNumber, status, approver, observer, acknowledger);
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

      // Create the request object GetReviewersRequestType for the getReviewers operation
      // Create an array of requests of type AgileGetReviewersRequestType. Batch operations may be
      // performed by populating as many request objects as required to obtain several
      // workflow status objects.

      GetReviewersRequestType getReviewersRequestType = new GetReviewersRequestType();                         
      AgileGetReviewersRequestType agileGetReviewersRequestType[] = new AgileGetReviewersRequestType[1];                         
      agileGetReviewersRequestType[0] = new AgileGetReviewersRequestType();           

      // For each batched request, specify the type of object whose statuses are to
      // be retrieved and its unique object number.
      agileGetReviewersRequestType[0].setClassIdentifier("ECO");             
      agileGetReviewersRequestType[0].setObjectNumber( changeNumber );
      agileGetReviewersRequestType[0].setStatusIdentifier( status );

      // The request objects are set and the agile Stub is used to make the getReviewers
      // webservice call. The status code obtained from the response object is printed to
      // verify the success of the getReviewers operation. 
      getReviewersRequestType.getReviewersRequest().addAll(Arrays.asList(agileGetReviewersRequestType));             
      GetReviewersResponseType getReviewersResponseType = agileStub.getReviewers(getReviewersRequestType);
      System.out.println("STATUS CODE: " +  getReviewersResponseType.getStatusCode() );

      // If the status code is not 'SUCCESS', then populate the list of exceptions
      // returned by the webservice. 
      if( !getReviewersResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){
        RunAllSamples.reportFailure( clsName );
        AgileExceptionListType[] agileExceptionListType = getReviewersResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
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
        AgileGetReviewersResponseType responses[] =  getReviewersResponseType.getReviewersResponse().toArray(new AgileGetReviewersResponseType[0]);
        if(responses!=null)
          for(int i=0; i<responses.length; i++){                                                               
            System.out.println("Approvers retrieved for the object " + responses[i].getIdentifier().getObjectName() + ": ");                                
            AgileUserUserGroupIdentifierType approvers[] = responses[i].getApprovers().toArray(new AgileUserUserGroupIdentifierType[0]);
            if(approvers!=null)
              for(int j=0; j<approvers.length; j++) {  
                System.out.println("User " + (j+1) + ". " + approvers[j].getObjectIdentifier());
                System.out.println("JobFunction " + (j+1) + ". " + approvers[j].getJobFunction());
              }
            System.out.println("Observers retrieved for the object " + responses[i].getIdentifier().getObjectName() + ": ");                                
            AgileUserUserGroupIdentifierType observers[] = responses[i].getObservers().toArray(new AgileUserUserGroupIdentifierType[0]);
            if(observers!=null)
              for(int j=0; j<observers.length; j++) {  
                System.out.println("User " + (j+1) + ". " + observers[j].getObjectIdentifier());
                System.out.println("JobFunction " + (j+1) + ". " + observers[j].getJobFunction());
              }
            System.out.println("Acknowledgers retrieved for the object " + responses[i].getIdentifier().getObjectName() + ": ");                                
            AgileUserUserGroupIdentifierType acknowledgers[] = responses[i].getAcknowledgers().toArray(new AgileUserUserGroupIdentifierType[0]);
            if(acknowledgers!=null)
              for(int j=0; j<acknowledgers.length; j++) {  
                System.out.println("User " + (j+1) + ". " + acknowledgers[j].getObjectIdentifier());
                System.out.println("JobFunction " + (j+1) + ". " + acknowledgers[j].getJobFunction());
              }
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
