package run;

import com.agile.api.ChangeConstants;
import com.agile.api.ItemConstants;
import com.agile.api.ManufacturingSiteConstants;
import com.agile.api.ServiceRequestConstants;
import com.agile.api.UserConstants;
import com.agile.ws.schema.attachment.v1.jaxws.AddFileAttachmentRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.AddFileAttachmentResponseType;
import com.agile.ws.schema.attachment.v1.jaxws.AddFileFFRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.AddFileFFResponseType;
import com.agile.ws.schema.attachment.v1.jaxws.AddFileFFType;
import com.agile.ws.schema.attachment.v1.jaxws.AgileAddFileAttachmentRequest;
import com.agile.ws.schema.attachment.v1.jaxws.AgileAddFileAttachmentRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.AgileAddFileFFRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.AgileCheckInFFRequest;
import com.agile.ws.schema.attachment.v1.jaxws.AgileCheckOutFFRequest;
import com.agile.ws.schema.attachment.v1.jaxws.CheckInFFRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.CheckInFFResponseType;
import com.agile.ws.schema.attachment.v1.jaxws.CheckOutFFRequestType;
import com.agile.ws.schema.attachment.v1.jaxws.CheckOutFFResponseType;
import com.agile.ws.schema.business.v1.jaxws.AgileCreateObjectRequest;
import com.agile.ws.schema.business.v1.jaxws.AgileDeleteObjectRequest;
import com.agile.ws.schema.business.v1.jaxws.AgileSaveAsObjectRequestType;
import com.agile.ws.schema.business.v1.jaxws.AgileUpdateObjectRequest;
import com.agile.ws.schema.business.v1.jaxws.CreateObjectRequestType;
import com.agile.ws.schema.business.v1.jaxws.CreateObjectResponseType;
import com.agile.ws.schema.business.v1.jaxws.DeleteObjectRequestType;
import com.agile.ws.schema.business.v1.jaxws.DeleteObjectResponseType;
import com.agile.ws.schema.business.v1.jaxws.SaveAsObjectRequestType;
import com.agile.ws.schema.business.v1.jaxws.SaveAsObjectResponseType;
import com.agile.ws.schema.business.v1.jaxws.UpdateObjectRequestType;
import com.agile.ws.schema.business.v1.jaxws.UpdateObjectResponseType;
import com.agile.ws.schema.collaboration.v1.jaxws.AddApproversRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.AddApproversResponseType;
import com.agile.ws.schema.collaboration.v1.jaxws.AddReviewersRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.AddReviewersResponseType;
import com.agile.ws.schema.collaboration.v1.jaxws.AgileAddApproversRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.AgileAddReviewersRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.AgileChangeStatusRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.AgileGetStatusRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.AgileGetStatusResponseType;
import com.agile.ws.schema.collaboration.v1.jaxws.AgileGetWorkflowsRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.AgileGetWorkflowsResponseType;
import com.agile.ws.schema.collaboration.v1.jaxws.AgileSetWorkFlowRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.AgileWorkflowType;
import com.agile.ws.schema.collaboration.v1.jaxws.ChangeStatusRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.ChangeStatusResponseType;
import com.agile.ws.schema.collaboration.v1.jaxws.GetStatusRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.GetStatusResponseType;
import com.agile.ws.schema.collaboration.v1.jaxws.GetWorkflowsRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.GetWorkflowsResponseType;
import com.agile.ws.schema.collaboration.v1.jaxws.SetWorkFlowRequestType;
import com.agile.ws.schema.collaboration.v1.jaxws.SetWorkFlowResponseType;
import com.agile.ws.schema.common.v1.jaxws.AdminListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionListType;
import com.agile.ws.schema.common.v1.jaxws.AgileExceptionType;
import com.agile.ws.schema.common.v1.jaxws.AgileListEntryType;
import com.agile.ws.schema.common.v1.jaxws.AgileRowType;
import com.agile.ws.schema.common.v1.jaxws.AgileTableType;
import com.agile.ws.schema.common.v1.jaxws.AgileUserUserGroupIdentifierType;
import com.agile.ws.schema.common.v1.jaxws.AutoNumberType;
import com.agile.ws.schema.common.v1.jaxws.ObjectFactory;
import com.agile.ws.schema.common.v1.jaxws.ObjectReferentIdType;
import com.agile.ws.schema.common.v1.jaxws.PropertyType;
import com.agile.ws.schema.common.v1.jaxws.ResponseStatusCode;
import com.agile.ws.schema.common.v1.jaxws.SchemaConstants;
import com.agile.ws.schema.common.v1.jaxws.SelectionType;
import com.agile.ws.schema.metadata.v1.jaxws.AgileGetAutoNumbersRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.AgileGetAutoNumbersResponseType;
import com.agile.ws.schema.metadata.v1.jaxws.AgileGetListsRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.AgileGetListsResponseType;
import com.agile.ws.schema.metadata.v1.jaxws.GetAutoNumbersRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.GetAutoNumbersResponseType;
import com.agile.ws.schema.metadata.v1.jaxws.GetListsRequestType;
import com.agile.ws.schema.metadata.v1.jaxws.GetListsResponseType;
import com.agile.ws.schema.table.v1.jaxws.AddRowsRequestType;
import com.agile.ws.schema.table.v1.jaxws.AddRowsResponseType;
import com.agile.ws.schema.table.v1.jaxws.AgileAddRowsRequest;
import com.agile.ws.schema.table.v1.jaxws.AgileUpdateRow;
import com.agile.ws.schema.table.v1.jaxws.AgileUpdateRowsRequest;
import com.agile.ws.schema.table.v1.jaxws.LoadTableRequestType;
import com.agile.ws.schema.table.v1.jaxws.LoadTableResponseType;
import com.agile.ws.schema.table.v1.jaxws.RequestTableType;
import com.agile.ws.schema.table.v1.jaxws.UpdateRowsRequestType;
import com.agile.ws.schema.table.v1.jaxws.UpdateRowsResponseType;
import com.agile.ws.service.attachment.v1.jaxws.AttachmentPortType;
import com.agile.ws.service.attachment.v1.jaxws.AttachmentService;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectPortType;
import com.agile.ws.service.business.v1.jaxws.BusinessObjectService;
import com.agile.ws.service.collaboration.v1.jaxws.CollaborationPortType;
import com.agile.ws.service.collaboration.v1.jaxws.CollaborationService;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataPortType;
import com.agile.ws.service.metadata.v1.jaxws.AdminMetadataService;
import com.agile.ws.service.table.v1.jaxws.TablePortType;
import com.agile.ws.service.table.v1.jaxws.TableService;
import common.util.WSUtil;


import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.w3c.dom.Element;
import org.w3c.dom.Node;



public class DataPrepare {    
    private static final String COMMAND_NAME = "DataPrepare";
    
	public String USERNAME   =  null;
    public String PASSWORD   =  null;
    public String SERVER_INF =  null;
    
    public AdminMetadataPortType adminAgileStub;
    public AttachmentPortType attachmentAgileStub;
    public BusinessObjectPortType businessAgileStub;
    public TablePortType tableAgileStub;
    public CollaborationPortType collabAgileStub;
    private final String COMMONNAMESPACEURI = "http://xmlns.oracle.com/AgileObjects/Core/Common/V1";
    
    public DataPrepare(String serverUrl, String userName, String password) {
    	this.SERVER_INF = serverUrl;
    	this.USERNAME = userName;
    	this.PASSWORD = password;
    }    


