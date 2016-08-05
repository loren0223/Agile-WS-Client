package table.isreadonly;



import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.table.v1.jaxws.AgileIsReadOnlyTableRequestType;
import com.agile.ws.schema.table.v1.jaxws.AgileIsReadOnlyTableResponseType;
import com.agile.ws.schema.table.v1.jaxws.IsReadOnlyTableRequestType;
import com.agile.ws.schema.table.v1.jaxws.IsReadOnlyTableResponseType;
import com.agile.ws.schema.table.v1.jaxws.RequestTableType;
import com.agile.ws.service.table.v1.jaxws.TablePortType;
import com.agile.ws.service.table.v1.jaxws.TableService;

import run.DataPrepare;
import run.RunAllSamples;



 /**
 * Sample      : IsReadOnly
 * Category    : Table webservice
 *
 * Description : This sample demonstrates the process of verifying the 'read-only'
 * status of a table in an Agile object. The request object consists of specifications 
 * identifying the table whose read-only status is to be queried for.
 */
public class IsReadOnly {
    private static final String COMMAND_NAME = "IsReadOnly";
	public static String clsName;
    public static String serviceName   =  "Table";    
    public static TablePortType agileStub;        
    
    public static String partNumber;
    public static String changeNumber;

    public IsReadOnly() {
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
            
            partNumber = dataPrepare.createNewObject("Part");
            changeNumber = dataPrepare.getNextAutoNumber("ECO");
            dataPrepare.createObjectByTags("ECO", new String[]{"number"},
                                           new String[]{changeNumber} );
            
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
            
            // Create the request object IsReadOnlyTableRequestType for the isReadOnly operation
            // Create an array of requests of type AgileIsReadOnlyTableRequestType. Batch operations may be
            // performed by populating as many request objects as required to query several tables at once
            IsReadOnlyTableRequestType isReadOnlyTableRequestType = new IsReadOnlyTableRequestType();                         
            AgileIsReadOnlyTableRequestType agileIsReadOnlyTableRequestType[] = new AgileIsReadOnlyTableRequestType[2];             
            for(int i=0; i<agileIsReadOnlyTableRequestType.length; i++)             
               agileIsReadOnlyTableRequestType[i] = new AgileIsReadOnlyTableRequestType();           

            
            // For each batched request, specify the table whose status is to be queried.
            
            // Tables in Agile webservices are defined as RequestTableType objects. A specifc
            // table may be identified by specifying the class identifier and table identifier
            // attributes as shown:
            RequestTableType table1 = new RequestTableType();
            RequestTableType table2 = new RequestTableType();
            table1.setClassIdentifier("Part");
            table1.setObjectNumber( partNumber );
            table1.setTableIdentifier( "Attachments" );
            table2.setClassIdentifier("ECO");                
            table2.setObjectNumber( changeNumber );
            table2.setTableIdentifier( "AffectedItems" );
            
            agileIsReadOnlyTableRequestType[0].setAgileTable(table1);
            agileIsReadOnlyTableRequestType[1].setAgileTable(table2);   
            
            System.out.println("Querying the following tables for the table's read-only status: (Batch operation)");
            System.out.println("Attachment table of part '" + partNumber + "'...");
            System.out.println("Affected items table of part '" + changeNumber + "'...\n");

            // The request objects are set and the agile Stub is used to make the isReadOnly
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the isReadOnly operation. 
            isReadOnlyTableRequestType.getIsReadOnlyTable().addAll(Arrays.asList(agileIsReadOnlyTableRequestType));             
            IsReadOnlyTableResponseType isReadOnlyTableResponseType = agileStub.isReadOnlyTable(isReadOnlyTableRequestType);
            System.out.println("STATUS CODE: " +  isReadOnlyTableResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !isReadOnlyTableResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = isReadOnlyTableResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
            }
                else{
                // If the webservice call was successful, then display the readonly
                // status of the tables queried
                    
                    AgileIsReadOnlyTableResponseType responses[] = isReadOnlyTableResponseType.getIsTableReadOnly().toArray(new AgileIsReadOnlyTableResponseType[0]);
                

                    for(int j=0; j<responses.length; j++){

                        System.out.println("-----------------------------------------------");                       
                        System.out.println("Table: " + responses[j].getAgileTable().getTableDisplayName() );
                        System.out.println("Read Only: " + responses[j].isIsReadOnlyTable() );
                    
                    }                
                    
                }


            }
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }


}
