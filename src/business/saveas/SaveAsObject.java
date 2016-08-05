package business.saveas;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;

import com.agile.api.ItemConstants;
import com.agile.ws.schema.business.v1.jaxws.AgileSaveAsObjectRequestType;
import com.agile.ws.schema.business.v1.jaxws.SaveAsObjectRequestType;
import com.agile.ws.schema.business.v1.jaxws.SaveAsObjectResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileRowType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.metadata.v1.jaxws.AgileGetAutoNumbersRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.AgileGetAutoNumbersResponseType;
import com.agile.ws.schema.metadata.v1.jaxws.GetAutoNumbersRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.GetAutoNumbersResponseType;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectPortType;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectService;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataPortType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataService;
import common.util.WSUtil;

import run.DataPrepare;
import run.RunAllSamples;



 /**
 * Sample      : SaveAsObject
 * Category    : BusinessObject webservice
 *
 * Description : This sample demonstrates the usage of 'SaveAsObject' webservice
 * to save a particular Agile object as another new object. The request object 
 *  contains specifications that identify the parent Agile object. 
 */
public class SaveAsObject {
    private static final String COMMAND_NAME = "SaveAsObject";
    
	public static String clsName;
    public static String serviceName   =  "BusinessObject";       
    public static String serviceName2   =  "AdminMetadata";
    
    public static BusinessObjectPortType agileStub;        
    public static AdminMetadataPortType agileStub_AdminMetadata;        
    
    public static String partNumber1;
    public static String partNumber2;
    public static String newPartNumber;

    public SaveAsObject() {
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
    

    
    public static void setupServerLogin_Metadata(String[] args) throws Exception {
         
    	 //The AdminMetadataServiceLocator is used to obtain a AdminMetadata_BindingStub
    	URL url = new URL(args[0] + "/" + serviceName2 + "?WSDL"); 
    	AdminMetadataService locator = 
             new AdminMetadataService(url);

        //A stub of type AdminMetadata_BindingStub is obtained by specifying the server URL
        //Eventually, the convertCurrency webservice will be invoked by using this agileStub.
    	agileStub_AdminMetadata = locator.getAdminMetadata();
                


        // Username and password details are specified for the agileStub
          Map<String, Object> reqContext = ((BindingProvider)agileStub_AdminMetadata).getRequestContext();
          reqContext.put(BindingProvider.USERNAME_PROPERTY, args[1]);
          reqContext.put(BindingProvider.PASSWORD_PROPERTY, args[2]);
     }
    
    public static String getAutoNumberSource(String[] args) throws Exception{
        
        setupServerLogin_Metadata(args);
        String autonumber = null;
        
        GetAutoNumbersRequestType getAutoNumbersRequestType = new GetAutoNumbersRequestType();
        AgileGetAutoNumbersRequestType agileGetAutoNumbersRequestType[] = new AgileGetAutoNumbersRequestType[1];
        agileGetAutoNumbersRequestType[0] = new AgileGetAutoNumbersRequestType();
        
        agileGetAutoNumbersRequestType[0].setClassIdentifier("Part");
        agileGetAutoNumbersRequestType[0].setIncludeAllAutoNumberSource(true);
        agileGetAutoNumbersRequestType[0].setSize(1);
        
        getAutoNumbersRequestType.getRequests().addAll(Arrays.asList(agileGetAutoNumbersRequestType));
        
        GetAutoNumbersResponseType getAutoNumbersResponseType = agileStub_AdminMetadata.getAutoNumbers(getAutoNumbersRequestType);
        if( !getAutoNumbersResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

            
            AgileExceptionListType[] agileExceptionListType = getAutoNumbersResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
            if(agileExceptionListType!=null)
            for(int i=0; i<agileExceptionListType.length; i++){
                AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                for(int j=0; j<exceptions.length; j++)
                    System.out.println(exceptions[j].getMessage() );
            }
            System.out.println("Failed to obtain an autonumber.");
            
        }
        else{
            AgileGetAutoNumbersResponseType responses[] = getAutoNumbersResponseType.getAutoNumberResponses().toArray(new AgileGetAutoNumbersResponseType[0]);            
            autonumber = responses[0].getAutoNumbers().get(0).getDisplayName();
            System.out.println("Autonumber obtained: " + autonumber);
        }
        
        return autonumber;
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
            
            partNumber1 = dataPrepare.createNewObject("Part");
            partNumber2 = dataPrepare.createNewObject("Part");
            newPartNumber = dataPrepare.getNextAutoNumber("Part");
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
            
            // Create the request object SaveAsObjectRequestType for the getObject operation
            // Create an array of requests of type AgileSaveAsObjectRequestType . Batch operations may be
            // performed by populating as many request objects as required to save several objects at once
            SaveAsObjectRequestType saveAsObjectRequestType = new SaveAsObjectRequestType();
            AgileSaveAsObjectRequestType agileSaveAsObjectRequestType[] = new AgileSaveAsObjectRequestType[2];             
            
            for(int i=0; i< agileSaveAsObjectRequestType.length; i++)
                agileSaveAsObjectRequestType[i] = new AgileSaveAsObjectRequestType();
            
            
            // For each of the requests, set the class identifier to specify the
            // type of object that will be saved and an object number to identify
            // the unique object and the class type of the new object
            agileSaveAsObjectRequestType[0].setParentClassIdentifier("Part");
            agileSaveAsObjectRequestType[0].setParentObjectNumber(partNumber1);
            agileSaveAsObjectRequestType[0].setNewClassIdentifier("Part");
            
            agileSaveAsObjectRequestType[1].setParentClassIdentifier("Part");
            agileSaveAsObjectRequestType[1].setParentObjectNumber(partNumber2);
            agileSaveAsObjectRequestType[1].setNewClassIdentifier("Part");
            
            System.out.println("Saving part '" + partNumber1 +  "' as '" + newPartNumber + "' using a message element to specify the new part");
            System.out.println("and saving part '" + partNumber2 +  "' using an autonumber source to specify the new part....\n");            
            
            // 1.  Using the element xsd:any to specify the new Object Number:
            // The object number of the new object can be specified through the 
            // xsd:any attribute of an AgileRowType by using MessageElements as shown:
            AgileRowType row = new AgileRowType();             

            Element element = WSUtil.createMessageElement("Message_Num");
            element.setAttribute("attributeId", ItemConstants.ATT_TITLE_BLOCK_NUMBER.toString());
            element.setTextContent(newPartNumber);
            
            row.getAny().add(element);                
            // The new object number information in this row is stored in the
            // request object using the setData method.
            agileSaveAsObjectRequestType[0].setData(row);             
            
            // 2.  Using an auto number source to specify the new Object Number:
            // The autonumber source is retrieved from a webservice call to the
            // the service 'getAutoNumber'            
            agileSaveAsObjectRequestType[1].setAutoNumberSource( getAutoNumberSource(args) );


            // The request objects are set and the agile Stub is used to make the SaveAsObject
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the saveAsObject operation.             
            saveAsObjectRequestType.getSaveAsObjectRequest().addAll(Arrays.asList(agileSaveAsObjectRequestType));
            SaveAsObjectResponseType saveAsObjectResponseType = agileStub.saveAsObject(saveAsObjectRequestType);            
            System.out.println("STATUS CODE: " +  saveAsObjectResponseType.getStatusCode() );
                         
                         
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !saveAsObjectResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = saveAsObjectResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
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