    public void setupTable() throws Exception {                
         
         //The TableServiceLocator is used to obtain a Table_BindingStub
    	URL url = new URL(SERVER_INF+"/Table?WSDL");
         TableService locator = new TableService(url);

        //A stub of type Table_BindingStub is obtained by specifying the server URL
        //Eventually, the addRows webservice will be invoked by using this agileStub.
         tableAgileStub =  locator.getTable();
  
        // Username and password details are specified for the agileStub
         
         Map<String, Object> reqContext = ((BindingProvider)tableAgileStub).getRequestContext();
         reqContext.put(BindingProvider.USERNAME_PROPERTY, USERNAME);
         reqContext.put(BindingProvider.PASSWORD_PROPERTY, PASSWORD);
     }
    
    
    public void setupCollaboration() throws Exception {                
         
         //The CollaborationServiceLocator is used to obtain a Collaboration_BindingStub
    	URL url = new URL(SERVER_INF+"/Collaboration?WSDL");
    	CollaborationService locator =  new CollaborationService(url);

        //A stub of type Collaboration_BindingStub is obtained by specifying the server URL
        //Eventually, the addApprovers webservice will be invoked by using this agileStub.
          collabAgileStub = locator.getCollaboration();


        // Username and password details are specified for the agileStub
          Map<String, Object> reqContext = ((BindingProvider)collabAgileStub).getRequestContext();
          reqContext.put(BindingProvider.USERNAME_PROPERTY, USERNAME);
          reqContext.put(BindingProvider.PASSWORD_PROPERTY, PASSWORD);
     }
    
    
    public void setupBusiness() throws Exception {         
        
        //The BusinessObjectServiceLocator is used to obtain a BusinessObject_BindingStub
    	URL url = new URL(SERVER_INF+"/BusinessObject?WSDL");
    	BusinessObjectService locator = 
             new BusinessObjectService(url);

        //A stub of type BusinessObject_BindingStub is obtained by specifying the server URL
        //Eventually, the checkPrivilege webservice will be invoked by using this agileStub.
          businessAgileStub = locator.getBusinessObject();
          

        // Username and password details are specified for the agileStub
          Map<String, Object> reqContext = ((BindingProvider)businessAgileStub).getRequestContext();
          reqContext.put(BindingProvider.USERNAME_PROPERTY, USERNAME);
          reqContext.put(BindingProvider.PASSWORD_PROPERTY, PASSWORD);
     }
    
    
    public void setupAttachment() throws Exception {       

         
         //The AttachmentServiceLocator is used to obtain a AdminMetadata_BindingStub
    	URL url = new URL(SERVER_INF+"/Attachment?WSDL");
         AttachmentService locator = 
             new AttachmentService(url);

        //A stub of type Attachment_BindingStub is obtained by specifying the server URL
        //Eventually, the addFileAttachment webservice will be invoked by using this agileStub.
          attachmentAgileStub = locator.getAttachment();
                

        // Username and password details are specified for the agileStub
          Map<String, Object> reqContext = ((BindingProvider)attachmentAgileStub).getRequestContext();
          reqContext.put(BindingProvider.USERNAME_PROPERTY, USERNAME);
          reqContext.put(BindingProvider.PASSWORD_PROPERTY, PASSWORD);
     }

    
    public void setupAdminMetadata() throws Exception {
         
        //The AdminMetadataServiceLocator is used to obtain a AdminMetadata_BindingStub
    	URL url = new URL(SERVER_INF+"/AdminMetadata?WSDL");
    	AdminMetadataService locator = 
             new AdminMetadataService(url);

        //A stub of type AdminMetadata_BindingStub is obtained by specifying the server URL
        //Eventually, the convertCurrency webservice will be invoked by using this agileStub.
          adminAgileStub = locator.getAdminMetadata();
                 

        // Username and password details are specified for the agileStub
          Map<String, Object> reqContext = ((BindingProvider)adminAgileStub).getRequestContext();
          reqContext.put(BindingProvider.USERNAME_PROPERTY, USERNAME);
          reqContext.put(BindingProvider.PASSWORD_PROPERTY, PASSWORD);
     }
    
    public String getNextAutoNumber(String cls) throws Exception{
        
        GetAutoNumbersRequestType getAutoNumbersRequestType = new GetAutoNumbersRequestType();
        AgileGetAutoNumbersRequestType agileGetAutoNumbersRequestType[] = new AgileGetAutoNumbersRequestType[1];        
        agileGetAutoNumbersRequestType[0] = new AgileGetAutoNumbersRequestType();        

        agileGetAutoNumbersRequestType[0].setClassIdentifier(cls);
        agileGetAutoNumbersRequestType[0].setIncludeAllAutoNumberSource(true);
        agileGetAutoNumbersRequestType[0].setSize(1);        
        getAutoNumbersRequestType.getRequests().addAll(Arrays.asList(agileGetAutoNumbersRequestType));
        
        GetAutoNumbersResponseType getAutoNumbersResponseType = adminAgileStub.getAutoNumbers(getAutoNumbersRequestType);        

        if( !getAutoNumbersResponseType.getStatusCode().equals(ResponseStatusCode.SUCCESS) )
            throw new Exception("getNextAutoNumber failed in Data Preparation");        
        else{

            AgileGetAutoNumbersResponseType responses[] = getAutoNumbersResponseType.getAutoNumberResponses().toArray(new AgileGetAutoNumbersResponseType[0]);            
            if(responses!=null)
            for(int i=0; i<responses.length; i++){
                AutoNumberType[] autonumbers = responses[i].getAutoNumbers().toArray(new AutoNumberType[0]);
                if(autonumbers!=null)
                    for(int j=0; j< autonumbers.length; j++)
                        return autonumbers[j].getAutoNumber().get(0);                
            } 
            
        }
        return null;
    }
    
    public String createNewObject(String cls) throws Exception{        

        CreateObjectRequestType createObjectRequestType = new CreateObjectRequestType();                         
        AgileCreateObjectRequest agileCreateObjectRequest[] = new AgileCreateObjectRequest[1];                     
        
        agileCreateObjectRequest[0] = new AgileCreateObjectRequest();                   
        agileCreateObjectRequest[0].setClassIdentifier( cls );
        AgileRowType row_1 = new AgileRowType();            
        String objectNumber = getNextAutoNumber( cls );
        
        Element number = WSUtil.createMessageElement("number");
        number.setTextContent(objectNumber);
        row_1.getAny().add(number);
        
        Element description = WSUtil.createMessageElement("description");
        description.setTextContent("Object Desc");
        row_1.getAny().add(description);
                              
        agileCreateObjectRequest[0].setData(row_1);   
        createObjectRequestType.getRequests().addAll(Arrays.asList(agileCreateObjectRequest));            
        CreateObjectResponseType createObjectResponseType = businessAgileStub.createObject(createObjectRequestType);        

        if( !createObjectResponseType.getStatusCode().equals(ResponseStatusCode.SUCCESS) ){
        	
        	if(createObjectResponseType.getExceptions().get(0).getException().get(0).getExceptionId().equals("403"))
                return createNewObject(cls);           
                
            throw new Exception("createObject failed in Data Preparation");        
        }
            
        else
            return objectNumber;
        
        
    }
    public String createNewReviewer(String cls, String[] jobFunctions) throws Exception{        
      CreateObjectRequestType createObjectRequestType = new CreateObjectRequestType();                         
      AgileCreateObjectRequest agileCreateObjectRequest[] = new AgileCreateObjectRequest[1];                     

      agileCreateObjectRequest[0] = new AgileCreateObjectRequest();                   
      agileCreateObjectRequest[0].setClassIdentifier(cls);
      AgileRowType row = new AgileRowType();            

      String user = "User" + System.currentTimeMillis();
      
      Element userid = WSUtil.createMessageElement("userID");
      userid.setTextContent(user);
      
      Element key_1 = WSUtil.createMessageElement("key-1");
      key_1.setTextContent(PASSWORD);
      key_1.setAttribute("attributeId","-1");
 
      

      SelectionType[] entries = new SelectionType[jobFunctions.length];
      SelectionType listEntry;
      for (int i=0;i<entries.length;i++){
        listEntry = new SelectionType();
        listEntry.setValue(jobFunctions[i]);
        entries[i] = listEntry;
      }
      AgileListEntryType listType = new AgileListEntryType();
      listType.getSelection().addAll(Arrays.asList(entries));
      
   
      Element jobFunctions_1 = WSUtil.createMessageElement("JobFunctions",listType);


      entries = new SelectionType[5];

      listEntry = new SelectionType();
      listEntry.setValue("Administrator");
      entries[0] = listEntry;

      listEntry = new SelectionType();
      listEntry.setValue("Approve / Reject");
      entries[1] = listEntry;

      listEntry = new SelectionType();
      listEntry.setValue("Acknowledge");
      entries[2] = listEntry;

      listEntry = new SelectionType();
      listEntry.setApiName("ChangeAnalyst");
      listEntry.setValue("Change Analyst");
      entries[3] = listEntry;

      listEntry = new SelectionType();
      listEntry.setValue("My User Profile");
      entries[4] = listEntry;

      listType = new AgileListEntryType();
      listType.getSelection().addAll(Arrays.asList(entries));

      
      Element roleS = WSUtil.createMessageElement("roleS",listType);
      
      row.getAny().add(userid);
      row.getAny().add(key_1);
      row.getAny().add(jobFunctions_1);
      row.getAny().add(roleS);
                             
      agileCreateObjectRequest[0].setData(row);       
      createObjectRequestType.getRequests().addAll(Arrays.asList(agileCreateObjectRequest));             
      CreateObjectResponseType createObjectResponseType = businessAgileStub.createObject(createObjectRequestType);        

      if( !createObjectResponseType.getStatusCode().equals(ResponseStatusCode.SUCCESS) ){
        if(createObjectResponseType.getExceptions().get(0).getException().get(0).getExceptionId().equals("403"))
          return createNewObject(cls);           

        throw new Exception("createNewReviewer failed in Data Preparation");        
      }
      else
        return user;
    }
    
