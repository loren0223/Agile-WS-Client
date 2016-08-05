package table.loadtable;


import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileListEntryType;
import com.agile.ws.schema.common.v1.jaxws.AgileRowType;
import com.agile.ws.schema.common.v1.jaxws.AgileTableType;
import com.agile.ws.schema.common.v1.jaxws.PropertyType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.common.v1.jaxws.SchemaConstants;
import com.agile.ws.schema.common.v1.jaxws.SelectionType;
import com.agile.ws.schema.table.v1.jaxws.LoadTableRequestType;
import com.agile.ws.schema.table.v1.jaxws.LoadTableResponseType;
import com.agile.ws.schema.table.v1.jaxws.RequestTableType;
import com.agile.ws.service.table.v1.jaxws.TablePortType;
import com.agile.ws.service.table.v1.jaxws.TableService;

import run.DataPrepare;
import run.RunAllSamples;
import common.util.WSUtil;
import org.w3c.dom.Element;


 /**
 * Sample      : LoadTableSite
 * Category    : Table webservice
 *
 * Description : This sample demonstrates the process of loading site-specific
 * content of a table from an Agile object. The request object consists of
 * specifications identifying the table and the site along with an option 'site'
 * that refers to the site with respect to which the table data is required.
 * The response object consists of the rows of that table, with row content
 * available as message elements.
 */
public class LoadTableSite {
    private static final String COMMAND_NAME = "LoadTableSite";
    
	public static String clsName;
    public static String serviceName   =  "Table";    
    public static String partNumber;
    public static TablePortType agileStub;       
    
    public static String parentPartNumber;
    public static String site1;    
    

