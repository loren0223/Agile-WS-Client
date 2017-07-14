package run;

import adminmetadata.convertcurrency.ConvertCurrency;

import adminmetadata.getAutoNumbers.GetAutoNumbers;

import adminmetadata.getattributes.getAttributes;

import adminmetadata.getclasses.GetAllClasses;

import adminmetadata.getlists.GetLists;

import adminmetadata.getmetadata.GetTableMetaData;

import adminmetadata.getnode.GetNode;

import adminmetadata.getsubclasses.GetSubClasses;

import adminmetadata.getusergroups.GetUserGroups;

import adminmetadata.getusers.GetUsers;

import attachment.addfileattachment.AddFileAttachment;
import attachment.addfileattachment.AddFileAttachmentReference;
import attachment.addfileattachment.AddFileAttachmentSingleFolder;
import attachment.addfileattachment.AddFileAttachmentURL;

import attachment.addfileff.AddFileFF;
import attachment.addfileff.AddFileFFReference;
import attachment.addfileff.AddFileFFUrls;
import attachment.affectedfile.AddFileToAF;
import attachment.affectedfile.CancelCheckoutAF;
import attachment.affectedfile.CheckInAF;
import attachment.affectedfile.CheckoutAF;
import attachment.affectedfile.UpdateAFRows;
import attachment.cancelcheckout.CancelCheckOut;

import attachment.checkinattachment.CheckInAttachment;
import attachment.checkinattachment.CheckInAttachmentFileId;

import attachment.checkinff.CheckInFF;

import attachment.checkoutattachment.CheckOutAttachment;
import attachment.checkoutattachment.CheckOutAttachmentAllFiles;
import attachment.checkoutattachment.CheckOutAttachmentFileFolder;
import attachment.checkoutattachment.CheckOutAttachmentFileIds;

import attachment.checkoutff.CheckOutFF;

import attachment.getfileattachment.GetFileAttachment;
import attachment.getfileattachment.GetFileAttachmentAllFiles;
import attachment.getfileattachment.GetFileAttachmentFileFolder;
import attachment.getfileattachment.GetFileAttachmentFileIds;

import attachment.getfileff.GetFileFF;
import attachment.getfileff.GetFileFFAllFiles;
import attachment.getfileff.GetFileFFFileVersion;
import attachment.markup.MarkupTableAction;
import attachment.setincorporateaf.SetIncorporateAF;
import business.checkprivilege.CheckPrivilege;

import business.create.CreateObjectAPIName;
import business.create.CreateObjectAttributeId;
import business.create.CreateObjectFixedProject;
import business.create.CreateObjectManufacturerPart;
import business.create.CreateObjectPublishedPriceonMPN;
import business.create.CreateObjectUpdateExistingObject;
import business.create.CreateReferenceObject;
import business.delete.DeleteObject;
import business.delete.DeleteReferenceObject;
import business.get.GetObject;
import business.get.GetObjectVersion;
import business.get.getManufacturerPart;
import business.get.getObjectRedlineChange;

import business.isdeleted.IsDeleted;

import business.saveas.SaveAsObject;
import business.saveas.SaveAsProgram;
import business.saveas.SaveAsProgramTemplate;

import business.send.SendObject;

import business.undelete.UndeleteObject;

import business.update.UpdateManufacturerPart;
import business.update.UpdateObject;
import business.update.UpdateObjectRedlineChange;
import business.update.UpdateObjectReturnUpdatedObject;
import business.update.UpdateReferenceObject;
import collaboration.addapprovers.AddApprovers;

import collaboration.approveRObject.ApproveROWarningResolution;
import collaboration.approveRObject.approveRObject;

import collaboration.auditro.AuditRObj;

import collaboration.changestatus.ChangeStatus;
import collaboration.changestatus.ChangeStatusProgram;

import collaboration.commentrobject.CommentRObject;

import collaboration.getapprovers.GetApprovers;
import collaboration.getreviewers.GetReviewers;
import collaboration.getstatus.GetStatus;

import collaboration.getworkflows.GetWorkflows;

import collaboration.rejectRObject.RejectRObject;

import collaboration.removeapprovers.RemoveApprovers;

