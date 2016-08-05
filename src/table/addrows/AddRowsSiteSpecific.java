package table.addrows;




import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;

import com.agile.ws.schema.common.v1.jaxws.AdditionalInfoType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileRowType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningListType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningType;
import com.agile.ws.schema.common.v1.jaxws.PropertyType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.common.v1.jaxws.SchemaConstants;
import com.agile.ws.schema.table.v1.jaxws.AddRowsRequestType;
import com.agile.ws.schema.table.v1.jaxws.AddRowsResponseType;
import com.agile.ws.schema.table.v1.jaxws.AgileAddRowsRequest;
import com.agile.ws.schema.table.v1.jaxws.RequestTableType;
import com.agile.ws.service.table.v1.jaxws.TablePortType;
import com.agile.ws.service.table.v1.jaxws.TableService;
import common.util.WSUtil;

import run.DataPrepare;
import run.RunAllSamples;



 /**
 * Sample      : AddRowsRedlineChange
 * Category    : Table webservice
 *
 * Description : This sample demonstrates the addition of site specific data rows 
 * to a table of an Agile object. The site associated with the data is specified
 * as an option 'site'. The content of the row to be added to the table is 
 * expressed in the form of message elements in the request object.
 */
public class AddRowsSiteSpecific {
    private static final String COMMAND_NAME = "AddRowsSiteSpecific";
    
	public static String clsName;
    public static String serviceName   =  "Table";    
    public static TablePortType agileStub;        
    
    public static String parentPartNumber;
    public static String childPartNumber;    
    public static String site1;    
    public static String site2;    

