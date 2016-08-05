package business.saveas;

import com.agile.api.ProgramConstants;
import com.agile.ws.schema.business.v1.jaxws.AgileSaveAsObjectRequestType;
import com.agile.ws.schema.business.v1.jaxws.SaveAsObjectRequestType;
import com.agile.ws.schema.business.v1.jaxws.SaveAsObjectResponseType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileRowType;
import com.agile.ws.schema.common.v1.jaxws.PropertyType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.common.v1.jaxws.SchemaConstants;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectPortType;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectService;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataPortType;
import common.util.WSUtil;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;

import run.DataPrepare;
import run.RunAllSamples;


 /**
 * Sample      : SaveAsProgramTemplate
 * Category    : BusinessObject webservice
 *
 * Description : This sample demonstrates the usage of 'SaveAsObject' webservice
 * for a specific case where one Agile program object is saved as another, along
 * with the explicit specification of the program template by using an option
 * 'program_template' to set the same. The request object contains a few additional
 * specifications in this case, these are specific to a program object.
 */
public class SaveAsProgramTemplate {
    private static final String COMMAND_NAME = "SaveAsProgramTemplate";
    
	public static String clsName;
    public static String serviceName   =  "BusinessObject";       
    public static BusinessObjectPortType agileStub;        
    public static AdminMetadataPortType agileStub_AdminMetadata;          
    
    public static String parentProgramNumber;
    public static String parentProgramName;
    public static String newProgramNumber;

    public SaveAsProgramTemplate() {
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
            
            parentProgramName = "Prog" + System.currentTimeMillis();
            parentProgramNumber = dataPrepare.createProgramObject( parentProgramName, "Proposed");
            newProgramNumber = dataPrepare.getNextAutoNumber("Program");
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
            AgileSaveAsObjectRequestType agileSaveAsObjectRequestType[] = new AgileSaveAsObjectRequestType[1];                         
            agileSaveAsObjectRequestType[0] = new AgileSaveAsObjectRequestType();
            
            
            // For each of the requests, set the class identifier to specify the
            // type of object that will be saved and an object number to identify
            // the unique object and the class type of the new object
            agileSaveAsObjectRequestType[0].setParentClassIdentifier("Program");
            agileSaveAsObjectRequestType[0].setParentObjectNumber( parentProgramNumber );
            agileSaveAsObjectRequestType[0].setNewClassIdentifier("Program");           
            
            System.out.println("Saving the program '" + parentProgramNumber + "' as '" + newProgramNumber + "'...\n");

            // The object number of the new object can be specified through the 
            // xsd:any attribute of an AgileRowType by using MessageElements as shown:
            AgileRowType row = new AgileRowType();             

            Element element = WSUtil.createMessageElement("name");
            element.setTextContent(newProgramNumber);
            row.getAny().add(element);                
            // The new object number information in this row is stored in the
            // request object using the setData method.
            agileSaveAsObjectRequestType[0].setData(row);   
            
            
            // Use the 'options' element to specify the template type of the 
            // new program object that is saved by the webservice. Properties are defined 
            // as name value pairs of objects of type PropertyType. The property names
            // may be obtained from the constants listed in the file 'SchemaConstants'
            PropertyType properties[] = new PropertyType[1];
            properties[0] = new PropertyType();
            properties[0].setPropertyName( SchemaConstants.PROGRAM_TEMPLATE.value());
            properties[0].setPropertyValue("Active");
            agileSaveAsObjectRequestType[0].getOptions().addAll(Arrays.asList(properties));           
            

            // The following fields are specific to the Program object
            
            // Specify the tables to be copied from the original program        
            String tables[] = {ProgramConstants.TABLE_ATTACHMENTS.toString() };
            agileSaveAsObjectRequestType[0].getTablesToCopy().addAll(Arrays.asList(tables));            
            // Specify if the properties are to be applied to children entities
            agileSaveAsObjectRequestType[0].setApplyToChildren(true);
            

            // The request objects are set and the agile Stub is used to make the saveAsProgram
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the saveAsProgram operation.             
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
