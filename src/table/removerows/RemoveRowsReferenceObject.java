package table.removerows;

import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;

import com.agile.api.ItemConstants;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileRowType;
import com.agile.ws.schema.common.v1.jaxws.AgileTableType;
import com.agile.ws.schema.table.v1.jaxws.AgileRemoveRowsRequest;
import com.agile.ws.schema.table.v1.jaxws.LoadTableRequestType;
import com.agile.ws.schema.table.v1.jaxws.LoadTableResponseType;
import com.agile.ws.schema.table.v1.jaxws.RequestTableType;
import com.agile.ws.schema.table.v1.jaxws.RemoveRowsRequestType;
import com.agile.ws.schema.table.v1.jaxws.RemoveRowsResponseType;
import com.agile.ws.service.table.v1.jaxws.TableService;
import com.agile.ws.service.table.v1.jaxws.TablePortType;

import run.DataPrepare;
import run.RunAllSamples;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;

 /**
 * Sample      : RemoveRows
 * Category    : Table webservice
 *
 * Description : This sample demonstrates the process of removing a row
 * in a table from an Agile object. The request object consists of
 * specifications identifying the table and the row to be removed. 
 */
public class RemoveRowsReferenceObject {
	private static final String COMMAND_NAME = null;

    public static String clsName;
    public static String serviceName   =  "Table";    
    public static TablePortType agileStub;
    
    public static String refSubclassName = "XxReferenceobject";
    public static String parentName="";
    public static String refObjPubKey="";
    public static String refObjName="";
    public static final String COMMONNAMESPACEURI = "http://xmlns.oracle.com/AgileObjects/Core/Common/V1";
	

    public RemoveRowsReferenceObject() {
             clsName = this.getClass().getName();

    }
      

    /**
     * The method setupServerLogin is used to login to the server by providing
     * the server url details and authentication credentials.
     * @param args 
     */
    
    public static void setupServerLogin(String[] args) throws Exception {       

    	URL url = new URL(args[0] + "/" + serviceName + "?WSDL");
    	//The TableService is used to obtain a TablePortType
    	TableService service = new TableService(url);

    	agileStub = (TablePortType)service.getTable();


    	// Username and password details are specified for the agileStub
    	Map<String, Object> reqContext = ((BindingProvider)agileStub).getRequestContext();
        reqContext.put(BindingProvider.USERNAME_PROPERTY, args[1]);
        reqContext.put(BindingProvider.PASSWORD_PROPERTY, args[2]);
    }
    
    public static Integer getRowID(String cls, String objectNum, Object tableId, String childNum ) throws Exception{       
        
        LoadTableRequestType loadTableRequestType = new LoadTableRequestType();
        RequestTableType table = new RequestTableType();
        table = new RequestTableType();
        table.setClassIdentifier( cls );
        table.setObjectNumber(objectNum);
        table.setTableIdentifier( tableId.toString() );
        loadTableRequestType.getTableRequest().add(table);
        Integer rowID = null;
        
        LoadTableResponseType loadTableResponseType = agileStub.loadTable(loadTableRequestType);
        if( loadTableResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ) {
            AgileTableType[] tables = loadTableResponseType.getTableContents().toArray(new AgileTableType[0]);
            if(tables!=null)
                for(AgileTableType tableType : tables) {
                            
                    List<AgileRowType> rows = tableType.getRow();
                    if(rows != null)
                        for(AgileRowType row : rows) {
                            List<Element> elements = row.getAny();
                            for(int m = 0; m < elements.size(); m++) {
                                Object element = elements.get(m);
                                String strName = "";
                                String strValue = "";
                                if (element instanceof Element) {
                            		strName = ((Element)element).getNodeName();
                            		if ("name".equalsIgnoreCase( strName ))
                            			strValue = ((Element)element).getTextContent();
                            	}
                            	else if (element instanceof JAXBElement) {
                            		strName = ((JAXBElement<?>)element).getName().getLocalPart();
                            		if ("name".equalsIgnoreCase( strName ))
                            			strValue = (String)((JAXBElement<?>)element).getValue();
                            	}
                                
                                if( strValue != null && childNum.equals(strValue) ) {
                        			rowID = row.getRowId();
                        			break;
                                }
                            }
                        }                                
                }
        }
        else {
            System.out.println("<Failed to load table information>");
            List<AgileExceptionListType> agileExceptionListType = loadTableResponseType.getExceptions();
            for(int i=0; i<agileExceptionListType.size(); i++){
                List<AgileExceptionType> exceptions = agileExceptionListType.get(i).getException();
                for(AgileExceptionType exception:exceptions)
                    System.out.println(exception.getMessage() );
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
            
            parentName = dataPrepare.createNewObject("Part");
            String[] objData = dataPrepare.createNewRefObject(refSubclassName);
            refObjName = objData[0];
            refObjPubKey = objData[1];
            dataPrepare.addRowReferenceObject(parentName, refObjName, refObjPubKey, refSubclassName);
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
            AgileRemoveRowsRequest agileRemoveRowsRequest = new AgileRemoveRowsRequest();             
            
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
            table.setObjectNumber( parentName );        
            // table.setObjectNumber( parentPartNumber );
            table.setTableIdentifier( ItemConstants.TABLE_RELATIONSHIPS.toString() );
            agileRemoveRowsRequest.setObjectInfo(table);            
            
            // The method 'getRowID' which is also written in this sample program obtains the
            // rowId of the specified BOM by issuing a loadTable webservice on the BOM table
            // of the parent and iterating through the rows till it finds the row queried for.
            // The request object is then set with this rowId as shown. The row will be removed
            // by using this value to identify it. 
            agileRemoveRowsRequest.getRowId().add( getRowID("Part",  parentName, ItemConstants.TABLE_RELATIONSHIPS.toString(), refObjName) );
            
            System.out.println("Removing the row containing the part '" + refObjName + "' from the BOM table");
            System.out.println("of the part '" +  parentName + "'...\n");

            // The request objects are set and the agile Stub is used to make the removeRows
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the removeRows operation. 
            removeRowsRequestType.getRows().add(agileRemoveRowsRequest);             
            RemoveRowsResponseType removeRowsResponseType = agileStub.removeRows(removeRowsRequestType);
            System.out.println("\nSTATUS CODE: " +  removeRowsResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !removeRowsResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                
                List<AgileExceptionListType> agileExceptionListType = removeRowsResponseType.getExceptions();
                if(agileExceptionListType!=null)
                for(int i = 0; i < agileExceptionListType.size(); i++){
                    List<AgileExceptionType> exceptions = agileExceptionListType.get(i).getException();
                    for(int j=0; j<exceptions.size(); j++)
                        System.out.println(exceptions.get(j).getMessage() );
                }
                
            }
            else {
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