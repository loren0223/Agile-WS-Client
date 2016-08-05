
package project.createCalendar;

import java.net.URL;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;

import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileListEntryType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.common.v1.jaxws.SelectionType;
import com.agile.ws.schema.project.v1.jaxws.*;
import com.agile.ws.service.project.v1.jaxws.ProjectService;
import com.agile.ws.service.project.v1.jaxws.ProjectService_Service;
import common.util.WSUtil;

import run.DataPrepare;
import run.RunAllSamples;


/**
 * Sample      : CreateCalendar
 * Category    : Project webservice
 *
 * Description : This sample demonstrates the creation of a new calendar.
 */
public class CreateCalendar {
    private static final String COMMAND_NAME = "CreateCalendar";
    
	private static String clsName;
    private static String serviceName   =  "Project";    
    
    private static String name = "Test_Calendar_";
    private static String templateName = "Template_Calendar_";
    private static String description = "This is a calendar created via web service.";
    private static String enabled = "Yes"; // Yes/No list
    private static ProjectService agileStub;
    /*
     * Note that for this parameter customers need to pass in the non-working days.
								 eg. If working days are from Mon to Fri then customers need to pass in "1,7".
								 1 - Sun
								 2 - Mon
								 3 - Tue
								 4 - Wed
								 5 - Thu
								 6 - Fri
								 7 - Sat
     */
    private static String weekSetting = "1,7";
    private static String createFrom;

    public CreateCalendar() {
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
            
            // Create the request object CraeteCalendarRequestType for the CraeteCalendar operation
            // Create an array of requests of type AgileCreateCalendarRequestType. Batch operations may be
            // performed by populating as many request objects as required to create several transfer authority records.

            CreateCalendarRequestType createCalendarRequestType = new CreateCalendarRequestType();                         
            AgileCreateCalendarRequestType  agileCreateCalendarRequestType = new AgileCreateCalendarRequestType();                         
            
            // For each batched request, specify the name of calendar, description, enabled, workWeekSetting, and template calendar.
            // Template calendar here is the one created in data prepare.
            templateName = templateName + System.currentTimeMillis();
            agileCreateCalendarRequestType.setName(templateName);
            agileCreateCalendarRequestType.setEnabled(enabled);
            agileCreateCalendarRequestType.setDescription(description);
            agileCreateCalendarRequestType.setWorkWeekSetting(weekSetting);
            agileCreateCalendarRequestType.setCreateFrom(name); 
            System.out.println("------------------Create Calendar from Template Calendar-----------------");
            createCalendarRequestType.getRequests().add(agileCreateCalendarRequestType);             
            CreateCalendarResponseType CreateCalendarResponse = agileStub.createCalendar(createCalendarRequestType);
            System.out.println("STATUS CODE: " +  CreateCalendarResponse.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !CreateCalendarResponse.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = CreateCalendarResponse.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
            }
                else{
                // If the webservice call was successful, then confirm the success of the operation
                	System.out.print("Calendar name => " + templateName + "\n");
                	System.out.print("Craeted from => " + name + "\n");
                    System.out.print("Calendar was successfully created.\n");         
                    
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
            dataPrepare.setupAdminMetadata(); 
            dataPrepare.setupBusiness();
            dataPrepare.setupCollaboration();
            dataPrepare.setupTable();
            CreateCalendarRequestType createCalendarRequestType = new CreateCalendarRequestType();                         
            AgileCreateCalendarRequestType  agileCreateCalendarRequestType = new AgileCreateCalendarRequestType();                         
            
            // For each batched request, specify the name of calendar, description, enabled, workWeekSetting, and template calendar.
            // Template calendar here is the one craeted in data prepare.
            name = name + System.currentTimeMillis();
            agileCreateCalendarRequestType.setName(name);
            agileCreateCalendarRequestType.setEnabled(enabled);
            agileCreateCalendarRequestType.setDescription(description);
            agileCreateCalendarRequestType.setWorkWeekSetting(weekSetting);
            System.out.println("------------------Create a new Calendar-----------------");
            createCalendarRequestType.getRequests().add(agileCreateCalendarRequestType);             
            CreateCalendarResponseType CreateCalendarResponse = agileStub.createCalendar(createCalendarRequestType);
            System.out.println("Calendar name => " + name);
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