import collaboration.setworkflows.SetWorkflows;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import pc.setincorporate.SetIncorporate;
import project.createCalendar.CreateCalendar;
import project.exportSearchedTimesheet.ExportSearchedTimesheet;
import project.exportToAXML.ExportToAXML;
import project.getCalendar.GetCalendar;
import project.loadDeliverables.LoadDeliverablesStatus;
import project.loadProjects.LoadProject;
import project.logOrChangeTimesheet.LogOrChangeTimesheet;
import project.removeCalendar.RemoveCalendar;
import project.retrieveTimesheet.RetrieveTimesheet;
import project.updateCalendar.UpdateCalendar;
import project.updateProjects.UpdateExistingProject;
import project.updateProjects.UpdateNewProject;
import project.validateProjects.ValidateProject;

import search.advancedsearch.AdvancedSearch;
import search.advancedsearch.AdvancedSearchReferenceObject;
import search.advancedsearchparams.AdvancedSearchParams;

import search.getattributes.GetSearchableAttributes;

import search.quicksearch.QuickSearch;

import search.whereusedquery.WhereUsedQuery;

import table.addrows.AddRowsAffectedItems;
import table.addrows.AddRowsAMLSite;
import table.addrows.AddRowsBOMRedlineChange;
import table.addrows.AddRowsReferenceObject;
import table.addrows.AddRowsSiteSpecific;

import table.cleartable.ClearAffectedItemsTable;

import table.copytable.CopyTable;

import table.isreadonly.IsReadOnly;

import table.loadtable.LoadTable;
import table.loadtable.LoadTableFolderChangeHistory;
import table.loadtable.LoadTableFolderPendingChanges;
import table.loadtable.LoadTableFolderVersion;
import table.loadtable.LoadTableBOMRedlineChange;
import table.loadtable.LoadTableRevision;
import table.loadtable.LoadTableSite;
import table.removerows.RemoveAFRows;
import table.removerows.RemoveRows;
import table.removerows.RemoveRowsReferenceObject;
import table.updaterows.UpdateRows;
import table.updaterows.UpdateRowsFileFolderRedlineTitleBlock;


public class RunAllSamples {   

    public static int failureOrWarningCounter;
    public static int runCount;
    
    public RunAllSamples() {
    }
    
    public static void reportFailure(String cls){

        try {
            failureOrWarningCounter++;                
            BufferedWriter out = new BufferedWriter(new FileWriter("Samples_RunResults.txt", true) );
            out.write("The webservice sample '" + cls + "' has failed");
            out.newLine();
            out.close();
            
        } catch (Exception e) {

        }
        
    }
    
