package adminmetadata.getusers;

import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;

import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileListEntryType;
import com.agile.ws.schema.common.v1.jaxws.AgileUserType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.metadata.v1.jaxws.GetUsersRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.GetUsersResponseType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataPortType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataService;

import run.RunAllSamples;



/**
* Sample      : GetUsers  
* Type        : AdminMetadata webservice 
* 
* Description : This sample demonstrates retrieval of Agile users. The
* request does not contain any element while the response consists of 
* AgileUserType objects which contains message elements that contain
* information pertaining to an Agile user.
*/
public class GetUsers {      
    private static final String COMMAND_NAME = "GetUsers";
    
	public static String clsName;
    public static String serviceName   =  "AdminMetadata";    
    public static AdminMetadataPortType agileStub;        

    public GetUsers() {
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
                
                // Login to the server by providing the server url and authentication credentials.
                setupServerLogin(args);             
                
                // Create the request object GetUsersRequestType for the getUsers operation
                GetUsersRequestType getUsersRequestType = new GetUsersRequestType();                                        
                
                // The request object for the getUsers operation does not contain any element.
                // We directly make the webservice call using this request object.
    
                // The getUsers webservice call is made. The status code obtained from the response object is printed to
                // verify the success of the getUsers operation. 
                GetUsersResponseType getUsersResponseType = agileStub.getUsers(getUsersRequestType);
                System.out.println("STATUS CODE: " +  getUsersResponseType.getStatusCode() );
                
                
                // If the status code is not 'SUCCESS', then populate the list of exceptions
                // returned by the webservice. 
                if( !getUsersResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                    
                    AgileExceptionListType[] agileExceptionListType = getUsersResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                    for(int i=0; i<agileExceptionListType.length; i++){
                        AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                        for(int j=0; j<exceptions.length; j++)
                            System.out.println(exceptions[j].getMessage() );
                    }
                    
                }
                    else{
                    // If the webservice call was successful, then display the users retrieved
                        
                        AgileUserType users[] = getUsersResponseType.getUsers().toArray(new AgileUserType[0]);
                    
                        if(users!=null)
                        for(int j=0; j<users.length; j++){
                            
                            System.out.println("----------------------------------------");
                            List<Element> messages = users[j].getAny();
                            if(messages!=null)
                                for(int jj=0; jj<messages.size(); jj++){
                                	if(messages.get(jj) instanceof Element){
                                	Element element = (Element)messages.get(jj);
                                    System.out.print( element.getTagName() + "--> " );
                                    if( element.getFirstChild()!=null )
                                        System.out.println(element.getFirstChild().getNodeValue() );
                                    else
                                        System.out.println();
                                	}else if((messages.get(jj) instanceof JAXBElement)){
                                		JAXBElement element = (JAXBElement)messages.get(jj);
                                		 System.out.print( element.getName().getLocalPart() + "--> " );
                                		Object obj = element.getValue();
                                		if(obj instanceof AgileListEntryType){
                                			AgileListEntryType agileListEntryType =(AgileListEntryType) obj;
                                			for(int i=0; i<agileListEntryType.getSelection().size();i++)
                                			{
                                				System.out.print(agileListEntryType.getSelection().get(i).getValue());
                                			}
                                		}
                                		 System.out.println();
                                	}
                                }
                        
                        }
                        System.out.println("----------------------------------------");

                        
                    }


            }
            
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }              
        }


}
