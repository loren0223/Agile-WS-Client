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
import com.agile.ws.schema.common.v1.jaxws.ObjectReferentIdType;
import com.agile.ws.schema.common.v1.jaxws.PropertyType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.common.v1.jaxws.SchemaConstants;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectPortType;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectService;
import common.util.WSUtil;

import run.DataPrepare;
import run.RunAllSamples;

/**
* Sample      : CreateObjectPublishedPriceonMPN
* Category    : BusinessObject webservice
*
* Description : This sample demonstrates the creation of a published price 
* on a manufacturer part using the 'createObject' webservice. Appropriate message
* elements are used to specify the mandatory fields required for creating a 
* published price in Agile PLM.  
*/
public class CreateObjectPublishedPriceonMPN {
    private static final String COMMAND_NAME = "CreateObjectPublishedPriceonMPN";
    
	public static String clsName;
    public static String serviceName   =  "BusinessObject";       
    public static BusinessObjectPortType agileStub;        
    
    public static String manufPartNumber;
    public static String manufName;
    public static String publishedPriceNumber;

    public CreateObjectPublishedPriceonMPN() {
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
            manufPartNumber = "MANUF_PART" + System.currentTimeMillis();
            publishedPriceNumber = dataPrepare.getNextAutoNumber("PublishedPrice");
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
          *    This sample demonstrates creation of a a published price on MPN
          *    by first creating a manufactuer and manufactuer part and then
          *    creating a published price on the same.
          */

        
         try {
             
             // Login to the server by providing the server url and authentication credentials.
             setupServerLogin(args);             

             
             // First we create the Manufacturer and Manufactuer Part on which a published price
             // will be eventually created.
             
             // Create the request object CreateObjectRequestType for the createObject operation
             // Create an array of requests of type AgileCreateObjectRequest. Batch operations may be
             // performed by populating as many request objects as required to create several new objects.
             CreateObjectRequestType createObjectRequestType = new CreateObjectRequestType();                         
             AgileCreateObjectRequest agileCreateObjectRequest[] = new AgileCreateObjectRequest[2];             
             for(int i=0; i<agileCreateObjectRequest.length; i++)             
                agileCreateObjectRequest[i] = new AgileCreateObjectRequest();           
             
             // Specify the type of object to be created in each of the request objects.
             agileCreateObjectRequest[0].setClassIdentifier("Manufacturer");
             agileCreateObjectRequest[1].setClassIdentifier("ManufacturerPart");             
             System.out.println("Creating a manufacturer and manufactuer part...");

             // Create a row of type AgileRowType to set the data for the request objects.
             // Create an array of message elements to specify various attributes for the new object.
             AgileRowType row_1 = new AgileRowType();                    
             String namespaceUri = null;  
             final String COMMONNAMESPACEURI = "http://xmlns.oracle.com/AgileObjects/Core/Common/V1";             

             String manufactuerName = manufName;           

             Element el_name = WSUtil.createMessageElement("name");
             el_name.setTextContent(manufactuerName);
             // The Agile row is updated with the content of these message elements and 
             // each request object is updated with the values of their respective Agile rows.
             row_1.getAny().add(el_name);                        
             agileCreateObjectRequest[0].setData(row_1);             
             

             //Similarly, the Manufactuer Part's request object is also populated with data
             AgileRowType row_2 = new AgileRowType();             
             String manufacturerPartName = manufPartNumber;             
             
             Element el_mfnNumber = WSUtil.createMessageElement("manufacturerPartNumber");
             el_mfnNumber.setTextContent(manufacturerPartName);
             Element el_mfnName = WSUtil.createMessageElement("manufacturerName");
             el_mfnName.setTextContent(manufactuerName);
             
             row_2.getAny().add(el_mfnNumber);   
             row_2.getAny().add(el_mfnName);  
             agileCreateObjectRequest[1].setData(row_2);                         

             // The request objects are set and the agile Stub is used to make the createObject
             // webservice call. The status code obtained from the response object is printed to
             // verify the success of the createObject operation. 
             createObjectRequestType.getRequests().addAll(Arrays.asList(agileCreateObjectRequest));             
             CreateObjectResponseType createObjectResponseType = agileStub.createObject(createObjectRequestType);
             System.out.println("STATUS CODE: " + createObjectResponseType.getStatusCode() );
             
             // If the status code is not 'SUCCESS', then populate the list of exceptions
             // returned by the webservice. 
             if( !createObjectResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                 
                 AgileExceptionListType[] agileExceptionListType = createObjectResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                 for(int i=0; i<agileExceptionListType.length; i++){
                     AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                     for(int j=0; j<exceptions.length; j++)
                         System.out.println(exceptions[j].getMessage() );
                 }
                 
             }
                          
             
             
             // Creation of a Published price on MPN
             // Using the manufactuer and manufactuer part now created, we create
             // a published price on the same
             
             createObjectRequestType = new CreateObjectRequestType();                         
             AgileCreateObjectRequest agileCreateObjectRequest2[] = new AgileCreateObjectRequest[1];             
             agileCreateObjectRequest2[0] = new AgileCreateObjectRequest();           
             
             agileCreateObjectRequest2[0].setClassIdentifier("PublishedPrice");
             
             row_1 = new AgileRowType();                                      

            // Formulate the message element that specifies the object number
            // of the published price which shall be created by the webservice            
             System.out.println("\nCreating a published price ' " + publishedPriceNumber  + "'...");
             Element el_number = WSUtil.createMessageElement("number");
             el_number.setTextContent(publishedPriceNumber);
             // For specifying a manufactuer part through a MessageElement in the
             // this operation, the message element cannot be specified in the
             // usual manner. In this case, the message element for the manufactuer part
             // must be of type 'ObjectReferentIdType'. Consequently, an object identifier
             // type object is created and appropriate class and object identifier values
             // are set, using the manufacturer part class and its number respectively.               
             
    
             ObjectReferentIdType objRefId = new ObjectReferentIdType();                          
             objRefId.setClassIdentifier("1488");            
             objRefId.setObjectIdentifier(manufacturerPartName );
             
             // The Manufacturer name must also be specified to identify a manufactuer part.
             // This is achieved by using the 'setOptions' feature available for an 
             // ObjectReferentIdType. A name value property pair is used to set the 
             // manufactuer name as shown:
             
             PropertyType[] properties = new PropertyType[1];
             properties[0] = new PropertyType();
             properties[0].setPropertyName(SchemaConstants.MANUFACTURER_NAME.value());
             properties[0].setPropertyValue(manufactuerName);                
             objRefId.getOptions().addAll(Arrays.asList(properties));                         

             Element el_mfrPartNumber = WSUtil.createMessageElement("mfrPartNumber");
             el_mfrPartNumber = WSUtil.marshal(el_mfrPartNumber, objRefId);
             
             // The message element for 'Supplier' is also of type ObjectReferentIdType             
             // Therefore, we construct a similar message element to add the 
             // supplier specifications
                      
             objRefId = new ObjectReferentIdType();                          
             objRefId.setClassIdentifier("17745");            
             objRefId.setObjectIdentifier("AVNET");
   
             
             Element el_supplier = WSUtil.createMessageElement("supplier");
             el_supplier = WSUtil.marshal(el_supplier, objRefId);

             row_1.getAny().add(el_number);      
             row_1.getAny().add(el_mfrPartNumber);
             row_1.getAny().add(el_supplier);
             agileCreateObjectRequest2[0].setData(row_1);             
             createObjectRequestType.getRequests().addAll(Arrays.asList(agileCreateObjectRequest2));             
             CreateObjectResponseType createObjectResponseType2 = agileStub.createObject(createObjectRequestType);
             System.out.println("STATUS CODE: " +  createObjectResponseType2.getStatusCode() );           

             if( !createObjectResponseType2.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                 
                 AgileExceptionListType[] agileExceptionListType = createObjectResponseType2.getExceptions().toArray(new AgileExceptionListType[0]);
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
