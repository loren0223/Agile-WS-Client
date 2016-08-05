package attachment.markup;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;

import com.agile.api.ChangeConstants;
import com.agile.ws.schema.attachment.v1.jaxws.AddMarkupRowRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.AddMarkupRowResponseType;
import com.agile.ws.schema.attachment.v1.jaxws.AgileAddMarkupRowRequest;
import com.agile.ws.schema.attachment.v1.jaxws.AgileLoadMarkupTableRequest;
import com.agile.ws.schema.attachment.v1.jaxws.AgileMarkupFileType;
import com.agile.ws.schema.attachment.v1.jaxws.AgileRemoveMarkupRowRequest;
import com.agile.ws.schema.attachment.v1.jaxws.AgileUpdateMarkupRowRequest;
import com.agile.ws.schema.attachment.v1.jaxws.LoadMarkupTableRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.LoadMarkupTableResponseType;
import com.agile.ws.schema.attachment.v1.jaxws.RemoveMarkupRowRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.RemoveMarkupRowResponseType;
import com.agile.ws.schema.attachment.v1.jaxws.UpdateMarkupRowRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.UpdateMarkupRowResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileListEntryType;
import com.agile.ws.schema.common.v1.jaxws.AgileRowType;
import com.agile.ws.schema.common.v1.jaxws.AgileTableType;
import com.agile.ws.schema.common.v1.jaxws.PropertyType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.common.v1.jaxws.SelectionType;
import com.agile.ws.schema.table.v1.jaxws.LoadTableRequestType;
import com.agile.ws.schema.table.v1.jaxws.LoadTableResponseType;
import com.agile.ws.schema.table.v1.jaxws.RequestTableType;
import com.agile.ws.service.attachment.v1.jaxws.AttachmentPortType;
import com.agile.ws.service.attachment.v1.jaxws.AttachmentService;
import com.agile.ws.service.table.v1.jaxws.TablePortType;
import com.agile.ws.service.table.v1.jaxws.TableService;
import common.util.WSUtil;

import run.DataPrepare;
import run.RunAllSamples;

/**
 * Sample : MarkupTable Category : Attachment webservice
 *
 * Description : This sample demonstrates the actions of the markup table:
 * addMarkupRow, loadMarkupTable, updateMarkupRow and removeMarkupRow
 *
 * @since 9.3.5
 * Note:- testing user must be allowed to do Add/Update/Remove operations for Affected Files.Markup Row.
 */
public class MarkupTableAction {
	private static final String COMMAND_NAME = "MarkupTableAction";
	
	public static String clsName;
	public static String serviceName = "Attachment";

	public static TablePortType agileTableStub;
	public static AttachmentPortType agileStub;
	public static String changeNumber;
	public static String folderNumber;
	public static int affectedfileRowId;
	public static int markupRowId;
	public static int tmp = 0;

	public MarkupTableAction() {
		clsName = this.getClass().getName();

	}

	/**
	 * The method setupServerLogin is used to login to the server by providing
	 * the server url details and authentication credentials.
	 * @param args 
	 */

	public static void setupServerLogin(String[] args) throws Exception {
		// The AdminMetadataServiceLocator is used to obtain a
		// AdminMetadata_BindingStub
		URL url = new URL(args[0] + "/" + serviceName + "?WSDL");
		AttachmentService locator = new AttachmentService(url);

		// Eventually, the addMarkupRow, loadMarkupTable, updateMarkupRow and
		// removeMarkupRow
		// webservice will be invoked by using thisagileStub.
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
			agileTableStub = dataPrepare.tableAgileStub;
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
			
			loadAFTable() ;
			
			if (affectedfileRowId < 1) {
				RunAllSamples.reportFailure(clsName);
				System.out.println("<Failed to load affected file row information>");
				return;
			}
			// The method displayTableContents in this loadTable sample
			// receives
			addMarkup();

			// an array of tables as input and displays their content.
			loadMarkup();
			if(markupRowId<1){
				RunAllSamples.reportFailure(clsName);
				System.out.println("<Failed to load markup row information>");
				return;
			}
				
			updateMarkup();
			
			addMarkup();
			
			removeMarkup();			

		}

