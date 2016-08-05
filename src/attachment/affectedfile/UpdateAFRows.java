package attachment.affectedfile;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;

import com.agile.api.ChangeConstants;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileListEntryType;
import com.agile.ws.schema.common.v1.jaxws.AgileRowType;
import com.agile.ws.schema.common.v1.jaxws.AgileTableType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.common.v1.jaxws.SelectionType;
import com.agile.ws.schema.table.v1.jaxws.AgileUpdateRow;
import com.agile.ws.schema.table.v1.jaxws.AgileUpdateRowsRequest;
import com.agile.ws.schema.table.v1.jaxws.LoadTableRequestType;
import com.agile.ws.schema.table.v1.jaxws.LoadTableResponseType;
import com.agile.ws.schema.table.v1.jaxws.RequestTableType;
import com.agile.ws.schema.table.v1.jaxws.UpdateRowsRequestType;
import com.agile.ws.schema.table.v1.jaxws.UpdateRowsResponseType;
import com.agile.ws.service.table.v1.jaxws.TablePortType;
import com.agile.ws.service.table.v1.jaxws.TableService;

import common.util.WSUtil;
import run.DataPrepare;
import run.RunAllSamples;

/**
 * Sample : UpdateAFRows Category : Table webservice
 *
 * Description : This sample demonstrates the process of updating a Affected
 * File row of Affected Files table. The request object consists of
 * specifications identifying the table and the row to be updated. The new row
 * content with which the updation will be performed is specified in the form of
 * appropriate message elements.
 * 
 * Note:- testing user must be allowed to do Update operation for AF.
 */
public class UpdateAFRows {

	private static final String COMMAND_NAME = "UpdateAFRows";
	
	public static String clsName;
	public static String serviceName = "Table";
	public static TablePortType agileStub;
	public static String folderNumber;
	public static String changeNumber;
	private static int affectedfileRowId;

	public UpdateAFRows() {
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
		TableService locator = new TableService(url);

		// A stub of type AdminMetadata_BindingStub is obtained by specifying
		// the server URL
		// Eventually, the convertCurrency webservice will be invoked by using
		// this agileStub.
		agileStub = locator.getTable();

		// Username and password details are specified for the agileStub
		Map<String, Object> reqContext = ((BindingProvider) agileStub).getRequestContext();
		reqContext.put(BindingProvider.USERNAME_PROPERTY, args[1]);
		reqContext.put(BindingProvider.PASSWORD_PROPERTY, args[2]);
	}

