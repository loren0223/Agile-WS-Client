package attachment.cancelcheckout;

import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.attachment.v1.jaxws.AgileCancelCheckOutFFRequest;
import com.agile.ws.schema.attachment.v1.jaxws.AgileCancelCheckOutFFResponse;
import com.agile.ws.schema.attachment.v1.jaxws.CancelCheckOutFFRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.CancelCheckOutFFResponseType;
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
* Sample      : v  
* Type        : Attachment webservice 
* 
* Description : This sample demonstrates the usage of 'cancelCheckOut'
* webservice which is used in this sample to cancel the 'checked out'
* status of a webservice.
*/
public class CancelCheckOut {
    private static final String COMMAND_NAME = "CancelCheckOut";
    
	public static String clsName;
    public static String serviceName   =  "Attachment";    
    public static AttachmentPortType agileStub;        

    public static String folderNumber;
    
    public CancelCheckOut() {
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

       //Eventually, the cancelCheckOutFF webservice will be invoked by using this agileStub.
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
            dataPrepare.setupAttachment();
            
            folderNumber = dataPrepare.createNewObject("FileFolder");
            dataPrepare.checkOutFolder(folderNumber);
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
            
            // Create the request object CancelCheckOutFFRequestType for the CancelCheckOutFF operation
            // Create an array of requests of type AgileCancelCheckOutFFRequestTypeType. Batch operations may be
            // performed by populating as many request objects as required to add checkout several
            // folders with one single operation.                        
            CancelCheckOutFFRequestType cancelCheckOutFFRequestType = new CancelCheckOutFFRequestType();                         
            AgileCancelCheckOutFFRequest agileCancelCheckOutFFRequest = new AgileCancelCheckOutFFRequest();                         
            agileCancelCheckOutFFRequest = new AgileCancelCheckOutFFRequest();           
            
            // For each batched request, specify the unique folder that will be checked out
            // by the webservice operation. Supply the folder number for the same.  
            agileCancelCheckOutFFRequest.setFolderNumber( folderNumber );            
            System.out.println("Cancelling 'Check out' status on folder '" + folderNumber + "'...");

            // The request objects are set and the agile Stub is used to make the CancelCheckOutFF
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the CancelCheckOutFF operation. 
            cancelCheckOutFFRequestType.getRequests().add(agileCancelCheckOutFFRequest);             
            CancelCheckOutFFResponseType CancelCheckOutFFResponseType = agileStub.cancelCheckOutFF(cancelCheckOutFFRequestType);
            System.out.println("\nSTATUS CODE: " +  CancelCheckOutFFResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !CancelCheckOutFFResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                
                List<AgileExceptionListType> lstAgileExceptionListType = CancelCheckOutFFResponseType.getExceptions();
                if(lstAgileExceptionListType!=null)
                for(AgileExceptionListType agileExceptionListType : lstAgileExceptionListType){
                    List<AgileExceptionType> exceptions = agileExceptionListType.getException();
                    for(AgileExceptionType exception : exceptions)
                        System.out.println("Exception Id:" + exception.getExceptionId() + "\nMessage: " + exception.getMessage() );
                }
                
                List<AgileWarningListType> lstAgileWarningListType = CancelCheckOutFFResponseType.getWarnings();
                if(lstAgileWarningListType!=null)
                for(AgileWarningListType agileWarningListType : lstAgileWarningListType){
                    List<AgileWarningType> warnings = agileWarningListType.getWarning();
                    for(AgileWarningType warning : warnings)
                        System.out.println("Warning Id: " + warning.getWarningId() + "\nMessage: " + warning.getMessage() );
                    
                }                    
                
            }
                else{
                // If the webservice call was successful, then list the folders checked out
                    System.out.println("The 'checked out' status on the following folders was successfully cancelled: ");                    
                    
                    List<AgileCancelCheckOutFFResponse> responses = CancelCheckOutFFResponseType.getResponses();
                    if(responses!=null)
                        for(int i=0; i<responses.size(); i++)
                            System.out.println( (i+1) + ". " + responses.get(i).getFolderNumber() );
                }


            }
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }

}