    public String[] createNewRefObject(String cls) throws Exception{        
      String[] result = new String[2];
      CreateObjectRequestType createObjectRequestType = new CreateObjectRequestType();                         
      AgileCreateObjectRequest agileCreateObjectRequest[] = new AgileCreateObjectRequest[1];                     
      
      agileCreateObjectRequest[0] = new AgileCreateObjectRequest();                   
      agileCreateObjectRequest[0].setClassIdentifier( cls );
      AgileRowType row_1 = new AgileRowType();            
      String objectNumber = "REF"+System.currentTimeMillis();
      
      Element number = WSUtil.createMessageElement("Name");
      number.setTextContent(objectNumber);
      row_1.getAny().add(number);
      
      Element desc = WSUtil.createMessageElement("Description");
      desc.setTextContent("Object Desc");
      row_1.getAny().add(desc);
      
      Element referencedObjKey = WSUtil.createMessageElement("referencedObjKey");
      String refPubKey = new Integer(new Random().nextInt(100)).toString()+"|"+ new Integer(new Random().nextInt(100)).toString();
      referencedObjKey.setTextContent(refPubKey);
      row_1.getAny().add(referencedObjKey);
                           
      agileCreateObjectRequest[0].setData(row_1);    
      createObjectRequestType.getRequests().addAll(Arrays.asList(agileCreateObjectRequest));            
      CreateObjectResponseType createObjectResponseType = businessAgileStub.createObject(createObjectRequestType);        
      result[0]=objectNumber;
      result[1]=refPubKey;
      if( !createObjectResponseType.getStatusCode().equals(ResponseStatusCode.SUCCESS) ){                       
          throw new Exception("createObject failed in Data Preparation");        
      }         
      else
          return result;
      
  }
    public void createObjectByTags(String cls, String tags[], String values[] ) throws Exception{        

        CreateObjectRequestType createObjectRequestType = new CreateObjectRequestType();                         
        AgileCreateObjectRequest agileCreateObjectRequest[] = new AgileCreateObjectRequest[1];                     
        
        agileCreateObjectRequest[0] = new AgileCreateObjectRequest();                   
        agileCreateObjectRequest[0].setClassIdentifier( cls );
        AgileRowType row_1 = new AgileRowType();            
                    
        String namespaceUri = null;                        
        
        for(int i=0; i<tags.length; i++){
        	 Element el;
            try{
                int num = Integer.parseInt( tags[i] );
               
                 el = WSUtil.createMessageElement("key" + tags[i]);
                el.setAttribute("attributeId", tags[i]);
               
            }
            catch(Exception e){
            	  el = WSUtil.createMessageElement(tags[i]);
            }
            el.setTextContent( values[i] ); 
            row_1.getAny().add(el);
        }
                            
        agileCreateObjectRequest[0].setData(row_1);       
        createObjectRequestType.getRequests().addAll(Arrays.asList(agileCreateObjectRequest));             
        CreateObjectResponseType createObjectResponseType = businessAgileStub.createObject(createObjectRequestType);        

        if( !createObjectResponseType.getStatusCode().equals(ResponseStatusCode.SUCCESS) )
            throw new Exception("createObject failed in Data Preparation");        
        
    }
    
    public void createObjectByTags(String cls, String tags[], String values[], String types[] ) throws Exception{        

        CreateObjectRequestType createObjectRequestType = new CreateObjectRequestType();                         
        AgileCreateObjectRequest agileCreateObjectRequest[] = new AgileCreateObjectRequest[1];                     
        
        agileCreateObjectRequest[0] = new AgileCreateObjectRequest();                   
        agileCreateObjectRequest[0].setClassIdentifier( cls );
        AgileRowType row_1 = new AgileRowType();            
                     
        String namespaceUri = null;                        
        
        for(int i=0; i<tags.length; i++){
        	Element el=null;
            
            if(types[i].equals("String"))
            {
            	
            	 el = WSUtil.createMessageElement(tags[i]);
            	 el.setTextContent( values[i] );
            	 row_1.getAny().add(el);
            }
            else
            if(types[i].equals("AgileListEntryType")){                
                String COMMONNAMESPACEURI = "http://xmlns.oracle.com/AgileObjects/Core/Common/V1";
                AgileListEntryType list = new AgileListEntryType();
                SelectionType[] multiSelect = new SelectionType[1];                
                multiSelect[0] = new SelectionType();
                multiSelect[0].setValue( values[i] );                 
                list.getSelection().addAll(Arrays.asList(multiSelect));
                
                el = WSUtil.createMessageElement( tags[i],list);
                row_1.getAny().add(el);
            }
           
        }
                              
        agileCreateObjectRequest[0].setData(row_1);       
        createObjectRequestType.getRequests().addAll(Arrays.asList(agileCreateObjectRequest));             
        CreateObjectResponseType createObjectResponseType = businessAgileStub.createObject(createObjectRequestType);        

        if( !createObjectResponseType.getStatusCode().equals(ResponseStatusCode.SUCCESS) )
            throw new Exception("createObject failed in Data Preparation");        
        
    }
    
    public void checkOutFolder(String folderName) throws Exception{       
        
        CheckOutFFRequestType checkOutFFRequestType = new CheckOutFFRequestType();                         
        AgileCheckOutFFRequest agileCheckOutFFRequest[] = new AgileCheckOutFFRequest[1];                         
        agileCheckOutFFRequest[0] = new AgileCheckOutFFRequest();           
        agileCheckOutFFRequest[0].setFolderNumber( folderName );            
        
        checkOutFFRequestType.getRequests().addAll(Arrays.asList(agileCheckOutFFRequest));             
        CheckOutFFResponseType checkOutFFResponseType = attachmentAgileStub.checkOutFF(checkOutFFRequestType);

        if( !checkOutFFResponseType.getStatusCode().equals(ResponseStatusCode.SUCCESS) )            
            throw new Exception("checkOutFolder failed in Data Preparation");        
    }
    
