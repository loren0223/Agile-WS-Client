package attachment.getfileff;

import com.agile.api.FileFolderConstants;
import com.agile.ws.schema.attachment.v1.jaxws.AgileFileAttachmentRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.AgileFileAttachmentResponseType;
import com.agile.ws.schema.attachment.v1.jaxws.AgileGetFileFFRequest;
import com.agile.ws.schema.attachment.v1.jaxws.AgileGetFileFFResponse;
import com.agile.ws.schema.attachment.v1.jaxws.GetFileFFRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.GetFileFFResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileListEntryType;
import com.agile.ws.schema.common.v1.jaxws.AgileRowType;
import com.agile.ws.schema.common.v1.jaxws.AgileTableType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningListType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningType;
import com.agile.ws.schema.common.v1.jaxws.SelectionType;
import com.agile.ws.schema.table.v1.jaxws.LoadTableRequestType;
import com.agile.ws.schema.table.v1.jaxws.LoadTableResponseType;
import com.agile.ws.schema.table.v1.jaxws.RequestTableType;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;

import run.DataPrepare;
import run.RunAllSamples;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.service.attachment.v1.jaxws.AttachmentPortType;
import com.agile.ws.service.attachment.v1.jaxws.AttachmentService;
import com.agile.ws.service.table.v1.jaxws.TablePortType;
import com.agile.ws.service.table.v1.jaxws.TableService;
import common.util.WSUtil;


/**
* Sample      : GetFileFF
* Type        : Attachment webservice
*
* Description : This sample demonstrates the usage of 'getFileFF' webservice
* using which a file attachment is retrieved from an Agile File Folder.
* The request object contains the specifications that identify the attachment to be
* downloaded an array of bytes is obtained in the response object.
*/
public class GetFileFF {
    private static final String COMMAND_NAME = "GetFileFF";
    private static String[] cmdArgs = null;
    
	public static String clsName;
    public static String serviceName_2 = "Table";
    public static String serviceName   =  "Attachment";    
    public static AttachmentPortType agileStub;        
    public static TablePortType agileStub_Table;        
    
    public static String folderNumber;
    public static String fileName;
    public static String folderVersion;

