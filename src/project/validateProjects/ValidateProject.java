package project.validateProjects;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
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
import com.agile.ws.schema.project.v1.jaxws.UpdateProjects;
import com.agile.ws.schema.project.v1.jaxws.UpdateProjectsResponseType;
import com.agile.ws.schema.project.v1.jaxws.ValidateProjects;
import com.agile.ws.schema.project.v1.jaxws.ValidateProjectsResponseType;
import com.agile.ws.service.project.v1.jaxws.ProjectService;
import com.agile.ws.service.project.v1.jaxws.ProjectService_Service;

/**
 * Sample : ValidateProject 
 * Category : Project webservice
 * 
 * Description : This sample demonstrates creating a new project and modify some
 * data to perform validation using the validateProjects webservcie
 */

public class ValidateProject {
	private static final String COMMAND_NAME = "ValidateProject";
	public static String clsName;
	public static String serviceName   =  "Project";    
	public static String updateNewProjectDataFile = "/project/validateProjects/UpdateNewProject.xml";
	public static String validateProjectDataFile = "/project/validateProjects/ValidateProject.xml";
	public static String projectName;
	public static ProjectService agileStub;

	public ValidateProject() {
		clsName = this.getClass().getName();
	}

	/**
	 * The method setupServerLogin is used to login to the server by providing
	 * the server url details and authentication credentials.
	 * @param args 
	 */
	public static void setupServerLogin(String[] args) throws Exception {
		URL url = new URL(args[0] + "/" + serviceName +"?WSDL");
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
			
			// Validate the project with some data modified in the ValidateProject.xml
			ValidateProjectsResponseType validateProjectsResponse = validateProject(objectId);


			// Process the response to get the Status.
			String status = validateProjectsResponse.getStatusCode().value();
			System.out.println("STATUS CODE: " +  status);

			
			// Process the response if failure to get the exceptionIds and Messages.
			if( !status.equals(ResponseStatusCode.SUCCESS.value() ) ){
				List<AgileExceptionListType> lstAgileExceptionListType = validateProjectsResponse.getExceptions();
                if(lstAgileExceptionListType!=null)
                for(AgileExceptionListType agileExceptionListType : lstAgileExceptionListType){
                    List<AgileExceptionType> exceptions = agileExceptionListType.getException();
                    for(AgileExceptionType exception : exceptions)
                        System.out.println("Exception Id:" + exception.getExceptionId() + "\nMessage: " + exception.getMessage() );
                }
                
                List<AgileWarningListType> lstAgileWarningListType = validateProjectsResponse.getWarnings();
                if(lstAgileWarningListType!=null)
                for(AgileWarningListType agileWarningListType : lstAgileWarningListType){
                    List<AgileWarningType> warnings = agileWarningListType.getWarning();
                    for(AgileWarningType warning : warnings)
                        System.out.println("Warning Id: " + warning.getWarningId() + "\nMessage: " + warning.getMessage() );
                }
				processValidateResponse(validateProjectsResponse, new String[]{"110044","584"});
			}
		}
		catch (Exception e) {            
			RunAllSamples.reportFailure( clsName );
			System.out.println("Exceptions: ");
			e.printStackTrace();
		}       
	}
	
	
	private static void processValidateResponse(ValidateProjectsResponseType validateProjectsResponse,String[] exceptionIds) throws Exception {
		System.out.println("\n\n Validating the Error Codes for known Errors");

		List<AgileExceptionType> exceptionList = new ArrayList<AgileExceptionType>();
		List<AgileExceptionListType> lstAgileExceptionListType = validateProjectsResponse.getExceptions();
        if(lstAgileExceptionListType!=null)
        for(AgileExceptionListType agileExceptionListType : lstAgileExceptionListType){
            List<AgileExceptionType> exceptions = agileExceptionListType.getException();
            for(AgileExceptionType exception : exceptions)
                exceptionList.add(exception);
        }

		boolean validateStatus = true;
		for(int k=0; k < exceptionIds.length; k++){
			if(!exceptionList.contains(exceptionIds[k])){
				validateStatus = false;
				break;
			}
		}
		
		if(validateStatus){
			System.out.println("\nValidation Status of Error code is Successful");
		}
		else{
			System.out.println("\nValidation Status of Error code is NOT Successful");
			RunAllSamples.reportFailure( clsName );
		}
	}
	
	
	private static ValidateProjectsResponseType validateProject(String objectId) throws Exception{
		// Validate the data file which has the XML Message  using validateProjects 
		// webservice.The RootProject name, Id and Description are replaced in the XML.
		String line = null;
		StringBuffer sb = new StringBuffer();
		InputStream is = ValidateProject.class.getResourceAsStream(validateProjectDataFile);
		BufferedReader reader=new BufferedReader ( new InputStreamReader(is));
		String taskName = "Test" + System.currentTimeMillis();
		while((line = reader.readLine()) != null) {
			line = line.replaceAll("@ROOTNAME@",projectName);
			line = line.replaceAll("@ROOTID@",objectId);
			line = line.replaceAll("@DESCRIPTION@",projectName);
			line = line.replaceAll("@TASKNAME@",taskName);
			sb.append(line);
		}
		reader.close();
		
		System.out.println("Validating a new Project with name  '" + projectName + "'...\n");
		
		// Unmarshal UpdateProjects object from the loaded XML string
		JAXBContext jc = JAXBContext.newInstance(ValidateProjects.class);
	    Unmarshaller unmarshaller = jc.createUnmarshaller();
	    StringReader sr = new StringReader(sb.toString());
	    ValidateProjects validateProjects = (ValidateProjects)unmarshaller.unmarshal(sr);
	    ValidateProjectsResponseType response = agileStub.validateProjects(validateProjects.getRequest());
	    return response;
	}


	private static String createProject() throws Exception{
		// Loading the data file which has the XML Message  for creating a new 
		// project using updateProjects webservice.The RootProject name and 
		// Description are replaced in the XML.
		String line = null;
		StringBuffer sb = new StringBuffer();
		InputStream is = ValidateProject.class.getResourceAsStream(updateNewProjectDataFile);
		BufferedReader reader=new BufferedReader ( new InputStreamReader(is));
		
		while((line = reader.readLine()) != null) {
			line = line.replaceAll("@ROOTNAME@",projectName);
			line = line.replaceAll("@DESCRIPTION@",projectName);
			sb.append(line);
		}
		reader.close();

		System.out.println("Creating a new Project with name  '" + projectName + "'...\n");

		// Unmarshal UpdateProjects object from the loaded XML string
		JAXBContext jc = JAXBContext.newInstance(UpdateProjects.class);
	    Unmarshaller unmarshaller = jc.createUnmarshaller();
	    StringReader sr = new StringReader(sb.toString());
	    UpdateProjects updateProjects = (UpdateProjects)unmarshaller.unmarshal(sr);
	    UpdateProjectsResponseType response = agileStub.updateProjects(updateProjects.getRequest());
	    String objectId = response.getUpdateProjectsStatuses().getUpdateProjectStatus().get(0).getProjectId().getObjectId().toString();
	    return objectId;
	}
}
