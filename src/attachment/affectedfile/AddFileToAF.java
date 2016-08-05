package attachment.affectedfile;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.attachment.v1.jaxws.AddFileFFRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.AddFileFFResponseType;
import com.agile.ws.schema.attachment.v1.jaxws.AddFileFFType;
import com.agile.ws.schema.attachment.v1.jaxws.AgileAddFileFFRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.AgileCheckInFFRequest;
import com.agile.ws.schema.attachment.v1.jaxws.AgileCheckOutAFRequest;
import com.agile.ws.schema.attachment.v1.jaxws.AgileCheckOutFFRequest;
import com.agile.ws.schema.attachment.v1.jaxws.AgileCheckOutFFResponse;
import com.agile.ws.schema.attachment.v1.jaxws.CheckInFFRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.CheckInFFResponseType;
import com.agile.ws.schema.attachment.v1.jaxws.CheckOutAFRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.CheckOutAFResponseType;
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
 * Sample : AddFileToAF Type : Attachment webservice
 * 
 * Description : This sample demonstrates addition of a file to an Agile File
 * Folder. The file will be added to the 'Files' tab of the file folder.
 * 
 * Note:- testing user must be allowed to do AddFile operation for AF.
 */
public class AddFileToAF {

	private static final String COMMAND_NAME = "AddFileToAF";
	public static String clsName;
	public static String serviceName = "Attachment";
	public static AttachmentPortType agileStub;

	public static String folderNumber;
	public static String changeNumber;

	public AddFileToAF() {
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

		// Eventually, the AddFileFF webservice will be invoked by using this
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
			dataPrepare.addRow("DFCO", changeNumber, "AffectedFiles", "folderNumber", folderNumber);
			System.out.println("<<< DataPrepare successful >>>");
		} catch (Exception e) {
			System.out.println(
					"<<< ALERT: DataPrepare failed. Your server settings may be invalid, this sample might not run successfully.");
			System.out.println(
					"To run the sample without the assitance of prepared data, you may edit the static variables after");
			System.out.println(
					"creating the necessary prerequisites through the Agile web client and then execute the sample >>>");
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		checkArguments(args);
		try {
			// Login to the server by providing the server url and
			// authentication credentials.
			setupServerLogin(args);

			// Comment this method out if you intend to use your own data
			// or scenario by editing the static variables at the top of this
			// code
			prepareData(args);

			System.out.println("\n------------------------------------------------------------------------");
			System.out.println("Executing webservice sample: ");

			// Before adding a new file to a folder, the folder object must be
			// checked out
			// prior to any file operation. In this sample, the method
			// 'checkOutFolder' is
			// written using the checkOutFF webservice to facilitate the
			// checkOut operation
			// on the folder before files are actually added to it. For a more
			// detailed
			// explanation of checking out folders using webservices, refer to
			// the sample
			// provided on that particular topic.
			// System.out.println("\nChecking out folder before adding a file...
			// " );
			 checkOutAFFolder(folderNumber);

			// Create the request object AddFileFFRequestType for the AddFileFF
			// operation
			// Create an array of requests of type
			// AgileAddFileFFRequestTypeType. Batch operations may be
			// performed by populating as many request objects as required to
			// add several files to different
			// folders with one single operation.
			AddFileFFRequestType addFileFFRequestType = new AddFileFFRequestType();
			AgileAddFileFFRequestType agileAddFileFFRequestType = new AgileAddFileFFRequestType();

			// For each batched request, specify the unique folder to whose
			// files tab
			// the files shall be added. Supply folder number details for the
			// same.
			agileAddFileFFRequestType.setFolderNumber(folderNumber);
			System.out.println("\nAdding a file to the folder '" + folderNumber + "'... ");

			// The exact specification of the attachment to be added is defined
			// as an
			// object of type AddFileFFType. This object includes information
			// about the
			// name of the file and its description and content.
			AddFileFFType file = new AddFileFFType();
			file = new AddFileFFType();
			file.setFileName("Filename.txt");
			file.setDescription("Description for file");

			File filedata = File.createTempFile("Filename", "txt");
			FileWriter writer = new FileWriter(filedata);
			writer.write("File Content...file");
			writer.close();

			FileDataSource datasource = new FileDataSource(filedata);
			file.setFileContent(new DataHandler(datasource));

			agileAddFileFFRequestType.getFiles().add(file);

			// The request objects are set and the agile Stub is used to make
			// the AddFileFF
			// webservice call. The status code obtained from the response
			// object is printed to
			// verify the success of the AddFileFF operation.
			addFileFFRequestType.getRequest().add(agileAddFileFFRequestType);
			AddFileFFResponseType AddFileFFResponseType = agileStub.addFileFF(addFileFFRequestType);
			System.out.println("\nSTATUS CODE: " + AddFileFFResponseType.getStatusCode());

			// If the status code is not 'SUCCESS', then populate the list of
			// exceptions
			// returned by the webservice.
			if (!AddFileFFResponseType.getStatusCode().toString().equals(ResponseStatusCode.SUCCESS.value())) {

				RunAllSamples.reportFailure(clsName);

				List<AgileExceptionListType> lstAgileExceptionListType = AddFileFFResponseType.getExceptions();
				if (lstAgileExceptionListType != null)
					for (AgileExceptionListType agileExceptionListType : lstAgileExceptionListType) {
						List<AgileExceptionType> exceptions = agileExceptionListType.getException();
						for (AgileExceptionType exception : exceptions)
							System.out.println("Exception Id:" + exception.getExceptionId() + "\nMessage: "
									+ exception.getMessage());
					}

				List<AgileWarningListType> lstAgileWarningListType = AddFileFFResponseType.getWarnings();
				if (lstAgileWarningListType != null)
					for (AgileWarningListType agileWarningListType : lstAgileWarningListType) {
						List<AgileWarningType> warnings = agileWarningListType.getWarning();
						for (AgileWarningType warning : warnings)
							System.out.println(
									"Warning Id: " + warning.getWarningId() + "\nMessage: " + warning.getMessage());

					}

			} else {
				// If the webservice call was successful, then state the same
				System.out.print("The specified file(s) were successfully added to the folder ");
				System.out.println(agileAddFileFFRequestType.getFolderNumber() + ".");
			}

			
			
		} catch (Exception e) {
			RunAllSamples.reportFailure(clsName);
			System.out.println("Exceptions: ");
			e.printStackTrace();
		}
	}

