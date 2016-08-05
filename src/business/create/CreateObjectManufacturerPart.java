package business.create;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;

import com.agile.ws.schema.business.v1.jaxws.AgileCreateObjectRequest;
import com.agile.ws.schema.business.v1.jaxws.CreateObjectRequestType;
import com.agile.ws.schema.business.v1.jaxws.CreateObjectResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileRowType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectPortType;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectService;
import common.util.WSUtil;

import run.DataPrepare;
import run.RunAllSamples;


/**
* Sample      : CreateObjectManufacturerPart
* Category    : BusinessObject webservice
*
* Description : This sample demonstrates the creation of a manufacturer part
* using the 'createObject' webservice by using appropriate message elements
* to specify the mandatory fields required for creating a manufacturer part
* in Agile PLM.  
*/
public class CreateObjectManufacturerPart {
    private static final String COMMAND_NAME = "CreateObjectManufacturerPart";
    
	public static String clsName;
    public static String serviceName   =  "BusinessObject";       
    public static BusinessObjectPortType agileStub;    
    
    public static String manufPartNumber;
    public static String manufName;

    public CreateObjectManufacturerPart() {
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
            
            manufName = "MANUF" + System.currentTimeMillis();
            dataPrepare.createObjectByTags("Manufacturer", new String[]{"name"}, new String[]{manufName} );
            manufPartNumber = "MANUF_PART" + System.currentTimeMillis();
            System.out.println("<<< DataPrepare successful >>>");
        }
        catch(Exception e){            
            System.out.println("<<< ALERT: DataPrepare failed. Your server settings may be invalid, this sample might not run successfully.");
            System.out.println("To run the sample without the assitance of prepared data, you may edit the static variables after");
            System.out.println("creating the necessary prerequisites through the Agile web client and then execute the sample >>>");
        }
        
        
    }


    /**
     *  Run this main method for running the create object sample code
     * @param args
     */
    
    public static void main(String[] args) {
        
        checkArguments(args); 

        // Comment this method out if you intend to use your own data
        // or scenario by editing the static variables at the top of this code
        prepareData(args);

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");        
         
         /** 
          *    This sample demonstrates the creation of a manufacturer part          
          */

        
         try {
             
             // Login to the server by providing the server url and authentication credentials.
             setupServerLogin(args);             

             
             // Create the request object CreateObjectRequestType for the createObject operation
             // Create an array of requests of type AgileCreateObjectRequest. Batch operations may be
             // performed by populating as many request objects as required to create several new objects.
             CreateObjectRequestType createObjectRequestType = new CreateObjectRequestType();                         
             AgileCreateObjectRequest agileCreateObjectRequest[] = new AgileCreateObjectRequest[1];             
             for(int i=0; i<agileCreateObjectRequest.length; i++)             
                agileCreateObjectRequest[i] = new AgileCreateObjectRequest();           

             
             // Specify the type of object to be created in each of the request objects.
             agileCreateObjectRequest[0].setClassIdentifier("ManufacturerPart");                          
             System.out.println("Creating a manufacturer part '" + manufPartNumber  + "'...\n");       

             // Create a row of type AgileRowType to set the data for the request objects.
             // Create an array of message elements to specify various attributes for the new object.
             AgileRowType row_1 = new AgileRowType();                      
             String namespaceUri = null;                         
             
             // 'manufacturerPartNumber', 'Description' and 'manufacturerName'
             // are the elements required for creating a manufacturer part.
             
             //manufacturerPartNumber  
             Element el_mfnnumber = WSUtil.createMessageElement("manufacturerPartNumber");
             el_mfnnumber.setTextContent(manufPartNumber);
             
             
             //Description
             Element el_desc = WSUtil.createMessageElement("description");
             el_desc.setTextContent("Manufacturer Part Desc");                     
             
             //Manufacturer name
             Element el_mfn = WSUtil.createMessageElement("manufacturerName");
             el_mfn.setTextContent(manufName);                     
             
             
             // The Agile row is updated with the content of these message elements and 
             // each request object is updated with the values of their respective Agile rows.
             row_1.getAny().add(el_mfnnumber);  
             row_1.getAny().add(el_desc); 
             row_1.getAny().add(el_mfn); 
             agileCreateObjectRequest[0].setData(row_1);             


             // The request objects are set and the agile Stub is used to make the createObject
             // webservice call. The status code obtained from the response object is printed to
             // verify the success of the createObject operation. 
             createObjectRequestType.getRequests().addAll(Arrays.asList(agileCreateObjectRequest));             
             CreateObjectResponseType createObjectResponseType = agileStub.createObject(createObjectRequestType);
             System.out.println("STATUS CODE: " +  createObjectResponseType.getStatusCode() );
             
             // If the status code is not 'SUCCESS', then populate the list of exceptions
             // returned by the webservice. 
             if( !createObjectResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                 
                 AgileExceptionListType[] agileExceptionListType = createObjectResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
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