	public static Integer getRowID() throws Exception {

		LoadTableRequestType loadTableRequestType = new LoadTableRequestType();
		RequestTableType table[] = new RequestTableType[1];
		table[0] = new RequestTableType();
		table[0].setClassIdentifier("DFCO");
		table[0].setObjectNumber(changeNumber);
		table[0].setTableIdentifier("AffectedFiles");
		loadTableRequestType.getTableRequest().addAll(Arrays.asList(table));
		Integer rowID = null;

		LoadTableResponseType loadTableResponseType = agileStub.loadTable(loadTableRequestType);
		if (loadTableResponseType.getStatusCode().toString().equals(ResponseStatusCode.SUCCESS.value())) {

			AgileTableType[] tables = loadTableResponseType.getTableContents().toArray(new AgileTableType[0]);
			if (tables != null)
				for (int i = 0; i < tables.length; i++) {

					AgileRowType[] rows = tables[i].getRow().toArray(new AgileRowType[0]);
					if (rows != null)
						for (int j = 0; j < rows.length; j++) {

							List<Element> messages = rows[j].getAny();
							for (int m = 0; m < messages.size(); m++) {

								if (messages.get(m) instanceof Element) {
									Element element = (Element) messages.get(m);

									if (!element.getTagName().equals("folderNumber"))
										continue;
									else
										rowID = rows[j].getRowId();
								}

							}
						}

				}
		} else {
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

		}

		if (rowID != null)
			return rowID;
		else
			return -1;

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
			dataPrepare.setupTable();
			dataPrepare.setupAttachment();

			folderNumber = dataPrepare.createNewObject("FileFolder");
			dataPrepare.checkOutFolder(folderNumber);
			dataPrepare.addAttachmentToFolder(folderNumber, new String[] { "File_" + folderNumber + ".txt" });
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
			// Create the request object UpdateRowsRequestType for the
			// updateRows operation
			// Create an array of requests of type AgileUpdateRowsRequestType.
			// Batch operations may be
			// performed by populating as many request objects as required to
			// update several tables at once.
			UpdateRowsRequestType updateRowsRequestType = new UpdateRowsRequestType();
			AgileUpdateRowsRequest agileUpdateRowsRequest[] = new AgileUpdateRowsRequest[1];
			agileUpdateRowsRequest[0] = new AgileUpdateRowsRequest();

			// For each batched request, specify the following details:
			// 1. The table whose rows will be updated
			// 2. The rowId of the row that will be updated with new values
			// 3. The new row that will replace the existing row.
			// 4. The updated content is specified in the form of Message
			// Elements.

			// Using the updateRows webservice, in this particular case we
			// modify the effective date
			// attribute on the affected items table of a change object by
			// finding and updating a specifc row.

			// Tables in Agile webservices are defined as RequestTableType
			// objects. A specifc
			// table may be identified by specifying the class identifier and
			// table identifier
			// attributes as shown. Rows in Agile are defined as AgileRowType
			// objects. Here
			// message elements may be used to specify the row data. This is
			// similar to how the
			// '_any' information is specified in a createObject or getObject
			// Business service call.

			RequestTableType table = new RequestTableType();
			table.setClassIdentifier("DFCO");
			table.setObjectNumber(changeNumber);
			table.setTableIdentifier("AffectedFiles");

			AgileUpdateRow updateRow[] = new AgileUpdateRow[1];
			updateRow[0] = new AgileUpdateRow();

			updateRow[0].setRowId(getRowID());// passing the row id
			AgileRowType row = new AgileRowType();

			// Specify the field to be updated through a message element
			// attribute as shown:
			String folderdesciption = "modified_folderdesciption";
			Element element = WSUtil.createMessageElement("FolderDescriptionMsg");
			element.setAttribute("attributeId", "2000025619");
			element.setTextContent(folderdesciption);

			row.getAny().add(element);

			updateRow[0].setRow(row);
			agileUpdateRowsRequest[0].getRow().addAll(Arrays.asList(updateRow));
			agileUpdateRowsRequest[0].setObjectInfo(table);

			System.out.println(
					"Updating the row containing the file folder '" + folderNumber + "' in the affected files table");
			System.out.println("of the change '" + changeNumber + "', updating the Folder Description attribute to "
					+ folderdesciption + "...\n");

			// The request objects are set and the agile Stub is used to make
			// the updateRows
			// web service call. The status code obtained from the response
			// object is printed to
			// verify the success of the updateRows operation.
			updateRowsRequestType.getData().addAll(Arrays.asList(agileUpdateRowsRequest));
			UpdateRowsResponseType updateRowsResponseType = agileStub.updateRows(updateRowsRequestType);
			System.out.println("STATUS CODE: " + updateRowsResponseType.getStatusCode());

			// If the status code is not 'SUCCESS', then populate the list of
			// exceptions
			// returned by the webservice.
			if (!updateRowsResponseType.getStatusCode().toString().equals(ResponseStatusCode.SUCCESS.value())) {

				RunAllSamples.reportFailure(clsName);

				AgileExceptionListType[] agileExceptionListType = updateRowsResponseType.getExceptions()
						.toArray(new AgileExceptionListType[0]);
				if (agileExceptionListType != null)
					for (int i = 0; i < agileExceptionListType.length; i++) {
						AgileExceptionType exceptions[] = agileExceptionListType[i].getException()
								.toArray(new AgileExceptionType[0]);
						for (int j = 0; j < exceptions.length; j++)
							System.out.println(exceptions[j].getMessage());
					}

			} else {
				// If the webservice call was successful, then confirm the
				// success of the operation

				System.out.print(table.getTableIdentifier() + " of " + table.getObjectNumber());
				System.out.println(" was updated with the specified row(s)");
			}
		}

		catch (Exception e) {
			RunAllSamples.reportFailure(clsName);
			System.out.println("Exceptions: ");
			e.printStackTrace();
		}
	}	

}