	public static void checkOutAFFolder(String folderNumber) {

		try {

			CheckOutAFRequestType checkoutAFRequestType = new CheckOutAFRequestType();
			AgileCheckOutAFRequest agileCheckAFRequestType = new AgileCheckOutAFRequest();

			// For each batched request, specify the unique object from whose
			// attachment tab
			// files will be checked out. Supply class identifier and object
			// number information
			// for the same.
			agileCheckAFRequestType.setChangeNumber(changeNumber);
			agileCheckAFRequestType.setFolderNumber(folderNumber);
			System.out.println("Checking out a file folder " + folderNumber
					+ " in the Affected Files tab of the Change '" + changeNumber + "'...\n");

			checkoutAFRequestType.getRequests().add(agileCheckAFRequestType);
			CheckOutAFResponseType checkOutAFResponseType = agileStub.checkOutAF(checkoutAFRequestType);
			System.out.println("\nSTATUS CODE: " + checkOutAFResponseType.getStatusCode());

			// If the status code is not 'SUCCESS', then populate the list of
			// exceptions
			// returned by the webservice.
			if (!checkOutAFResponseType.getStatusCode().toString().equals(ResponseStatusCode.SUCCESS.value())) {

				RunAllSamples.reportFailure(clsName);

				AgileExceptionListType[] agileExceptionListType = checkOutAFResponseType.getExceptions()
						.toArray(new AgileExceptionListType[0]);
				if (agileExceptionListType != null)
					for (int i = 0; i < agileExceptionListType.length; i++) {
						AgileExceptionType exceptions[] = agileExceptionListType[i].getException()
								.toArray(new AgileExceptionType[0]);
						for (int j = 0; j < exceptions.length; j++)
							System.out.println("Exception Id:" + exceptions[j].getExceptionId() + "\nMessage: "
									+ exceptions[j].getMessage());
					}

				AgileWarningListType agileWarningListType[] = checkOutAFResponseType.getWarnings()
						.toArray(new AgileWarningListType[0]);
				if (agileWarningListType != null)
					for (int i = 0; i < agileWarningListType.length; i++) {
						AgileWarningType warnings[] = agileWarningListType[i].getWarning()
								.toArray(new AgileWarningType[0]);
						for (int j = 0; j < warnings.length; j++)
							System.out.println("Warning Id: " + warnings[j].getWarningId() + "\nMessage: "
									+ warnings[j].getMessage());
					}
			} else {
				// If the webservice call was successful, then state the same.
				System.out.print("The check operation is done sucessfully ");
				System.out.println(agileCheckAFRequestType.getChangeNumber() + ".");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