    public static void createAllObjects(){

        ConvertCurrency objConvertCurrency = new ConvertCurrency();
        getAttributes objgetAttributes = new getAttributes();
        GetAutoNumbers objGetAutoNumbers = new GetAutoNumbers();
        GetAllClasses objGetAllClasses = new GetAllClasses();
        GetLists objGetLists = new GetLists();
        GetTableMetaData objGetTableMetaData = new GetTableMetaData();
        GetNode objGetNode = new GetNode();
        GetSubClasses objGetSubClasses = new GetSubClasses();
        GetUserGroups objGetUserGroups = new GetUserGroups();
        GetUsers objGetUsers = new GetUsers();
        AddFileAttachment objAddFileAttachment = new AddFileAttachment();
        AddFileAttachmentReference objAddFileAttachmentReference = new AddFileAttachmentReference();
        AddFileAttachmentSingleFolder objAddFileAttachmentSingleFolder = new AddFileAttachmentSingleFolder();
        AddFileAttachmentURL objAddFileAttachmentURL = new AddFileAttachmentURL();
        AddFileFF objAddFileFF = new AddFileFF();
        AddFileFFReference objAddFileFFReference = new AddFileFFReference();
        AddFileFFUrls objAddFileFFUrls = new AddFileFFUrls();
        CancelCheckOut objCancelCheckOut = new CancelCheckOut();
        CheckInAttachment objCheckInAttachment = new CheckInAttachment();
        CheckInAttachmentFileId objCheckInAttachmentFileId = new CheckInAttachmentFileId();
        CheckInFF objCheckInFF = new CheckInFF();
        CheckOutAttachment objCheckOutAttachment = new CheckOutAttachment();
        CheckOutAttachmentAllFiles objCheckOutAttachmentAllFiles = new CheckOutAttachmentAllFiles();
        CheckOutAttachmentFileFolder objCheckOutAttachmentFileFolder = new CheckOutAttachmentFileFolder();
        CheckOutAttachmentFileIds objCheckOutAttachmentFileIds = new CheckOutAttachmentFileIds();
        CheckOutFF objCheckOutFF = new CheckOutFF();
        GetFileAttachment objGetFileAttachment = new GetFileAttachment();
        GetFileAttachmentAllFiles objGetFileAttachmentAllFiles = new GetFileAttachmentAllFiles();
        GetFileAttachmentFileFolder objGetFileAttachmentFileFolder = new GetFileAttachmentFileFolder();
        GetFileAttachmentFileIds objGetFileAttachmentFileIds = new GetFileAttachmentFileIds();
        GetFileFF objGetFileFF = new GetFileFF();
        GetFileFFAllFiles objGetFileFFAllFiles = new GetFileFFAllFiles();
        GetFileFFFileVersion objGetFileFFFileVersion = new GetFileFFFileVersion();
        CheckPrivilege objCheckPrivilege = new CheckPrivilege();
        CreateObjectAPIName objCreateObjectAPIName = new CreateObjectAPIName();
        CreateObjectAttributeId objCreateObjectAttributeId = new CreateObjectAttributeId();
        CreateObjectFixedProject objCreateObjectFixedProject = new CreateObjectFixedProject();
        CreateObjectManufacturerPart objCreateObjectManufacturerPart = new CreateObjectManufacturerPart();
        CreateObjectPublishedPriceonMPN objCreateObjectPublishedPriceonMPN = new CreateObjectPublishedPriceonMPN();
        CreateObjectUpdateExistingObject objCreateObjectUpdateExistingObject = new CreateObjectUpdateExistingObject();
        DeleteObject objDeleteObject = new DeleteObject();
        getManufacturerPart objgetManufacturerPart = new getManufacturerPart();
        GetObject objGetObject = new GetObject();
        getObjectRedlineChange objgetObjectRedlineChange = new getObjectRedlineChange();
        GetObjectVersion objGetObjectVersion = new GetObjectVersion();
        IsDeleted objIsDeleted = new IsDeleted();
        SaveAsObject objSaveAsObject = new SaveAsObject();
        SaveAsProgram objSaveAsProgram = new SaveAsProgram();
        SaveAsProgramTemplate objSaveAsProgramTemplate = new SaveAsProgramTemplate();
        SendObject objSendObject = new SendObject();
        UndeleteObject objUndeleteObject = new UndeleteObject();
        UpdateManufacturerPart objUpdateManufacturerPart = new UpdateManufacturerPart();
        UpdateObject objUpdateObject = new UpdateObject();
        UpdateObjectRedlineChange objUpdateObjectRedlineChange = new UpdateObjectRedlineChange();
        UpdateObjectReturnUpdatedObject objUpdateObjectReturnUpdatedObject = new UpdateObjectReturnUpdatedObject();
        AddApprovers objAddApprovers = new AddApprovers();
        ApproveROWarningResolution objApproveROWarningResolution = new ApproveROWarningResolution();
        approveRObject objapproveRObject = new approveRObject();
        AuditRObj objAuditRObj = new AuditRObj();
        ChangeStatus objChangeStatus = new ChangeStatus();
        ChangeStatusProgram objChangeStatusProgram = new ChangeStatusProgram();
        CommentRObject objCommentRObject = new CommentRObject();
        GetApprovers objGetApprovers = new GetApprovers();
        GetStatus objGetStatus = new GetStatus();
        GetWorkflows objGetWorkflows = new GetWorkflows();
        RejectRObject objRejectRObject = new RejectRObject();
        RemoveApprovers objRemoveApprovers = new RemoveApprovers();
        SetWorkflows objSetWorkflows = new SetWorkflows();
        SetIncorporate objSetIncorporate = new SetIncorporate();
        AdvancedSearch objAdvancedSearch = new AdvancedSearch();
        AdvancedSearchParams objAdvancedSearchParams = new AdvancedSearchParams();
        GetSearchableAttributes objGetSearchableAttributes = new GetSearchableAttributes();
        QuickSearch objQuickSearch = new QuickSearch();
        WhereUsedQuery objWhereUsedQuery = new WhereUsedQuery();
        AddRowsAffectedItems objAddRows = new AddRowsAffectedItems();
        AddRowsAMLSite objAddRowsAMLSite = new AddRowsAMLSite();
        AddRowsBOMRedlineChange objAddRowsRedlineChange = new AddRowsBOMRedlineChange();
        AddRowsSiteSpecific objAddRowsSiteSpecific = new AddRowsSiteSpecific();
        ClearAffectedItemsTable objClearTable = new ClearAffectedItemsTable();
        CopyTable objCopyTable = new CopyTable();
        IsReadOnly objIsReadOnly = new IsReadOnly();
        LoadTable objLoadTable = new LoadTable();
        LoadTableFolderVersion objLoadTableFolderVersion = new LoadTableFolderVersion();
        LoadTableBOMRedlineChange objLoadTableRedlineChange = new LoadTableBOMRedlineChange();
        LoadTableRevision objLoadTableRevision = new LoadTableRevision();
        LoadTableSite objLoadTableSite = new LoadTableSite();
        RemoveRows objRemoveRows = new RemoveRows();
        UpdateRows objUpdateRows = new UpdateRows();  
        
        UpdateNewProject updateNewProject = new UpdateNewProject();
        UpdateExistingProject updateExistingProject = new UpdateExistingProject();
        ValidateProject validateProject = new ValidateProject();
        LoadProject loadProject = new LoadProject();
        LoadDeliverablesStatus loadDeliverablesStatus = new LoadDeliverablesStatus();
        
        AddFileToAF addFileToAF = new AddFileToAF();  
        CheckoutAF checkoutAF = new CheckoutAF();  
        CheckInAF checkInAF = new CheckInAF();  
        CancelCheckoutAF cancelCheckoutAF = new CancelCheckoutAF();  
        UpdateAFRows updateAFRows = new UpdateAFRows();  
        MarkupTableAction markupTableAction = new MarkupTableAction();  
        SetIncorporateAF setIncorporateAF = new SetIncorporateAF();  
        RemoveAFRows removeAFRows = new RemoveAFRows();  
        UpdateRowsFileFolderRedlineTitleBlock updateRowsFileFolderRedlineTitleBlock = new UpdateRowsFileFolderRedlineTitleBlock();  
        LoadTableFolderChangeHistory loadTableFolderChangeHistory = new LoadTableFolderChangeHistory();  
        LoadTableFolderPendingChanges loadTableFolderPendingChanges = new LoadTableFolderPendingChanges();  
        CreateReferenceObject createReferenceObject = new CreateReferenceObject();  
        UpdateReferenceObject updateReferenceObject = new UpdateReferenceObject();  
        DeleteReferenceObject deleteReferenceObject = new DeleteReferenceObject();  
        AdvancedSearchReferenceObject advancedSearchReferenceObject = new AdvancedSearchReferenceObject();  
        AddRowsReferenceObject addRowsReferenceObject = new AddRowsReferenceObject();  
        RemoveRowsReferenceObject removeRowsReferenceObject = new RemoveRowsReferenceObject();  
        GetReviewers getReviewers = new GetReviewers();  
        CreateCalendar vreateCalendar = new CreateCalendar();  
        UpdateCalendar updateCalendar = new UpdateCalendar();  
        GetCalendar getCalendar = new GetCalendar();  
        RemoveCalendar removeCalendar = new RemoveCalendar();  
        ExportSearchedTimesheet exportSearchedTimesheet = new ExportSearchedTimesheet();  
        ExportToAXML exportToAXML = new ExportToAXML();  
        LogOrChangeTimesheet logOrChangeTimesheet = new LogOrChangeTimesheet();  
        RetrieveTimesheet retrieveTimesheet = new RetrieveTimesheet();  
        
    }
    
