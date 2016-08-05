package adminmetadata.getnode;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.agile.api.NodeConstants;
import com.agile.ws.schema.common.v1.jaxws.AdminNodeType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.metadata.v1.jaxws.AgileGetNodeRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.AgileGetNodeResponseType;
import com.agile.ws.schema.metadata.v1.jaxws.GetNodeRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.GetNodeResponseType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataPortType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataService;

import run.RunAllSamples;



/**
* Sample      : GetNode  
* Type        : AdminMetadata webservice 
* 
* Description : This sample demonstrates retrieval of nodes from a given node.
* The node identifier in the request object identifies the node and determines
* whether nodes shall be fetched recursively. The response contains objects of 
* AdminNodeType from which the node information may be obtained.
*/
public class GetNode {
    private static final String COMMAND_NAME = "GetNode";
    
	public static String clsName;
    public static String serviceName   =  "AdminMetadata";    
    public static AdminMetadataPortType agileStub;        

    public GetNode() {
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
            
            // Create the request object GetNodeRequestType for the createObject operation
            // Create an array of requests of type AgileGetNodeRequestType. Batch operations may be
            // performed by populating as many request objects as required to retrieve several nodes
            
            GetNodeRequestType getNodeRequestType = new GetNodeRequestType();                         
            AgileGetNodeRequestType agileGetNodeRequestType[] = new AgileGetNodeRequestType[1];             
            agileGetNodeRequestType[0] = new AgileGetNodeRequestType();
                    
            // For each batched request, specify the string that identifies the node
            // and whether children of a given node will also be retrieved recursively
            agileGetNodeRequestType[0].setNodeIdentifier( NodeConstants.NODE_AUTONUMBERS.toString() );
            agileGetNodeRequestType[0].setRecursive( true );
             

            // The request objects are set and the agile Stub is used to make the getNode
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the getNode operation. 
            getNodeRequestType.getNodeRequest().addAll(Arrays.asList(agileGetNodeRequestType));             
            GetNodeResponseType getNodeResponseType = agileStub.getNode(getNodeRequestType);
            System.out.println("STATUS CODE: " +  getNodeResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !getNodeResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = getNodeResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
            }
                else{
                // If the webservice call was successful, then display the list of nodes retrieved
                    
                    AgileGetNodeResponseType nodes[] = getNodeResponseType.getNodeResponse().toArray(new AgileGetNodeResponseType[0]);
                    if(nodes!=null)
                    for(AgileGetNodeResponseType node: nodes ){ 
                            
                        AdminNodeType adminNode = node.getNode();
                        displayNodes(adminNode, "");
                    
                    }                
                    
                }


            }
            
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }

    public static void displayNodes(AdminNodeType adminNode, String level){        
        
            System.out.println( level + ": " + adminNode.getDisplayName() );
            AdminNodeType children[] = adminNode.getChildNode().toArray(new AdminNodeType[0]);
            
            if( (children!=null)&&(children.length!=0) )
                for(AdminNodeType node: children)
                    displayNodes(node, level + "      " );
        
    }


}
