package business.get;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;

import com.agile.api.ChangeConstants;
import com.agile.api.ItemConstants;
import com.agile.ws.schema.business.v1.jaxws.AgileDataTableRequestType;
import com.agile.ws.schema.business.v1.jaxws.AgileGetObjectRequest;
import com.agile.ws.schema.business.v1.jaxws.AgileGetObjectResponse;
import com.agile.ws.schema.business.v1.jaxws.GetObjectRequestType;
import com.agile.ws.schema.business.v1.jaxws.GetObjectResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileListEntryType;
import com.agile.ws.schema.common.v1.jaxws.AgileObjectType;
import com.agile.ws.schema.common.v1.jaxws.AgileRowType;
import com.agile.ws.schema.common.v1.jaxws.AgileTableType;
import com.agile.ws.schema.common.v1.jaxws.ObjectIdentifierType;
import com.agile.ws.schema.common.v1.jaxws.PropertyType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.common.v1.jaxws.SchemaConstants;
import com.agile.ws.schema.common.v1.jaxws.SelectionType;
import com.agile.ws.schema.common.v1.jaxws.TableIdentifierType;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectPortType;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectService;
import common.util.WSUtil;

import run.DataPrepare;
import run.RunAllSamples;


 /**
 * Sample      : GetObject
 * Category    : BusinessObject webservice
 *
 * Description : This sample demonstrates the usage of 'getObject' webservice
 * to retrieve a particular version of  an Agile Object using its version number
 * as a reference. The request object contains the specifications that identify 
 * the Agile object along with the option 'revision' which is specially
 * used in this case. The response contains message elements that represent
 * information about the Agile object retrieved. 
 */
public class GetObjectVersion {
    private static final String COMMAND_NAME = "GetObjectVersion";
    
	public static String clsName;
    public static String serviceName   =  "BusinessObject";       
    public static BusinessObjectPortType agileStub;        
    
    public static String partNumber;
    public static String partVersion;

    public GetObjectVersion() {
             clsName = this.getClass().getName();

    }
      

    /**
     * The method setupServerLogin is used to login to the server by providing
     * the server url details and authentication credentials.
     * @param args 
     */
    public static void setupServerLogin(String[] args) throws Exception {
        
    	//The BusinessObjectServiceLocator is used to obtain a BusinessObject_BindingStub
    	URL url = new URL(args[0] + "/" + serviceName + "?WSDL"); 
    	BusinessObjectService locator = new BusinessObjectService(url);

        agileStub = (BusinessObjectPortType)locator.getBusinessObject();


        // Username and password details are specified for the agileStub
        Map<String, Object> reqContext = ((BindingProvider)agileStub).getRequestContext();
        reqContext.put(BindingProvider.USERNAME_PROPERTY, args[1]);
        reqContext.put(BindingProvider.PASSWORD_PROPERTY, args[2]);
     }
    
public static void displayMessageElementValue(Element element){
        
        if(element.getFirstChild()!=null){                                          

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
            dataPrepare.setupCollaboration();
            dataPrepare.setupTable();
            
            partNumber = dataPrepare.createNewObject("Part");
            String changeNumber = dataPrepare.getNextAutoNumber("ECO");
            dataPrepare.createObjectByTags("ECO", new String[]{"number"}, new String[]{changeNumber} );
            dataPrepare.addRow("ECO", changeNumber, ChangeConstants.TABLE_AFFECTEDITEMS.toString(),
                               ChangeConstants.ATT_AFFECTED_ITEMS_ITEM_NUMBER.toString(), partNumber);
            partVersion = dataPrepare.changeStatusToReleased( changeNumber, partNumber);
            
            System.out.println("<<< DataPrepare successful >>>");
        }
        catch(Exception e){            
            System.out.println("<<< ALERT: DataPrepare failed. Your server settings may be invalid, this sample might not run successfully.");
            System.out.println("To run the sample without the assitance of prepared data, you may edit the static variables after");
            System.out.println("creating the necessary prerequisites through the Agile web client and then execute the sample >>>");
        }
        
        
    }    
        