    public GetFileFF() {
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

        //Eventually, the GetFileFF webservice will be invoked by using this agileStub.
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

    public static int getRowOrFileId(String filename, String clazz, String objectNumber, String tableId, String methodType ){
        
        try{
        
        // Login to the server by providing the server url and authentication credentials
        // for making the loadTable webservices call.
        setupServerLogin_LoadTable();             
        
        // Create the request object LoadTableRequestType for the loadTable operation.
        LoadTableRequestType loadTableRequestType = new LoadTableRequestType();
                                                                               
        // For each request, specify the table(s) whose content is to be retrieved.
        
        // Tables in Agile webservices are defined as RequestTableType objects. A specifc
        // table may be identified by specifying the class identifier and table identifier
        // attributes as shown:
        RequestTableType table = new RequestTableType();
        table = new RequestTableType();
        table.setClassIdentifier(clazz);
        table.setObjectNumber(objectNumber);
        table.setTableIdentifier( tableId );
        
        // The request objects are set and the agile Stub is used to make the loadTable
        // webservice call. The status code obtained from the response object is printed to
        // verify the success of the loadTable operation. 
        loadTableRequestType.getTableRequest().add(table);
        LoadTableResponseType loadTableResponseType = agileStub_Table.loadTable(loadTableRequestType);
        System.out.println("Obtaining row Id / fileId information.....");
        System.out.println("STATUS CODE: " +  loadTableResponseType.getStatusCode() );        
        
        // If the status code is 'SUCCESS', then populate the information retrieved
        // by the webservice call.  
            if( loadTableResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){


                AgileTableType[] tables = loadTableResponseType.getTableContents().toArray(new AgileTableType[0]);                    
                
                // Search for the necessary rowId or fileId by using the filename
                // to look for a match and return either of the two, as per the 
                // requirement specified in the input parameter 'methodType'
                return DataPrepare.getRowOrFileId(tables, "filename", filename, methodType);
            }
            
            // If the status code indicates that the webservice call was not successful,
            // then populate a list of exceptions.
            else{
                
                System.out.println("<Failed to load table information>");
                AgileExceptionListType[] agileExceptionListType = loadTableResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                    
                AgileWarningListType agileWarningListType[] = loadTableResponseType.getWarnings().toArray(new AgileWarningListType[0]);
                if(agileWarningListType!=null)
                for( int i=0; i<agileWarningListType.length; i++){
                    AgileWarningType warnings[] = agileWarningListType[i].getWarning().toArray(new AgileWarningType[0]);
                    for(int j=0; j<warnings.length; j++)
                        System.out.println("Warning Id: " + warnings[j].getWarningId() + "\nMessage: " + warnings[j].getMessage() );
                        
                }                             
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }        
        return -1;        
    }
    
    public static void setupServerLogin_LoadTable() throws Exception {       

         // The table webservices can be invoked only on its corresponding
         // agileStub. Hence we create and authenticate an agile Table_BindingStub
         // to facilitate the loadTable webservice call.
         
    	//The TableService is used to obtain a TablePortType
   	    URL url = new URL(cmdArgs[0] + "/" + serviceName_2 + "?WSDL");
        TableService locator = new TableService(url);

        //Eventually, the loadTable webservice will be invoked by using this agileStub.
        agileStub_Table = locator.getTable();


        // Username and password details are specified for the agileStub
        Map<String, Object> reqContext = ((BindingProvider)agileStub_Table).getRequestContext();
        reqContext.put(BindingProvider.USERNAME_PROPERTY, cmdArgs[1]);
        reqContext.put(BindingProvider.PASSWORD_PROPERTY, cmdArgs[2]);
    }

    public static void prepareData(){
        
        try{
            System.out.println("<<< Preparing Data... >>>");
            DataPrepare dataPrepare = new DataPrepare(cmdArgs[0], cmdArgs[1], cmdArgs[2]);
            dataPrepare.setupAdminMetadata(); 
            dataPrepare.setupBusiness();
            dataPrepare.setupAttachment();
            
            folderNumber = dataPrepare.createNewObject("FileFolder");
            fileName = folderNumber + "_File123.txt";
            dataPrepare.checkOutFolder(folderNumber);
            dataPrepare.addAttachmentToFolder(folderNumber, new String[] {fileName} );
            dataPrepare.checkInFolder(folderNumber);
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
        prepareData();

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");
        
        try{
            
            // Login to the server by providing the server url and authentication credentials.
            setupServerLogin();             
            
            // Create the request object GetFileFFRequestType for the GetFileFF operation
            // Create an array of requests of type AgileGetFileFFRequest. Batch operations may be
            // performed by populating as many request objects as required to obtain several files 
            // from different folders with one single operation.            
            GetFileFFRequestType getFileFFRequestType = new GetFileFFRequestType();
            AgileGetFileFFRequest agileGetFileFFRequest = new AgileGetFileFFRequest();
            
            // For each batched request, specify the unique folder name and version
            // from which files will be retrieved. Use the element 'files', an object
            // of type AgileFileAttachmentRequestType to define the attachment to be downloaded.

            agileGetFileFFRequest.setFolderNumber(folderNumber);

            System.out.println("Retrieving a file from the file folder '" + folderNumber + "'...\n");            
            
            // The attachment to be downloaded is defined as an element of type
            // AgileFileAttachmentRequestType. The rowId field of this object is 
            // set to specify the file that has been downloaded.
            AgileFileAttachmentRequestType files = new AgileFileAttachmentRequestType();
            
            // The method getRowOrFileId is written in this sample program to obtain the rowId 
            // or fileId of a particular row when given its filename. This method utilizes the
            // table webservices to issue a 'loadTable' webservice call after which the message
            // elements of all rows are searched to find a match. A string input with values of
            // either "getRowId" or "getFileId" is also passed a paramter input. Depending on
            // the value of this string, either the rowId or fileId is returned by the method
            files.setRowId( getRowOrFileId(fileName, 
                                              FileFolderConstants.CLASS_FILE_FOLDER.toString(),
                                              agileGetFileFFRequest.getFolderNumber(),                                              
                                              FileFolderConstants.TABLE_FILES.toString(), "getRowId" ) );
            agileGetFileFFRequest.getFiles().add(files);

            // The request objects are set and the agile Stub is used to make the GetFileFF
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the GetFileFF operation. 
            getFileFFRequestType.getRequests().add(agileGetFileFFRequest);             
            GetFileFFResponseType getFileFFResponseType = agileStub.getFileFF(getFileFFRequestType);
            System.out.println("\nSTATUS CODE: " +  getFileFFResponseType.getStatusCode() );
            
            
                // If the status code is not 'SUCCESS', then populate the list of exceptions
                // returned by the webservice. 
                if( !getFileFFResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                    
                    AgileExceptionListType[] agileExceptionListType = getFileFFResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                    if(agileExceptionListType!=null)
                    for(int i=0; i<agileExceptionListType.length; i++){
                        AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                        for(int j=0; j<exceptions.length; j++)
                            System.out.println("Exception Id:" + exceptions[j].getExceptionId() + "\nMessage: " + exceptions[j].getMessage() );
                    }
                    
                    AgileWarningListType agileWarningListType[] = getFileFFResponseType.getWarnings().toArray(new AgileWarningListType[0]);
                    if(agileWarningListType!=null)
                    for(int i=0; i<agileWarningListType.length; i++){
                        AgileWarningType warnings[] = agileWarningListType[i].getWarning().toArray(new AgileWarningType[0]);
                        for(int j=0; j<warnings.length; j++)
                            System.out.println("Warning Id: " + warnings[j].getWarningId() + "\nMessage: " + warnings[j].getMessage() );
                        
                    }                    
                    
                }
                    else{
                    // If the webservice call was successful, then display information about the file(s) retrieved.
                        
                        AgileGetFileFFResponse responses[] = getFileFFResponseType.getResponses().toArray(new AgileGetFileFFResponse[0]);                    
                    
                        if(responses!=null)
                        for(int i=0; i<responses.length; i++){
                            
                            System.out.println("The following files were retrieved from " + responses[i].getFolderNumber() + ": ");
                            
                            AgileFileAttachmentResponseType attachmentsResp[] = responses[i].getFiles().toArray(new AgileFileAttachmentResponseType[0]);
                            if(attachmentsResp!=null)
                                for(int j=0; j<attachmentsResp.length; j++){
                                    
                                    System.out.println("File name: " + attachmentsResp[j].getName() );
                                    System.out.println("File type: " + attachmentsResp[j].getFileType() );
                                    System.out.println("File size: " + attachmentsResp[j].getFileSize() );
                                    if(attachmentsResp[j].getContent()!=null)
                                        System.out.println("Stream data: " + attachmentsResp[j].getContent() + ", File Content received successfully.");
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