    public AddRowsSiteSpecific() {
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
            dataPrepare.setupTable();
            
            parentPartNumber = dataPrepare.createNewObject("Part");
            childPartNumber = dataPrepare.createNewObject("Part");
            
            site1 = "Site1" + System.currentTimeMillis();
            site2 = "Site2" + System.currentTimeMillis();
            dataPrepare.createObjectByTags("Site",
                                           new String[]{"name" },
                                           new String[]{site1} );
            dataPrepare.createObjectByTags("Site",
                                           new String[]{"name" },
                                           new String[]{site2} );            

            dataPrepare.updateRow("Site", site1, "GeneralInfo", "name", site1, "lifecyclePhase", "Enabled", "AgileListEntryType");                                    
            dataPrepare.updateRow("Site", site2, "GeneralInfo", "name", site2, "lifecyclePhase", "Enabled", "AgileListEntryType");                                                
            dataPrepare.addRow("Part", parentPartNumber, "Sites", "siteName", site1, "ObjectReferentIdType");
            dataPrepare.addRow("Part", parentPartNumber, "Sites", "siteName", site2, "ObjectReferentIdType");
            dataPrepare.addRow("Part", childPartNumber, "Sites", "siteName", site1, "ObjectReferentIdType");            
            
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
            
            // Create the request object AddRowsRequestType for the addRows operation
            // Create an array of requests of type AgileAddRowsRequestType. Batch operations may be
            // performed by populating as many request objects as required to copy several tables at once.
            AddRowsRequestType addRowsRequestType = new AddRowsRequestType();                         
            AgileAddRowsRequest agileAddRowsRequest[] = new AgileAddRowsRequest[1];             
            agileAddRowsRequest[0] = new AgileAddRowsRequest();
            
            // For each batched request, specify the table whose contents are to be cleared.
            
            // Using the addRows webservice, we add a child element to a Part by adding rows 
            // to the BOM table of the parent object.
            
            // Tables in Agile webservices are defined as RequestTableType objects. A specifc
            // table may be identified by specifying the class identifier and table identifier
            // attributes as shown. Rows in Agile are defined as AgileRowType objects. Here
            // message elements may be used to specify the row data. This is similar to how the 
            // '_any' information is specified in a createObject or getObject Business service call. 
           
            RequestTableType table = new RequestTableType();
            table.setClassIdentifier("Part");
            table.setObjectNumber( parentPartNumber );
            table.setTableIdentifier( "BOM" );
            
            
            // Adding a BOM to a specific site: 
            
            // To add a child object to a specific site, we can utilize either the
            // 'setOptions' feature on the table object, or use the
            // 'setAdditionalRowInfo' method on the row object. Using setOptions 
            // on the table object will add all new rows to a particular
            // site. On the other hand, 'setAdditionalRowInfo' may be used to 
            // specify a site for each individual row, meaning that if
            // several rows are to be added with a webservice call, each row
            // may be added to a different site.
            
            // In this sample 'setAdditionalRowInfo' is used to add a given row
            // to a specific site of a Part using the 'addRows' webservice. 
            // The section of code illustrating the setOptions method is commmented out
            
            /* ---COMMENTED OUT----
            // Note 1: Using 'setOptions' to add all rows to a given site 
            // Set the properties onto the table options using 'setOptions' 
            // by creaing PropertyType objects and setting values onto the same.            
            // Name value property pairs are defined as 'PropertyType' objects in 
            // Agile webservices. Use this propertyType object to define any property.
            // The class SchemaConstants may be used to find property names of common options.            
            
            PropertyType[] properties = new PropertyType[1];
            properties[0] = new PropertyType();
            properties[0].setPropertyName( SchemaConstants.site.getValue() );
            properties[0].setPropertyValue(site1);               
            table.setOptions(properties);

             ---COMMENTED OUT----*/

            PropertyType[] properties = new PropertyType[1];
            properties[0] = new PropertyType();
            properties[0].setPropertyName( SchemaConstants.SITE.value() );
            properties[0].setPropertyValue(site1);
            
            // Update the table's 'Options' information with the relevant site properties 
            table.getOptions().addAll(Arrays.asList(properties));
            // The row to be added is now defined as in any ordinary case of row 
            // addition; Attributes and values are passed as Message Elements in 
            // rows which are set into the request object as shown:
            AgileRowType[] rows = new AgileRowType[1];            
            rows[0] = new AgileRowType(); 
            String namespaceUri = null;
           
            Element element  = WSUtil.createMessageElement("itemNumber");
            element.setTextContent(childPartNumber);
            rows[0].getAny().add(element);
            // Note 2: Using 'setAdditionalRowInfo' to add a given row to a specific Site.
            // Set additional row information using the object type AdditionalInfoType
            // The site to which the row must be added may now be specified using the
            // method 'setSite' as shown here:

            AdditionalInfoType additionalInfoType = new AdditionalInfoType();
            additionalInfoType.setSite(site1);
            rows[0].setAdditionalRowInfo(additionalInfoType);
            
            agileAddRowsRequest[0].getRow().addAll(Arrays.asList(rows));
            agileAddRowsRequest[0].setObjectInfo(table);
            
            System.out.println("Adding the part '" + childPartNumber + "' to the part '" + parentPartNumber + "'");
            System.out.println("specifically to the site '" + site1 + "'...\n");

            // The request objects are set and the agile Stub is used to make the addRows
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the addRows operation. 
            addRowsRequestType.getData().addAll(Arrays.asList(agileAddRowsRequest));             
            AddRowsResponseType addRowsResponseType = agileStub.addRows(addRowsRequestType);
            System.out.println("STATUS CODE: " +  addRowsResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !addRowsResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = addRowsResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println("Exception: " + exceptions[j].getMessage() );
                }
                
                AgileWarningListType agileWarningListType[] = addRowsResponseType.getWarnings().toArray(new AgileWarningListType[0]);
                if(agileWarningListType!=null)
                for(int i=0; i<agileWarningListType.length; i++){
                    AgileWarningType warnings[] = agileWarningListType[i].getWarning().toArray(new AgileWarningType[0]);
                    for(int j=0; j<warnings.length; j++)
                        System.out.println("Warning: " + warnings[j].getMessage() );
                }                                   
                
            }
                else{
                // If the webservice call was successful, then confirm the success of the operation

                    System.out.print(table.getTableIdentifier() + " of " + table.getObjectNumber() );
                    System.out.println(" was updated with the specified row(s)");
                    System.out.println("This was added specifically to the site " + site1);
                }


            }
           
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }

}
