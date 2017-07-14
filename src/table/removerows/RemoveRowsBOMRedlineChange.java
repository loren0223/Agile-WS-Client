package table.removerows;


import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;

import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileRowType;
import com.agile.ws.schema.common.v1.jaxws.AgileTableType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningListType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningType;
import com.agile.ws.schema.common.v1.jaxws.PropertyType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.common.v1.jaxws.SchemaConstants;
import com.agile.ws.schema.table.v1.jaxws.AddRowsRequestType;
import com.agile.ws.schema.table.v1.jaxws.AddRowsResponseType;
import com.agile.ws.schema.table.v1.jaxws.AgileAddRowsRequest;
import com.agile.ws.schema.table.v1.jaxws.AgileRemoveRowsRequest;
import com.agile.ws.schema.table.v1.jaxws.LoadTableRequestType;
import com.agile.ws.schema.table.v1.jaxws.LoadTableResponseType;
import com.agile.ws.schema.table.v1.jaxws.RemoveRowsRequestType;
import com.agile.ws.schema.table.v1.jaxws.RemoveRowsResponseType;
import com.agile.ws.schema.table.v1.jaxws.RequestTableType;
import com.agile.ws.service.table.v1.jaxws.TablePortType;
import com.agile.ws.service.table.v1.jaxws.TableService;
import common.util.WSUtil;

import run.DataPrepare;
import run.RunAllSamples;



 /**
 * Sample      : RemoveRowsRedlineChange
 * Category    : Table webservice
 *
 * Description : This sample demonstrates the addition of rows to a table
 * of a particular version of an Agile object. The change object associated with
 * the object is specified as an option 'redline_change. The content of the row to be 
 * added to the table is expressed in the form of message elements in the request
 * object.
 */
public class RemoveRowsBOMRedlineChange {
    private static final String COMMAND_NAME = "RemoveRowsRedlineChange";
    
	public static String clsName;
    public static String serviceName   =  "Table";    
    public static TablePortType agileStub;        
    
    public static String parentPartNumber = "9PA5507300";
    public static String[] childPartNumber = {"1DB000214BGP", "1DC0000914GP"};
    public static String changeNumber = "EC-00000000";

    public RemoveRowsBOMRedlineChange() {
             clsName = this.getClass().getName();

    }
      

    /**
     * The method setupServerLogin is used to login to the server by providing
     * the server url details and authentication credentials.
     * @param args 
     */
    
    public static void setupServerLogin(String[] args) throws Exception {       

    	//The AdminMetadataServiceLocator is used to obtain a AdminMetadata_BindingStub
    	URL url = new URL(args[0] + "/" + serviceName + "?WSDL"); 
    	TableService locator = 
             new TableService(url);

        //A stub of type AdminMetadata_BindingStub is obtained by specifying the server URL
        //Eventually, the convertCurrency webservice will be invoked by using this agileStub.
          agileStub = locator.getTable();
                
          
        // Username and password details are specified for the agileStub
          Map<String, Object> reqContext = ((BindingProvider)agileStub).getRequestContext();
          reqContext.put(BindingProvider.USERNAME_PROPERTY, args[1]);
          reqContext.put(BindingProvider.PASSWORD_PROPERTY, args[2]);
     }
    
