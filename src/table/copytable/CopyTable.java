package table.copytable;


import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.table.v1.jaxws.AgileCopyTableRequestType;
import com.agile.ws.schema.table.v1.jaxws.CopyTableRequestType;
import com.agile.ws.schema.table.v1.jaxws.CopyTableResponseType;
import com.agile.ws.schema.table.v1.jaxws.RequestTableType;
import com.agile.ws.service.table.v1.jaxws.TablePortType;
import com.agile.ws.service.table.v1.jaxws.TableService;

import run.DataPrepare;
import run.RunAllSamples;



 /**
 * Sample      : CopyTable
 * Category    : Table webservice
 *
 * Description : This sample demonstrates the process of copying all rows of a table
 * in an Agile object to another table. The request object consists of specifications 
 * identifying the table to be copied and the target table.
 */
public class CopyTable {
    private static final String COMMAND_NAME = "CopyTable";
    
	public static String clsName;
    public static String serviceName   =  "Table";    
    public static TablePortType agileStub;        
    
    public static String partNumber1;
    public static String partNumber2;

    public CopyTable() {
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
            dataPrepare.setupAttachment();
            dataPrepare.setupTable();
            
            partNumber1 = dataPrepare.createNewObject("Part");
            partNumber2 = dataPrepare.createNewObject("Part");            
            
            
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
            
            // Create the request object CopyTableRequestType for the CopyTable operation
            // Create an array of requests of type AgileCopyTableRequestType. Batch operations may be
            // performed by populating as many request objects as required to copy several tables at once.
            CopyTableRequestType copyTableRequestType = new CopyTableRequestType();                         
            AgileCopyTableRequestType agileCopyTableRequestType[] = new AgileCopyTableRequestType[1];             
            agileCopyTableRequestType[0] = new AgileCopyTableRequestType();
            

            
            // For each batched request, specify the table whose contents are to be cleared.
            
            // Tables in Agile webservices are defined as RequestTableType objects. A specifc
            // table may be identified by specifying the class identifier and table identifier
            // attributes as shown:
            RequestTableType table1 = new RequestTableType();
            RequestTableType table2 = new RequestTableType();
            table1.setClassIdentifier("Part");
            table1.setObjectNumber( partNumber1 );
            table1.setTableIdentifier( "Compositions" );
            table2.setClassIdentifier("Part");                
            table2.setObjectNumber( partNumber2 );
            table2.setTableIdentifier( "Compositions" );
            
            agileCopyTableRequestType[0].setSourceTable(table1);
            agileCopyTableRequestType[0].setTargetTable(table2);             
            
            System.out.println("Copying the compositions table from part '" + partNumber1 + "' to '" + partNumber2 + "'...\n");

            // The request objects are set and the agile Stub is used to make the CopyTable
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the CopyTable operation. 
            copyTableRequestType.getCopyTable().addAll(Arrays.asList(agileCopyTableRequestType));             
            CopyTableResponseType copyTableResponseType = agileStub.copyTable(copyTableRequestType);
            System.out.println("STATUS CODE: " +  copyTableResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !copyTableResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = copyTableResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
            }
                else{
                // If the webservice call was successful, then display the tables that were copied
                    
                    System.out.print(table1.getTableIdentifier() + " of " + table1.getObjectNumber() );
                    System.out.println(" was successfully copied onto");
                    System.out.println(table2.getTableIdentifier() + " of " + table2.getObjectNumber() );

                }


            }
          
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }


}
