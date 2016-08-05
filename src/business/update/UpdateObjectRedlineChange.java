package business.update;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;

import com.agile.api.ChangeConstants;
import com.agile.api.ItemConstants;
import com.agile.ws.schema.business.v1.jaxws.AgileUpdateObjectRequest;
import com.agile.ws.schema.business.v1.jaxws.UpdateObjectRequestType;
import com.agile.ws.schema.business.v1.jaxws.UpdateObjectResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileRowType;
import com.agile.ws.schema.common.v1.jaxws.PropertyType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.common.v1.jaxws.SchemaConstants;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectPortType;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectService;
import common.util.WSUtil;

import run.DataPrepare;
import run.RunAllSamples;


 /**
 * Sample      : UpdateObjectRedlineChange
 * Category    : BusinessObject webservice
 *
 * Description : This sample demonstrates the usage of 'updateObject' webservice
 * for a specific case where a particular version of an Agile object is to be 
 * updated. The request object contains specifications identifying the object
 * which needs to be updated along with the option 'redline_change'.
 */
public class UpdateObjectRedlineChange {
    private static final String COMMAND_NAME = "UpdateObjectRedlineChange";
    
	public static String clsName;
    public static String serviceName   =  "BusinessObject";       
    public static BusinessObjectPortType agileStub;         
    
    public static String partNumber;
    public static String changeNumber;    

    public UpdateObjectRedlineChange() {
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
            
            partNumber = dataPrepare.createNewObject("Part");
            changeNumber = dataPrepare.getNextAutoNumber("ECO");
            dataPrepare.createObjectByTags("ECO", new String[]{"number"}, new String[]{changeNumber} );
            dataPrepare.addRow("ECO", changeNumber, ChangeConstants.TABLE_AFFECTEDITEMS.toString(),
                               ChangeConstants.ATT_AFFECTED_ITEMS_ITEM_NUMBER.toString(), partNumber);            
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
            
            setupServerLogin(args);
            
            /**
             * In this sample we use the option 'redline_change' to update a specific 
             * version of a Part object whose change has not yet been released. The 
             * description of the part is changed in this case.
             */            
            
            // Create the request object UpdateObjectRequestType for the getObject operation
            // Create an array of requests of type AgileUpdateObjectRequest . Batch operations may be
            // performed by populating as many request objects as required to update several  objects.
            UpdateObjectRequestType updateObjectRequestType = new UpdateObjectRequestType();
            AgileUpdateObjectRequest agileUpdateObjectRequest[] = new AgileUpdateObjectRequest[1];             
            agileUpdateObjectRequest[0] = new AgileUpdateObjectRequest();
            
            // For each of the requests, set the class identifier to specify the
            // type of object that will be updated and an object number to identify
            // the unique object.
            agileUpdateObjectRequest[0].setClassIdentifier("Part");
            agileUpdateObjectRequest[0].setObjectNumber(partNumber);
            
            System.out.println("Updating part '" + partNumber + "' with the pending change '" + changeNumber + "'...\n");
            
            // The actual data to be updated is specified in the form of message elements
            // which are set into the _any field of AgileRowType elements.
            AgileRowType rows = new AgileRowType();
            String namespaceUri = null;                         
            
            // Based on this message element, the Description field of the specified
            // agile object will be updated with a new value.

            Element element = WSUtil.createMessageElement("Message_Desc");
            element.setAttribute("attributeId", ItemConstants.ATT_TITLE_BLOCK_DESCRIPTION.toString());
            element.setTextContent("Updated value of Doc Description");
            
            // The data field of the request object is updated with the new row data
            rows.getAny().add(element);            
            agileUpdateObjectRequest[0].setData(rows);
            
            // Usage of options to specify special characteristics.
            // While retrieving manufacturer parts, the manufactuer name is expressed
            // as a name value pair as outlined here: 
            PropertyType propertyType[] =  new PropertyType[1];
            propertyType[0] = new PropertyType();
            propertyType[0].setPropertyName( SchemaConstants.REDLINE_CHANGE.value());
            propertyType[0].setPropertyValue(changeNumber);
            
            // Once the options are defined, the PropertyType object may be
            // stored as a part of the request object as follows:
            agileUpdateObjectRequest[0].getOptions().addAll(Arrays.asList(propertyType));                         
            
            // The request objects are set and the agile Stub is used to make the updateObject
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the updateObject operation.             
            updateObjectRequestType.getRequests().addAll(Arrays.asList(agileUpdateObjectRequest));
            UpdateObjectResponseType updateObjectResponseType = agileStub.updateObject(updateObjectRequestType);
            
            System.out.println("STATUS CODE: " +  updateObjectResponseType.getStatusCode() );
                         
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !updateObjectResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = updateObjectResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
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
