package attachment.affectedfile;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.ws.BindingProvider;

import run.DataPrepare;
import run.RunAllSamples;

import com.agile.ws.schema.attachment.v1.jaxws.AgileCheckInAFRequest;
import com.agile.ws.schema.attachment.v1.jaxws.AgileCheckOutAFRequest;
import com.agile.ws.schema.attachment.v1.jaxws.CheckInAFFileType;
import com.agile.ws.schema.attachment.v1.jaxws.CheckInAFRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.CheckInAFResponseType;
import com.agile.ws.schema.attachment.v1.jaxws.CheckOutAFRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.CheckOutAFResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningListType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.service.attachment.v1.jaxws.AttachmentPortType;
import com.agile.ws.service.attachment.v1.jaxws.AttachmentService;

/**
* Sample      : CheckInAF  
* Type        : Attachment webservice 
* 
* Description : This sample demonstrates the webservice 'checkInAF'
* The sample 'checks in' an attachment in an affected file folder after modifications (if any).
* The attachment must be checked out prior to the 'check in' operation.
* 
* Note:- testing user must be allowed to do CheckIn operation for AF.
*/
public class CheckInAF {
	private static final String COMMAND_NAME = "CheckInAF";
	public static String clsName;
	public static String serviceName = "Attachment";
	public static AttachmentPortType agileStub;
	public static String fileName;
	public static String folderNumber;
	public static String changeNumber;

	public CheckInAF() {
		clsName = this.getClass().getName();
	}

	public static void setupServerLogin(String[] args) throws Exception {
		// The AttachmentService is used to obtain a AdminMetadataPortType
		URL url = new URL(args[0] + "/" + serviceName + "?WSDL");
		AttachmentService locator = new AttachmentService(url);

		// Eventually, the CheckInAF web service will be invoked by using this
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
			dataPrepare.setupTable();

			folderNumber = dataPrepare.createNewObject("FileFolder");
			dataPrepare.checkOutFolder(folderNumber);
			fileName = "File_" + folderNumber + ".txt";
			dataPrepare.addAttachmentToFolder(folderNumber, new String[] { fileName });
			dataPrepare.checkInFolder(folderNumber);
			changeNumber = dataPrepare.getNextAutoNumber("DFCO");

			dataPrepare.createObjectByTags("DFCO", new String[] { "number" }, new String[] { changeNumber });
			System.out.println("Add Affected File " + folderNumber + " to DFCO " + changeNumber);
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

			CheckInAFRequestType checkInAFRequestType = new CheckInAFRequestType();
			AgileCheckInAFRequest agileCheckAFRequestType = new AgileCheckInAFRequest();

			// For each batched request, specify the unique object from whose
			// attachment tab
			// files will be checked out. Supply class identifier and object
			// number information
			// for the same.
			agileCheckAFRequestType.setChangeNumber(changeNumber);
			agileCheckAFRequestType.setFolderNumber(folderNumber);
			agileCheckAFRequestType.setRowId(6101401);
			System.out.println("Checking in a file folder " + folderNumber
					+ " in the Affected Files tab of the Change '" + changeNumber + "'...\n");

			CheckInAFFileType attachment = new CheckInAFFileType();

			File file = File.createTempFile("Filename", "txt");
			FileWriter writer = new FileWriter(file);
			writer.write("Modified file information added after the checkin");
			writer.close();

			FileDataSource datasource = new FileDataSource(file);
			attachment.setFileContent(new DataHandler(datasource));

			attachment.setFileName("Modified_" + fileName);

			agileCheckAFRequestType.setReplaceFile(attachment);

			checkInAFRequestType.getRequests().add(agileCheckAFRequestType);
			CheckInAFResponseType checkInAFResponseType = agileStub.checkInAF(checkInAFRequestType);
			System.out.println("\nSTATUS CODE: " + checkInAFResponseType.getStatusCode());

			// If the status code is not 'SUCCESS', then populate the list of
			// exceptions
			// returned by the webservice.
			if (!checkInAFResponseType.getStatusCode().toString().equals(ResponseStatusCode.SUCCESS.value())) {

				RunAllSamples.reportFailure(clsName);

				AgileExceptionListType[] agileExceptionListType = checkInAFResponseType.getExceptions()
						.toArray(new AgileExceptionListType[0]);
				if (agileExceptionListType != null)
					for (int i = 0; i < agileExceptionListType.length; i++) {
						AgileExceptionType exceptions[] = agileExceptionListType[i].getException()
								.toArray(new AgileExceptionType[0]);
						for (int j = 0; j < exceptions.length; j++)
							System.out.println("Exception Id:" + exceptions[j].getExceptionId() + "\nMessage: "
									+ exceptions[j].getMessage());
					}

				AgileWarningListType agileWarningListType[] = checkInAFResponseType.getWarnings()
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
				System.out.print("The check in operation is done sucessfully ");
				System.out.println(agileCheckAFRequestType.getChangeNumber() + ".");
			}

		} catch (Exception e) {
			RunAllSamples.reportFailure(clsName);
			System.out.println("Exceptions: ");
			e.printStackTrace();
		}
	}

	public static void checkoutAF() {
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
				System.out.print("The check out operation is done sucessfully ");
				System.out.println(agileCheckAFRequestType.getChangeNumber() + ".");
			}
		} catch (Exception e) {
			RunAllSamples.reportFailure(clsName);
			System.out.println("Exceptions: ");
			e.printStackTrace();
		}

	}

}
