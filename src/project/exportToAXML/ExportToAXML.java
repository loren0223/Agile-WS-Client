
package project.exportToAXML;

import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;

import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileListEntryType;
import com.agile.ws.schema.common.v1.jaxws.AgileRowType;
import com.agile.ws.schema.common.v1.jaxws.AgileTableType;
import com.agile.ws.schema.common.v1.jaxws.AgileUserUserGroupIdentifierType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.common.v1.jaxws.SelectionType;
import com.agile.ws.schema.project.v1.jaxws.*;
import com.agile.ws.service.project.v1.jaxws.ProjectService;
import com.agile.ws.service.project.v1.jaxws.ProjectService_Service;
import common.util.WSUtil;


import run.DataPrepare;
import run.RunAllSamples;


/**
 * Sample      : ExportToAXML
 * Category    : Project webservice
 *
 * Description : This sample demonstrates how to export project object to aXML file, 
 * taking advantage of export module.
 */
public class ExportToAXML {
    private static final String COMMAND_NAME = "ExportToAXML";
    
	private static String clsName;
    private static String serviceName   =  "Project";    
   
    private static ProjectService agileStub;
    
    private static String programNumber;
    private static String programName;

    public ExportToAXML() {
             clsName = this.getClass().getName();

    }
      

    /**
     * The method setupServerLogin is used to login to the server by providing
     * the server url details and authentication credentials.
     * @param args 
     */
    
    public static void setupServerLogin(String[] args) throws Exception {       

         
    	//The ProjectService_Service is used to obtain a ProjectService 
    	URL url = new URL(args[0] + "/" + serviceName + "?WSDL");
        ProjectService_Service locator = new ProjectService_Service(url);

        //Eventually, the addApprovers webservice will be invoked by using this agileStub.
          agileStub = locator.getProject();


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

    public static void main(String[] args) {
        
        // This sample may be configured by either directly modifying the string values of
        // 'SERVER_URL', 'USERNAME' and 'PASSWORD' (defined in this class), or by
        // passing them as program arguments in the same order. In this sample, the
        // method 'checkArguments' checks for these values.
        checkArguments(args);         

        // Comment this method out if you intend to use your own data
        // or scenario by editing the static variables at the top of this code

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");
        
        try{
            
            // Login to the server by providing the server url and authentication credentials.
            setupServerLogin(args); 
            prepareData(args);
            

            ExportToAXMLRequestType exportToAXMLRequestType = new ExportToAXMLRequestType();                         
            AgileExportToAXMLRequestType agileExportToAXMLRequestType[] = new AgileExportToAXMLRequestType[1];
            agileExportToAXMLRequestType[0] = new AgileExportToAXMLRequestType();
            
            agileExportToAXMLRequestType[0].setFileName("output.aXML"); // file name of result file 
            agileExportToAXMLRequestType[0].setFilePath("C:\\temp\\timesheet"); //the URL to put the output file
            agileExportToAXMLRequestType[0].setFilter("Default Project Filter");
            AgileObjectIdentifierType agileObjectIdentifierType[] = new AgileObjectIdentifierType[1];
            agileObjectIdentifierType[0] = new AgileObjectIdentifierType();
            agileObjectIdentifierType[0].setObjectName(programNumber);
            agileExportToAXMLRequestType[0].getProgram().addAll(Arrays.asList(agileObjectIdentifierType));
           
            System.out.println("------------------Export searched Timesheet-----------------");
            exportToAXMLRequestType.getRequests().addAll(Arrays.asList(agileExportToAXMLRequestType));             
            ExportToAXMLResponseType ExportToAXMLResponse = agileStub.exportToAXML(exportToAXMLRequestType);
            System.out.println("STATUS CODE: " +  ExportToAXMLResponse.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !ExportToAXMLResponse.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = ExportToAXMLResponse.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
            }
                else{
                // If the webservice call was successful, then confirm the success of the operation
                    System.out.print("Program " + programName + "("+programNumber+")  was successfully exported.\n");                           
                    
                }

            }
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }       
    }

    public static void prepareData(String[] args) {
        try{
            System.out.println("<<< Preparing Data... >>>");
            DataPrepare dataPrepare = new DataPrepare(args[0], args[1], args[2]);
            dataPrepare.setupBusiness();
            programName = String.valueOf(System.currentTimeMillis());
            programName = "Program_" + programName;
            programNumber = dataPrepare.createProgramObject( programName, "ACTIVE" ); 
            System.out.println("<<< program is created : " + programNumber + ">>>");           
            System.out.println("<<< DataPrepare successful >>>");
        }
        catch(Exception e){            
            System.out.println("<<< ALERT: DataPrepare failed. Your server settings may be invalid, this sample might not run successfully.");
            System.out.println("To run the sample without the assitance of prepared data, you may edit the static variables after");
            System.out.println("creating the necessary prerequisites through the Agile web client and then execute the sample >>>");
        }  
        
    }


    public static void displayMessageElementValue(Element element){
        
        if(element.getFirstChild()!=null){                                          

            // Important Note: Certain message element values will be obtained 
            // as selections of type "AgileListEntryType". Such values must be
            // accessed as objects of AgileListEntryType by using the 'getObjectValue'
            // method of the message element and by casting the object appropriately.
            
            // The code below checks if the message element is of type AgileListEntryType.
            // If so, it retrieves the value information as a selection and displays the same.
        	if(element.getAttribute("xsi:type").contains("AgileListEntryType")){
                
            	AgileListEntryType list = (AgileListEntryType) WSUtil.unmarshalToAgileListEntryType(element);
                SelectionType selection[] = list.getSelection().toArray(new SelectionType[0]);
                for( SelectionType sel: selection){
                    System.out.println(sel.getValue());
                }
            }   
            // If the message element contains an ordinary text value, then print the same
            else
                System.out.println( element.getFirstChild().getNodeValue() );                                        
        }                                            
        else
            System.out.println();
        
    }

    
}


