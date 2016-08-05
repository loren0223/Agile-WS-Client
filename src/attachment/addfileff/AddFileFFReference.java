package attachment.addfileff;

import com.agile.ws.schema.attachment.v1.jaxws.AddFileAttachmentRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.AddFileAttachmentResponseType;
import com.agile.ws.schema.attachment.v1.jaxws.AddFileFFRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.AddFileFFResponseType;
import com.agile.ws.schema.attachment.v1.jaxws.AddFileReferenceFFType;
import com.agile.ws.schema.attachment.v1.jaxws.AgileAddFileAttachmentRequest;
import com.agile.ws.schema.attachment.v1.jaxws.AgileAddFileAttachmentRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.AgileAddFileFFRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.AgileCheckOutFFRequest;
import com.agile.ws.schema.attachment.v1.jaxws.AgileCheckOutFFResponse;
import com.agile.ws.schema.attachment.v1.jaxws.CheckOutFFRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.CheckOutFFResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningListType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningType;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.ws.BindingProvider;

import run.DataPrepare;
import run.SDKDataPrepare;
import run.RunAllSamples;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.service.attachment.v1.jaxws.AttachmentPortType;
import com.agile.ws.service.attachment.v1.jaxws.AttachmentService;


/**
* Sample      : AddFileFFReference  
* Type        : Attachment webservice 
* 
* Description : This sample demonstrates addition of a file to the 
* 'Files' tab of a file folder using a reference obtained from the
* DFS file server, for a file that was already added to DFS.
*/
public class AddFileFFReference {
    private static final String COMMAND_NAME = "AddFileFFReference";
    
	public static String clsName;
    public static String SERVER_INF =  null;
    public static String WEB_URL =  null;
    public static String USERNAME   =  null;
    public static String PASSWORD   =  null;
    public static String serviceName   =  "Attachment";    
    public static String SERVER_URL   =  null;
    public static AttachmentPortType agileStub;        
    
    public static String folderNumber;
    public static String partNumberForFileAttachment;
    public static String DFS_FILESERVER_URL = null;

    public AddFileFFReference() {
             clsName = this.getClass().getName();

    }
      

    /**
     * The method setupServerLogin is used to login to the server by providing
     * the server url details and authentication credentials.
     */
    
    public static void setupServerLogin() throws Exception {       

        
        //The AttachmentService is used to obtain a AdminMetadataPortType
    	URL url = new URL(SERVER_URL+"?WSDL");
        AttachmentService locator = new AttachmentService(url);

       //Eventually, the AddFileFF webservice will be invoked by using this agileStub.
         agileStub = locator.getAttachment();


       // Username and password details are specified for the agileStub
         Map<String, Object> reqContext = ((BindingProvider)agileStub).getRequestContext();
         reqContext.put(BindingProvider.USERNAME_PROPERTY, USERNAME);
         reqContext.put(BindingProvider.PASSWORD_PROPERTY, PASSWORD);
    }
    
    public static String getDFSFileReference() throws Exception{

//  	Create the request object AddFileAttachmentRequestType for the addFileAttachment operation
    	// Create an array of requests of type AgileAddFileAttachmentRequestType. Batch operations may be
    	// performed by populating as many request objects as required to add several files to different
    	// objects with one single operation.            
    	AddFileAttachmentRequestType addFileAttachmentRequestType = new AddFileAttachmentRequestType();                         
    	AgileAddFileAttachmentRequest agileAddFileAttachmentRequest = new AgileAddFileAttachmentRequest();                         

    	// For each batched request, specify the unique object to whose attachment tab
    	// the files shall be added. Supply class identifier and object number information
    	// for the same.
    	agileAddFileAttachmentRequest.setClassIdentifier("Part");                        
    	agileAddFileAttachmentRequest.setObjectNumber( partNumberForFileAttachment );
    	System.out.println("@@@@@@@ : " + partNumberForFileAttachment);
    	System.out.println("Adding an attachment to the part '" + partNumberForFileAttachment + "'...");

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

    	SDKDataPrepare sdkDataPrepare = new SDKDataPrepare(WEB_URL, USERNAME, PASSWORD);
    	sdkDataPrepare.connect();

    	int refId = sdkDataPrepare.getAttachedFileRefId("Part",partNumberForFileAttachment);
    	return String.valueOf(refId);
    }


