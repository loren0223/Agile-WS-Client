package attachment.getfileattachment;

import java.net.URL;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.attachment.v1.jaxws.AgileFileAttachmentRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.AgileFileAttachmentResponseType;
import com.agile.ws.schema.attachment.v1.jaxws.AgileGetFileAttachmentRequest;
import com.agile.ws.schema.attachment.v1.jaxws.AgileGetFileAttachmentResponse;
import com.agile.ws.schema.attachment.v1.jaxws.GetFileAttachmentRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.GetFileAttachmentResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningListType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningType;

import run.DataPrepare;
import run.RunAllSamples;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.service.attachment.v1.jaxws.AttachmentPortType;
import com.agile.ws.service.attachment.v1.jaxws.AttachmentService;
import com.agile.ws.service.table.v1.jaxws.TablePortType;


/**
* Sample      : GetFileAttachmentAllFiles
* Type        : Attachment webservice
*
* Description : This sample demonstrates the usage of 'getFileAttachment' webservice
* for a specific case where all the files of an Agile object need to be retrieved.
* The request object contains the specifications that identify the attachment to be
* downloaded and for each attachment an array of bytes is obtained in the response object.
*/
public class GetFileAttachmentAllFiles {
    private static final String COMMAND_NAME = "GetFileAttachmentAllFiles";
    private static String[] cmdArgs = null;
    
	public static String clsName;
    public static String serviceName_2 = "Table";
    public static String serviceName   =  "Attachment";    
    public static AttachmentPortType agileStub;        
    public static TablePortType agileStub_Table;        
    
    public static String partNumber;

    public GetFileAttachmentAllFiles() {
             clsName = this.getClass().getName();

    }
      

    /**
     * The method setupServerLogin is used to login to the server by providing
     * the server url details and authentication credentials.
     */
    
    public static void setupServerLogin() throws Exception {       

         
    	//The AttachmentService is used to obtain a AdminMetadataPortType
     	URL url = new URL(cmdArgs[0] + "/" + serviceName + "?WSDL");
        AttachmentService locator = new AttachmentService(url);

        //Eventually, the getFileAttachment webservice will be invoked by using this agileStub.
        agileStub = locator.getAttachment();


        // Username and password details are specified for the agileStub
        Map<String, Object> reqContext = ((BindingProvider)agileStub).getRequestContext();
        reqContext.put(BindingProvider.USERNAME_PROPERTY, cmdArgs[1]);
        reqContext.put(BindingProvider.PASSWORD_PROPERTY, cmdArgs[2]);
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
		
		cmdArgs = args;
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

    public static void prepareData(){
        
        try{
            System.out.println("<<< Preparing Data... >>>");
            DataPrepare dataPrepare = new DataPrepare(cmdArgs[0], cmdArgs[1], cmdArgs[2]);
            dataPrepare.setupAdminMetadata(); 
            dataPrepare.setupBusiness();
            dataPrepare.setupAttachment();
            dataPrepare.setupTable();
            
            partNumber = dataPrepare.createNewObject("Part");            
            String fileName[] = new String[2];
            for(int i=0; i<fileName.length; i++)
                fileName[i] = partNumber + "_File12" + (i+1) + ".txt";
            dataPrepare.addAttachmentToObject("Part", partNumber, new String[] {fileName[0]} );
            dataPrepare.addAttachmentToObject("Part", partNumber, new String[] {fileName[1]} );
            
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
            
            // Create the request object GetFileAttachmentRequestType for the getFileAttachment operation
            // Create an array of requests of type AgileGetFileAttachmentRequestType. Batch operations may be
            // performed by populating as many request objects as required to retrieve several files
            // with one single operation.            
            GetFileAttachmentRequestType GetFileAttachmentRequestType = new GetFileAttachmentRequestType();                         
            AgileGetFileAttachmentRequest agileGetFileAttachmentRequest = new AgileGetFileAttachmentRequest();                         
            
            // For each batched request, specify the unique object from whose attachment tab
            // files shall be retrieved. Supply class identifier and object number information
            // for the same.
            agileGetFileAttachmentRequest.setClassIdentifier("Part");
            agileGetFileAttachmentRequest.setObjectNumber(partNumber);
            agileGetFileAttachmentRequest.setAllFiles(true);                        
            System.out.println("Retrieving all the files from the attachment tab of the part '" + partNumber + "'...\n");
            
            // The exact specification of the attachment to be downloaded is defined as an
            // object of type AgileFileAttachmentRequestType. This object includes information
            // about rowId, a boolean to indicate whether all the files of the object are to
            // be downloaded and finally provision for fileIds to be used in special cases.            
            AgileFileAttachmentRequestType attachments = new AgileFileAttachmentRequestType();            
            
            // In this sample the boolean element 'allFiles' is set to true. By using this 
            // option, the necessity to derive rowIds or fileIds is negated. The response
            // object will consist of all the files present in the attachment tab of the 
            // Agile object specified in the request.            

            agileGetFileAttachmentRequest.getAttachments().add(attachments);             

            // The request objects are set and the agile Stub is used to make the getFileAttachment
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the getFileAttachment operation. 
            GetFileAttachmentRequestType.getRequests().add(agileGetFileAttachmentRequest);             
            GetFileAttachmentResponseType getFileAttachmentResponseType = agileStub.getFileAttachment(GetFileAttachmentRequestType);
            System.out.println("STATUS CODE: " +  getFileAttachmentResponseType.getStatusCode() + " for 'getFileAttachment'." );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !getFileAttachmentResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = getFileAttachmentResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println("Exception Id:" + exceptions[j].getExceptionId() + "\nMessage: " + exceptions[j].getMessage() );
                }
                
                AgileWarningListType agileWarningListType[] = getFileAttachmentResponseType.getWarnings().toArray(new AgileWarningListType[0]);
                if(agileWarningListType!=null)
                for(int i=0; i<agileWarningListType.length; i++){
                    AgileWarningType warnings[] = agileWarningListType[i].getWarning().toArray(new AgileWarningType[0]);
                    for(int j=0; j<warnings.length; j++)
                        System.out.println("Warning Id: " + warnings[j].getWarningId() + "\nMessage: " + warnings[j].getMessage() );
                    
                }                    
                
            }
                else{
                // If the webservice call was successful, then display information about the file(s) retrieved.
                    
                    AgileGetFileAttachmentResponse responses[] = getFileAttachmentResponseType.getResponses().toArray(new AgileGetFileAttachmentResponse[0]);                    
                
                    if(responses!=null)
                    for(int i=0; i<responses.length; i++){
                        
                        System.out.println("\nThe following files were retrieved from " + responses[i].getObjectNumber() + ": ");
                        
                        AgileFileAttachmentResponseType attachmentsResp[] = responses[i].getAttachment().toArray(new AgileFileAttachmentResponseType[0]);
                        if(attachmentsResp!=null)
                            for(int j=0; j<attachmentsResp.length; j++){
                                System.out.println("---------------------------------------------");
                                System.out.println("File name: " + attachmentsResp[j].getName() );
                                System.out.println("File type: " + attachmentsResp[j].getFileType() );
                                System.out.println("File size: " + attachmentsResp[j].getFileSize() );
                                if(attachmentsResp[j].getContent()!=null)
                                    System.out.println("Stream data: " + attachmentsResp[j].getContent() + ", File Content received successfully.");
                            }
                        System.out.println("---------------------------------------------");
                    
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
