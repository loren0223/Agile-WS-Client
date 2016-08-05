package business.update;

import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;

import com.agile.api.ReferenceObjectConstants;
import com.agile.ws.schema.business.v1.jaxws.AgileUpdateObjectRequest;
import com.agile.ws.schema.business.v1.jaxws.UpdateObjectRequestType;
import com.agile.ws.schema.business.v1.jaxws.UpdateObjectResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileRowType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningListType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectPortType;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectService;
import common.util.WSUtil;

import run.DataPrepare;
import run.RunAllSamples;



 /**
 * Sample      : UpdateReferenceObject
 * Category    : BusinessObject webservice
 *
 * Description : This sample demonstrates the usage of 'updateObject' webservice
 * using which the particulars of an Agile object may be updated in their detail.
 * The request object contains specifications identifying the object which needs
 * to be updated.
 */
public class UpdateReferenceObject {
    private static final String COMMAND_NAME = "UpdateReferenceObject";
    
	public static String clsName;
    public static String serviceName   =  "BusinessObject";       
    public static BusinessObjectPortType agileStub;        
    
    public static String refSubclassName = "XxReferenceobject";
    public static String refObjPubKey;
    public static String refObjName;

    public UpdateReferenceObject() {
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

    public static void prepareData(String[] args){
        
        try{
            System.out.println("<<< Preparing Data... >>>");
            DataPrepare dataPrepare = new DataPrepare(args[0], args[1], args[2]);
            dataPrepare.setupAdminMetadata(); 
            dataPrepare.setupBusiness();
            
            String[] objData = dataPrepare.createNewRefObject(refSubclassName);
            refObjName = objData[0];
            refObjPubKey = objData[1]; 
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
            
            // Create the request object UpdateObjectRequestType for the getObject operation
            // Create an array of requests of type AgileUpdateObjectRequest . Batch operations may be
            // performed by populating as many request objects as required to update several  objects.
            UpdateObjectRequestType updateObjectRequestType = new UpdateObjectRequestType();
            AgileUpdateObjectRequest agileUpdateObjectRequest = new AgileUpdateObjectRequest();             
            
            // For each of the requests, set the class identifier to specify the
            // type of object that will be updated and an object number to identify
            // the unique object.
            agileUpdateObjectRequest.setClassIdentifier(refSubclassName);
            agileUpdateObjectRequest.setReferencedObjKey(refObjPubKey); //optional
            agileUpdateObjectRequest.setObjectNumber(refObjName); //ignored if refObjPubKey is provided
            
            System.out.println("Updating part '" + refObjName + "'...\n");
            
            // The actual data to be updated is specified in the form of elements
            // which are set into the _any field of AgileRowType elements.
            AgileRowType row = new AgileRowType();
            
            // Based on this message element, the Description field of the specified
            // agile object will be updated with a new value.
            Element messageName = WSUtil.createMessageElement("Message_Name");
            messageName.setAttribute("attributeId", ReferenceObjectConstants.ATT_GENERAL_INFO_NUMBER.toString());             
            messageName.setTextContent(refObjName+"Updated");
            
            Element messageDesc = WSUtil.createMessageElement("Message_Desc");
            messageDesc.setAttribute("attributeId", ReferenceObjectConstants.ATT_GENERAL_INFO_DESCRIPTION.toString());             
            messageDesc.setTextContent("Updated value of Ref Obj Description");   
            
            Element messageStatus = WSUtil.createMessageElement("Message_Status");
            messageStatus.setAttribute("attributeId", ReferenceObjectConstants.ATT_GENERAL_INFO_STATUS.toString());             
            messageStatus.setTextContent("Updated value of Ref Obj Status");
            
            // The data field of the request object is updated with the new row data
            row.getAny().add(messageName);
            row.getAny().add(messageDesc);
            row.getAny().add(messageStatus);
            agileUpdateObjectRequest.setData(row);
            
            // The request objects are set and the agile Stub is used to make the updateObject
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the updateObject operation.             
            updateObjectRequestType.getRequests().add(agileUpdateObjectRequest);
            UpdateObjectResponseType updateObjectResponseType = agileStub.updateObject(updateObjectRequestType);
            
            System.out.println("STATUS CODE: " +  updateObjectResponseType.getStatusCode() );
                         
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !updateObjectResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                
                List<AgileExceptionListType> agileExceptionListType = updateObjectResponseType.getExceptions();
                for(int i=0; i<agileExceptionListType.size(); i++){
                    List<AgileExceptionType> exceptions = agileExceptionListType.get(i).getException();
                    for(AgileExceptionType exception:exceptions)
                        System.out.println(exception.getMessage() );
                }
                
                List<AgileWarningListType> lstAgileWarningListType = updateObjectResponseType.getWarnings();
                if(lstAgileWarningListType!=null)
                for(AgileWarningListType agileWarningListType : lstAgileWarningListType){
                    List<AgileWarningType> warnings = agileWarningListType.getWarning();
                    for(AgileWarningType warning : warnings)
                        System.out.println(warning.getMessage() );
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
