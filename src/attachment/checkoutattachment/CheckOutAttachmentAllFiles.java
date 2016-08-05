package attachment.checkoutattachment;

import com.agile.api.ItemConstants;
import com.agile.ws.schema.attachment.v1.jaxws.AgileCheckOutAttachmentRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.AgileCheckOutAttachmentResponse;
import com.agile.ws.schema.attachment.v1.jaxws.AgileCheckOutAttachmentResponseType;
import com.agile.ws.schema.attachment.v1.jaxws.CheckOutAttachmentRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.CheckOutAttachmentResponseType;
import com.agile.ws.schema.attachment.v1.jaxws.CheckOutAttachmentType;
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
* Sample      : CheckOutAttachmentAllFiles  
* Type        : Attachment webservice 
* 
* Description : This sample demonstrates the usage of 'checkOutAttachment' webservice
* for a specific case where all the attachments of a particular Agile object will 
* be checked out by the webservice operation.
*/
public class CheckOutAttachmentAllFiles {
    private static final String COMMAND_NAME = "CheckOutAttachmentAllFiles";
    private static String[] cmdArgs = null;
    
	public static String clsName;
    public static String serviceName_2 = "Table";
    public static String serviceName   =  "Attachment";    
    public static AttachmentPortType agileStub;        
    public static TablePortType agileStub_Table;        
    
    public static String partNumber;
    public static String fileName[];

    public CheckOutAttachmentAllFiles() {
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

        //Eventually, the checkOutAttachment webservice will be invoked by using this agileStub.
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
            
            partNumber = dataPrepare.createNewObject("Part");
            fileName = new String[2];
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
        cmdArgs = args;

        // Comment this method out if you intend to use your own data
        // or scenario by editing the static variables at the top of this code
        prepareData();

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");
        
        try{
            
            // Login to the server by providing the server url and authentication credentials.
            setupServerLogin();             
            
            // Create the request object CheckOutAttachmentRequestType for the checkOutAttachment operation
            // Create an array of requests of type AgileCheckOutAttachmentRequestType. Batch operations may be
            // performed by populating as many request objects as required to add several files to different
            // objects with one single operation.            
            CheckOutAttachmentRequestType checkOutAttachmentRequestType = new CheckOutAttachmentRequestType();
            AgileCheckOutAttachmentRequestType agileCheckOutAttachmentRequestType = new AgileCheckOutAttachmentRequestType();
            agileCheckOutAttachmentRequestType = new AgileCheckOutAttachmentRequestType();
            
            // For each batched request, specify the unique object from whose attachment tab
            // files will be checked out. Supply class identifier and object number information
            // for the same.
            agileCheckOutAttachmentRequestType.setClassIdentifier("Part");
            agileCheckOutAttachmentRequestType.setObjectNumber( partNumber );
            agileCheckOutAttachmentRequestType.setAllFiles(true);
            System.out.println("Checking out all the files on the attachment tab of the part '" + partNumber + "'...\n");
            
            // In this sample,a ll the files in the attachment tab of a particular object 
            // will be checked out To achieve this, the boolean element 'allFiles' is set
            // to 'true', thereby negating the need to explicitly pass rowIds or fileIds 
            // in the request object. The webservice will checkout all the files and 
            // the response object will consist of information relating to these files.
            
            CheckOutAttachmentType attachments = new CheckOutAttachmentType();                                        
            attachments = new CheckOutAttachmentType();                
            agileCheckOutAttachmentRequestType.getAttachments().add(attachments);

            // The request objects are set and the agile Stub is used to make the checkOutAttachment
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the checkOutAttachment operation. 
            checkOutAttachmentRequestType.getRequests().add(agileCheckOutAttachmentRequestType);             
            CheckOutAttachmentResponseType checkOutAttachmentResponseType = agileStub.checkOutAttachment(checkOutAttachmentRequestType);
            System.out.println("\nSTATUS CODE: " +  checkOutAttachmentResponseType.getStatusCode() );
            
            
                // If the status code is not 'SUCCESS', then populate the list of exceptions
                // returned by the webservice. 
                if( !checkOutAttachmentResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                	RunAllSamples.reportFailure( clsName );
                    
	                AgileExceptionListType[] agileExceptionListType = checkOutAttachmentResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
	                if(agileExceptionListType!=null)
	                for(int i=0; i<agileExceptionListType.length; i++){
	                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
	                    for(int j=0; j<exceptions.length; j++)
	                        System.out.println("Exception Id:" + exceptions[j].getExceptionId() + "\nMessage: " + exceptions[j].getMessage() );
	                }
	                
	                AgileWarningListType agileWarningListType[] = checkOutAttachmentResponseType.getWarnings().toArray(new AgileWarningListType[0]);
	                if(agileWarningListType!=null)
	                for(int i=0; i<agileWarningListType.length; i++){
	                    AgileWarningType warnings[] = agileWarningListType[i].getWarning().toArray(new AgileWarningType[0]);
	                    for(int j=0; j<warnings.length; j++)
	                        System.out.println("Warning Id: " + warnings[j].getWarningId() + "\nMessage: " + warnings[j].getMessage() );
	                    
	                }                   
                    
                }
                String statusCode = checkOutAttachmentResponseType.getStatusCode().toString();
                if(statusCode.equals("SUCCESS") || (statusCode.equals("SUCCESS_WITH_WARNINGS")) ){
                    
                // If the webservice call was successful, then display information about the file(s) checked out.                        
                    
                    AgileCheckOutAttachmentResponseType responses[] = checkOutAttachmentResponseType.getResponses().toArray(new AgileCheckOutAttachmentResponseType[0]);                    
                    
                    if(responses!=null)
                        for(int i=0; i<responses.length; i++){
                            
                            System.out.println("\nThe following files were checked out from " + responses[i].getObjectNumber() + ": ");
                            
                            AgileCheckOutAttachmentResponse attachmentsResp[] = responses[i].getFiles().toArray(new AgileCheckOutAttachmentResponse[0]);
                            if(attachmentsResp!=null)
                                for(int j=0; j<attachmentsResp.length; j++){
                                    
                                    System.out.println("-----------------------------------------");
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
}
