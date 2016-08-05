package attachment.checkoutff;

import java.net.URL;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.attachment.v1.jaxws.AgileCheckOutFFRequest;
import com.agile.ws.schema.attachment.v1.jaxws.AgileCheckOutFFResponse;
import com.agile.ws.schema.attachment.v1.jaxws.CheckOutFFRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.CheckOutFFResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningListType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningType;

import run.DataPrepare;
import run.RunAllSamples;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.service.attachment.v1.jaxws.AttachmentPortType;
import com.agile.ws.service.attachment.v1.jaxws.AttachmentService;


/**
* Sample      : CheckOutFF  
* Type        : Attachment webservice 
* 
* Description : This sample demonstrates the usage of 'checkOutFF' webservice.
* This sample 'checks out' an Agile file folder, an operation which must be
* performed prior to any modification of its contents.
*/
public class CheckOutFF {
    private static final String COMMAND_NAME = "CheckOutFF";
    
	public static String clsName;
    public static String serviceName   =  "Attachment";    
    public static AttachmentPortType agileStub;        
    
    public static String folderNumber;

    public CheckOutFF() {
             clsName = this.getClass().getName();

    }
      

    /**
     * The method setupServerLogin is used to login to the server by providing
     * the server url details and authentication credentials.
     * @param args 
     */
    
    public static void setupServerLogin(String[] args) throws Exception {       

         
    	//The AttachmentService is used to obtain a AdminMetadataPortType
     	URL url = new URL(args[0] + "/" + serviceName + "?WSDL");
        AttachmentService locator = new AttachmentService(url);

        //Eventually, the CheckOutFF webservice will be invoked by using this agileStub.
        agileStub = locator.getAttachment();


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
            
            folderNumber = dataPrepare.createNewObject("FileFolder");
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
            
            // Login to the server by providing the server url and authentication credentials.
            setupServerLogin(args);
            
            // Before adding a new file to a folder, the folder object must be checked out 
            // prior to any file operation. The checkout webservice is used to achieve the 
            // same, as demonstrated in this sample.
            
            // Create the request object CheckOutFFRequestType for the CheckOutFF operation
            // Create an array of requests of type AgileCheckOutFFRequestTypeType. Batch operations may be
            // performed by populating as many request objects as required to add checkout several
            // folders with one single operation.                        
            CheckOutFFRequestType checkOutFFRequestType = new CheckOutFFRequestType();                         
            AgileCheckOutFFRequest agileCheckOutFFRequest = new AgileCheckOutFFRequest();                         
            
            // For each batched request, specify the unique folder that will be checked out
            // by the webservice operation. Supply the folder number for the same.  
            agileCheckOutFFRequest.setFolderNumber( folderNumber );            
            System.out.println("Checking out the file folder '" + folderNumber + "'...\n");            

            // The request objects are set and the agile Stub is used to make the CheckOutFF
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the CheckOutFF operation. 
            checkOutFFRequestType.getRequests().add(agileCheckOutFFRequest);             
            CheckOutFFResponseType checkOutFFResponseType = agileStub.checkOutFF(checkOutFFRequestType);
            System.out.println("\nSTATUS CODE: " +  checkOutFFResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !checkOutFFResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = checkOutFFResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println("Exception Id:" + exceptions[j].getExceptionId() + "\nMessage: " + exceptions[j].getMessage() );
                }
                
                AgileWarningListType agileWarningListType[] = checkOutFFResponseType.getWarnings().toArray(new AgileWarningListType[0]);
                if(agileWarningListType!=null)
                for(int i=0; i<agileWarningListType.length; i++){
                    AgileWarningType warnings[] = agileWarningListType[i].getWarning().toArray(new AgileWarningType[0]);
                    for(int j=0; j<warnings.length; j++)
                        System.out.println("Warning Id: " + warnings[j].getWarningId() + "\nMessage: " + warnings[j].getMessage() );
                    
                }                    
                
            }
                else{
                // If the webservice call was successful, then list the folders checked out
                    System.out.println("The following folders were successfully checked out: ");                    
                    
                    AgileCheckOutFFResponse responses[] = checkOutFFResponseType.getResponses().toArray(new AgileCheckOutFFResponse[0]);
                    if(responses!=null)
                        for(int i=0; i<responses.length; i++)
                            System.out.println( (i+1) + ". " + responses[i].getFolderNumber() );
                }


            }
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }

}