    public void checkInFolder(String folderNumber) throws Exception{
        
        CheckInFFRequestType checkInFFRequestType = new CheckInFFRequestType();                         
        AgileCheckInFFRequest agileCheckInFFRequest[] = new AgileCheckInFFRequest[1];                         
        agileCheckInFFRequest[0] = new AgileCheckInFFRequest();                 
        agileCheckInFFRequest[0].setFolderNumber( folderNumber );    
        checkInFFRequestType.getRequests().addAll(Arrays.asList(agileCheckInFFRequest));             
        
        CheckInFFResponseType CheckInFFResponseType = attachmentAgileStub.checkInFF(checkInFFRequestType);
        if( !CheckInFFResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) )
            throw new Exception("checkInFolder failed in Data Preparation");        
    }
    
    
    public void addAttachmentToObject(String cls, String objectNumber, String fileName[]) throws Exception{       
        
        AddFileAttachmentRequestType addFileAttachmentRequestType = new AddFileAttachmentRequestType();                         
        AgileAddFileAttachmentRequest agileAddFileAttachmentRequest[] = new AgileAddFileAttachmentRequest[1];                         
        agileAddFileAttachmentRequest[0] = new AgileAddFileAttachmentRequest();           

        agileAddFileAttachmentRequest[0].setClassIdentifier( cls );                        
        agileAddFileAttachmentRequest[0].setObjectNumber( objectNumber );        

        AgileAddFileAttachmentRequestType attachments[] = new AgileAddFileAttachmentRequestType[fileName.length];                                        
        
        for(int i=0; i<fileName.length; i++){
            
            attachments[i] = new AgileAddFileAttachmentRequestType();                
            attachments[i].setName(fileName[i]);
            attachments[i].setDescription("Description for file " + (i+1) );
            File file = File.createTempFile(fileName[i], "txt");
            FileWriter writer  = new FileWriter(file);
            writer.write("File Content...file");
            writer.close();
            
            FileDataSource datasource = new FileDataSource(file);
            attachments[i].setContent( new DataHandler(datasource)  );
                
        }
        if(fileName.length > 1)
            agileAddFileAttachmentRequest[0].setSingleFolder(true);
        agileAddFileAttachmentRequest[0].getAttachments().addAll(Arrays.asList(attachments));                     
        
        addFileAttachmentRequestType.getRequests().addAll(Arrays.asList(agileAddFileAttachmentRequest));             
        AddFileAttachmentResponseType addFileAttachmentResponseType = attachmentAgileStub.addFileAttachment(addFileAttachmentRequestType);        

        if( !addFileAttachmentResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS) )            
            throw new Exception("addAttachmentToPart failed in Data Preparation");        
    }    
 
    public void addAttachmentToFolder(String folderNumber, String fileName[]) throws Exception{

        AddFileFFRequestType addFileFFRequestType = new AddFileFFRequestType();                         
        AgileAddFileFFRequestType agileAddFileFFRequestType[] = new AgileAddFileFFRequestType[1];                         
        agileAddFileFFRequestType[0] = new AgileAddFileFFRequestType();                   

        agileAddFileFFRequestType[0].setFolderNumber(folderNumber);

        AddFileFFType files[] = new AddFileFFType[1];
        for(int i=0; i<files.length; i++){
            files[i] = new AddFileFFType();
            files[i].setFileName(fileName[i]);
            files[i].setDescription("Description for file " + (i+1) );
            File file = File.createTempFile("Filename", "txt");
            FileWriter writer  = new FileWriter(file);
            writer.write("File Content...file");
            writer.close();
            
            FileDataSource datasource = new FileDataSource(file);
            files[i].setFileContent( new DataHandler(datasource)  );                    
        }
        
        agileAddFileFFRequestType[0].getFiles().addAll(Arrays.asList(files));
        addFileFFRequestType.getRequest().addAll(Arrays.asList(agileAddFileFFRequestType));             
        AddFileFFResponseType AddFileFFResponseType = attachmentAgileStub.addFileFF(addFileFFRequestType);
        if( !AddFileFFResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) )
            throw new Exception("addAttachmentToFolder failed in Data Preparation");        
    }
    
    public String getFolderNameFromAttachment(String cls, String objectNumber, String tableId, String fileName) throws Exception{
        
        LoadTableRequestType loadTableRequestType = new LoadTableRequestType();                                                                               

        RequestTableType table[] = new RequestTableType[1];
        table[0] = new RequestTableType();
        table[0].setClassIdentifier(cls);
        table[0].setObjectNumber(objectNumber);
        table[0].setTableIdentifier( tableId );        

        loadTableRequestType.getTableRequest().addAll(Arrays.asList(table));
        LoadTableResponseType loadTableResponseType = tableAgileStub.loadTable(loadTableRequestType);        

            if( loadTableResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){                

                AgileTableType[] tables = loadTableResponseType.getTableContents().toArray(new AgileTableType[0]);                                    
                return getFolderNameFromAttachment(tables, fileName);
            }
            else                                          
                throw new Exception("getFolderNameFromAttachment failed in Data Preparation");        
       
    }    


    public static String getFolderNameFromAttachment(AgileTableType[] tables, String fileName) throws Exception{
        
        if(tables!=null)
            for(int i=0; i<tables.length; i++){
                
                AgileRowType[] rows = tables[i].getRow().toArray(new AgileRowType[0]);                
                if(rows!=null)
                    for(int j=0; j<rows.length; j++){                                                    

                        List<Element> messages = rows[j].getAny();                        
                        for(int m=0; m<messages.size(); m++){
                        	Object element = messages.get(m);
                        	String strName = "";
                        	HashMap fileValues[] = null;
                        	if (element instanceof Element) {
                        		strName = ((Element)element).getNodeName();
                        		if( strName.equalsIgnoreCase( "filename" ) ) 
                        			fileValues = getValuesFromSelection((Element)element, "filename");
                        	}
                        	else if (element instanceof JAXBElement) {
                        		strName = ((JAXBElement<?>)element).getName().getLocalPart();
                        		if( strName.equalsIgnoreCase( "filename" ) ) {
                        			JAXBElement<?> jEle = (JAXBElement<?>)element;
                        			AgileListEntryType lstEntry = (AgileListEntryType)jEle.getValue();
                        			fileValues = getValuesFromSelection(lstEntry, "filename");
                        		}
                        	}
                                    
                            if (fileValues != null) {
                                for(HashMap fileValue:fileValues)
                                    if( fileValue.get("filename").equals(fileName) ){                                        

                                        for(int mm=0; mm < messages.size(); mm++){
                                        	Object objAny = messages.get(mm);
                                        	if (objAny instanceof Element) {
                                        		Element mElement = (Element)objAny;
	                                            if(mElement.getNodeName().equalsIgnoreCase("folderNumber") )
	                                                return mElement.getTextContent();  
                                        	}
                                        }
                                            
                                    }  
                            }
                        }

                    }
                            
            }
        return null;
        
    }

    public static HashMap[] getValuesFromSelection(Element element, String keyTag) throws Exception{        

        HashMap fileValues[] = null;
        String xsiType = element.getAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type");
        if("common:AgileListEntryType".equals(xsiType)) {
            
            AgileListEntryType list = (AgileListEntryType)WSUtil.unmarshalToAgileListEntryType(element) ;
            SelectionType selection[] = list.getSelection().toArray(new SelectionType[0]);
            fileValues = new HashMap [selection.length];
            for(int i=0; i<selection.length; i++){
                fileValues[i] = new HashMap();
                fileValues[i].put( keyTag, selection[i].getValue() );
                fileValues[i].put("fileid", selection[i].getId() );
            }
        }
        else{
            fileValues = new HashMap [1];
            fileValues[0] = new HashMap();
            fileValues[0].put( keyTag, element.getFirstChild().getNodeValue() );
            fileValues[0].put("fileId", null );
        }
        return fileValues;
    	
    }
    
    public static HashMap[] getValuesFromSelection(AgileListEntryType lstEntry, String keyTag) throws Exception{        
        HashMap fileValues[] = null;
        
        if(lstEntry != null) {
            SelectionType selection[] = lstEntry.getSelection().toArray(new SelectionType[0]);
            fileValues = new HashMap [selection.length];
            for(int i=0; i<selection.length; i++){
                fileValues[i] = new HashMap();
                fileValues[i].put( keyTag, selection[i].getValue() );
                fileValues[i].put("fileid", selection[i].getId() );
            }
        }
        
        return fileValues;
    }
    
    public void updateRow(String cls, String objectNumber, String tableId, String keyTag, String keyValue, String attId, String newValue, String elementType) throws Exception{        

        setupTable();

        UpdateRowsRequestType updateRowsRequestType = new UpdateRowsRequestType();                         
        AgileUpdateRowsRequest agileUpdateRowsRequest[] = new AgileUpdateRowsRequest[1];             
        agileUpdateRowsRequest[0] = new AgileUpdateRowsRequest();

        RequestTableType table = new RequestTableType();
        table.setClassIdentifier(cls);
        table.setObjectNumber(objectNumber);
        table.setTableIdentifier( tableId );
        
        AgileUpdateRow updateRow[] = new AgileUpdateRow[1];           
        updateRow[0] = new AgileUpdateRow();        

        Integer rowId =  getRowOrFileId(keyTag, keyValue, cls, objectNumber, tableId, "getRowId" ) ;
        updateRow[0].setRowId( rowId );
        AgileRowType row = new AgileRowType();        

        String namespaceUri = null;
        if( elementType.equals("normalMessageElement") ){
            Element modified_element = WSUtil.createMessageElement("modified_element");
            modified_element.setTextContent(newValue);
            modified_element.setAttribute("attributeId", attId);
            row.getAny().add(modified_element);
        }
        else
            if( elementType.equals("AgileListEntryType") ){
            
                String COMMONNAMESPACEURI = "http://xmlns.oracle.com/AgileObjects/Core/Common/V1";
                //AgileListEntryType list = new AgileListEntryType();
                
                ObjectFactory factory = new ObjectFactory();
                AgileListEntryType list = factory.createAgileListEntryType();
                
                SelectionType[] multiSelect = new SelectionType[1];
                
                multiSelect[0] = factory.createSelectionType();
                multiSelect[0].setValue(newValue);                 
                list.getSelection().addAll(Arrays.asList(multiSelect));
                
                Element modified_element = WSUtil.createMessageElement("modified_element");
                modified_element = WSUtil.marshal(modified_element, list);
                
                modified_element.setAttribute("attributeId", attId);

                row.getAny().add(modified_element);
            }
  
        
        updateRow[0].setRow(row);
        agileUpdateRowsRequest[0].getRow().addAll(Arrays.asList(updateRow));
        agileUpdateRowsRequest[0].setObjectInfo(table);
        
        updateRowsRequestType.getData().addAll(Arrays.asList(agileUpdateRowsRequest));             
        UpdateRowsResponseType updateRowsResponseType = tableAgileStub.updateRows(updateRowsRequestType);

        if( !updateRowsResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) )
            throw new Exception("updateRow failed in Data Preparation");        
        
    }
    
    public void addRow(String cls, String objectNumber, String tableId, String tag, String newValue) throws Exception{

        AddRowsRequestType addRowsRequestType = new AddRowsRequestType();                         
        AgileAddRowsRequest agileAddRowsRequest[] = new AgileAddRowsRequest[1];             
        agileAddRowsRequest[0] = new AgileAddRowsRequest();        
        
        RequestTableType table = new RequestTableType();
        table.setClassIdentifier(cls);
        table.setObjectNumber(objectNumber);
        table.setTableIdentifier( tableId );
        
        AgileRowType[] rows = new AgileRowType[1];
        rows[0] = new AgileRowType();
        
        String namespaceUri = null;

        Element key_1 = null;
        
        try{
            int num = Integer.parseInt( tag );
             key_1 = WSUtil.createMessageElement("key" + tag);
             key_1.setAttribute("attributeId", tag);
           
        }
        catch(Exception e){
            key_1 = WSUtil.createMessageElement( tag);
        }
        
        key_1.setTextContent(newValue);
        
        
        rows[0].getAny().add(key_1);
        
        agileAddRowsRequest[0].getRow().addAll(Arrays.asList(rows));
        agileAddRowsRequest[0].setObjectInfo(table);
        addRowsRequestType.getData().addAll(Arrays.asList(agileAddRowsRequest));             
        AddRowsResponseType addRowsResponseType = tableAgileStub.addRows(addRowsRequestType);
        if( !addRowsResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) )
            throw new Exception("addRow failed in Data Preparation");   
        
}
    
    public void addRow(String cls, String objectNumber, String tableId, String tag, String newValue, String type) throws Exception{

        AddRowsRequestType addRowsRequestType = new AddRowsRequestType();                         
        AgileAddRowsRequest agileAddRowsRequest[] = new AgileAddRowsRequest[1];             
        agileAddRowsRequest[0] = new AgileAddRowsRequest();        
        
        RequestTableType table = new RequestTableType();
        table.setClassIdentifier(cls);
        table.setObjectNumber(objectNumber);
        table.setTableIdentifier( tableId );
        
        AgileRowType[] rows = new AgileRowType[1];
        rows[0] = new AgileRowType();
        
        String namespaceUri = null;  
        
        Element key_1 = null;
        try{
            int num = Integer.parseInt( tag );
            key_1 = WSUtil.createMessageElement("key" + tag);
            key_1.setAttribute("attributeId", tag);
        }
        catch(Exception e){
        	 key_1 = WSUtil.createMessageElement( tag);
        	 key_1.setAttribute("attributeId", tag);
        }
        if(type.equals("String"))
        	key_1.setTextContent(newValue);
        else
        if(type.equals("ObjectReferentIdType")) {
          
            ObjectReferentIdType obj = new ObjectReferentIdType();
            obj.setObjectIdentifier(newValue);
            obj.setClassIdentifier("Site");
            
            key_1 = WSUtil.marshal(key_1, obj);
            
            key_1.setAttribute("attributeId", tag);
            
            /* rows[0].getAny().add(obj);
            JAXBContext jc = JAXBContext.newInstance(AgileRowType.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.marshal(rows[0], System.out);*/
            
            /*key_1 = WSUtil.marshal(key_1, obj);
            key_1 = WSUtil.createMessageElement(tag,obj);*/
            rows[0].getAny().add(key_1);
        }
        
      //  System.out.println(key_1.toString());
        
        //rows[0].getAny().add(key_1);
        
        agileAddRowsRequest[0].getRow().addAll(Arrays.asList(rows));
        agileAddRowsRequest[0].setObjectInfo(table);
        addRowsRequestType.getData().addAll(Arrays.asList(agileAddRowsRequest)); 
        
        JAXBContext  jc = JAXBContext.newInstance(ObjectReferentIdType.class);
		Marshaller marshaller = jc.createMarshaller();
		
		/*marshaller.marshal(new JAXBElement<ObjectReferentIdType>(new QName("http://xmlns.oracle.com/AgileObjects/Core/Common/V1","ObjectReferentIdType11"),
	    		ObjectReferentIdType.class, objectReferentIdType),key_1);*/
        AddRowsResponseType addRowsResponseType = tableAgileStub.addRows(addRowsRequestType);
        if( !addRowsResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) )
            throw new Exception("addRow failed in Data Preparation");   
        
    }

    
    public int getRowOrFileId(String keyTag, String keyValue, String clazz, String objectNumber, String tableId, String methodType ) throws Exception{
       
        LoadTableRequestType loadTableRequestType = new LoadTableRequestType();                                                                               
       
        RequestTableType table[] = new RequestTableType[1];
        table[0] = new RequestTableType();
        table[0].setClassIdentifier(clazz);
        table[0].setObjectNumber(objectNumber);
        table[0].setTableIdentifier( tableId );

        loadTableRequestType.getTableRequest().addAll(Arrays.asList(table));
        LoadTableResponseType loadTableResponseType = tableAgileStub.loadTable(loadTableRequestType);

        if( loadTableResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){                

                AgileTableType[] tables = loadTableResponseType.getTableContents().toArray(new AgileTableType[0]);                    
                return getRowOrFileId(tables, keyTag, keyValue, methodType);
        }        
        else
            throw new Exception("getRowOrFileId failed in Data Preparation");        

    }
    
    public static int getRowOrFileId(AgileTableType[] tables, String keyTag, String keyValue, String methodType) throws Exception{
        
        if(tables!=null)
            for(int i=0; i<tables.length; i++){
                
                AgileRowType[] rows = tables[i].getRow().toArray(new AgileRowType[0]);                
                if(rows!=null)
                    for(int j=0; j<rows.length; j++){                                                    

                        List<Element> messages = rows[j].getAny();                        
                        for(int m=0; m<messages.size(); m++){                                                                    
                        	Object element = messages.get(m);
                        	String strName = "";
                        	HashMap fileValues[] = null;
                        	if (element instanceof Element) {
                        		strName = ((Element)element).getNodeName();
                        		if( strName.equalsIgnoreCase( keyTag ) ) 
                        			fileValues = getValuesFromSelection((Element)element, keyTag);
                        	}
                        	else if (element instanceof JAXBElement) {
                        		strName = ((JAXBElement<?>)element).getName().getLocalPart();
                        		if( strName.equalsIgnoreCase( keyTag ) ) {
                        			JAXBElement<?> jEle = (JAXBElement<?>)element;
                        			AgileListEntryType lstEntry = (AgileListEntryType)jEle.getValue();
                        			fileValues = getValuesFromSelection(lstEntry, keyTag);
                        		}
                        	}
                                    
                            if (fileValues != null) {
                                for(HashMap fileValue:fileValues)
                                    if( fileValue.get( keyTag ).equals(keyValue) ){                                        

                                        if(methodType.equals("getRowId"))
                                            return rows[j].getRowId();                                        
                                        else
                                        if(methodType.equals("getFileId"))
                                            return (Integer) fileValue.get("fileid");
                                    }
                            }
                        }

                    }
                            
            }
        return 0;
        
    }
    
    
    public Object getValueFromTable(String cls, String objectNumber, String tableId, String tag) throws Exception{
        
        LoadTableRequestType loadTableRequestType = new LoadTableRequestType();                                                                               

        RequestTableType table[] = new RequestTableType[1];
        table[0] = new RequestTableType();
        table[0].setClassIdentifier(cls);
        table[0].setObjectNumber(objectNumber);
        table[0].setTableIdentifier( tableId );        

        loadTableRequestType.getTableRequest().addAll(Arrays.asList(table));
        LoadTableResponseType loadTableResponseType = tableAgileStub.loadTable(loadTableRequestType);        

            if( loadTableResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) ){                

                AgileTableType[] tables = loadTableResponseType.getTableContents().toArray(new AgileTableType[0]);                                    
                return getValueFromTable(tables, tag);
            }
            else                                          
                throw new Exception("getValueFromTable failed in Data Preparation");        
       
    }           


    public static Object getValueFromTable(AgileTableType[] tables, String tag) throws Exception{
        
        if(tables!=null)
            for(int i=0; i<tables.length; i++){
                
                AgileRowType[] rows = tables[i].getRow().toArray(new AgileRowType[0]);
                if(rows!=null)
                    for(int j=0; j<rows.length; j++){

                    	
                        //MessageElement[] messages = rows[j].getAny();  
                        List<Element> messages = rows[j].getAny();
                        for(int m=0; m<messages.size(); m++){
                        	if(messages.get(m) instanceof Element){
                            Element element = (Element)messages.get(m);
                            
                            if( element.getNodeName().toString().equalsIgnoreCase( tag ) ){
                            	
                                    if(element.getLocalName().equals("AgileListEntryType")){
                                        
                                        AgileListEntryType list = (AgileListEntryType) WSUtil.unmarshalToAgileListEntryType(element);
                                        SelectionType selection[] = list.getSelection().toArray(new SelectionType[0]);
                                        String selectionValues[] = new String[selection.length];
                                        for(int ii=0; ii<selection.length; ii++)
                                            selectionValues[ii] = selection[ii].getValue();
                                        return selectionValues;
                                    }
                                    else
                                        return element.getFirstChild().getNodeValue();
                            }
                        	}
                        }
                    }                           
            }
        return null;        
    }
    
    public Integer getWorkFlow(String changeNumber) throws Exception{
        
        GetWorkflowsRequestType getWorkflowsRequestType = new GetWorkflowsRequestType();                         
        AgileGetWorkflowsRequestType agileGetWorkflowsRequestType[] = new AgileGetWorkflowsRequestType[1];                         
        agileGetWorkflowsRequestType[0] = new AgileGetWorkflowsRequestType();           

        agileGetWorkflowsRequestType[0].setClassIdentifier("ECO");             
        agileGetWorkflowsRequestType[0].setObjectNumber(changeNumber);
        getWorkflowsRequestType.getWorkflowRequest().addAll(Arrays.asList(agileGetWorkflowsRequestType));             
        GetWorkflowsResponseType getWorkflowsResponseType = collabAgileStub.getWorkFlows(getWorkflowsRequestType);

        if( !getWorkflowsResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) )
            throw new Exception("getWorkFlow failed in Data Preparation");   
        else{
            
            AgileGetWorkflowsResponseType responses[] =  getWorkflowsResponseType.getWorkflowResponse().toArray(new AgileGetWorkflowsResponseType[0]);
            if(responses!=null)
                for(int i=0; i<responses.length; i++){                                
                    AgileWorkflowType workflows[] = responses[i].getWorkflow().toArray(new AgileWorkflowType[0]);
                    if(workflows!=null)
                    for(int j=0; j<workflows.length; j++)
                        return workflows[j].getWorkflowId();
                }            
        } 
        return null;
    }
    
    public void setWorkflow(String changeNumber, String workflowId) throws Exception{
        
        SetWorkFlowRequestType setWorkFlowRequestType = new SetWorkFlowRequestType();                         
        AgileSetWorkFlowRequestType agileSetWorkFlowRequestType[] = new AgileSetWorkFlowRequestType[1];                         
        agileSetWorkFlowRequestType[0] = new AgileSetWorkFlowRequestType();                   

        agileSetWorkFlowRequestType[0].setClassIdentifier("ECO");             
        agileSetWorkFlowRequestType[0].setObjectNumber(changeNumber);
        agileSetWorkFlowRequestType[0].setWorkFlowIdentifier(workflowId);        
        setWorkFlowRequestType.getSetWorkFlowRequest().addAll(Arrays.asList(agileSetWorkFlowRequestType));             
        SetWorkFlowResponseType setWorkflowResponseType = collabAgileStub.setWorkFlow(setWorkFlowRequestType);

        if( !setWorkflowResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) )
            throw new Exception("setWorkflow failed in Data Preparation");   
        
    }
    
    public String getnextStatus(String changeNumber) throws Exception{

        GetStatusRequestType getStatusRequestType = new GetStatusRequestType();                         
        AgileGetStatusRequestType agileGetStatusRequestType[] = new AgileGetStatusRequestType[1];                         
        agileGetStatusRequestType[0] = new AgileGetStatusRequestType();           

        agileGetStatusRequestType[0].setClassIdentifier("ECO");             
        agileGetStatusRequestType[0].setObjectNumber(changeNumber);   
        getStatusRequestType.getStatusRequest().addAll(Arrays.asList(agileGetStatusRequestType));             
        GetStatusResponseType getStatusResponseType = collabAgileStub.getStatus(getStatusRequestType);        

        if( !getStatusResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) )
            throw new Exception("getStatuses failed in Data Preparation");   
        else{
            
                AgileGetStatusResponseType responses[] =  getStatusResponseType.getStatusResponse().toArray(new AgileGetStatusResponseType[0]);
                if(responses!=null)
                    for(int i=0; i<responses.length; i++){                                
                        return responses[i].getNextDefaultStatus().getStatusName();

//                        AgileStatusType nextValidStatuses[] = responses[i].getNextStatus();                                
//                        if(nextValidStatuses!=null)
//                            for(int j=0; j<nextValidStatuses.length; j++)
//                                return nextValidStatuses[j].getStatusName();
                                //System.out.println( (j+1) + ". " + nextValidStatuses[j].getStatusDisplayName() );                                
                    }                            
                }  
        return null;
    }
    
    public void changeStatus(String changeNumber, String newStatus) throws Exception{           
        
        ChangeStatusRequestType changeStatusRequestType = new ChangeStatusRequestType();                         
        AgileChangeStatusRequestType agileChangeStatusRequestType[] = new AgileChangeStatusRequestType[1];                         
        agileChangeStatusRequestType[0] = new AgileChangeStatusRequestType();           

        agileChangeStatusRequestType[0].setClassIdentifier("ECO");             
        agileChangeStatusRequestType[0].setObjectNumber(changeNumber);
        agileChangeStatusRequestType[0].setNewStatusIdentifier(newStatus);        
        
        agileChangeStatusRequestType[0].setComment("Comments");
        agileChangeStatusRequestType[0].setPassword(PASSWORD);
        agileChangeStatusRequestType[0].setAuditRelease(false);
        agileChangeStatusRequestType[0].setUrgent(false);
        agileChangeStatusRequestType[0].setNotifyOriginator(true);
        agileChangeStatusRequestType[0].setNotifyChangeAnalyst(true);
        agileChangeStatusRequestType[0].setNotifyCCB(true);
        
        changeStatusRequestType.setDisableAllWarnings(true);
        changeStatusRequestType.getChangeStatusRequest().addAll(Arrays.asList(agileChangeStatusRequestType));             
        ChangeStatusResponseType changeStatusResponseType = collabAgileStub.changeStatus(changeStatusRequestType);        
        
        if( !changeStatusResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) )
            throw new Exception("changeStatus failed in Data Preparation");   
        

    }
    
    public String changeStatusToReleased(String changeNumber, String partNumber) throws Exception{
        
            String version = "2";
            setWorkflow(changeNumber, getWorkFlow(changeNumber).toString() );
            updateRow("ECO", changeNumber, ChangeConstants.TABLE_AFFECTEDITEMS.toString(),
                      "itemNumber",partNumber, "newRev", version, "normalMessageElement");
            changeStatus(changeNumber, "Submitted");
            changeStatus(changeNumber, "CCB");
            changeStatus(changeNumber, "Released");
/*          String nextStatus = getnextStatus(changeNumber);

            while( !nextStatus.equals("Implemented") ){                
                changeStatus(changeNumber, nextStatus);
                System.out.println("Changed status to " + nextStatus);
                nextStatus = getnextStatus(changeNumber);
            }
*/
            return version;
    }
    
    public String saveObjectAs(String cls, String object, String keyTags[], String keyValues[]) throws Exception{

        SaveAsObjectRequestType saveAsObjectRequestType = new SaveAsObjectRequestType();
        AgileSaveAsObjectRequestType agileSaveAsObjectRequestType[] = new AgileSaveAsObjectRequestType[1];             
        agileSaveAsObjectRequestType[0] = new AgileSaveAsObjectRequestType();
        
        agileSaveAsObjectRequestType[0].setParentClassIdentifier(cls);
        agileSaveAsObjectRequestType[0].setParentObjectNumber(object);
        agileSaveAsObjectRequestType[0].setNewClassIdentifier(cls);               

        AgileRowType row = new AgileRowType();         
        String namespaceUri = null;
        for(int i=0; i<keyTags.length; i++){
        	Element el;
            try{
                int num = Integer.parseInt( keyTags[i] );
  
                 el = WSUtil.createMessageElement("key" + keyTags[i]);
                 el.setAttribute("attributeId", keyTags[i]);

            }
            catch(Exception e){
                el = WSUtil.createMessageElement(keyTags[i]);
            }
                  
            el.setTextContent(keyValues[i]);
            row.getAny().add(el);
        }            
        agileSaveAsObjectRequestType[0].setData(row);             
        
        saveAsObjectRequestType.getSaveAsObjectRequest().addAll(Arrays.asList(agileSaveAsObjectRequestType));
        SaveAsObjectResponseType saveAsObjectResponseType = businessAgileStub.saveAsObject(saveAsObjectRequestType);            
                     
        if( !saveAsObjectResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) )
            throw new Exception("saveObjectAs failed in Data Preparation");
        else
            return saveAsObjectResponseType.getSaveAsObjectResponse().get(0).getObjectNumber();
        
        
    }
    
    public void deleteObject(String cls, String objectNumber) throws Exception{

        DeleteObjectRequestType deleteObjectRequestType = new DeleteObjectRequestType();
        AgileDeleteObjectRequest agileDeleteObjectRequest[] = new AgileDeleteObjectRequest[1];                     
        
        agileDeleteObjectRequest[0] = new AgileDeleteObjectRequest();
        agileDeleteObjectRequest[0].setClassIdentifier(cls);
        agileDeleteObjectRequest[0].setObjectNumber(objectNumber);       
        
        deleteObjectRequestType.getRequests().addAll(Arrays.asList(agileDeleteObjectRequest));
        DeleteObjectResponseType deleteObjectResponseType = businessAgileStub.deleteObject(deleteObjectRequestType);            
        if( !deleteObjectResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS) )
            throw new Exception("deleteObject failed in Data Preparation");
    }
    
    public void addUserApprovers(String changeNumber, String status, String[] approvers) throws Exception{
        
        AddApproversRequestType addApproversRequestType = new AddApproversRequestType();                         
        AgileAddApproversRequestType agileAddApproversRequestType[] = new AgileAddApproversRequestType[1];                         
        agileAddApproversRequestType[0] = new AgileAddApproversRequestType();           
        
        agileAddApproversRequestType[0].setClassIdentifier("ECO");             
        agileAddApproversRequestType[0].setObjectNumber( changeNumber );
        agileAddApproversRequestType[0].setStatusIdentifier(status);
        
        AgileUserUserGroupIdentifierType users[] = new AgileUserUserGroupIdentifierType[approvers.length];
        for(int i=0; i<users.length; i++){
            users[i] = new AgileUserUserGroupIdentifierType();        
            users[i].setClassIdentifier(UserConstants.CLASS_USER.toString());
            users[i].setObjectIdentifier(approvers[i]);
        }
        agileAddApproversRequestType[0].getApprovers().addAll(Arrays.asList(users));
        agileAddApproversRequestType[0].setUrgent(false);
        agileAddApproversRequestType[0].setComment("Comments");
        addApproversRequestType.getAddApproversRequest().addAll(Arrays.asList(agileAddApproversRequestType));             
        AddApproversResponseType addApproversResponseType = collabAgileStub.addApprovers(addApproversRequestType);
        if( !addApproversResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) )
            throw new Exception("addUserApprovers failed in Data Preparation");
    }
    
    public void addReviewers(String changeNumber, String status, String approver, String observer, String acknowledger) throws Exception{    
      AddReviewersRequestType addReviewersRequestType = new AddReviewersRequestType();                         
      AgileAddReviewersRequestType agileAddReviewersRequestType[] = new AgileAddReviewersRequestType[1];                         
      agileAddReviewersRequestType[0] = new AgileAddReviewersRequestType();           
      
      agileAddReviewersRequestType[0].setClassIdentifier("ECO");             
      agileAddReviewersRequestType[0].setObjectNumber( changeNumber );
      agileAddReviewersRequestType[0].setStatusIdentifier(status);
      
      AgileUserUserGroupIdentifierType approvers[] = new AgileUserUserGroupIdentifierType[1];
      approvers[0] = new AgileUserUserGroupIdentifierType();
      approvers[0].setClassIdentifier(UserConstants.CLASS_USER.toString());
      approvers[0].setObjectIdentifier(approver);
      approvers[0].setJobFunction("QA");
      agileAddReviewersRequestType[0].getApprovers().addAll(Arrays.asList(approvers));
      
      AgileUserUserGroupIdentifierType observers[] = new AgileUserUserGroupIdentifierType[1];
      observers[0] = new AgileUserUserGroupIdentifierType();
      observers[0].setClassIdentifier(UserConstants.CLASS_USER.toString());
      observers[0].setObjectIdentifier(observer);
      observers[0].setJobFunction("Developer");
      agileAddReviewersRequestType[0].getObservers().addAll(Arrays.asList(observers));
      
      AgileUserUserGroupIdentifierType acknowledgers[] = new AgileUserUserGroupIdentifierType[1];
      acknowledgers[0] = new AgileUserUserGroupIdentifierType();
      acknowledgers[0].setClassIdentifier(UserConstants.CLASS_USER.toString());
      acknowledgers[0].setObjectIdentifier(acknowledger);
      acknowledgers[0].setJobFunction(null);      
      agileAddReviewersRequestType[0].getAcknowledgers().addAll(Arrays.asList(acknowledgers));
      
      agileAddReviewersRequestType[0].setUrgent(false);
      agileAddReviewersRequestType[0].setComment("Comments");
      addReviewersRequestType.getAddReviewersRequest().addAll(Arrays.asList(agileAddReviewersRequestType));             
      AddReviewersResponseType addReviewersResponseType = collabAgileStub.addReviewers(addReviewersRequestType);
      if( !addReviewersResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) )
          throw new Exception("addUserReviewers failed in Data Preparation");
  }

    public String createProgramObject(String programName, String projectState) throws Exception {

        String objNum = null;

        CreateObjectRequestType createObjectRequestType = new CreateObjectRequestType();                         
        AgileCreateObjectRequest agileCreateObjectRequest[] = new AgileCreateObjectRequest[1];
        for(int i=0; i<agileCreateObjectRequest.length; i++)             
           agileCreateObjectRequest[i] = new AgileCreateObjectRequest();           
        
        agileCreateObjectRequest[0].setClassIdentifier("Program");

        AgileRowType row_1 = new AgileRowType();                         
        String namespaceUri = null;   
        final String COMMONNAMESPACEURI = "http://xmlns.oracle.com/AgileObjects/Core/Common/V1";

        //Name

        Element el_name = WSUtil.createMessageElement("name");
        el_name.setTextContent(programName);
        
        //Description

        Element el_description = WSUtil.createMessageElement("description");
        el_description.setTextContent("Fixed Project Desc");
        
        //The field 'Template' is of type 'AgileListEntryType' and is handled as follows:
        AgileListEntryType list = new AgileListEntryType();
        SelectionType[] multiSelect = new SelectionType[1];
        multiSelect[0] = new SelectionType();
        multiSelect[0].setId(1);        
        multiSelect[0].setValue( projectState );
        list.getSelection().addAll(Arrays.asList(multiSelect));     
       Element el_projectState = WSUtil.createMessageElement("projectState", list);
        
        //The field 'DurationType' is of type 'AgileListEntryType' and is handled as follows:
           
        list = new AgileListEntryType();
        multiSelect = new SelectionType[1];
        multiSelect[0] = new SelectionType();
        multiSelect[0].setId(1);        
        multiSelect[0].setValue("Fixed");                 
        list.getSelection().addAll(Arrays.asList(multiSelect));

        
        Element el_fixed = WSUtil.createMessageElement("durationType", list);
        
        //The field 'Schedule Start Date' is of type 'Date' and is handled as follows:
        Date date = new Date();
        date.setTime( date.getTime() );
        
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

        Element el_scheduleStartDate = WSUtil.createMessageElement("scheduleStartDate");
        el_scheduleStartDate.setTextContent(gc.toString());

        //scheduleDuration
        Element el_scheduleDuration= WSUtil.createMessageElement("scheduleDuration");
        el_scheduleDuration.setTextContent("7");
        
        
        //The field 'Schedule End Date' is of type 'Date' and is handled as follows:
        date = new Date();
        date.setTime( date.getTime() + (7 * 1000 * 60 * 60 * 24)  );
        
        cal.setTime(date);
        gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        Element el_scheduleEndDate = WSUtil.createMessageElement("scheduleEndDate");
        el_scheduleEndDate.setTextContent(gc.toString());
        
        row_1.getAny().add(el_name);
        row_1.getAny().add(el_description);
        row_1.getAny().add(el_projectState);
        row_1.getAny().add(el_fixed);
        row_1.getAny().add(el_scheduleStartDate);
        row_1.getAny().add(el_scheduleDuration);
        row_1.getAny().add(el_scheduleEndDate);
        
        agileCreateObjectRequest[0].setData(row_1);             
        createObjectRequestType.getRequests().addAll(Arrays.asList(agileCreateObjectRequest));             
        CreateObjectResponseType createObjectResponseType = businessAgileStub.createObject(createObjectRequestType);
        
        if(createObjectResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS )){
            objNum = createObjectResponseType.getResponses().get(0).getAgileObject().getObjectIdentifier().getObjectName();
            return objNum;
        }               
        else    
            throw new Exception("createProgramObject failed in Data Preparation");    
    }
    
    public void addRowsSiteSpecific(String parentPartNumber, String childPartNumber, String site1) throws Exception{
        
        AddRowsRequestType addRowsRequestType = new AddRowsRequestType();                         
        AgileAddRowsRequest agileAddRowsRequest[] = new AgileAddRowsRequest[1];             
        agileAddRowsRequest[0] = new AgileAddRowsRequest();
        
        RequestTableType table = new RequestTableType();
        table.setClassIdentifier("Part");
        table.setObjectNumber( parentPartNumber );
        table.setTableIdentifier( ItemConstants.TABLE_BOM.toString() );
        
        PropertyType[] properties = new PropertyType[1];
        properties[0] = new PropertyType();
        properties[0].setPropertyName(SchemaConstants.SITE.toString());
        properties[0].setPropertyValue(site1);        
        table.getOptions().addAll(Arrays.asList(properties));        

        AgileRowType[] rows = new AgileRowType[1];            
        rows[0] = new AgileRowType();
        
        String namespaceUri = null;
        
        Element msgBomChild = WSUtil.createMessageElement("Message_BOM_Child");
        msgBomChild.setTextContent(childPartNumber);
        msgBomChild.setAttribute("attributeId", ItemConstants.ATT_BOM_ITEM_NUMBER.toString()  );
        rows[0].getAny().add(msgBomChild);
   
        
        agileAddRowsRequest[0].getRow().addAll(Arrays.asList(rows));
        agileAddRowsRequest[0].setObjectInfo(table);
        
        addRowsRequestType.getData().addAll(Arrays.asList(agileAddRowsRequest));             
        AddRowsResponseType addRowsResponseType = tableAgileStub.addRows(addRowsRequestType);
        if( !addRowsResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) )
            throw new Exception("addRowsSiteSpecific failed in Data Preparation");            
        
    }
    
    public void updateObjectPendingChange(String partNumber, String changeNumber, String tag, String newValue ) throws Exception{

        
        UpdateObjectRequestType updateObjectRequestType = new UpdateObjectRequestType();
        AgileUpdateObjectRequest agileUpdateObjectRequest[] = new AgileUpdateObjectRequest[1];             
        agileUpdateObjectRequest[0] = new AgileUpdateObjectRequest();       
        
        agileUpdateObjectRequest[0].setClassIdentifier("Part");
        agileUpdateObjectRequest[0].setObjectNumber(partNumber);        

        AgileRowType rows = new AgileRowType();
        
        String namespaceUri = null;               
        
        Element msgDesc = WSUtil.createMessageElement("Message_Desc");
        msgDesc.setTextContent(newValue);
        msgDesc.setAttribute("attributeId", tag );
        rows.getAny().add(msgDesc);
        
        agileUpdateObjectRequest[0].setData(rows);        

        PropertyType propertyType[] =  new PropertyType[1];
        propertyType[0] = new PropertyType();
        propertyType[0].setPropertyName("redline_change");
        propertyType[0].setPropertyValue(changeNumber);        

        agileUpdateObjectRequest[0].getOptions().addAll(Arrays.asList(propertyType));                                 

        updateObjectRequestType.getRequests().addAll(Arrays.asList(agileUpdateObjectRequest));
        UpdateObjectResponseType updateObjectResponseType = businessAgileStub.updateObject(updateObjectRequestType);

        if( !updateObjectResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) )
            throw new Exception("updateObjectPendingChange failed in Data Preparation");            
        
    }
    
    public String getList(String listIdentifier) throws Exception{
        
        GetListsRequestType getListsRequestType = new GetListsRequestType();                         
        AgileGetListsRequestType agileGetListsRequestType[] = new AgileGetListsRequestType[1];             
        agileGetListsRequestType[0] = new AgileGetListsRequestType();                   
        agileGetListsRequestType[0].setListIdentifier(listIdentifier);

        getListsRequestType.getListsRequest().addAll(Arrays.asList(agileGetListsRequestType));            
        GetListsResponseType getListsResponseType =  adminAgileStub.getLists(getListsRequestType);

        if( !getListsResponseType.getStatusCode().equals( ResponseStatusCode.SUCCESS ) )            
            throw new Exception("getList failed in Data Preparation");            
        else{                        
                AgileGetListsResponseType responses[] = getListsResponseType.getListsResponse().toArray(new AgileGetListsResponseType[0]);            
                if(responses!=null) 
                for(int j=0; j<responses.length; j++){                

                    AdminListType list[] = responses[j].getList().toArray(new AdminListType[0]);
                    if(list!=null)
                        for(int i=0; i<list.length; i++){
                            AdminListType values[] = list[i].getEntry().toArray(new AdminListType[0]);
                            if(values!=null)
                            for(int jj=0; jj<values.length; jj++)
                                return values[jj].getApiName();                                    
                        }
                }                
            }    
        return null;
    }
    
    public static void main(String args[]){
    	checkArguments(args);
    	
        try{

            
            DataPrepare dp = new DataPrepare(args[0], args[1], args[2]);
            dp.setupAdminMetadata(); 
            dp.setupBusiness();
            dp.setupTable();
            dp.setupCollaboration();

            String partNumber = dp.createNewObject("Part");
            String changeNumber = dp.getNextAutoNumber("ECO");
            dp.createObjectByTags("ECO", new String[]{"number"}, new String[]{changeNumber} );
            
            String version = "2";
            dp.setWorkflow(changeNumber, dp.getWorkFlow(changeNumber).toString() );
            dp.addRow("ECO", changeNumber, ChangeConstants.TABLE_AFFECTEDITEMS.toString(),
                               ChangeConstants.ATT_AFFECTED_ITEMS_ITEM_NUMBER.toString(), partNumber);
            dp.updateRow("ECO", changeNumber, ChangeConstants.TABLE_AFFECTEDITEMS.toString(),
                      "itemNumber",partNumber, "newRev", version, "normalMessageElement");
            
            String nextStatus = "Submitted";
            dp.changeStatus(changeNumber, nextStatus);
            nextStatus = "CCB";            
            dp.changeStatus(changeNumber, nextStatus);
            nextStatus = "Released";            
            dp.changeStatus(changeNumber, nextStatus);            
            
            
        }catch(Exception e){
            
            //e.printStackTrace();
            System.out.println("Data prepare failed. Ensure that your server configuration is valid");
        }
    }
    
 	/**
	 * This sample can be configured by passing server url, user name and
	 * password as program arguments in the same order. This method checks for
	 * these values.
	 * 
	 * @param args
	 */
	private static void checkArguments(String[] args) {
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
    
    public void changeStatusProgram(String programNumber, String newStatus) throws Exception {
    	ChangeStatusRequestType changeStatusRequestType = new ChangeStatusRequestType();                         
        AgileChangeStatusRequestType agileChangeStatusRequestType = new AgileChangeStatusRequestType();                         
        agileChangeStatusRequestType = new AgileChangeStatusRequestType();           

        agileChangeStatusRequestType.setClassIdentifier("Program");             
        agileChangeStatusRequestType.setObjectNumber(programNumber);
        agileChangeStatusRequestType.setNewStatusIdentifier(newStatus);        
        
        agileChangeStatusRequestType.setComment("Comments");
        agileChangeStatusRequestType.setPassword(PASSWORD);
        agileChangeStatusRequestType.setAuditRelease(false);
        agileChangeStatusRequestType.setUrgent(false);
        agileChangeStatusRequestType.setNotifyOriginator(true);
        agileChangeStatusRequestType.setNotifyChangeAnalyst(true);
        agileChangeStatusRequestType.setNotifyCCB(true);
        
        changeStatusRequestType.setDisableAllWarnings(true);
        changeStatusRequestType.getChangeStatusRequest().add(agileChangeStatusRequestType);             
        ChangeStatusResponseType changeStatusResponseType = collabAgileStub.changeStatus(changeStatusRequestType);        
        
        if( !changeStatusResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) )
            throw new Exception("changeStatus failed in Data Preparation");   
    	
    }
    
    public void addRowReferenceObject(String strParentName, String strRefObjName, String strRefObjPubKey, String strRefSubclassName) throws Exception {
		AddRowsRequestType addRowsRequestType = new AddRowsRequestType();                         
		AgileAddRowsRequest agileAddRowsRequest = new AgileAddRowsRequest();             
		
		RequestTableType table = new RequestTableType();
		table.setClassIdentifier("Part");
		table.setObjectNumber( strParentName );
		table.setTableIdentifier( ServiceRequestConstants.TABLE_RELATIONSHIPS.toString() );
		
		AgileRowType row = new AgileRowType();
		
		Element element = WSUtil.createMessageElement("number");
		element.setAttributeNS("http://xmlns.oracle.com/AgileObjects/Core/Common/V1", "type", "ObjectReferentIdType");
		ObjectReferentIdType objRefId = new ObjectReferentIdType();            
		objRefId.setClassIdentifier(strRefSubclassName);
		objRefId.setObjectIdentifier(strRefObjName);
		objRefId.setReferencedObjKey(strRefObjPubKey);
		element = WSUtil.marshal(element, objRefId);
		row.getAny().add(element);
		
		agileAddRowsRequest.getRow().add(row);
		agileAddRowsRequest.setObjectInfo(table);
		
		System.out.println("Adding the Reference Object '" + strRefObjName + "' to the Part '" + strParentName + "'...\n");
		
		addRowsRequestType.getData().add(agileAddRowsRequest);             
		AddRowsResponseType addRowsResponseType = tableAgileStub.addRows(addRowsRequestType);
		
		if( !addRowsResponseType.getStatusCode().toString().equals( ResponseStatusCode.SUCCESS.value() ) ){
            List<AgileExceptionListType> agileExceptionListType = addRowsResponseType.getExceptions();
            if(agileExceptionListType!=null)
            for(int i = 0; i < agileExceptionListType.size(); i++){
                List<AgileExceptionType> exceptions = agileExceptionListType.get(i).getException();
                for(int j=0; j<exceptions.size(); j++)
                    System.out.println(exceptions.get(j).getMessage() );
            }
            throw new Exception("addRowReferenceObject failed in Data Preparation");
        }
	}
}
