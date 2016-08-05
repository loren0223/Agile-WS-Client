package attachment.setincorporateaf;

import java.net.URL;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import run.DataPrepare;
import run.RunAllSamples;

import com.agile.ws.schema.attachment.v1.jaxws.AgileCheckInFFRequest;
import com.agile.ws.schema.attachment.v1.jaxws.AgileSetIncorporateAFRequest;
import com.agile.ws.schema.attachment.v1.jaxws.CheckInFFRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.CheckInFFResponseType;
import com.agile.ws.schema.attachment.v1.jaxws.SetIncorporateAFRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.SetIncorporateAFResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningListType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.service.attachment.v1.jaxws.AttachmentPortType;
import com.agile.ws.service.attachment.v1.jaxws.AttachmentService;

/**
 * Sample : SetIncorporateAF Type : Attachment webservice
 * 
 * Description : This sample demonstrates the usage of 'SetIncorporateAF'
 * webservice. This sample 'Set Incorporate status' an Agile Affected file
 * folder.
 * 
 * Note:- testing user must be allowed to do SetIncorporate operation for AF.
 */
public class SetIncorporateAF {
	private static final String COMMAND_NAME = "SetIncorporateAF";
	
	public static String clsName;
	public static String serviceName = "Attachment"; // Table Attachment
	public static AttachmentPortType agileStub;

	public static String afNumber;
	public static String changeNumber;

	public static String folderNumber;

	public SetIncorporateAF() {
		clsName = this.getClass().getName();

	}

	/**
	 * The method setupServerLogin is used to login to the server by providing
	 * the server url details and authentication credentials.
	 * @param args 
	 */

	public static void setupServerLogin(String[] args) throws Exception {

		// The AttachmentService is used to obtain a AdminMetadataPortType
		URL url = new URL(args[0] + "/" + serviceName + "?WSDL");
		AttachmentService locator = new AttachmentService(url);

		// Eventually, the CheckOutFF webservice will be invoked by using this
		// agileStub.
		agileStub = locator.getAttachment();

		// Username and password details are specified for the agileStub
		Map<String, Object> reqContext = ((BindingProvider) agileStub).getRequestContext();
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

	public static void prepareData(String[] args) {

		try {
			System.out.println("<<< Preparing Data... >>>");
			DataPrepare dataPrepare = new DataPrepare(args[0], args[1], args[2]);
			dataPrepare.setupAdminMetadata();
			dataPrepare.setupBusiness();
			dataPrepare.setupAttachment();

			folderNumber = dataPrepare.createNewObject("FileFolder");
			dataPrepare.checkOutFolder(folderNumber);
			dataPrepare.addAttachmentToFolder(folderNumber, new String[] { "File_" + folderNumber + ".txt" });
			dataPrepare.checkInFolder(folderNumber);
			changeNumber = dataPrepare.getNextAutoNumber("DFCO");

			dataPrepare.createObjectByTags("DFCO", new String[] { "number" }, new String[] { changeNumber });
			dataPrepare.setupTable();
			System.out.println("Add Affected File "+ folderNumber +" to DFCO "+ changeNumber);
			dataPrepare.addRow("DFCO", changeNumber, "AffectedFiles", "folderNumber", folderNumber);
			System.out.println("<<< DataPrepare successful >>>");
		} catch (Exception e) {
			System.out.println(
					"<<< ALERT: DataPrepare failed. Your server settings may be invalid, this sample might not run successfully.");
			System.out.println(
					"To run the sample without the assitance of prepared data, you may edit the static variables after");
			System.out.println(
					"creating the necessary prerequisites through the Agile web client and then execute the sample >>>");
		}

	}

	public static void main(String[] args) {

		// This sample may be configured by either directly modifying the string
		// values of
		// 'SERVER_URL', 'USERNAME' and 'PASSWORD' (defined in this class), or
		// by
		// passing them as program arguments in the same order. In this sample,
		// the
		// method 'checkArguments' checks for these values.
		checkArguments(args);

		// Comment this method out if you intend to use your own data
		// or scenario by editing the static variables at the top of this code
		prepareData(args);

		System.out.println("\n------------------------------------------------------------------------");
		System.out.println("Executing webservice sample: ");

		try {

			// Login to the server by providing the server url and
			// authentication credentials.
			setupServerLogin(args);
			SetIncorporateAFRequestType setIncorporateAFRequestType = new SetIncorporateAFRequestType();
			AgileSetIncorporateAFRequest agileSetIncorporateAFRequest = new AgileSetIncorporateAFRequest();

			agileSetIncorporateAFRequest.setFolderNumber(folderNumber);
			agileSetIncorporateAFRequest.setChangeNumber(changeNumber);
			agileSetIncorporateAFRequest.setIncorporate(true);
			System.out.println(
					"SetIncorporate for '" + folderNumber + "'...\n");

			// The request objects are set and the agile Stub is used to make
			// the CheckInFF
			// webservice call. The status code obtained from the response
			// object is printed to
			// verify the success of the CheckInFF operation.
			setIncorporateAFRequestType.getRequests().add(agileSetIncorporateAFRequest);
			SetIncorporateAFResponseType setIncorporateAFResponseType = agileStub.setIncorporateAF(setIncorporateAFRequestType);
			System.out.println("\nSTATUS CODE: " + setIncorporateAFResponseType.getStatusCode());
			if( !setIncorporateAFResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = setIncorporateAFResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println("Exception Id:" + exceptions[j].getExceptionId() + "\nMessage: " + exceptions[j].getMessage() );
                }
                
                AgileWarningListType agileWarningListType[] = setIncorporateAFResponseType.getWarnings().toArray(new AgileWarningListType[0]);
                if(agileWarningListType!=null)
                for(int i=0; i<agileWarningListType.length; i++){
                    AgileWarningType warnings[] = agileWarningListType[i].getWarning().toArray(new AgileWarningType[0]);
                    for(int j=0; j<warnings.length; j++)
                        System.out.println("Warning Id: " + warnings[j].getWarningId() + "\nMessage: " + warnings[j].getMessage() );
                    
                }  
			}

		} catch (Exception e) {
			RunAllSamples.reportFailure(clsName);
			System.out.println("Exceptions: ");
			e.printStackTrace();
		}
	}

}
