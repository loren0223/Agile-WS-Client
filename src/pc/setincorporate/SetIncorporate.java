package pc.setincorporate;


import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.pc.v1.jaxws.AgileSetIncorporateRequest;
import com.agile.ws.schema.pc.v1.jaxws.AgileSetIncorporateResponse;
import com.agile.ws.schema.pc.v1.jaxws.SetIncorporateRequestType;
import com.agile.ws.schema.pc.v1.jaxws.SetIncorporateResponseType;
import com.agile.ws.service.pc.v1.jaxws.PCPortType;
import com.agile.ws.service.pc.v1.jaxws.PCService;
import com.agile.ws.service.search.v1.jaxws.SearchService_Service;

import run.DataPrepare;
import run.RunAllSamples;

/**
* Sample      : SetIncorporate
* Category    : PC webservice
*
* Description : This sample demonstrates the act of 'incorporating' or 
* 'unincorporating' an Agile object using the 'setIncorporate' webservice.
*/
public class SetIncorporate {
    private static final String COMMAND_NAME = "SetIncorporate";
    
	public static String clsName;
    public static String serviceName   =  "PC";    
    public static PCPortType agileStub;        
    
    public static String partNumber;

    public SetIncorporate() {
             clsName = this.getClass().getName();
    }
      

    /**
     * The method setupServerLogin is used to login to the server by providing
     * the server url details and authentication credentials.
     * @param args command line arguments
     */
    
    public static void setupServerLogin(String[] args) throws Exception {       

         
    	//The AdminMetadataServiceLocator is used to obtain a AdminMetadata_BindingStub
    	URL url = new URL(args[0] + "/" + serviceName + "?WSDL"); 
    	PCService locator = 
             new PCService(url);

        //A stub of type AdminMetadata_BindingStub is obtained by specifying the server URL
        //Eventually, the convertCurrency webservice will be invoked by using this agileStub.
          agileStub = locator.getPC();
                
          
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
		System.err.println("\t" + "user_name: user name");
		System.err.println("\t" + "password: password");
	}

    public static void prepareData(String[] args){
        
        try{
            System.out.println("<<< Preparing Data... >>>");
            DataPrepare dataPrepare = new DataPrepare(args[0], args[1], args[2]);
            dataPrepare.setupAdminMetadata(); 
            dataPrepare.setupBusiness();
            
            partNumber = dataPrepare.createNewObject("Part");
            System.out.println("<<< DataPrepare successful >>>");
        }
        catch(Exception e){            
            System.out.println("<<< ALERT: DataPrepare failed. Your server settings may be invalid, this sample might not run successfully.");
            System.out.println("To run the sample without the assitance of prepared data, you may edit the static variables after");
            System.out.println("creating the necessary prerequisites through the Agile web client and then execute the sample >>>");
        }
        
        
    }

    public static void main(String[] args) {
        checkArguments(args);      

        // Comment this method out if you intend to use your own data
        // or scenario by editing the static variables at the top of this code
        prepareData(args);

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");
        
        try{
            
            // Login to the server by providing the server url and authentication credentials.
            setupServerLogin(args);             
            
            // Create the request object SetIncorporateRequestType for the setIncorporate operation
            // Create an array of requests of type AgileSetIncorporateRequest. Batch operations may be
            // performed by populating as many request objects as required to set the state of 
            // incorporation for several Agile objects at once.
            SetIncorporateRequestType setIncorporateRequestType = new SetIncorporateRequestType();                         
            AgileSetIncorporateRequest agileSetIncorporateRequest[] = new AgileSetIncorporateRequest[1];                         
            agileSetIncorporateRequest[0] = new AgileSetIncorporateRequest();           

            
            // For each batched request, specify the type and number of the object whose
            // state of incorporation is to be modified. Pass a boolean value in the 
            // 'incorporate' field to denote whether the specified object should be
            // incorporated or unincorporated
            agileSetIncorporateRequest[0].setClassIdentifier("Part");
            agileSetIncorporateRequest[0].setObjectNumber( partNumber );
            agileSetIncorporateRequest[0].setIncorporate( true );
             
             System.out.println("Incorporating the part object '" + partNumber + "'...\n");

            // The request objects are set and the agile Stub is used to make the setIncorporate
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the setIncorporate operation. 
            setIncorporateRequestType.getRequests().addAll(Arrays.asList(agileSetIncorporateRequest));             
            SetIncorporateResponseType setIncorporateResponseType = agileStub.setIncorporate(setIncorporateRequestType);
            System.out.println("STATUS CODE: " +  setIncorporateResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !setIncorporateResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = setIncorporateResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
            }
                else{
                // If the webservice call was successful, then display the state of incorporation
                // for all the objects modified.
                    AgileSetIncorporateResponse responses[] = setIncorporateResponseType.getResponses().toArray(new AgileSetIncorporateResponse[0]);
                

                    for(int j=0; j<responses.length; j++){

                        System.out.println("-----------------------------------------------");
                        System.out.println( agileSetIncorporateRequest[j].getObjectNumber() );
                        System.out.println("Incorporated-> " + responses[j].isIsIncorporated());
                        System.out.println("-----------------------------------------------");
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
