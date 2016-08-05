package attachment.addfileattachment;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.attachment.v1.jaxws.AddFileAttachmentRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.AddFileAttachmentResponseType;
import com.agile.ws.schema.attachment.v1.jaxws.AgileAddFileAttachmentRequest;
import com.agile.ws.schema.attachment.v1.jaxws.AgileAddFileAttachmentRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.AgileAddFileAttachmentResponse;
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
* Sample      : AddFileAttachment  
* Type        : Attachment webservice 
* 
* Description : This sample demonstrates addition of a file attachment
* to the attachment tab of an Agile object. 
*/
public class AddFileAttachment {
    private static final String COMMAND_NAME = "AddFileAttachment";
    
	public static String clsName;
    public static String serviceName   =  "Attachment";    
    public static AttachmentPortType agileStub;        
    
    public static String partNumber;

    public AddFileAttachment() {
             clsName = this.getClass().getName();

    }
      

    /**
     * The method setupServerLogin is used to login to the server by providing
     * the server url details and authentication credentials.
     * @param args 
     */
    
    public static void setupServerLogin(String[] args) throws Exception {       

         
         //The AttachmentService is used to obtain a AttachmentPortType
    	 URL url = new URL(args[0] + "/" + serviceName + "?WSDL");
         AttachmentService locator = new AttachmentService(url);

         //Eventually, the addFileAttachment webservice will be invoked by using this agileStub.
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
            
            // Login to the server by providing the server url and authentication credentials.
            setupServerLogin(args);             
            
            // Create the request object AddFileAttachmentRequestType for the addFileAttachment operation
            // Create an array of requests of type AgileAddFileAttachmentRequestType. Batch operations may be
            // performed by populating as many request objects as required to add several files to different
            // objects with one single operation.            
            AddFileAttachmentRequestType addFileAttachmentRequestType = new AddFileAttachmentRequestType();                         
            AgileAddFileAttachmentRequest agileAddFileAttachmentRequest = new AgileAddFileAttachmentRequest();                         
            agileAddFileAttachmentRequest = new AgileAddFileAttachmentRequest();           
            
            // For each batched request, specify the unique object to whose attachment tab
            // the files shall be added. Supply class identifier and object number information
            // for the same.
            agileAddFileAttachmentRequest.setClassIdentifier("Part");                        
            agileAddFileAttachmentRequest.setObjectNumber( partNumber );
            System.out.println("Adding an attachment to the part '" + partNumber + "'...");
            
            // The exact specification of the attachment to be added is defined as an
            // object of type AgileAddFileAttachmentRequestType. This object includes information
            // about the name of the file and its description and content.
            AgileAddFileAttachmentRequestType attachments = new AgileAddFileAttachmentRequestType();                                        
            attachments = new AgileAddFileAttachmentRequestType();                
            attachments.setName("Filename.txt");
            attachments.setDescription("Description for file ");
            
            File file = File.createTempFile("Filename", "txt");
            FileWriter writer  = new FileWriter(file);
            writer.write("File Content...file");
            writer.close();
            
            FileDataSource datasource = new FileDataSource(file);
            attachments.setContent( new DataHandler(datasource) );
            
            agileAddFileAttachmentRequest.getAttachments().add(attachments);                         
            agileAddFileAttachmentRequest.setSingleFolder(false);

            // The request objects are set and the agile Stub is used to make the addFileAttachment
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the addFileAttachment operation. 
            addFileAttachmentRequestType.getRequests().add(agileAddFileAttachmentRequest);             
            AddFileAttachmentResponseType addFileAttachmentResponseType = agileStub.addFileAttachment(addFileAttachmentRequestType);
            System.out.println("\nSTATUS CODE: " +  addFileAttachmentResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !addFileAttachmentResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                
                List<AgileExceptionListType> lstAgileExceptionListType = addFileAttachmentResponseType.getExceptions();
                if(lstAgileExceptionListType!=null)
                for(AgileExceptionListType agileExceptionListType : lstAgileExceptionListType){
                    List<AgileExceptionType> exceptions = agileExceptionListType.getException();
                    for(AgileExceptionType exception : exceptions)
                        System.out.println("Exception Id:" + exception.getExceptionId() + "\nMessage: " + exception.getMessage() );
                }
                
                List<AgileWarningListType> lstAgileWarningListType = addFileAttachmentResponseType.getWarnings();
                if(lstAgileWarningListType!=null)
                for(AgileWarningListType agileWarningListType : lstAgileWarningListType){
                    List<AgileWarningType> warnings = agileWarningListType.getWarning();
                    for(AgileWarningType warning : warnings)
                        System.out.println("Warning Id: " + warning.getWarningId() + "\nMessage: " + warning.getMessage() );
                    
                }                    
                
            }
                else{
                // If the webservice call was successful, then state the same
                    
                    List<AgileAddFileAttachmentResponse> responses = addFileAttachmentResponseType.getResponses();                    
                
                    if(responses!=null)
                    for(AgileAddFileAttachmentResponse response : responses){
                        
                        System.out.println("The specified file was successfully added to the Attachment tab");
                        System.out.println("of the object: " + response.getObjectNumber() );
                    
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
