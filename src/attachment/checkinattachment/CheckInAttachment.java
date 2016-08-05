package attachment.checkinattachment;

import com.agile.api.ItemConstants;
import com.agile.ws.schema.attachment.v1.jaxws.AgileCheckInAttachmentRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.CheckInAttachmentRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.CheckInAttachmentResponseType;
import com.agile.ws.schema.attachment.v1.jaxws.CheckInAttachmentType;
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

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.bind.JAXBElement;
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
* Sample      : CheckInAttachment  
* Type        : Attachment webservice 
* 
* Description : This sample demonstrates the usage of 'checkInAttachment' webservice.
* The sample 'checks in' a file attachment of an Agile object after modifications (if any)
* have been performed on the same. The attachment must be checked out prior
* to the 'check in' operation.
*/
public class CheckInAttachment {
    private static final String COMMAND_NAME = "CheckInAttachment";
    private static String[] cmdArgs = null;
    
	public static String clsName;
    public static String serviceName_2 = "Table";
    public static String serviceName   =  "Attachment";    
    public static AttachmentPortType agileStub;        
    public static TablePortType agileStub_Table;        
    
    public static String partNumber; 
    public static String fileName;

    public CheckInAttachment() {
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

       //Eventually, the checkInAttachment webservice will be invoked by using this agileStub.
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
            dataPrepare.setupTable();
            
            partNumber = dataPrepare.createNewObject("Part");
            fileName = partNumber + "_File123.txt";
            dataPrepare.addAttachmentToObject("Part", partNumber, new String[] {fileName} );
            String folderName = dataPrepare.getFolderNameFromAttachment("Part", partNumber, ItemConstants.TABLE_ATTACHMENTS.toString(), fileName);
            dataPrepare.checkOutFolder(folderName);
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
        prepareData(args);

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");
        
        try{
            
            // Login to the server by providing the server url and authentication credentials.
            setupServerLogin(args);                  
            
            // Create the request object CheckInAttachmentRequestType for the checkInAttachment operation
            // Create an array of requests of type AgileCheckInAttachmentRequestType. Batch operations may be
            // performed by populating as many request objects as required to checkIn several files to different
            // objects with one single operation.            
            CheckInAttachmentRequestType checkInAttachmentRequestType = new CheckInAttachmentRequestType();
            AgileCheckInAttachmentRequestType agileCheckInAttachmentRequestType = new AgileCheckInAttachmentRequestType();
            
            // For each batched request, specify the unique object from whose attachment tab
            // files will be checked out. Supply class identifier and object number information
            // for the same.
            agileCheckInAttachmentRequestType.setClassIdentifier("Part");
            agileCheckInAttachmentRequestType.setObjectNumber(partNumber);
            System.out.println("Checking in a file in the attachment tab of the part '" + partNumber + "'...\n");
            
            // The exact specification of the file that has to be checked in by the webservice
            // is achieved by using an object of type CheckInAttachmentType is used to achieve this.
            // This object defines the file to be checked in using elements that refer to the new
            // file name and also the modified / new file content. It is to be noted that the
            // file name of the file being checked in must have the same extension as that of 
            // the file that was checked out.
            
            CheckInAttachmentType attachments = new CheckInAttachmentType();                                        
            attachments = new CheckInAttachmentType();
            
            File file = File.createTempFile("Filename", "txt");
            FileWriter writer  = new FileWriter(file);
            writer.write("Modified file information added after the checkin");
            writer.close();
            
            FileDataSource datasource = new FileDataSource(file);
            attachments.setFileContent(new DataHandler(datasource));
            
            attachments.setFileName("Modified_" + fileName);
            
            agileCheckInAttachmentRequestType.getAttachments().add(attachments);                         
            
            // The method getRowOrFileId is written in this sample program to obtain the rowId 
            // or fileId of a particular row when given its filename. This method utilizes the
            // table webservices to issue a 'loadTable' webservice call after which the message
            // elements of all rows are searched to find a match. A string input with values of
            // either "getRowId" or "getFileId" is also passed a paramter input. Depending on
            // the value of this string, either the rowId or fileId is returned by the method            
            
            // Set the rowId into which the file shall be checkedIn
            agileCheckInAttachmentRequestType.setRowId( getRowOrFileId(fileName, 
                                              agileCheckInAttachmentRequestType.getClassIdentifier(),
                                              agileCheckInAttachmentRequestType.getObjectNumber(),                                              
                                              ItemConstants.TABLE_ATTACHMENTS.toString(), "getRowId") );

            // The request objects are set and the agile Stub is used to make the checkInAttachment
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the checkInAttachment operation. 
            checkInAttachmentRequestType.getRequest().add(agileCheckInAttachmentRequestType);             
            CheckInAttachmentResponseType checkInAttachmentResponseType = agileStub.checkInAttachment(checkInAttachmentRequestType);
            System.out.println("\nSTATUS CODE: " +  checkInAttachmentResponseType.getStatusCode() );
            
            
                // If the status code is not 'SUCCESS', then populate the list of exceptions
                // returned by the webservice. 
                if( !checkInAttachmentResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                    
                    AgileExceptionListType[] agileExceptionListType = checkInAttachmentResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                    if(agileExceptionListType!=null)
                    for(int i=0; i<agileExceptionListType.length; i++){
                        AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                        for(int j=0; j<exceptions.length; j++)
                            System.out.println("Exception Id:" + exceptions[j].getExceptionId() + "\nMessage: " + exceptions[j].getMessage() );
                    }
                    
                    AgileWarningListType agileWarningListType[] = checkInAttachmentResponseType.getWarnings().toArray(new AgileWarningListType[0]);
                    if(agileWarningListType!=null)
                    for(int i=0; i<agileWarningListType.length; i++){
                        AgileWarningType warnings[] = agileWarningListType[i].getWarning().toArray(new AgileWarningType[0]);
                        for(int j=0; j<warnings.length; j++)
                            System.out.println("Warning Id: " + warnings[j].getWarningId() + "\nMessage: " + warnings[j].getMessage() );
                        
                    }                    
                    
                }
                else{
                    // If the webservice call was successful, then state the same.
                    System.out.print("The modified file was sucessfully checked into the attachment tab of the object ");
                    System.out.println( agileCheckInAttachmentRequestType.getObjectNumber() + "." );
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
         // agileStub. Hence we create and authenticate an agile TablePortType
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
