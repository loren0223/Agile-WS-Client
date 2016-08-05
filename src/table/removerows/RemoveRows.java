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
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.table.v1.jaxws.AgileRemoveRowsRequest;
import com.agile.ws.schema.table.v1.jaxws.LoadTableRequestType;
import com.agile.ws.schema.table.v1.jaxws.LoadTableResponseType;
import com.agile.ws.schema.table.v1.jaxws.RemoveRowsRequestType;
import com.agile.ws.schema.table.v1.jaxws.RemoveRowsResponseType;
import com.agile.ws.schema.table.v1.jaxws.RequestTableType;
import com.agile.ws.service.table.v1.jaxws.TablePortType;
import com.agile.ws.service.table.v1.jaxws.TableService;
import run.DataPrepare;
import run.RunAllSamples;

import table.updaterows.UpdateRows;

 /**
 * Sample      : RemoveRows
 * Category    : Table webservice
 *
 * Description : This sample demonstrates the process of removing a row
 * in a table from an Agile object. The request object consists of
 * specifications identifying the table and the row to be removed. 
 */
public class RemoveRows {
    private static final String COMMAND_NAME = "RemoveRows";
    
	public static String clsName;
    public static String serviceName   =  "Table";    
    public static TablePortType agileStub;        
    
    public static String parentPartNumber;
    public static String childPartNumber;

    public RemoveRows() {
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

    public static void prepareData(String[] args){
        
        try{
            System.out.println("<<< Preparing Data... >>>");
            DataPrepare dataPrepare = new DataPrepare(args[0], args[1], args[2]);
            dataPrepare.setupAdminMetadata(); 
            dataPrepare.setupBusiness();
            dataPrepare.setupTable();
            
            parentPartNumber = dataPrepare.createNewObject("Part");
            childPartNumber = dataPrepare.createNewObject("Part");                        
            dataPrepare.addRow("Part", parentPartNumber, "BOM", "itemNumber", childPartNumber );
            
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
        prepareData(args);

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");
        
        try{
            
            // Login to the server by providing the server url and authentication credentials.
            setupServerLogin(args);             
            
            // Create the request object RemoveRowsRequestType for the removeRows operation
            // Create an array of requests of type AgileRemoveRowsRequestType. Batch operations may be
            // performed by populating as many request objects as required to remove several rows at once.
            RemoveRowsRequestType removeRowsRequestType = new RemoveRowsRequestType();                         
            AgileRemoveRowsRequest agileRemoveRowsRequest[] = new AgileRemoveRowsRequest[1];             
            agileRemoveRowsRequest[0] = new AgileRemoveRowsRequest();
            
            // For each batched request, specify the following details:
            // 1. The table whose rows will be removed
            // 2. The rowId of the row that will be updated with new values
            // Tables in Agile webservices are defined as RequestTableType objects. A specifc
            // table may be identified by specifying the class identifier and table identifier
            // attributes as shown. 
            
            // Using the removeRows webservice, in this particular case we remove a child element in
            // the BOM table of its parent object by finding and removing its specifc row.
           
            //Todo: Create a parent part eg:P00001 and a child, eg:P00002 to its BOM table
            RequestTableType table = new RequestTableType();
            table.setClassIdentifier("Part");
            table.setObjectNumber( parentPartNumber );
            table.setTableIdentifier( "BOM" );
            
            agileRemoveRowsRequest[0].setObjectInfo(table);            
            
            // The method 'getRowID' which is also written in this sample program obtains the
            // rowId of the specified BOM by issuing a loadTable webservice on the BOM table
            // of the parent and iterating through the rows till it finds the row queried for.
            // The request object is then set with this rowId as shown. The row will be removed
            // by using this value to identify it. 
        
            agileRemoveRowsRequest[0].getRowId().add(getRowID("Part", parentPartNumber, "BOM", childPartNumber));
            
            System.out.println("Removing the row containing the part '" + childPartNumber + "' from the BOM table");
            System.out.println("of the part '" + parentPartNumber + "'...\n");

            // The request objects are set and the agile Stub is used to make the removeRows
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the removeRows operation. 
            removeRowsRequestType.getRows().addAll(Arrays.asList(agileRemoveRowsRequest));             
            RemoveRowsResponseType removeRowsResponseType = agileStub.removeRows(removeRowsRequestType);
            System.out.println("\nSTATUS CODE: " +  removeRowsResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !removeRowsResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = removeRowsResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
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