    public LoadTableSite() {
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
            String childPartNumber = dataPrepare.createNewObject("Part");
            
            site1 = "Site1" + System.currentTimeMillis();
            dataPrepare.createObjectByTags("Site",
                                           new String[]{"name" },
                                           new String[]{site1} );
            dataPrepare.updateRow("Site", site1, "GeneralInfo", "name", site1, "lifecyclePhase", "Enabled", "AgileListEntryType");                                    
            
            dataPrepare.addRow("Part", parentPartNumber, "Sites", "siteName", site1, "ObjectReferentIdType");
            dataPrepare.addRow("Part", childPartNumber, "Sites", "siteName", site1, "ObjectReferentIdType");            
            
            dataPrepare.addRowsSiteSpecific(parentPartNumber, childPartNumber, site1);
            
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
            
            // Create the request object LoadTableRequestType for the loadTable operation.
            LoadTableRequestType loadTableRequestType = new LoadTableRequestType();;           

            
            // For each request, specify the table(s) whose content is to be retrieved.
            
            // Tables in Agile webservices are defined as RequestTableType objects. A specifc
            // table may be identified by specifying the class identifier and table identifier
            // attributes as shown:
            RequestTableType table[] = new RequestTableType[1];
            table[0] = new RequestTableType();
            table[0].setClassIdentifier( "Part" );
            table[0].setObjectNumber( parentPartNumber );
            table[0].setTableIdentifier( "BOM" );
            
            // Use the 'options' element to specify the particular revision of the 
            // object whose table has to be loaded. Properties are defined as 
            // name value pairs of objects of type PropertyType. The property names
            // may be obtained from the constants listed in the file 'SchemaConstants'
            PropertyType properties[] = new PropertyType[1];
            properties[0] = new PropertyType();
            properties[0].setPropertyName( SchemaConstants.SITE.value() );
            properties[0].setPropertyValue( site1 );
            table[0].getOptions().addAll(Arrays.asList(properties));
            
            System.out.println("Loading the BOM table of part '" + parentPartNumber + "' and of its site '" + site1 + "'...\n");
            
            // The request objects are set and the agile Stub is used to make the loadTable
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the loadTable operation. 
            loadTableRequestType.getTableRequest().addAll(Arrays.asList(table));
            LoadTableResponseType loadTableResponseType = agileStub.loadTable(loadTableRequestType);
            System.out.println("STATUS CODE: " +  loadTableResponseType.getStatusCode() );

            // If the status code indicates that the webservice call was not successful,
            // then populate a list of exceptions.            
                if(!loadTableResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                
                    System.out.println("<Failed to load table information>");
                    AgileExceptionListType[] agileExceptionListType = loadTableResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                    if(agileExceptionListType!=null)
                    for(int i=0; i<agileExceptionListType.length; i++){
                        AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                        for(int j=0; j<exceptions.length; j++)
                            System.out.println(exceptions[j].getMessage() );
                    }                
                }               
                // If the status code is 'SUCCESS', then populate the information retrieved
                // by the webservice call.  
                else{
                    System.out.println("Object: " + table[0].getObjectNumber() );
                    AgileTableType[] tables = loadTableResponseType.getTableContents().toArray(new AgileTableType[0]);                    
                    
                    // The method displayTableContents in this loadTable sample receives
                    // an array of tables as input and displays their content.
                    displayTableContents(tables);                    
                }

            }
            
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }

  public static void displayMessageElementValue(Element element){
        
        if(element.getFirstChild()!=null){                                          

            // Important Note: Certain message element values will be obtained 
            // as selections of type "AgileListEntryType". Such values must be
            // accessed as objects of AgileListEntryType by using the 'getObjectValue'
            // method of the message element and by casting the object appropriately.
            
            // The code below checks if the message element is of type AgileListEntryType.
            // If so, it retrieves the value information as a selection and displays the same.
            if(element.getAttribute("xsi:type").contains("AgileListEntryType")){
                
                AgileListEntryType list = (AgileListEntryType) WSUtil.unmarshalToAgileListEntryType(element);;
                SelectionType selection[] = list.getSelection().toArray(new SelectionType[0]);
                for( SelectionType sel: selection){
                    System.out.println(sel.getValue());
                }
            }   
            // If the message element contains an ordinary text value, then print the same
            else
                System.out.println( element.getFirstChild().getNodeValue() );                                        
        }                                            
        else
            System.out.println();
        
    }
    
    public static void displayTableContents(AgileTableType[] tables){
        
        if(tables!=null)
            for(int i=0; i<tables.length; i++){
                        
                System.out.println("---------------------------");
                System.out.print("Table: "); 
                String tableName = tables[i].getTableIdentifier().getTableDisplayName();
                if(tableName!=null)
                    System.out.println(tableName);
                else
                    System.out.println();
                
                AgileRowType[] rows = tables[i].getRow().toArray(new AgileRowType[0]);
                if(rows!=null)
                    for(int j=0; j<rows.length; j++){
                        
                        System.out.println("Row " + (j+1) + ":" );
                        List<Element> messages = rows[j].getAny();
                        for(int m=0; m<messages.size(); m++){  
                        	if(messages.get(m) instanceof Element){
	                            Element element = (Element) messages.get(m);
	                            System.out.print( element.getNodeName() + "--> " );                            
	                            displayMessageElementValue(element);
                        	}else if(messages.get(m) instanceof JAXBElement){
                        		JAXBElement element = (JAXBElement) messages.get(m);
                        		System.out.print( element.getName().getLocalPart() + "--> " ); 
                        		displayMessageElementValue(element);
                        	}
                            
                          }
                        System.out.println("---------------------------");
                        }
                            
                    }
        
        
    }
    
    
 public static void displayMessageElementValue(JAXBElement element){
	Object obj = element.getValue();
	
	if(obj instanceof AgileListEntryType){
		AgileListEntryType agileListEntryType = (AgileListEntryType) obj;
		 SelectionType selection[] = agileListEntryType.getSelection().toArray(new SelectionType[0]);
         for( SelectionType sel: selection){
             System.out.print(sel.getValue()+",");
         }
		
	}
	
	 System.out.println();
 }
}
