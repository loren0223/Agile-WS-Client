package project.getCalendar;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;

import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileListEntryType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.common.v1.jaxws.SelectionType;
import com.agile.ws.schema.project.v1.jaxws.AgileGetCalendarRequestType;
import com.agile.ws.schema.project.v1.jaxws.GetCalendarRequestType;
import com.agile.ws.schema.project.v1.jaxws.GetCalendarResponseType;
import com.agile.ws.schema.project.v1.jaxws.PPMCalendarType;
import com.agile.ws.service.project.v1.jaxws.ProjectService;
import com.agile.ws.service.project.v1.jaxws.ProjectService_Service;
import common.util.WSUtil;
import run.RunAllSamples;


/**
 * Sample      : GetCalendar
 * Category    : Project webservice
 *
 * Description : This sample demonstrates how to get calendar in Agile.
 * 
 * Note:- testing user must be allowed to do Get operation for Calendar.
 */
public class GetCalendar {
    private static final String COMMAND_NAME = "GetCalendar";
    
    private static String serviceName   =  "Project";    
    
    private static ProjectService agileStub;
    
    private static String calendarName =  "WS_Calendar_1";

    public GetCalendar() {
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
            
            GetCalendarRequestType getCalendarRequestType = new GetCalendarRequestType();                         
            AgileGetCalendarRequestType  agileGetCalendarRequestType[] = new AgileGetCalendarRequestType[1];                         
            agileGetCalendarRequestType[0] = new AgileGetCalendarRequestType();          
            
            agileGetCalendarRequestType[0].setCalendarIdentifier(calendarName);
            
            System.out.println("------------------Get calendar-------------------");
                    
            getCalendarRequestType.getRequests().addAll(Arrays.asList(agileGetCalendarRequestType));             
            GetCalendarResponseType getCalendarResponse = agileStub.getCalendar(getCalendarRequestType);
            System.out.println("STATUS CODE: " +  getCalendarResponse.getStatusCode() );
            

            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !getCalendarResponse.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){

                
                AgileExceptionListType[] agileExceptionListType = getCalendarResponse.getExceptions().toArray(new AgileExceptionListType[0]);
                if(agileExceptionListType!=null)
                for(int i=0; i<agileExceptionListType.length; i++){
                    
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
            }
                else{
                // If the webservice call was successful, then confirm the success of the operation
                    System.out.print("Calendar was successfully loaded.\n");
                    PPMCalendarType getCalendar = getCalendarResponse.getResponses().get(0).getAgileCalendar();
                    System.out.println("name:" +getCalendar.getCalendarName());
                  for(int i=0;i<getCalendar.getAny().size() ;i++){
                	  displayMessageElementValue((Element) getCalendar.getAny().get(i));
                  }
                }

            }
            catch (Exception e) {            
             System.out.println("Exceptions: ");
             e.printStackTrace();
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