		catch (Exception e) {
			RunAllSamples.reportFailure(clsName);
			e.printStackTrace();
		}
	}
 
	//Need get Affected File Row
	private static void loadAFTable() {
		try {
			// Create the request object LoadTableRequestType for the loadTable
			// operation.
			LoadTableRequestType loadTableRequestType = new LoadTableRequestType();

			// For each request, specify the table(s) whose content is to be
			// retrieved.

			// Tables in Agile webservices are defined as RequestTableType
			// objects. A specifc
			// table may be identified by specifying the class identifier and
			// table identifier
			// attributes as shown:
			RequestTableType table[] = new RequestTableType[1];
			table[0] = new RequestTableType();
			table[0].setClassIdentifier("DFCO");
			table[0].setObjectNumber(changeNumber);
			table[0].setTableIdentifier("AffectedFiles");

			System.out.println("Loading the affected file table of the change '" + changeNumber + "'...\n");

			// The request objects are set and the agile Stub is used to make
			// the loadTable
			// webservice call. The status code obtained from the response
			// object is printed to
			// verify the success of the loadTable operation.
			loadTableRequestType.getTableRequest().addAll(Arrays.asList(table));
			LoadTableResponseType loadTableResponseType = agileTableStub.loadTable(loadTableRequestType);
			System.out.println("STATUS CODE: " + loadTableResponseType.getStatusCode());

			// If the status code indicates that the webservice call was not
			// successful,
			// then populate a list of exceptions.
			if (!loadTableResponseType.getStatusCode().toString().equals(ResponseStatusCode.SUCCESS.value())) {

				RunAllSamples.reportFailure(clsName);
				System.out.println("<Failed to load table information>");
				AgileExceptionListType[] agileExceptionListType = loadTableResponseType.getExceptions()
						.toArray(new AgileExceptionListType[0]);
				if (agileExceptionListType != null)
					for (int i = 0; i < agileExceptionListType.length; i++) {
						AgileExceptionType exceptions[] = agileExceptionListType[i].getException()
								.toArray(new AgileExceptionType[0]);
						for (int j = 0; j < exceptions.length; j++)
							System.out.println(exceptions[j].getMessage());
					}
				return;
			}else {
				// If the status code is 'SUCCESS', then populate the information
				// retrieved
				// by the webservice call.
				System.out.println("Object: " + table[0].getObjectNumber());
				AgileTableType[] aftables = loadTableResponseType.getTableContents().toArray(new AgileTableType[0]);
				affectedfileRowId = getAFRowID(aftables);
			}
		} catch (Exception e) {

		}

	}
	/**
	 * Display the Message Element value
	 */
	public static void displayMessageElementValue(Element element) {

		if (element.getFirstChild() != null) {
			// Important Note: Certain message element values will be obtained
			// as selections of type "AgileListEntryType". Such values must be
			// accessed as objects of AgileListEntryType by using the
			// 'getObjectValue'
			// method of the message element and by casting the object
			// appropriately.

			// The code below checks if the message element is of type
			// AgileListEntryType.
			// If so, it retrieves the value information as a selection and
			// displays the same.
			if (element.getAttribute("xsi:type").contains("AgileListEntryType")) {

				AgileListEntryType list = (AgileListEntryType) WSUtil.unmarshalToAgileListEntryType(element);
				;
				SelectionType selection[] = list.getSelection().toArray(new SelectionType[0]);
				for (SelectionType sel : selection) {
					System.out.println(sel.getValue());
				}
			}
			// If the message element contains an ordinary text value, then
			// print the same
			else
				System.out.println(element.getFirstChild().getNodeValue());
		} else
			System.out.println();

	}

	/**
	 * get first affected files row id
	 */
	public static int getAFRowID(AgileTableType[] afTables) {
		int firstId = -1;
		if (afTables != null)
			for (int i = 0; i < afTables.length; i++) {
				System.out.println("---------------------------");
				System.out.print("Table: ");
				String tableName = afTables[i].getTableIdentifier().getTableDisplayName();
				if (tableName != null)
					System.out.println(tableName);
				else
					System.out.println();

				AgileRowType[] rows = afTables[i].getRow().toArray(new AgileRowType[0]);
				if (rows != null)
					for (int j = 0; j < rows.length; j++) {
						firstId = rows[j].getRowId();
						System.out.println("Row " + (j + 1) + ":" + firstId);
						return firstId;
					}
			}
		return firstId;
	}

	/**
	 * load markup table
	 */
	public static void loadMarkup() {
		AgileTableType[] markuptable;		
		LoadMarkupTableRequestType loadMarkupTableRequestType = new LoadMarkupTableRequestType();
		AgileLoadMarkupTableRequest loadMarkupTable = new AgileLoadMarkupTableRequest();
		loadMarkupTable.setAfRowId(affectedfileRowId);
		loadMarkupTable.setChangeNumber(changeNumber);
		loadMarkupTableRequestType.getRequests().add(loadMarkupTable);
		LoadMarkupTableResponseType response = agileStub.loadMarkupTable(loadMarkupTableRequestType);
		// If the status code indicates that the webservice call was not
		// successful,
		// then populate a list of exceptions.
		if (!response.getStatusCode().toString().equals(ResponseStatusCode.SUCCESS.value())) {

			RunAllSamples.reportFailure(clsName);
			System.out.println("<Failed to load table information>");
			AgileExceptionListType[] agileExceptionListType = response.getExceptions()
					.toArray(new AgileExceptionListType[0]);
			if (agileExceptionListType != null)
				for (int i = 0; i < agileExceptionListType.length; i++) {
					AgileExceptionType exceptions[] = agileExceptionListType[i].getException()
							.toArray(new AgileExceptionType[0]);
					for (int j = 0; j < exceptions.length; j++)
						System.out.println(exceptions[j].getMessage());
				}
		}
		// If the status code is 'SUCCESS', then populate the information
		// retrieved
		// by the webservice call.
		else {
			markuptable = response.getTableContents().toArray(new AgileTableType[0]);
			// The method displayTableContents in this loadTable sample
			// receives
			// an array of tables as input and displays their content.
			displayTableContents(markuptable);

		}
	}

	/**
	 * display Table Contents
	 */
	private static void displayTableContents(AgileTableType[] tables) {
		boolean firstRow = false;
		if (tables != null)
			for (int i = 0; i < tables.length; i++) {
				System.out.println("---------------------------");
				AgileRowType[] rows = tables[i].getRow().toArray(new AgileRowType[0]);
				if (rows != null)
					for (int j = 0; j < rows.length; j++) {
						if (!firstRow) {
							markupRowId = rows[j].getRowId();
							firstRow = true;
						}
						List<Element> messages = rows[j].getAny();
						for (int m = 0; m < messages.size(); m++) {
							System.out.print(messages.get(m).getNodeName() + "--> ");
							if (messages.get(m) instanceof Element) {
								Element element = (Element) messages.get(m);

								// The method displayMessageElementValue is used
								// to display
								// the value(s) contained in a message element
								// and handles
								// special value types such as
								// AgileListEntryType objects
								displayMessageElementValue(element);
							}

						}
						System.out.println("---------------------------");
					}

			}

	}
	
	private static void updateMarkup() {
		UpdateMarkupRowRequestType updateMarkupRowRequestType = new UpdateMarkupRowRequestType();

		AgileUpdateMarkupRowRequest updateMarkupRow = new AgileUpdateMarkupRowRequest();

		updateMarkupRow.setChangeNumber(changeNumber);
		updateMarkupRow.setAfRowId(affectedfileRowId);
		updateMarkupRow.setMarkupRowId(markupRowId);
		PropertyType property = new PropertyType();
		property.setPropertyName("markupDescription");
		property.setPropertyValue("Description for markup");
		updateMarkupRow.getOptions().add(property);
		updateMarkupRowRequestType.getRequests().add(updateMarkupRow);
		UpdateMarkupRowResponseType response = agileStub.updateMarkupRow(updateMarkupRowRequestType);
		// If the status code indicates that the webservice call was not
		// successful,
		// then populate a list of exceptions.
		if (!response.getStatusCode().toString().equals(ResponseStatusCode.SUCCESS.value())) {

			RunAllSamples.reportFailure(clsName);
			System.out.println("<Failed to update markup row>");
			AgileExceptionListType[] agileExceptionListType = response.getExceptions()
					.toArray(new AgileExceptionListType[0]);
			if (agileExceptionListType != null)
				for (int i = 0; i < agileExceptionListType.length; i++) {
					AgileExceptionType exceptions[] = agileExceptionListType[i].getException()
							.toArray(new AgileExceptionType[0]);
					for (int j = 0; j < exceptions.length; j++)
						System.out.println(exceptions[j].getMessage());
				}
			return;
		} else {
			System.out.println("<markup row is updated>");
		}

	}

	private static void addMarkup() {
		AddMarkupRowRequestType addMarkupRowRequestType = new AddMarkupRowRequestType();

		AgileAddMarkupRowRequest addMarkupRow = new AgileAddMarkupRowRequest();
		addMarkupRow.setRowId(affectedfileRowId);
		addMarkupRow.setChangeNumber(changeNumber);
		AgileMarkupFileType markupFile = new AgileMarkupFileType();

		markupFile.setFileName("File_" + tmp + "_" + folderNumber + ".txt");
		tmp++;
		markupFile.setDescription("Description for file 0 ");
		File file;
		try {
			file = File.createTempFile("Filename", "txt");
			FileWriter writer = new FileWriter(file);
			writer.write("File Content...file");
			writer.close();
			FileDataSource datasource = new FileDataSource(file);
			markupFile.setFileContent(new DataHandler(datasource));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addMarkupRow.setMarkupfile(markupFile);
		addMarkupRowRequestType.getRequests().add(addMarkupRow);
		AddMarkupRowResponseType response = agileStub.addMarkupRow(addMarkupRowRequestType);
		// If the status code indicates that the webservice call was not
		// successful,
		// then populate a list of exceptions.
		if (!response.getStatusCode().toString().equals(ResponseStatusCode.SUCCESS.value())) {

			RunAllSamples.reportFailure(clsName);
			System.out.println("<Failed to add markup row>");
			AgileExceptionListType[] agileExceptionListType = response.getExceptions()
					.toArray(new AgileExceptionListType[0]);
			if (agileExceptionListType != null)
				for (int i = 0; i < agileExceptionListType.length; i++) {
					AgileExceptionType exceptions[] = agileExceptionListType[i].getException()
							.toArray(new AgileExceptionType[0]);
					for (int j = 0; j < exceptions.length; j++)
						System.out.println(exceptions[j].getMessage());
				}
			return;
		} else {
			System.out.println("<markup row is added>");
		}
	}

	private static void removeMarkup() {
		RemoveMarkupRowRequestType removeMarkupRowRequestType = new RemoveMarkupRowRequestType();

		AgileRemoveMarkupRowRequest removeMarkupRow = new AgileRemoveMarkupRowRequest();

		removeMarkupRow.setChangeNumber(changeNumber);
		removeMarkupRow.setAfRowId(affectedfileRowId);
		removeMarkupRow.setMarkupRowId(markupRowId);
		removeMarkupRowRequestType.getRequests().add(removeMarkupRow);
		RemoveMarkupRowResponseType response = agileStub.removeMarkupRow(removeMarkupRowRequestType);
		// If the status code indicates that the webservice call was not
		// successful,
		// then populate a list of exceptions.
		if (!response.getStatusCode().toString().equals(ResponseStatusCode.SUCCESS.value())) {

			RunAllSamples.reportFailure(clsName);
			System.out.println("<Failed to remove markup row>");
			AgileExceptionListType[] agileExceptionListType = response.getExceptions()
					.toArray(new AgileExceptionListType[0]);
			if (agileExceptionListType != null)
				for (int i = 0; i < agileExceptionListType.length; i++) {
					AgileExceptionType exceptions[] = agileExceptionListType[i].getException()
							.toArray(new AgileExceptionType[0]);
					for (int j = 0; j < exceptions.length; j++)
						System.out.println(exceptions[j].getMessage());
				}
			return;
		} else {
			System.out.println("<markup row is removed>");
		}

	}

}