    public static void main(String args[]){     
        
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
                 
          /** 
          *    This sample demonstrates the usage of the 'getObject' webservice
          *    to retrive a manufacturer part. This particular case involves
          *    the usage of webservice request options, which are name value 
          *    pairs expressed as 'PropertyType' objects. In this sample, the
          *    name of the manufactuer is specified by using an option while 
          *    the other getObject request elements are used as in the normal cases.
          */    
                  
         try {
             
             // Login to the server by providing the server url and authentication credentials.
             setupServerLogin(args);             

             
             // Create the request object GetObjectRequestType for the getObject operation
             // Create an array of requests of type AgileGetObjectRequest . Batch operations may be
             // performed by populating as many request objects as required to retrieve several  objects.
             GetObjectRequestType getObjectRequestType = new GetObjectRequestType();
             AgileGetObjectRequest agileGetObjectRequest[] = new AgileGetObjectRequest[1];             
             agileGetObjectRequest[0] = new AgileGetObjectRequest();
             
             // For each request, set the class Identifier to denote the type of object to be retrieved and
             // the object identifier to specify the object number of the object to be retrieved.
             agileGetObjectRequest[0].setClassIdentifier("Part");
             agileGetObjectRequest[0].setObjectNumber(partNumber);
             
             System.out.println("Retrieving object '" + partNumber + "', version: " + partVersion  + "...\n");             
             
             // Usage of options to specify special characteristics.
             // While retrieving manufacturer parts, the manufactuer name is expressed
             // as a name value pair as outlined here: 
             PropertyType propertyType[] =  new PropertyType[1];
             propertyType[0] = new PropertyType();
             propertyType[0].setPropertyName( SchemaConstants.REVISION.value());
             propertyType[0].setPropertyValue(partVersion);
             
             // Once the options are defined, the PropertyType object may be
             // stored as a part of the request object as follows:
             agileGetObjectRequest[0].getOptions().addAll(Arrays.asList(propertyType));             
                        
             // AgileDataTableRequestType is used to specifiy the tables pertaining to the Agile object
             // that will be retrieved by the 'getObject' webservice. The table identifier is used for this 
             // purpose. If the meta data is also required, then the boolean loadCellMetaData is set to 'true'
             AgileDataTableRequestType tableRequests[] = new AgileDataTableRequestType[1];
             tableRequests[0] = new AgileDataTableRequestType();
             tableRequests[0].setTableIdentifier( ItemConstants.TABLE_ATTACHMENTS.toString() );
             tableRequests[0].setLoadCellMetaData(false);
             agileGetObjectRequest[0].getTableRequests().addAll(Arrays.asList(tableRequests));                       
             
             // The request objects are set and the agile Stub is used to make the getObject
             // webservice call. The status code obtained from the response object is printed to
             // verify the success of the getObject operation.                          
             getObjectRequestType.getRequests().addAll(Arrays.asList(agileGetObjectRequest));
             GetObjectResponseType getObjectResponseType = agileStub.getObject(getObjectRequestType);                           
             System.out.println("STATUS CODE: " +  getObjectResponseType.getStatusCode() );
                          
             // If the status code is not 'SUCCESS', then populate the list of exceptions
             // returned by the webservice. 
             if( !getObjectResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){
               RunAllSamples.reportFailure( clsName );
                 
                 AgileExceptionListType[] agileExceptionListType = getObjectResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                 for(int i=0; i<agileExceptionListType.length; i++){
                     AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                     for(int j=0; j<exceptions.length; j++)
                         System.out.println(exceptions[j].getMessage() );
                 }
                 
             }
             else{ // If the object, was successfully retrieved, then examine its contents
                         
            	 AgileGetObjectResponse responses[] = getObjectResponseType.getResponses().toArray(new AgileGetObjectResponse[0]);
          		if(responses!=null)
                          for(int i=0; i<responses.length; i++)
          		{
          			// Obtain the agile object from the response
          			AgileObjectType agileObject = responses[i].getAgileObject();
                                  List<Element> messages =  agileObject.getAny();
                                  
          			ObjectIdentifierType object = agileObject.getObjectIdentifier();
                                  System.out.println( object.getObjectName() );
                              
                                  System.out.println("------------------------------------------------------------");
                                  System.out.println("Object Information: ");
                                  System.out.println("------------------------------------------------------------");
                                  for(int j=0; j<messages.size(); j++){
                                  	if(messages.get(j) instanceof Element){
                                  		Element el = (Element)messages.get(j);
                                  		System.out.print( el.getTagName() + "--> " );
                                  		 displayMessageElementValue(el);
                                  	}
                                      
                                     
                                  }
                                      
          			
          			
                                  // Obtain a list of tables from the agile object.
          			AgileTableType tables[] = agileObject.getTable().toArray(new AgileTableType[0]);
                                  if(tables!=null)
          			for(int j=0; j<tables.length; j++){
          				//Obtain a set of rows and print the values of the objects within these rows.
                                          TableIdentifierType tableId = tables[j].getTableIdentifier();                            
                                          System.out.println("------------------------------------------------------------");
          				System.out.println("Table type: " +  tableId.getTableDisplayName() );
                                          System.out.println("------------------------------------------------------------");
                                      
          				AgileRowType rows[] = tables[j].getRow().toArray(new AgileRowType[0]);
                                          if(rows!=null)
          				for(int k=0; k<rows.length; k++){		
                                              
                                                  messages =  rows[k].getAny();				    
          				    
                                                  if(messages!=null)
                                                  	 for(int jj=0; jj<messages.size(); jj++){
                                                           if(messages.get(jj) instanceof Element){
                                                       		Element el = (Element)messages.get(jj);
                                                       		System.out.print( el.getTagName() + "--> " );
                                                       		 displayMessageElementValue(el);
                                                       	}
                                                   }
          				
          				}					
          			}
                    System.out.println("------------------------------------------------------------");
                    

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