    public static void main(String args[]){      

    	createAllObjects();

    	File file = new File("Samples_RunResults.txt");
    	if( file.exists() )
    		file.delete();

    	System.out.println("AdminMetadata Webservice samples"); 
    	System.out.println("-------------------------------------------"); 
    	System.out.println("\nRunning sample 1. 'ConvertCurrency'..."); 
    	ConvertCurrency.main(args);
    	System.out.println("\nRunning sample 2. 'getAttributes'..."); 
    	getAttributes.main(args);
    	System.out.println("\nRunning sample 3. 'GetAutoNumbers'..."); 
    	GetAutoNumbers.main(args);
    	System.out.println("\nRunning sample 4. 'GetAllClasses'..."); 
    	GetAllClasses.main(args);
    	System.out.println("\nRunning sample 5. 'GetLists'..."); 
    	GetLists.main(args);
    	System.out.println("\nRunning sample 6. 'GetTableMetaData'..."); 
    	GetTableMetaData.main(args);
    	System.out.println("\nRunning sample 7. 'GetNode'..."); 
    	GetNode.main(args);
    	System.out.println("\nRunning sample 8. 'GetSubClasses'..."); 
    	GetSubClasses.main(args);
    	System.out.println("\nRunning sample 9. 'GetUserGroups'..."); 
    	GetUserGroups.main(args);
    	System.out.println("\nRunning sample 10. 'GetUsers'..."); 
    	GetUsers.main(args);


    	System.out.println("Attachment Webservice samples"); 
    	System.out.println("-------------------------------------------"); 
    	System.out.println("\nRunning sample 11. 'AddFileAttachment'..."); 
    	AddFileAttachment.main(args);
    	System.out.println("\nRunning sample 12. 'AddFileAttachmentReference'..."); 
    	AddFileAttachmentReference.main(args);
    	System.out.println("\nRunning sample 13. 'AddFileAttachmentSingleFolder'..."); 
    	AddFileAttachmentSingleFolder.main(args);
    	System.out.println("\nRunning sample 14. 'AddFileAttachmentURL'..."); 
    	AddFileAttachmentURL.main(args);
    	System.out.println("\nRunning sample 15. 'AddFileFF'..."); 
    	AddFileFF.main(args);
    	System.out.println("\nRunning sample 16. 'AddFileFFReference'..."); 
    	AddFileFFReference.main(args);
    	System.out.println("\nRunning sample 17. 'AddFileFFUrls'..."); 
    	AddFileFFUrls.main(args);
    	System.out.println("\nRunning sample 18. 'CancelCheckOut'..."); 
    	CancelCheckOut.main(args);
    	System.out.println("\nRunning sample 19. 'CheckInAttachment'..."); 
    	CheckInAttachment.main(args);
    	System.out.println("\nRunning sample 20. 'CheckInAttachmentFileId'..."); 
    	CheckInAttachmentFileId.main(args);
    	System.out.println("\nRunning sample 21. 'CheckInFF'..."); 
    	CheckInFF.main(args);
    	System.out.println("\nRunning sample 22. 'CheckOutAttachment'..."); 
    	CheckOutAttachment.main(args);
    	System.out.println("\nRunning sample 23. 'CheckOutAttachmentAllFiles'..."); 
    	CheckOutAttachmentAllFiles.main(args);
    	System.out.println("\nRunning sample 24. 'CheckOutAttachmentFileFolder'..."); 
    	CheckOutAttachmentFileFolder.main(args);
    	System.out.println("\nRunning sample 25. 'CheckOutAttachmentFileIds'..."); 
    	CheckOutAttachmentFileIds.main(args);
    	System.out.println("\nRunning sample 26. 'CheckOutFF'..."); 
    	CheckOutFF.main(args);
    	System.out.println("\nRunning sample 27. 'GetFileAttachment'..."); 
    	GetFileAttachment.main(args);
    	System.out.println("\nRunning sample 28. 'GetFileAttachmentAllFiles'..."); 
    	GetFileAttachmentAllFiles.main(args);
    	System.out.println("\nRunning sample 29. 'GetFileAttachmentFileFolder'..."); 
    	GetFileAttachmentFileFolder.main(args);
    	System.out.println("\nRunning sample 30. 'GetFileAttachmentFileIds'..."); 
    	GetFileAttachmentFileIds.main(args);
    	System.out.println("\nRunning sample 31. 'GetFileFF'..."); 
    	GetFileFF.main(args);
    	System.out.println("\nRunning sample 32. 'GetFileFFAllFiles'..."); 
    	GetFileFFAllFiles.main(args);
    	System.out.println("\nRunning sample 33. 'GetFileFFFileVersion'..."); 
    	GetFileFFFileVersion.main(args);


    	System.out.println("BusinessObject Webservice samples"); 
    	System.out.println("-------------------------------------------"); 
    	System.out.println("\nRunning sample 34. 'CheckPrivilege'..."); 
    	CheckPrivilege.main(args);
    	System.out.println("\nRunning sample 35. 'CreateObjectAPIName'..."); 
    	CreateObjectAPIName.main(args);
    	System.out.println("\nRunning sample 36. 'CreateObjectAttributeId'..."); 
    	CreateObjectAttributeId.main(args);
    	System.out.println("\nRunning sample 37. 'CreateObjectFixedProject'..."); 
    	CreateObjectFixedProject.main(args);
    	System.out.println("\nRunning sample 38. 'CreateObjectManufacturerPart'..."); 
    	CreateObjectManufacturerPart.main(args);
    	System.out.println("\nRunning sample 39. 'CreateObjectPublishedPriceonMPN'..."); 
    	CreateObjectPublishedPriceonMPN.main(args);
    	System.out.println("\nRunning sample 40. 'CreateObjectUpdateExistingObject'..."); 
    	CreateObjectUpdateExistingObject.main(args);
    	System.out.println("\nRunning sample 41. 'DeleteObject'..."); 
    	DeleteObject.main(args);
    	System.out.println("\nRunning sample 42. 'getManufacturerPart'..."); 
    	getManufacturerPart.main(args);
    	System.out.println("\nRunning sample 43. 'GetObject'..."); 
    	GetObject.main(args);
    	System.out.println("\nRunning sample 44. 'getObjectRedlineChange'..."); 
    	getObjectRedlineChange.main(args);
    	System.out.println("\nRunning sample 45. 'GetObjectVersion'..."); 
    	GetObjectVersion.main(args);
    	System.out.println("\nRunning sample 46. 'IsDeleted'..."); 
    	IsDeleted.main(args);
    	System.out.println("\nRunning sample 47. 'SaveAsObject'..."); 
    	SaveAsObject.main(args);
    	System.out.println("\nRunning sample 48. 'SaveAsProgram'..."); 
    	SaveAsProgram.main(args);
    	System.out.println("\nRunning sample 49. 'SaveAsProgramTemplate'..."); 
    	SaveAsProgramTemplate.main(args);
    	System.out.println("\nRunning sample 50. 'SendObject'..."); 
    	SendObject.main(args);
    	System.out.println("\nRunning sample 51. 'UndeleteObject'..."); 
    	UndeleteObject.main(args);
    	System.out.println("\nRunning sample 52. 'UpdateManufacturerPart'..."); 
    	UpdateManufacturerPart.main(args);
    	System.out.println("\nRunning sample 53. 'UpdateObject'..."); 
    	UpdateObject.main(args);
    	System.out.println("\nRunning sample 54. 'UpdateObjectRedlineChange'..."); 
    	UpdateObjectRedlineChange.main(args);
    	System.out.println("\nRunning sample 55. 'UpdateObjectReturnUpdatedObject'..."); 
    	UpdateObjectReturnUpdatedObject.main(args);


    	System.out.println("Collaboration Webservice samples"); 
    	System.out.println("-------------------------------------------"); 
    	System.out.println("\nRunning sample 56. 'AddApprovers'..."); 
    	AddApprovers.main(args);
    	System.out.println("\nRunning sample 57. 'approveRObject'..."); 
    	approveRObject.main(args);
    	System.out.println("\nRunning sample 58. 'ApproveROWarningResolution'..."); 
    	ApproveROWarningResolution.main(args);
    	System.out.println("\nRunning sample 59. 'AuditRObj'..."); 
    	AuditRObj.main(args);
    	System.out.println("\nRunning sample 60. 'ChangeStatus'..."); 
    	ChangeStatus.main(args);
    	System.out.println("\nRunning sample 61. 'ChangeStatusProgram'..."); 
    	ChangeStatusProgram.main(args);
    	System.out.println("\nRunning sample 62. 'CommentRObject'..."); 
    	CommentRObject.main(args);
    	System.out.println("\nRunning sample 63. 'GetApprovers'..."); 
    	GetApprovers.main(args);
    	System.out.println("\nRunning sample 64. 'GetStatus'..."); 
    	GetStatus.main(args);
    	System.out.println("\nRunning sample 65. 'GetWorkflows'..."); 
    	GetWorkflows.main(args);
    	System.out.println("\nRunning sample 66. 'RejectRObject'..."); 
    	RejectRObject.main(args);
    	System.out.println("\nRunning sample 67. 'RemoveApprovers'..."); 
    	RemoveApprovers.main(args);
    	System.out.println("\nRunning sample 68. 'SetWorkflows'..."); 
    	SetWorkflows.main(args);


    	System.out.println("PC Webservice samples"); 
    	System.out.println("-------------------------------------------"); 
    	System.out.println("\nRunning sample 69. 'SetIncorporate'..."); 
    	SetIncorporate.main(args);


    	System.out.println("Search Webservice samples"); 
    	System.out.println("-------------------------------------------"); 
    	System.out.println("\nRunning sample 70. 'AdvancedSearch'..."); 
    	AdvancedSearch.main(args);
    	System.out.println("\nRunning sample 71. 'AdvancedSearchParams'..."); 
    	AdvancedSearchParams.main(args);
    	System.out.println("\nRunning sample 72. 'GetSearchableAttributes'..."); 
    	GetSearchableAttributes.main(args);
    	System.out.println("\nRunning sample 73. 'QuickSearch'..."); 
    	QuickSearch.main(args);
    	System.out.println("\nRunning sample 74. 'WhereUsedQuery'..."); 
    	WhereUsedQuery.main(args);


    	System.out.println("Table Webservice samples"); 
    	System.out.println("-------------------------------------------"); 
    	System.out.println("\nRunning sample 75. 'AddRows'..."); 
    	AddRowsAffectedItems.main(args);
    	System.out.println("\nRunning sample 76. 'AddRowsAMLSite'..."); 
    	AddRowsAMLSite.main(args);
    	System.out.println("\nRunning sample 77. 'AddRowsRedlineChange'..."); 
    	AddRowsBOMRedlineChange.main(args);
    	System.out.println("\nRunning sample 78. 'AddRowsSiteSpecific'..."); 
    	AddRowsSiteSpecific.main(args);
    	System.out.println("\nRunning sample 79. 'ClearTable'..."); 
    	ClearAffectedItemsTable.main(args);
    	System.out.println("\nRunning sample 80. 'CopyTable'..."); 
    	CopyTable.main(args);
    	System.out.println("\nRunning sample 81. 'IsReadOnly'..."); 
    	IsReadOnly.main(args);
    	System.out.println("\nRunning sample 82. 'LoadTable'..."); 
    	LoadTable.main(args);
    	System.out.println("\nRunning sample 83. 'LoadTableFolderVersion'..."); 
    	LoadTableFolderVersion.main(args);
    	System.out.println("\nRunning sample 84. 'LoadTableRedlineChange'..."); 
    	LoadTableBOMRedlineChange.main(args);
    	System.out.println("\nRunning sample 85. 'LoadTableRevision'..."); 
    	LoadTableRevision.main(args);
    	System.out.println("\nRunning sample 86. 'LoadTableSite'..."); 
    	LoadTableSite.main(args);
    	System.out.println("\nRunning sample 87. 'RemoveRows'..."); 
    	RemoveRows.main(args);
    	System.out.println("\nRunning sample 88. 'UpdateRows'..."); 
    	UpdateRows.main(args);

    	System.out.println("Project Webservice samples"); 
    	System.out.println("-------------------------------------------"); 
    	System.out.println("\nRunning sample 89. 'UpdateNewProject'...");
    	UpdateNewProject.main(args);
    	System.out.println("\nRunning sample 90. 'UpdateExistingProject'...");
    	UpdateExistingProject.main(args);
    	System.out.println("\nRunning sample 91. 'ValidateProject'...");
    	ValidateProject.main(args);
    	System.out.println("\nRunning sample 92. 'LoadProject'...");
    	LoadProject.main(args);
    	System.out.println("\nRunning sample 93. 'LoadDeliverablesStatus'...");
    	LoadDeliverablesStatus.main(args);

    	System.out.println("Affected Files samples"); 
    	System.out.println("-------------------------------------------"); 
    	System.out.println("\nRunning sample 94. 'AddFileToAF'...");
    	AddFileToAF.main(args);
    	System.out.println("\nRunning sample 95. 'CheckoutAF'...");
    	CheckoutAF.main(args);
    	System.out.println("\nRunning sample 96. 'CheckInAF'...");
    	CheckInAF.main(args);
    	System.out.println("\nRunning sample 97. 'CancelCheckoutAF'...");
    	CancelCheckoutAF.main(args);
    	System.out.println("\nRunning sample 98. 'UpdateAFRows'...");
    	UpdateAFRows.main(args);
    	System.out.println("\nRunning sample 99. 'MarkupTableAction'...");
    	MarkupTableAction.main(args); 	
    	System.out.println("\nRunning sample 100. 'SetIncorporateAF'...");
    	SetIncorporateAF.main(args); 	
    	System.out.println("\nRunning sample 101. 'RemoveAFRows'...");
    	RemoveAFRows.main(args);
    	System.out.println("\nRunning sample 102. 'UpdateRowsFileFolderRedlineTitleBlock'...");    	
    	UpdateRowsFileFolderRedlineTitleBlock.main(args);
    	System.out.println("\nRunning sample 103. 'LoadTableFolderChangeHistory'...");    	
    	LoadTableFolderChangeHistory.main(args);
    	System.out.println("\nRunning sample 104. 'LoadTableFolderPendingChanges'...");    	
    	LoadTableFolderPendingChanges.main(args);
    	
    	System.out.println("ReferenceObject Webservice samples"); 
    	System.out.println("-------------------------------------------");     	
    	System.out.println("\nRunning sample 105. 'CreateReferenceObject'...");    	
    	CreateReferenceObject.main(args);
    	System.out.println("\nRunning sample 106. 'UpdateReferenceObject'...");    	
    	UpdateReferenceObject.main(args);
    	System.out.println("\nRunning sample 107. 'DeleteReferenceObject'...");    	
    	DeleteReferenceObject.main(args);
    	System.out.println("\nRunning sample 108 'AdvancedSearchReferenceObject'...");    	
    	AdvancedSearchReferenceObject.main(args);
    	System.out.println("\nRunning sample 109. 'AddRowsReferenceObject'...");    	
    	AddRowsReferenceObject.main(args);
    	System.out.println("\nRunning sample 110. 'RemoveRowsReferenceObject'...");    	
    	RemoveRowsReferenceObject.main(args);
    	    	
    	System.out.println("-------------------------------------------");     	    	
    	System.out.println("\nRunning sample 111. 'GetReviewers'...");    	
    	GetReviewers.main(args);
    	
    	System.out.println("Project Webservice samples"); 
    	System.out.println("-------------------------------------------"); 
    	System.out.println("\nRunning sample 112. 'CreateCalendar'...");
    	CreateCalendar.main(args);
    	System.out.println("\nRunning sample 113. 'UpdateCalendar'...");
    	UpdateCalendar.main(args);
    	System.out.println("\nRunning sample 114. 'GetCalendar'...");
    	GetCalendar.main(args);
    	System.out.println("\nRunning sample 115. 'RemoveCalendar'...");
    	RemoveCalendar.main(args);
    	System.out.println("\nRunning sample 116. 'ExportSearchedTimesheet'...");
    	ExportSearchedTimesheet.main(args);
    	System.out.println("\nRunning sample 117. 'ExportToAXML'...");
    	ExportToAXML.main(args);
    	System.out.println("\nRunning sample 118. 'LogOrChangeTimesheet'...");
    	LogOrChangeTimesheet.main(args);
    	System.out.println("\nRunning sample 119. 'RetrieveTimesheet'...");
    	RetrieveTimesheet.main(args);    	
    	
    	
    	System.out.println("Summary"); 
    	System.out.println("----------------------------------------------");
    	System.out.println("Run count: " + runCount );
    	System.out.println("'SUCCESS' count: " + (runCount - failureOrWarningCounter) );
    	System.out.println("'FAILURE' or 'WARNING' count: " + failureOrWarningCounter);
    	System.out.println("----------------------------------------------");


    }
}