 	/**
	 * This sample can be configured by passing server inf, user name,
	 * password, DFS server url and web server url as program arguments
	 * in the same order. This method checks for these values.
	 * 
	 * @param args
	 */
	private static void checkArguments(String[] args) {
		RunAllSamples.runCount++;
		
		if (args.length < 5) {
			// should pass arguments through the command line
			printUsage();
			System.exit(-1);
		}
		
		SERVER_INF = args[0];
		SERVER_URL   =  SERVER_INF + "/" + serviceName;            
        USERNAME = args[1];
        PASSWORD = args[2];
        DFS_FILESERVER_URL = args[3];
        WEB_URL = args[4];
	}

	/**
	 * print usage message to the standard error
	 */
	private static void printUsage() {
		System.err.println("Usage: " + COMMAND_NAME + 
				" server_inf user_name password dfs_server_url web_server_url");
		System.err.println("\t" + "server_inf: the server information");
		System.err.println("\t" + "user_name: the user name");
		System.err.println("\t" + "password: the password");
		System.err.println("\t" + "dfs_server_url: the DFS server url");
		System.err.println("\t" + "web_server_url: the web server url");
	}

    public static void prepareData(){
        
        try{
            System.out.println("<<< Preparing Data... >>>");
            DataPrepare dataPrepare = new DataPrepare(SERVER_INF, USERNAME, PASSWORD);
            dataPrepare.setupAdminMetadata(); 
            dataPrepare.setupBusiness();
            
            folderNumber = dataPrepare.createNewObject("FileFolder");
            partNumberForFileAttachment = dataPrepare.createNewObject("Part");
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
        prepareData();

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");
        
        try{
            
            // Login to the server by providing the server url and authentication credentials.
            setupServerLogin();
            
            // Before adding a new file to a folder, the folder object must be checked out 
            // prior to any file operation. In this sample, the method 'checkOutFolder' is
            // written using the checkOutFF webservice to facilitate the checkOut operation
            // on the folder before files are actually added to it. For a more detailed 
            // explanation of checking out folders using webservices, refer to the sample
            // provided on that particular topic.
            System.out.println("\nChecking out folder before adding a file... " );
            checkOutFolder(folderNumber);
            
            // Create the request object AddFileFFRequestType for the AddFileFF operation
            // Create an array of requests of type AgileAddFileFFRequestTypeType. Batch operations may be
            // performed by populating as many request objects as required to add several files to different
            // folders with one single operation.                        
            AddFileFFRequestType addFileFFRequestType = new AddFileFFRequestType();                         
            AgileAddFileFFRequestType agileAddFileFFRequestType = new AgileAddFileFFRequestType();                         
            agileAddFileFFRequestType = new AgileAddFileFFRequestType();           
            
            // For each batched request, specify the unique folder to whose files tab
            // the files shall be added. Supply folder number details for the same.  
            agileAddFileFFRequestType.setFolderNumber(folderNumber);
            System.out.println("\nAdding a file to the folder '" + folderNumber + "' by using a file reference");
            System.out.println("that was obtained by using the DFS file server");
            
            // The exact specification of the attachment to be added is defined as an
            // object of type AddFileFFType. This object includes information about the 
            // name of the file and its description and content.                        
            AddFileReferenceFFType reference = new AddFileReferenceFFType();
            reference = new AddFileReferenceFFType();
            reference.setFileId( Integer.parseInt( getDFSFileReference() ) );
            reference.setFileName("FileThroReference.txt");
            reference.setDescription("Description of a file that was added using a reference");
            reference.setFileSize( new Long(1) );
            agileAddFileFFRequestType.getFileRefs().add( reference );

            // The request objects are set and the agile Stub is used to make the AddFileFF
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the AddFileFF operation. 
            addFileFFRequestType.getRequest().add(agileAddFileFFRequestType);             
            AddFileFFResponseType AddFileFFResponseType = agileStub.addFileFF(addFileFFRequestType);
            System.out.println("\nSTATUS CODE: " +  AddFileFFResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !AddFileFFResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                
                List<AgileExceptionListType> lstAgileExceptionListType = AddFileFFResponseType.getExceptions();
                if(lstAgileExceptionListType!=null)
                for(AgileExceptionListType agileExceptionListType : lstAgileExceptionListType){
                    List<AgileExceptionType> exceptions = agileExceptionListType.getException();
                    for(AgileExceptionType exception : exceptions)
                        System.out.println("Exception Id:" + exception.getExceptionId() + "\nMessage: " + exception.getMessage() );
                }
                
                List<AgileWarningListType> lstAgileWarningListType = AddFileFFResponseType.getWarnings();
                if(lstAgileWarningListType!=null)
                for(AgileWarningListType agileWarningListType : lstAgileWarningListType){
                    List<AgileWarningType> warnings = agileWarningListType.getWarning();
                    for(AgileWarningType warning : warnings)
                        System.out.println("Warning Id: " + warning.getWarningId() + "\nMessage: " + warning.getMessage() );
                    
                }                    
                
            }
                else{
                // If the webservice call was successful, then state the same
                    System.out.print("The specified file(s) were successfully added to the folder ");                    
                    System.out.println( agileAddFileFFRequestType.getFolderNumber() + ".");
                }


            }
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }

    public static void checkOutFolder(String folderNumber){
        
        try{        
        
        CheckOutFFRequestType checkOutFFRequestType = new CheckOutFFRequestType();                         
        AgileCheckOutFFRequest agileCheckOutFFRequest = new AgileCheckOutFFRequest();                         
        agileCheckOutFFRequest = new AgileCheckOutFFRequest();                   

        agileCheckOutFFRequest.setFolderNumber(folderNumber);            

        checkOutFFRequestType.getRequests().add(agileCheckOutFFRequest);             
        CheckOutFFResponseType checkOutFFResponseType = agileStub.checkOutFF(checkOutFFRequestType);
        System.out.println("STATUS CODE: " +  checkOutFFResponseType.getStatusCode() );

        if( !checkOutFFResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){
            
        	List<AgileExceptionListType> lstAgileExceptionListType = checkOutFFResponseType.getExceptions();
            if(lstAgileExceptionListType!=null)
            for(AgileExceptionListType agileExceptionListType : lstAgileExceptionListType){
                List<AgileExceptionType> exceptions = agileExceptionListType.getException();
                for(AgileExceptionType exception : exceptions)
                    System.out.println("Exception Id:" + exception.getExceptionId() + "\nMessage: " + exception.getMessage() );
            }
            
            List<AgileWarningListType> lstAgileWarningListType = checkOutFFResponseType.getWarnings();
            if(lstAgileWarningListType!=null)
            for(AgileWarningListType agileWarningListType : lstAgileWarningListType){
                List<AgileWarningType> warnings = agileWarningListType.getWarning();
                for(AgileWarningType warning : warnings)
                    System.out.println("Warning Id: " + warning.getWarningId() + "\nMessage: " + warning.getMessage() );
                
            } 
            
        }
            else{
                System.out.println("The following folders were successfully checked out: ");                    
                
                List<AgileCheckOutFFResponse> responses = checkOutFFResponseType.getResponses();
                if(responses!=null)
                    for(int i=0; i<responses.size(); i++)
                        System.out.println( (i+1) + ". " + responses.get(i).getFolderNumber() );
            }

        } catch (Exception ex) {
        ex.printStackTrace();
        }        
        
        
    }

}
