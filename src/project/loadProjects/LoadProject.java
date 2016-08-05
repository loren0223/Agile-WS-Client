package project.loadProjects;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.ws.BindingProvider;
import run.SDKDataPrepare;
import run.RunAllSamples;

import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningListType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.project.v1.jaxws.LoadProjects;
import com.agile.ws.schema.project.v1.jaxws.LoadProjectsResponseType;
import com.agile.ws.schema.project.v1.jaxws.UpdateProjects;
import com.agile.ws.schema.project.v1.jaxws.UpdateProjectsResponseType;
import com.agile.ws.service.project.v1.jaxws.ProjectService;
import com.agile.ws.service.project.v1.jaxws.ProjectService_Service;

/**
 * Sample : LoadProject 
 * Category : Project webservice
 * 
 * Description : This sample demonstrates creating a new project tree with
 * phase,task,gate and dependency,P2,P3,assignments etc and then load the
 * Project using loadProjects webservice operation.
 */

public class LoadProject {
	private static final String COMMAND_NAME = "LoadProject";
	
	public static String clsName;
	public static String serviceName   =  "Project";    
	public static String updateNewProjectDataFile = "/project/loadProjects/UpdateNewProject.xml";
	public static String loadProjectDataFile = "/project/loadProjects/LoadProject.xml";
	public static String projectName = "";
	public static ProjectService agileStub;

	public LoadProject() {
		clsName = this.getClass().getName();
	}


	/**
	 * The method setupServerLogin is used to login to the server by providing
	 * the server url details and authentication credentials.
	 * @param args 
	 */

	public static void setupServerLogin(String[] args) throws Exception {
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
			
			// Create the users,userGroups and assign users to Usergroups which are used in the Project Tree
			dataPrepare.createUser("TestPPMWSUsr1", args[2],"Active");
			dataPrepare.createUser("TestPPMWSUsr2", args[2],"Active");
			dataPrepare.createUser("TestPPMWSUsr3", args[2],"Active");
			dataPrepare.createUserGroup("TestPPMWSUsrGroup1", "Global","Active","Yes");
			dataPrepare.assignUserToUserGroup("TestPPMWSUsrGroup1", "TestPPMWSUsr2");
			dataPrepare.createUserGroup("TestPPMWSUsrGroup2", "Global","Active","Yes");
			dataPrepare.assignUserToUserGroup("TestPPMWSUsrGroup2", "TestPPMWSUsr3");
			
			// Enable the page two page three attributes used in the sample
			dataPrepare.setVisibilityOfP2P3Attributes(true);
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

		System.out.println("\n------------------------------------------------------------------------");
		System.out.println("Executing webservice sample: ");

		try{

			// Login to the server by providing the server url and authentication credentials.
			setupServerLogin(args);
			
			// Create a new Project using the from UpdateNewProject.xml
			String objectId = createProject();
			
			// Load the project. This would give the details of the Phase,Task,Gate numbers existing in the Project
			LoadProjectsResponseType loadProjectResponse = loadProject(objectId);
			
			// Process the response to get the Status.
			String status = loadProjectResponse.getStatusCode().value();
			System.out.println("STATUS CODE: " +  status);

			
			// Process the response if failure to get the exceptionIds and Messages.
			if( !status.equals(ResponseStatusCode.SUCCESS.value() ) ){
				RunAllSamples.reportFailure( clsName );

				List<AgileExceptionListType> lstAgileExceptionListType = loadProjectResponse.getExceptions();
                if(lstAgileExceptionListType!=null)
                for(AgileExceptionListType agileExceptionListType : lstAgileExceptionListType){
                    List<AgileExceptionType> exceptions = agileExceptionListType.getException();
                    for(AgileExceptionType exception : exceptions)
                        System.out.println("Exception Id:" + exception.getExceptionId() + "\nMessage: " + exception.getMessage() );
                }
                
                List<AgileWarningListType> lstAgileWarningListType = loadProjectResponse.getWarnings();
                if(lstAgileWarningListType!=null)
                for(AgileWarningListType agileWarningListType : lstAgileWarningListType){
                    List<AgileWarningType> warnings = agileWarningListType.getWarning();
                    for(AgileWarningType warning : warnings)
                        System.out.println("Warning Id: " + warning.getWarningId() + "\nMessage: " + warning.getMessage() );
                }
			}
		}
		catch (Exception e) {            
			RunAllSamples.reportFailure( clsName );
			System.out.println("Exceptions: ");
			e.printStackTrace();
		}       
	}


	private static String createProject() throws Exception{
		// Loading the data file which has the XML Message  for creating a new 
		// project using updateProjects webservice.The RootProject name and 
		// Description are replaced in the XML.
		String line = null;
		StringBuffer sb = new StringBuffer();
		InputStream is = LoadProject.class.getResourceAsStream(updateNewProjectDataFile);
		BufferedReader reader=new BufferedReader ( new InputStreamReader(is));
		while((line = reader.readLine()) != null) {
			line = line.replaceAll("@ROOTNAME@",projectName);
			line = line.replaceAll("@DESCRIPTION@",projectName);
			sb.append(line);
		}
		reader.close();

		System.out.println("Creating a new Project with name  '" + projectName + "'...\n");
		
		// Unmarshal UpdateProjects object from the loaded XML string
		// and send it as the request for create a new project
		JAXBContext jc = JAXBContext.newInstance(UpdateProjects.class);
	    Unmarshaller unmarshaller = jc.createUnmarshaller();
	    StringReader sr = new StringReader(sb.toString());
	    UpdateProjects updateProjects = (UpdateProjects)unmarshaller.unmarshal(sr);
	    UpdateProjectsResponseType response = agileStub.updateProjects(updateProjects.getRequest());
	    String objectId = response.getUpdateProjectsStatuses().getUpdateProjectStatus().get(0).getProjectId().getObjectId().toString();
	    return objectId;
	}
	
	
	private static LoadProjectsResponseType loadProject(String objectId) throws Exception{
		// Loading the data file which has the XML Message  for loading the 
		// project , The RootProject Id is replaced in the XML.
		String line = null;
		StringBuffer sb = new StringBuffer();
		InputStream is = LoadProject.class.getResourceAsStream(loadProjectDataFile);
		BufferedReader reader=new BufferedReader ( new InputStreamReader(is));
		while((line = reader.readLine()) != null) {
			line = line.replaceAll("@ROOTID@",objectId);
			sb.append(line);
		}
		reader.close();
		
		System.out.println("Loading Project with name  '" + projectName + "'...\n");
		JAXBContext jc = JAXBContext.newInstance(LoadProjects.class);
	    Unmarshaller unmarshaller = jc.createUnmarshaller();
	    StringReader sr = new StringReader(sb.toString());
	    LoadProjects request = (LoadProjects)unmarshaller.unmarshal(sr);
	    LoadProjectsResponseType response = agileStub.loadProjects(request.getRequest());
		return response;
	}
}
