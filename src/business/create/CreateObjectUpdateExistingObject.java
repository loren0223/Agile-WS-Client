package business.create;

import com.agile.ws.schema.business.v1.jaxws.AgileCreateObjectRequest;
import com.agile.ws.schema.business.v1.jaxws.CreateObjectRequestType;
import com.agile.ws.schema.business.v1.jaxws.CreateObjectResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileRowType;
import com.agile.ws.schema.common.v1.jaxws.PropertyType;
import com.agile.ws.schema.common.v1.jaxws.SchemaConstants;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectService;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectPortType;

import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;


import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import common.util.WSUtil;
import run.RunAllSamples;

/**
* Sample      : CreateObjectUpdateExistingObject
* Category    : BusinessObject webservice
*
* Description : This sample demonstrates the usage of the 'createObject' webservice
* for a specific case where the webservice must not fail if the object to be created
* already exists, but instead updates the existing object with the data currently 
* specified. This may prove to be advantageous when batch requests are used to 
* create several objects with a single webservice call with the possiblility of 
* some pre-existing objects included in the list. 
* 
* The option tag 'updateExistingObject' is used from the schema constants to
* achieve the same.
*/
public class CreateObjectUpdateExistingObject {
    private static final String COMMAND_NAME = "CreateObjectUpdateExistingObject";
    
	public static String clsName;
    public static String serviceName   =  "BusinessObject";       
    public static BusinessObjectPortType agileStub;        
    
    public static String partNumber = "TP00001";

    public CreateObjectUpdateExistingObject() {
             clsName = this.getClass().getName();

    }
      


    /**
     * The method setupServerLogin is used to login to the server by providing
     * the server url details and authentication credentials.
     * @param args 
     */
    public static void setupServerLogin(String[] args) throws Exception {
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
 
    /**
     *  Run this main method for running the create object sample code
     * @param args
     */

    public static void main(String[] args) {

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");
         
         
         /** 
          *    This sample demonstrates creation of a new object using an Agile
          *    object number that already exists. In such a case, we make use of  
          *    the option 'updateExistingObject' to ensure that instead of issuing
          *    a failure, the webservice updates the object that already exists
          *    with the new information specified by the message elements.
          */

        checkArguments(args);
         try {
             
             // Login to the server by providing the server url and authentication credentials.
             setupServerLogin(args);             

             
             // Create the request object CreateObjectRequestType for the createObject operation
             // Create an array of requests of type AgileCreateObjectRequest. Batch operations may be
             // performed by populating as many request objects as required to create several new objects.
             CreateObjectRequestType createObjectRequestType = new CreateObjectRequestType();                         
             AgileCreateObjectRequest agileCreateObjectRequest = new AgileCreateObjectRequest();             
             
             // Specify the type of object to be created in each of the request objects.
             agileCreateObjectRequest.setClassIdentifier("Part");                          

             // Create a row of type AgileRowType to set the data for the request objects.
             // Create an array of message elements to specify various attributes for the new object.
             AgileRowType row_1 = new AgileRowType();             
             System.out.println("Updating object '" + partNumber + "' through createObject WS by using");
             System.out.println("the special option 'updateExistingObject'. This is applicable");
             System.out.println("while trying to create an object that already exists.\n");
             
             // The API name "Number" is used to identify that this message pertains to the 
             // object number for the new object. A textNode is then added with the actual
             // value of the object number. Similarly, the "Description" for the new object
             // is specified.
             Element number = WSUtil.createMessageElement("number");
             number.setTextContent(partNumber);
             row_1.getAny().add(number);
             
             Element description = WSUtil.createMessageElement("description");
             description.setTextContent("Desc updated through special options");
             row_1.getAny().add(description);
             
             // Set the option 'updateExistingObject' to ensure that if the specified object
             // already exists, then the webservice would still not return a failure.
             // Instead, the object that already exists is updated with the information
             // provided in these message elements. Properties are defined as name value
             // pairs of objects of type PropertyType. The property names may be 
             // obtained from the constants listed in the file 'SchemaConstants'             

             PropertyType properties = new PropertyType();
             properties.setPropertyName( SchemaConstants.UPDATE_EXISTING_OBJECT.value() );
             properties.setPropertyValue("true");
             agileCreateObjectRequest.getOptions().add(properties);
             
             agileCreateObjectRequest.setData(row_1);                          

             // The request objects are set and the agile Stub is used to make the createObject
             // webservice call. The status code obtained from the response object is printed to
             // verify the success of the createObject operation. 
             createObjectRequestType.getRequests().add(agileCreateObjectRequest);             
             CreateObjectResponseType createObjectResponseType = agileStub.createObject(createObjectRequestType);
             System.out.println("STATUS CODE: " +  createObjectResponseType.getStatusCode() );
             
             
             // If the status code is not 'SUCCESS', then populate the list of exceptions
             // returned by the webservice. 
             if( !createObjectResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                 List<AgileExceptionListType> agileExceptionListType = createObjectResponseType.getExceptions();
                 for(int i=0; i<agileExceptionListType.size(); i++){
                     List<AgileExceptionType> exceptions = agileExceptionListType.get(i).getException();
                     for(AgileExceptionType exception:exceptions)
                         System.out.println(exception.getMessage() );
                 }
                 
             }
             
            }
            catch (Exception e) {            
             System.out.println("Exceptions: ");
             e.printStackTrace();
            } 
     }
}
