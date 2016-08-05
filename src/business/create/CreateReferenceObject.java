package business.create;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;

import com.agile.ws.schema.business.v1.jaxws.AgileCreateObjectRequest;
import com.agile.ws.schema.business.v1.jaxws.CreateObjectRequestType;
import com.agile.ws.schema.business.v1.jaxws.CreateObjectResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileRowType;
import run.RunAllSamples;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectPortType;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectService;
import common.util.WSUtil;


 /**
 * Sample      : CreateReferenceObject
 * Category    : BusinessObject webservice
 *
 * Description : This sample demonstrates the creation of an Agile Reference object
 * by using API names to detail the mandatory specifications.  
 * 
 * Note:- before running this case, you must follow the reference object readme to 
 * do the admin configuration.
 */
public class CreateReferenceObject {
    private static final String COMMAND_NAME = "CreateReferenceObject";
    
	public static String clsName;
    public static String serviceName   =  "BusinessObject";       
    public static BusinessObjectPortType agileStub;        
    
    public static String refSubclassName = "XxReferenceobject";

    public CreateReferenceObject() {
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
        
        // This sample may be configured by either directly modifying the string values of
        // 'SERVER_URL', 'USERNAME' and 'PASSWORD' (defined in this class), or by
        // passing them as program arguments in the same order. In this sample, the
        // method 'checkArguments' checks for these values.
        checkArguments(args);        

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");
         
         
         /** 
          *    This sample demonstrates creation of new objects using batch
          *    operations with the CreateObject webservice while specifying their
          *    attributes using approproate API names.
          */

        
         try {
             
             // Login to the server by providing the server url and authentication credentials.
             setupServerLogin(args);             

             
             // Create the request object CreateObjectRequestType for the createObject operation
             // Create an array of requests of type AgileCreateObjectRequest. Batch operations may be
             // performed by populating as many request objects as required to create several new objects.
             CreateObjectRequestType createObjectRequestType = new CreateObjectRequestType();                         
             AgileCreateObjectRequest agileCreateObjectRequest = new AgileCreateObjectRequest();                     
             
             agileCreateObjectRequest.setClassIdentifier(refSubclassName);
             AgileRowType row_1 = new AgileRowType();            
             String objectNumber = "REF"+System.currentTimeMillis();
             
             Element name = WSUtil.createMessageElement("Name");
             name.setTextContent( objectNumber );
             Element description = WSUtil.createMessageElement("Description");
             description.setTextContent( "Object Desc" );
             Element roKey = WSUtil.createMessageElement("referencedObjKey");
             //create randomly generated publicKey
             String refPubKey = new Integer(new Random().nextInt(100)).toString()+"|"+ new Integer(new Random().nextInt(100)).toString();
             roKey.setTextContent(refPubKey);
             
             row_1.getAny().add(name);
             row_1.getAny().add(description);
             row_1.getAny().add(roKey);
             agileCreateObjectRequest.setData(row_1);       
             createObjectRequestType.getRequests().add(agileCreateObjectRequest);      
             
             CreateObjectResponseType createObjectResponseType = agileStub.createObject(createObjectRequestType);
             System.out.println("STATUS CODE: " +  createObjectResponseType.getStatusCode() );
                          
             // If the status code is not 'SUCCESS', then populate the list of exceptions
             // returned by the webservice. 
             if( !createObjectResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                
                List<AgileExceptionListType> agileExceptionListType = createObjectResponseType.getExceptions();
                for(int i=0; i<agileExceptionListType.size(); i++){
                    List<AgileExceptionType> exceptions = agileExceptionListType.get(i).getException();
                    for(AgileExceptionType exception:exceptions)
                        System.out.println(exception.getMessage() );
                }
                 
             }else
               System.out.println("Reference Object '"+ objectNumber + "' with public key '" + refPubKey + "' was successfully created" );

        }
        catch (Exception e) {
			RunAllSamples.reportFailure(clsName);
			System.out.println("Exceptions: ");
			e.printStackTrace();
		} 
     }
}