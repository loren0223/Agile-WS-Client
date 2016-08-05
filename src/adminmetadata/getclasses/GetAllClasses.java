package adminmetadata.getclasses;


import java.net.URL;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningListType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningType;
import com.agile.ws.schema.common.v1.jaxws.ClassFilterType;
import com.agile.ws.schema.common.v1.jaxws.ClassType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.metadata.v1.jaxws.GetAllClassesRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.GetAllClassesResponseType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataPortType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataService;

import run.RunAllSamples;



/**
* Sample      : GetAllClasses  
* Type        : AdminMetadata webservice 
* 
* Description : This sample demonstrates retrieval of all classes in the Agile
* system by using a metadata webservice call. Results are obtained through the
* response object where objects of type 'ClassType' are obtained
*/
public class GetAllClasses {
    
    private static final String COMMAND_NAME = "GetAllClasses";
    
	public static String clsName;
    public static String serviceName   =  "AdminMetadata";    
    public static AdminMetadataPortType agileStub;        

    public GetAllClasses() {
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


    public static void main(String[] args) {
        
        // This sample may be configured by either directly modifying the string values of
        // 'SERVER_URL', 'USERNAME' and 'PASSWORD' (defined in this class), or by
        // passing them as program arguments in the same order. In this sample, the
        // method 'checkArguments' checks for these values.
        checkArguments(args);        

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");
        
        try{
            
            setupServerLogin(args);            
            
            // Create the request object GetAllClassesRequestType for the getAllClasses operatione
            GetAllClassesRequestType getAllClassesRequestType = new GetAllClassesRequestType();          


            // Set the 'level' for the getAllClasses request object. This filter is specified
            // by using a ClassFilterType object which can hold values of 'TOP', 'CONCRETE' or
            // 'ALL'. This ClassFilterType.ALL, ClassFilterType.TOP or ClassFilterType.CONCRETE
            // may be used.
            getAllClassesRequestType.setLevel(ClassFilterType.ALL);
            GetAllClassesResponseType getAllClassesResponseType = agileStub.getAllClasses(getAllClassesRequestType);            
            System.out.println("STATUS CODE: " +  getAllClassesResponseType.getStatusCode() );
                         
                         
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !getAllClassesResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = getAllClassesResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }

                AgileWarningListType agileWarningListType[] = getAllClassesResponseType.getWarnings().toArray(new AgileWarningListType[0]);
                if(agileWarningListType!=null)
                for(int i=0; i<agileWarningListType.length; i++){
                    AgileWarningType warnings[] = agileWarningListType[i].getWarning().toArray(new AgileWarningType[0]);
                    for(int j=0; j<warnings.length; j++)
                        System.out.println("Warning Id: " + warnings[j].getWarningId() + "\nMessage: " + warnings[j].getMessage() );                    
                }                                    
                
            }
            else{
            // If the webservice call was successful, then display the list of classes retrieved
                
                ClassType classes[] = getAllClassesResponseType.getClazz().toArray(new ClassType[0]);
                System.out.println("List of classes retrieved");
                for(int i=0; i<classes.length; i++)
                    System.out.println( classes[i].getDisplayName() );
                
                
            }
            
            }
            
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
        
    }

    
}

