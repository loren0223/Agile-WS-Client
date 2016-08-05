package adminmetadata.getattributes;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.common.v1.jaxws.AdminListType;
import com.agile.ws.schema.common.v1.jaxws.AdminPropertyType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningListType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningType;
import com.agile.ws.schema.common.v1.jaxws.AttributeType;
import com.agile.ws.schema.common.v1.jaxws.OperatorsType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.metadata.v1.jaxws.AgileGetAttributesRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.AgileGetAttributesResponseType;
import com.agile.ws.schema.metadata.v1.jaxws.GetAttributesRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.GetAttributesResponseType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataPortType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataService;

import run.RunAllSamples;


/**
* Sample      : GetAttributes  
* Type        : AdminMetadata webservice 
* 
* Description : The sample is used to demonstrate the retrieval of 
* attribute information for a particular Agile class. The response  
* contains AttributeType elements which may be processed to 
* obtain comprehensive information about the retrieved attributes
*/
public class getAttributes {
	private static final String COMMAND_NAME = "getAttributes";

    public static String clsName;
    public static String serviceName   =  "AdminMetadata";    
    public static AdminMetadataPortType agileStub;
    
    public getAttributes() {
             clsName = this.getClass().getName();

    }
      

    /**
     * The method setupServerLogin is used to login to the server by providing
     * the server url details and authentication credentials.
     * @param args 
     */
    
    public static void setupServerLogin(String[] args) throws Exception {

    	 //The AdminMetadataServiceLocator is used to obtain a AdminMetadata_BindingStub
    	URL url = new URL(args[0] + "/" + serviceName +"?WSDL"); 
    	AdminMetadataService locator = 
             new AdminMetadataService(url);

        //A stub of type AdminMetadata_BindingStub is obtained by specifying the server URL
        //Eventually, the convertCurrency webservice will be invoked by using this agileStub.
          agileStub = locator.getAdminMetadata();
                


        // Username and password details are specified for the agileStub
          Map<String, Object> reqContext = ((BindingProvider)agileStub).getRequestContext();
          reqContext.put(BindingProvider.USERNAME_PROPERTY, args[1]);
          reqContext.put(BindingProvider.PASSWORD_PROPERTY, args[2]);
     }

    public static void main(String[] args) {
        checkArguments(args);        

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");
        
        try{
            
            // Login to the server by providing the server url and authentication credentials.
            setupServerLogin(args);             
            
            // Create the request object GetAttributesRequestType for the getAttributes operation
            // Create an array of requests of type AgileGetAttributesRequestType. Batch operations may be
            // performed by populating as many request objects as required to retrieve several attributes

            GetAttributesRequestType getAttributesRequestType = new GetAttributesRequestType();                         
            AgileGetAttributesRequestType agileGetAttributesRequestType[] = new AgileGetAttributesRequestType[1];                         
            agileGetAttributesRequestType[0] = new AgileGetAttributesRequestType();           
                
            
            // For each batched request, specify the type of object whose attributes will be retrieved.
            // Also specify the attribute which will be retrieved
            agileGetAttributesRequestType[0].setClassIdentifier("Specification");
            agileGetAttributesRequestType[0].getAttributeIdentifier().add(new String("description"));         

             

            // The request objects are set and the agile Stub is used to make the getAttributes
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the getAttributes operation. 
            getAttributesRequestType.getAttributesRequests().addAll(Arrays.asList(agileGetAttributesRequestType));             
            GetAttributesResponseType getAttributesResponseType = agileStub.getAttributes(getAttributesRequestType);
            System.out.println("STATUS CODE: " +  getAttributesResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !getAttributesResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = getAttributesResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
                AgileWarningListType agileWarningListType[] = getAttributesResponseType.getWarnings().toArray(new AgileWarningListType[0]);
                if(agileWarningListType!=null)
                for(int i=0; i<agileWarningListType.length; i++){
                    AgileWarningType warnings[] = agileWarningListType[i].getWarning().toArray(new AgileWarningType[0]);
                    for(int j=0; j<warnings.length; j++)
                        System.out.println("Warning Id: " + warnings[j].getWarningId() + "\nMessage: " + warnings[j].getMessage() );                    
                }                    
                
            }
                else{
                // If the webservice call was successful, then display the attributes retrieved
                    
                    AgileGetAttributesResponseType responses[] = getAttributesResponseType.getAttributesResponses().toArray(new AgileGetAttributesResponseType[0]);
                
                    if(responses!=null)                
                    for(int j=0; j<responses.length; j++){
                            
                        AttributeType attributes[] = responses[j].getAttributes().toArray(new AttributeType[0]);
                        System.out.print("Attributes for ");
                        System.out.println( agileGetAttributesRequestType[j].getAttributeIdentifier().get(0) + ": \n" );
                        if(attributes!=null)
                            for(int i=0; i<attributes.length; i++){
                                System.out.println( "Display name      --> " + attributes[i].getDisplayName() );
                                System.out.println( "Data Type         --> " + attributes[i].getDataType() );
                                System.out.println( "Max length        --> " + attributes[i].getMaxLength() );
                                AdminListType value = attributes[i].getPossibleValues();
                                
                                AdminListType values[] = (value==null? null : value.getEntry().toArray(new AdminListType[0]) );
                                System.out.println( "Possible values   --> " );
                                if(values!=null)
                                    for(int ii=0; ii< values.length; ii++)                                    
                                        System.out.println( values[ii].getDescription() + " / ");
                                
                                System.out.println( "Required field    --> " + attributes[i].isRequired());
                                System.out.println( "Searchable field  --> " + attributes[i].isSearchable() );
                                
                                System.out.print( "Rel operators     --> " );
                                OperatorsType operators[] = attributes[i].getRelationalOperators().toArray(new OperatorsType[0]);
                                if(operators!=null)
                                    for(int ii=0; ii<operators.length; ii++)
                                        System.out.print( operators[ii].value() + " / ");
                                
                                System.out.println( "\nProperties        --> " );
                                AdminPropertyType properties[] = attributes[i].getProperties().toArray(new AdminPropertyType[0]);
                                if(properties!=null)
                                    for(int ii=0; ii<properties.length; ii++)
                                        System.out.println("\t" + properties[ii].getDisplayName() + ",  Readonly: " + properties[ii].isReadOnly() );
                                
                            }
                            
                    
                    }                 
                     
                }


            }
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }

	/**
	 * This sample can be configured by passing server url, user name and
	 * password as program arguments in the same order. This method checks for
	 * these values.
	 * 
	 * @param args
	 */
    private static void checkArguments(String[] args){

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
		System.err.println("Usage: " + COMMAND_NAME  + " server_url user_name password");
		System.err.println("\t" + "server_url: the server URL");
		System.err.println("\t" + "user_name: user name");
		System.err.println("\t" + "password: password");
	}

}
