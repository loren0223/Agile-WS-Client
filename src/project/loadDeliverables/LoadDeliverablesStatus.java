package project.loadDeliverables;

import java.net.URL;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import run.SDKDataPrepare;
import run.RunAllSamples;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.project.v1.jaxws.AgileObjectIdentifierType;
import com.agile.ws.schema.project.v1.jaxws.LoadDeliverablesStatusRequestType;
import com.agile.ws.schema.project.v1.jaxws.LoadDeliverablesStatusResponseType;
import com.agile.ws.schema.project.v1.jaxws.LoadProjectsRequestType;
import com.agile.ws.schema.project.v1.jaxws.LoadProjectsResponseType;
import com.agile.ws.service.project.v1.jaxws.ProjectService;
import com.agile.ws.service.project.v1.jaxws.ProjectService_Service;

/**
 * Sample : LoadDeliverablesStatus 
 * Category : Project webservice
 * 
 * Description : This sample demonstrates creating a project with deliverable
 * and Loading the Deliverables Statuses for a Project using
 * LoadDeliverablesStatus webservice operation. This sample uses the axis
 * generated objects for request rsponse.
 */

public class LoadDeliverablesStatus {
	private static final String COMMAND_NAME = "LoadDeliverablesStatus";
	
	public static String clsName;
	public static String serviceName   =  "Project";    
	public static String projectName;
	public static String projectNumber;
	public static ProjectService agileStub;

	public LoadDeliverablesStatus() {
		clsName = this.getClass().getName();
	}


	/**
	 * The method setupServerLogin is used to login to the server by providing
	 * the server url details and authentication credentials.
	 * @param args 
	 */

	public static void setupServerLogin(String[] args) throws Exception {
		// The ProjectService is obtained and the credentials are set.
		URL url = new URL(args[0] + "/" + serviceName + "?WSDL");
        ProjectService_Service locator = new ProjectService_Service(url);
        agileStub = locator.getProject();
        Map<String, Object> reqContext = ((BindingProvider)agileStub).getRequestContext();
        reqContext.put(BindingProvider.USERNAME_PROPERTY, args[1]);
        reqContext.put(BindingProvider.PASSWORD_PROPERTY, args[2]);
	}

	
 	/**
	 * This sample can be configured by passing server inf, user name,
	 * password and web server url as program arguments in the same
	 * order. This method checks for these values.
	 * 
	 * @param args
	 */
	private static void checkArguments(String[] args) {
		RunAllSamples.runCount++;
		
		if (args.length < 5) {
			// should pass arguments through the command line
			printUsage();
			System.exit(-1);
		}
	}

	/**
	 * print usage message to the standard error
	 */
	private static void printUsage() {
		System.err.println("Usage: " + COMMAND_NAME + 
				" server_inf user_name password web_server_url");
		System.err.println("\t" + "server_inf: the server information");
		System.err.println("\t" + "user_name: the user name");
		System.err.println("\t" + "password: the password");
		System.err.println("\t" + "dfs_server_url: the DFS server url");
		System.err.println("\t" + "web_server_url: the web server url");
	}
	

	/**
	 * This methods creates any prerequisite data required for executing the web
	 * service sample.
	 * @param args 
	 * 
	 */
	public static void prepareData(String[] args){

		try{
			System.out.println("<<< Preparing Data... >>>");
			
			SDKDataPrepare dataPrepare = new SDKDataPrepare(args[4], args[1], args[2]);
			// Creates the SDK session in ProjectDataPrepare
			dataPrepare.connect();

			projectName = "Test" + System.currentTimeMillis();
			projectNumber = dataPrepare.createProgramWithDeliverable(projectName);
			
			
		}
		catch(Exception e){            
			System.out.println("<<< ALERT: ProjectDataPrepare failed. Your server settings may be invalid, this sample might not run successfully.");
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
		System.out.println("Project Number:"+projectNumber);
		System.out.println("\n------------------------------------------------------------------------");
		System.out.println("Executing webservice sample: ");

		try{

			// Login to the server by providing the server url and authentication credentials.
			setupServerLogin(args);
			
			// Create the request for loadDeliverablesStatus webservice operation
			LoadDeliverablesStatusRequestType loadDeliverablesStatusReq = new LoadDeliverablesStatusRequestType();
			loadDeliverablesStatusReq.setMessageId("Project WS Sample - LoadDeliverablesStatus");
			loadDeliverablesStatusReq.setMessageName("Project WS Sample - LoadDeliverablesStatus");
			
			// Set the project created in dataPrepare for loading the deliverable status
			AgileObjectIdentifierType[] ids = new AgileObjectIdentifierType[1];
			ids[0] = new AgileObjectIdentifierType();
			ids[0].setObjectName(projectNumber);
			ids[0].setClassName("ActivitiesClass");
			ids[0].setSubClassName("Program");
			ids[0].setClassId(18022);
			ids[0].setSubClassId(18027);
			loadDeliverablesStatusReq.setProjectIdentifier(ids[0]);
			loadDeliverablesStatusReq.setIncludeAllLevels(new Boolean(true));
			
			// The agile Stub is used to make the loadDeliverablesStatus webservice
			// call. The status code obtained from the response object is printed to
			// verify the success of the updateProjects operation.
			LoadDeliverablesStatusResponseType loadDeliverablesStatusRes = agileStub.loadDeliverablesStatus(loadDeliverablesStatusReq);
			System.out.println("STATUS CODE: " +  loadDeliverablesStatusRes.getStatusCode() );
			
			// The criteria met for second status in deliverables statuses would be false.
			String status = loadDeliverablesStatusRes.getTaskDeliverablesStatuses().getTaskDeliverablesStatus().get(0).getDeliverablesStatus().getStatus().get(1).getStatusType().getSelection().get(0).getValue();
			boolean deliverableStatusFound = loadDeliverablesStatusRes.getTaskDeliverablesStatuses().getTaskDeliverablesStatus().get(0).getDeliverablesStatus().getStatus().get(1).isStatusMet().booleanValue();
			System.out.println("The project cannot move to status : "+ status + " since  the critera met is " + deliverableStatusFound);
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !loadDeliverablesStatusRes.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

            	RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = loadDeliverablesStatusRes.getExceptions().toArray(new AgileExceptionListType[0]);
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                    	System.out.println("Error " + exceptions[j].getExceptionId() + " : " + exceptions[j].getMessage());
                }
            }
			
		}
		catch (Exception e) {            
			RunAllSamples.reportFailure( clsName );
			System.out.println("Exceptions: ");
			e.printStackTrace();
		}       
	}

	private static LoadProjectsResponseType loadProject(String objectNumber) throws Exception{
		// Loading the Project using Axis, Creating an request of type LoadProjectsRequestType
		LoadProjectsRequestType request = new LoadProjectsRequestType();
		request.setMessageId("Project WS Sample - LoadProject");
		request.setMessageName("Project WS Sample - LoadProject");

		AgileObjectIdentifierType objectIdentifiers = new AgileObjectIdentifierType();
		objectIdentifiers.setObjectName(objectNumber);
		objectIdentifiers.setClassName("ActivitiesClass");
		objectIdentifiers.setSubClassName("Program");
		request.getProjectIdentifiers().add(objectIdentifiers);
		
		System.out.println("Loading the Project with name  '" + projectName + "'...\n");
		
		// Making a loadProjects webservice call and returning the project from LoadProjectsResponseType
		LoadProjectsResponseType response = agileStub.loadProjects(request);
		return response;
	}
}
