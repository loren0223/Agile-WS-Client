package adminmetadata.convertcurrency;

import java.net.URL;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;

import run.RunAllSamples;

import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileMoneyType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningListType;
import com.agile.ws.schema.common.v1.jaxws.AgileWarningType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.metadata.v1.jaxws.AgileConvertCurrencyRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.AgileConvertCurrencyResponseType;
import com.agile.ws.schema.metadata.v1.jaxws.ConvertCurrencyRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.ConvertCurrencyResponseType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataPortType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataService;


/**
* Sample      : ConvertCurrency  
* Type         : AdminMetadata webservice 
* 
* Description : The sample demonstrates usage of the convertCurrency webservice
* to convert money from one currency to another given a certain date. 
* The money is expressed as an object of type AgileMoneyType.
*/
public class ConvertCurrency {
	private static final String COMMAND_NAME = "ConvertCurrency";
	
    public static String clsName;
    public static String serviceName   =  "AdminMetadata";    
    public static AdminMetadataPortType agileStub;

    public ConvertCurrency() {
           clsName = this.getClass().getName();
    }
      

    /**
     * The method setupServerLogin is used to login to the server by providing
     * the server url details and authentication credentials.
     * @param args 
     */
    
    private static void setupServerLogin(String[] args) throws Exception {
       
         
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

    public static void main(String[] args) {
        checkArguments(args);        

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Executing webservice sample: ");
        
        try{
            
            // Login to the server by providing the server url and authentication credentials.
            setupServerLogin(args);             
            
            // Create the request object ConvertCurrencyRequestType for the convertCurrency operation
            // Create an array of requests of type AgileGetAttributesRequestType. Batch operations may be
            // performed by populating as many request objects as required to retrieve several attributes
            ConvertCurrencyRequestType convertCurrencyRequestType = new ConvertCurrencyRequestType();                                        
            AgileConvertCurrencyRequestType agileConvertCurrencyRequestType[] = new AgileConvertCurrencyRequestType[1];                         
            agileConvertCurrencyRequestType[0] = new AgileConvertCurrencyRequestType();           
            
            // For each batched request, we declare the specifications for which currecy will be converted
            // Money is specified using an object of AgileMoneyType. Date and new currency are also specified.
            AgileMoneyType money = new AgileMoneyType();
            money.setAmount(new Double(100));
            money.setCurrency("INR");
            agileConvertCurrencyRequestType[0].setMoney(money);  
            GregorianCalendar cal = new GregorianCalendar();
            XMLGregorianCalendar gc = null;
            try {
                gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            } catch (Exception e) {
     
                 e.printStackTrace();
            }

            agileConvertCurrencyRequestType[0].setDate(gc);
            agileConvertCurrencyRequestType[0].setToCurrency("GBP");            



            // The request objects are set and the agile Stub is used to make the convertCurrency
            // webservice call. The status code obtained from the response object is printed to
            // verify the success of the convertCurrency operation. 
            convertCurrencyRequestType.getRequests().addAll(Arrays.asList(agileConvertCurrencyRequestType));             
            ConvertCurrencyResponseType convertCurrencyResponseType = agileStub.convertCurrency(convertCurrencyRequestType);
            System.out.println("STATUS CODE: " +  convertCurrencyResponseType.getStatusCode() );
            
            
            // If the status code is not 'SUCCESS', then populate the list of exceptions
            // returned by the webservice. 
            if( !convertCurrencyResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                
                AgileExceptionListType[] agileExceptionListType = convertCurrencyResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                for(int i=0; i<agileExceptionListType.length; i++){
                    AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                    for(int j=0; j<exceptions.length; j++)
                        System.out.println(exceptions[j].getMessage() );
                }
                
                AgileWarningListType agileWarningListType[] = convertCurrencyResponseType.getWarnings().toArray(new AgileWarningListType[0]);
                if(agileWarningListType!=null)
                for(int i=0; i<agileWarningListType.length; i++){
                    AgileWarningType warnings[] = agileWarningListType[i].getWarning().toArray(new AgileWarningType[0]);
                    for(int j=0; j<warnings.length; j++)
                        System.out.println("Warning Id: " + warnings[j].getWarningId() + "\nMessage: " + warnings[j].getMessage() );                    
                }                    
                
            }
                else{
                // If the webservice call was successful, then display the users retrieved
                    
                    AgileConvertCurrencyResponseType responses[] = convertCurrencyResponseType.getResponses().toArray(new AgileConvertCurrencyResponseType[0]);                
                    
                    for(int j=0; j<responses.length; j++){                       

                        System.out.println("Currency :" + responses[j].getMoney().getCurrency() );
                        System.out.println("Amount: " + responses[j].getMoney().getAmount() );                        
                        System.out.println("Date: " + responses[j].getDate().toGregorianCalendar().getTime() );

                    }                    
                }

            }
          
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }        
    }

	/**
	 * This sample can be configured by passing server url, user name and
	 * password as program arguments in the same order. This method checks for
	 * these values.
	 * 
	 * @param args
	 */
    private static void checkArguments(String[] args){
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
}
