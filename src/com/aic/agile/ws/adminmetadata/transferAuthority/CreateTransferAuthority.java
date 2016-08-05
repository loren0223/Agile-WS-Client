package com.aic.agile.ws.adminmetadata.transferAuthority;

import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;

import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileListEntryType;
import com.agile.ws.schema.common.v1.jaxws.AgileUserType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.metadata.v1.jaxws.AgileCreateTransferAuthorityRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.CreateTransferAuthorityRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.CreateTransferAuthorityResponseType;
import com.agile.ws.schema.metadata.v1.jaxws.GetUsersRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.GetUsersResponseType;
import com.agile.ws.schema.metadata.v1.jaxws.TransferAuthorityRequestType;
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
public class CreateTransferAuthority {      
    private static final String COMMAND_NAME = "CreateTransferAuthority";
    
	public static String clsName;
    public static String serviceName   =  "AdminMetadata";    
    public static AdminMetadataPortType agileStub;        

    public CreateTransferAuthority() {
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
                
                CreateTransferAuthorityRequestType createTransferAuthorityRequestType = new CreateTransferAuthorityRequestType(); 
                AgileCreateTransferAuthorityRequestType[] agileCreateTransferAuthorityRequestTypeArray = new AgileCreateTransferAuthorityRequestType[1]; 
                agileCreateTransferAuthorityRequestTypeArray[0] = new AgileCreateTransferAuthorityRequestType();
                 
                TransferAuthorityRequestType transferAuthorityRequestType = new TransferAuthorityRequestType(); 
                //loginID of from user 
                transferAuthorityRequestType.setFromUser("admin"); 
                //loginID of to user
                transferAuthorityRequestType.setToUser("antony");   
                //Set fromDate/toDate
                Calendar fromDate = Calendar.getInstance(); 
                Calendar toDate = (Calendar) fromDate.clone(); 
                toDate.add(Calendar.DAY_OF_YEAR, + 5); 
                XMLGregorianCalendar xFromDate = toXMLGregorianCalendar(fromDate);
                XMLGregorianCalendar xToDate = toXMLGregorianCalendar(toDate);
                transferAuthorityRequestType.setFromDate(xFromDate); 
                transferAuthorityRequestType.setToDate(xToDate); 
                //For an exhaustive list of various criterion that may be used, refer to the Java Client 
                transferAuthorityRequestType.setCriteria("AllChanges"); 
                //This field has two possible input values corresponding to the types 'AFFECT_CHANGES_ALL'(0) or 'AFFECT_CHANGES_IN_PERIOD'(1) 
                transferAuthorityRequestType.setAffectChanges(0); 

                agileCreateTransferAuthorityRequestTypeArray[0].setTransferAuthorityRecord(transferAuthorityRequestType); 
                createTransferAuthorityRequestType.getRequests().addAll(Arrays.asList(agileCreateTransferAuthorityRequestTypeArray)); 
                CreateTransferAuthorityResponseType createTransferAuthorityResponseType = agileStub.createTransferAuthority(createTransferAuthorityRequestType);
                
                
                System.out.println("STATUS CODE: " +  createTransferAuthorityResponseType.getStatusCode() );
                
                
                // If the status code is not 'SUCCESS', then populate the list of exceptions
                // returned by the webservice. 
                if( !createTransferAuthorityResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){

                RunAllSamples.reportFailure( clsName );
                    
                    AgileExceptionListType[] agileExceptionListType = createTransferAuthorityResponseType.getExceptions().toArray(new AgileExceptionListType[0]);
                    for(int i=0; i<agileExceptionListType.length; i++){
                        AgileExceptionType exceptions[] = agileExceptionListType[i].getException().toArray(new AgileExceptionType[0]);
                        for(int j=0; j<exceptions.length; j++)
                            System.out.println(exceptions[j].getMessage() );
                    }
                    
                }
                    else{
                    // If the webservice call was successful, then display the users retrieved
                        /*
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
						
                        */
                    }


            }
            
            catch (Exception e) {            
             RunAllSamples.reportFailure( clsName );
             System.out.println("Exceptions: ");
             e.printStackTrace();
            }              
        }

    public static XMLGregorianCalendar toXMLGregorianCalendar(Calendar c)
    		 throws DatatypeConfigurationException {
    		 GregorianCalendar gc = new GregorianCalendar();
    		 gc.setTimeInMillis(c.getTimeInMillis());
    		 XMLGregorianCalendar xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
    		 return xc;
    		}

}