    public static Integer getRowID(String cls, String objectNum, Object tableId, String childNum ) throws Exception{       
        
        LoadTableRequestType loadTableRequestType = new LoadTableRequestType();
        RequestTableType table[] = new RequestTableType[1];
        table[0] = new RequestTableType();
        table[0].setClassIdentifier( cls );
        table[0].setObjectNumber(objectNum);
        table[0].setTableIdentifier( tableId.toString() );
        
        PropertyType properties[] = new PropertyType[1];
        properties[0] = new PropertyType();
        properties[0].setPropertyName("redline_change" );
        properties[0].setPropertyValue( changeNumber );
        table[0].getOptions().addAll(Arrays.asList(properties));
        
        loadTableRequestType.getTableRequest().addAll(Arrays.asList(table));
        
        
        
        Integer rowID = null;
        
        LoadTableResponseType loadTableResponseType = agileStub.loadTable(loadTableRequestType);
        if( loadTableResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

            
            AgileTableType[] tables = loadTableResponseType.getTableContents().toArray(new AgileTableType[0]);
            if(tables!=null)
                for(int i=0; i<tables.length; i++){
                            
                    AgileRowType[] rows = tables[i].getRow().toArray(new AgileRowType[0]);
                    if(rows!=null)
                        for(int j=0; j<rows.length; j++){
                            
                            List<Element> messages = rows[j].getAny();
                            for(int m=0; m<messages.size(); m++){
                            	
                            	if( messages.get(m) instanceof Element){
	                                Element element = (Element) messages.get(m);
	                           
	                                if(! element.getTagName().equals("itemNumber") )
	                                    continue;
	                                else
	                                if(! element.getFirstChild().getNodeValue().equals(childNum))
	                                    continue;
	                                else
	                                       rowID = rows[j].getRowId();
                            	}
                              }                                    
                            }
                                
                        }
                }
        else{
            System.out.println("<Failed to load table information>");
            AgileExceptionListType[] agileExceptionListType = loadTableResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
            if(agileExceptionListType!=null)
            for(int i=0; i<agileExceptionListType.length; i++){
                AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                for(int j=0; j<exceptions.length; j++)
                    System.out.println(exceptions[j].getMessage() );
            }
            
        }
            
        if(rowID!=null)
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

    /*    
    public static void prepareData(String[] args){
        
        try{
            System.out.println("<<< Preparing Data... >>>");
            DataPrepare dataPrepare = new DataPrepare(args[0], args[1], args[2]);
            dataPrepare.setupAdminMetadata(); 
            dataPrepare.setupBusiness();
            dataPrepare.setupCollaboration();
            dataPrepare.setupTable();
            
            parentPartNumber = dataPrepare.createNewObject("Part");
            childPartNumber =  dataPrepare.createNewObject("Part");
            changeNumber = dataPrepare.getNextAutoNumber("ECO");
            dataPrepare.createObjectByTags("ECO", new String[]{"number"}, new String[]{changeNumber} );
            dataPrepare.addRow("ECO", changeNumber, "AffectedItems",
                               "itemNumber", parentPartNumber);
            
            System.out.println("<<< DataPrepare successful >>>");
        }
        catch(Exception e){            
            System.out.println("<<< ALERT: DataPrepare failed. Your server settings may be invalid, this sample might not run successfully.");
            System.out.println("To run the sample without the assitance of prepared data, you may edit the static variables after");
            System.out.println("creating the necessary prerequisites through the Agile web client and then execute the sample >>>");
        }
        
        
    }
    */

    public static void main(String[] args) {
        
        // This sample may be configured by either directly modifying the string values of
        // 'SERVER_URL', 'USERNAME' and 'PASSWORD' (defined in this class), or by
        // passing them as program arguments in the same order. In this sample, the
        // method 'checkArguments' checks for these values.
        checkArguments(args);        

        // Comment this method out if you intend to use your own data
        // or scenario by editing the static variables at the top of this code
        //prepareData(args);

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");
        
        try{
            
            // Login to the server by providing the server url and authentication credentials.
            setupServerLogin(args);             
            
            // Create the request object AddRowsRequestType for the addRows operation
            // Create an array of requests of type AgileAddRowsRequestType. Batch operations may be
            // performed by populating as many request objects as required to copy several tables at once.
            RemoveRowsRequestType removeRowsRequestType = new RemoveRowsRequestType();                         
            AgileRemoveRowsRequest agileRemoveRowsRequest[] = new AgileRemoveRowsRequest[2];             
            agileRemoveRowsRequest[0] = new AgileRemoveRowsRequest();
            agileRemoveRowsRequest[1] = new AgileRemoveRowsRequest();
            
            System.out.println("Removing a redline row to the Redline-Bom table of the part '" + parentPartNumber + "'...");    
            System.out.println("Corresponding change object: '" + changeNumber + "'\n");
                
            agileRemoveRowsRequest[0].getRowId().add(getRowID("Parts", parentPartNumber, "-803", childPartNumber[0]));
            agileRemoveRowsRequest[1].getRowId().add(getRowID("Parts", parentPartNumber, "-803", childPartNumber[1]));
            
            // Important note: While updating the redline table of an item, the
            // corresponding change object must also be mentioned. This is
            // achieved by passing the same as a name value property of type
            // 'PropertyType' and setting the same into the 'setOptions' method
            // of the 'objectInfo' (RequestTableType) element            
            
            RequestTableType table = new RequestTableType();
            table.setClassIdentifier("Parts");
            table.setObjectNumber( parentPartNumber );
            table.setTableIdentifier( "-803" );
            // -803 corresponds to the redline BOM table
            
            PropertyType properties[] = new PropertyType[1];
            properties[0] = new PropertyType();
            properties[0].setPropertyName( SchemaConstants.REDLINE_CHANGE.value() );
            properties[0].setPropertyValue( changeNumber );
            table.getOptions().addAll(Arrays.asList(properties));            
            
            agileRemoveRowsRequest[0].setObjectInfo(table);                       
            agileRemoveRowsRequest[1].setObjectInfo(table);        

            // The request objects are set and the agile Stub is used to make the addRows
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the addRows operation. 
            removeRowsRequestType.getRows().addAll(Arrays.asList(agileRemoveRowsRequest));             
            RemoveRowsResponseType removeRowsResponseType = agileStub.removeRows(removeRowsRequestType);
            System.out.println("STATUS CODE: " +  removeRowsResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !removeRowsResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = removeRowsResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println("Exception: " + exceptions[j].getMessage() );
                }
                
                AgileWarningListType agileWarningListType[] = removeRowsResponseType.getWarnings().toArray(new AgileWarningListType[0]);
                if(agileWarningListType!=null)
                for(int i=0; i<agileWarningListType.length; i++){
                    AgileWarningType warnings[] = agileWarningListType[i].getWarning().toArray(new AgileWarningType[0]);
                    for(int j=0; j<warnings.length; j++)
                        System.out.println("Warning: " + warnings[j].getMessage() );
                }                    
                                
            }
                else{
                // If the webservice call was successful, then confirm the success of the operation
                    
                	System.out.print("The specified row in ");
                    System.out.print(table.getTableIdentifier() + " of " + table.getObjectNumber() );
                    System.out.println(" was removed");
                }


            }
          
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }

}
