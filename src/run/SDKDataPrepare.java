package run;

import com.agile.api.APIException;
import com.agile.api.AgileSessionFactory;
import com.agile.api.CommonConstants;
import com.agile.api.IAdmin;
import com.agile.api.IAdminList;
import com.agile.api.IAgileClass;
import com.agile.api.IAgileList;
import com.agile.api.IAgileSession;
import com.agile.api.IAttribute;
import com.agile.api.IDataObject;
import com.agile.api.IItem;
import com.agile.api.IListLibrary;
import com.agile.api.IProgram;
import com.agile.api.IRow;
import com.agile.api.IStateful;
import com.agile.api.IStatus;
import com.agile.api.ITable;
import com.agile.api.IUser;
import com.agile.api.IUserGroup;
import com.agile.api.ItemConstants;
import com.agile.api.ProgramConstants;
import com.agile.api.PropertyConstants;
import com.agile.api.UserConstants;
import com.agile.api.UserGroupConstants;
import java.io.ByteArrayInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * This class has the methods required for preparing data using SDK. Used for
 * preparing data for running the web service samples
 */

public class SDKDataPrepare {    
    private IAgileSession agileSession = null;
    private String USERNAME = null;
    private String PASSWORD = null;
    private String WEB_URL = null;
    
    
    public SDKDataPrepare(String serverUrl, String userName, String password) {
    	this.WEB_URL = serverUrl;
    	this.USERNAME = userName;
    	this.PASSWORD = password;
    }    
    
    /**
     * Creates the SDK Session
     *
     */
    public void connect() {
		try {
			HashMap params = new HashMap();
			params.put(AgileSessionFactory.URL,WEB_URL);
			params.put(AgileSessionFactory.USERNAME, USERNAME);
			params.put(AgileSessionFactory.PASSWORD, PASSWORD);
			agileSession=AgileSessionFactory.createSessionEx(params);
		}catch (APIException e) {
			System.out.println("Error while creating SDk session");
			e.printStackTrace();
		}
	}
    
    /**
     * Returns the attached file Reference Id on the Part
     * @param objectType
     * @param objectNumber
     * @param filename
     * @return
     * @throws Exception
     */
    public int getAttachedFileRefId(Object objectType,String objectNumber) throws Exception {
    	IDataObject item = (IDataObject) agileSession.getObject(objectType,objectNumber);
    	ITable table = item.getTable(CommonConstants.TABLE_ATTACHMENTS);

    	byte[] content = "Temporary data for testing WS GetAttachment".getBytes();
    	ByteArrayInputStream in = new ByteArrayInputStream(content);
    	IRow row = (IRow)table.getTableIterator().next();

    	String rowidString = row.getId().toString();
    	StringTokenizer token;
    	token = new StringTokenizer(rowidString, ":");
    	token.nextElement();
    	return Integer.parseInt(token.nextElement().toString());
    }
    
    /**
     * Creates the user if not already exists
     * @param userId
     * @param password
     * @param status
     * @return
     * @throws Exception
     */
    public IUser createUser(String userId,String password,String status) throws Exception{
		IUser user = (IUser) agileSession.getObject(UserConstants.CLASS_USER,userId);
		if(user == null){
			Map params = new HashMap();
			params.put(UserConstants.ATT_GENERAL_INFO_USER_ID, userId);
			params.put(UserConstants.ATT_LOGIN_PASSWORD, password);
			params.put(UserConstants.ATT_GENERAL_INFO_STATUS,status);
			user = (IUser)agileSession.createObject(UserConstants.CLASS_USER, params);
		}
		return user;
	}

    /**
     * Creates the userGroup if not already exists
     * @param userGrpName
     * @param globalPersonal
     * @param status
     * @param isResourcePool
     * @return
     * @throws Exception
     */
	public IUserGroup createUserGroup(String userGrpName,String globalPersonal,String status,String isResourcePool) throws Exception{
		IUserGroup ug = (IUserGroup) agileSession.getObject(IUserGroup.OBJECT_TYPE,userGrpName);
		if (ug == null) {
			Map params = new HashMap();
			params.put(UserGroupConstants.ATT_GENERAL_INFO_NAME, userGrpName);
			params.put(UserGroupConstants.ATT_GENERAL_INFO_GLOBAL_PERSONAL,globalPersonal);
			params.put(UserGroupConstants.ATT_GENERAL_INFO_STATUS,status);
			params.put(UserGroupConstants.ATT_GENERAL_INFO_RESOURCE_POOL,isResourcePool);
			ug = (IUserGroup)agileSession.createObject(UserGroupConstants.CLASS_USER_GROUP, params);
		}
		return ug;
	}
	
	/**
	 * Assigns the user to the Usergroup
	 * @param userGrpName
	 * @param userId
	 * @throws Exception
	 */
	public void assignUserToUserGroup(String userGrpName,String userId) throws Exception{
		IUserGroup ug = (IUserGroup) agileSession.getObject(IUserGroup.OBJECT_TYPE,userGrpName);
		if (ug == null) {
			ug = (IUserGroup) agileSession.createObject(UserGroupConstants.CLASS_USER_GROUP,userGrpName);
		}
		ITable usersTab = ug.getTable(UserGroupConstants.TABLE_USERS);
		if(!usersTab.iterator().hasNext()){
			IUser user = (IUser) agileSession.getObject(UserConstants.CLASS_USER,userId);
			usersTab.add(user);
		}
	}
	
	/**
	 * Enables/Disables the visibility of Page two and Page Three attributes and
	 * asigns lists to list and multilist attributes
	 * 
	 * @param isVisible
	 * @throws Exception
	 */
	public void setVisibilityOfP2P3Attributes(boolean isVisible) throws Exception{
		String LIST_NUMBER = "One/Two/Three/Four/Five/Six/Seven/Eight/Nine/Ten";
		String LIST_COLORS = "Red/Blue/Green/Yellow/Pink/Black";
		
		Integer activitiesClass = ProgramConstants.CLASS_ACTIVITIES_CLASS;
		Integer p2TableId = ProgramConstants.TABLE_PAGETWO;
		Integer[] p2AttrIds = new Integer[7];
		p2AttrIds[0] = ProgramConstants.ATT_PAGE_TWO_DATE01;
		p2AttrIds[1] = ProgramConstants.ATT_PAGE_TWO_LIST01;
		p2AttrIds[2] = ProgramConstants.ATT_PAGE_TWO_MONEY01;
		p2AttrIds[3] = ProgramConstants.ATT_PAGE_TWO_MULTILIST01;
		p2AttrIds[4] = ProgramConstants.ATT_PAGE_TWO_MULTITEXT10;
		p2AttrIds[5] = ProgramConstants.ATT_PAGE_TWO_NUMERIC01;
		p2AttrIds[6] = ProgramConstants.ATT_PAGE_TWO_TEXT01;
		visiblityOfAttributes(activitiesClass, p2TableId, p2AttrIds, isVisible);
		IAdminList listColors = createList("Colors", LIST_COLORS, true, false);
		setList(activitiesClass, p2TableId, ProgramConstants.ATT_PAGE_TWO_LIST01, listColors);
		setList(activitiesClass, p2TableId, ProgramConstants.ATT_PAGE_TWO_MULTILIST01, listColors);

		Integer taskSubClass = ProgramConstants.CLASS_TASK;
		Integer p3TableId = ProgramConstants.TABLE_PAGETHREE;
		Integer[] p3AttrIds = new Integer[7];
		p3AttrIds[0] = ProgramConstants.ATT_PAGE_THREE_DATE01;
		p3AttrIds[1] = ProgramConstants.ATT_PAGE_THREE_LIST01;
		p3AttrIds[2] = ProgramConstants.ATT_PAGE_THREE_MONEY01;
		p3AttrIds[3] = ProgramConstants.ATT_PAGE_THREE_MULTILIST01;
		p3AttrIds[4] = ProgramConstants.ATT_PAGE_THREE_MULTITEXT10;
		p3AttrIds[5] = ProgramConstants.ATT_PAGE_THREE_NUMERIC01;
		p3AttrIds[6] = ProgramConstants.ATT_PAGE_THREE_TEXT01;
		visiblityOfAttributes(taskSubClass, p3TableId, p3AttrIds, isVisible);
		IAdminList listNumber = createList( "Number", LIST_NUMBER, true, false);
		setList(taskSubClass, p3TableId, ProgramConstants.ATT_PAGE_THREE_LIST01, listNumber);
		setList(taskSubClass, p3TableId, ProgramConstants.ATT_PAGE_THREE_MULTILIST01, listNumber);
	}
	
	/**
	 * Sets the visibility on attributes
	 * @param classId
	 * @param tableId
	 * @param attributeId
	 * @param visible
	 */
	public void visiblityOfAttributes(Integer classId, Integer tableId, Integer[] attributeId,boolean visible) {
		IAgileClass agileClass = null;
		IAttribute[] attributes = null;

		try {
			agileClass = agileSession.getAdminInstance().getAgileClass(classId);
			attributes = agileClass.getTableAttributes(tableId);
			for (int i = 0; i < attributes.length; i++) {
				for (int j = 0; j < attributeId.length; j++) {
					if (attributes[i].getId().equals(attributeId[j])) {
						if (visible) {
							attributes[i].setValue("Visible", "Yes");
							break;
						} else {
							attributes[i].setValue("Visible", "No");
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Associates the list to the attribute
	 * @param classId
	 * @param tableId
	 * @param attributeId
	 * @param list
	 */
	public void setList(Integer classId,Integer tableId, Integer attributeId, IAdminList list) {

		IAdmin admin = null;
		IAgileClass agileClass = null;
		IAttribute[] attributes = null;

		try {
			admin = agileSession.getAdminInstance();
			agileClass = admin.getAgileClass(classId);
			attributes = agileClass.getTableAttributes(tableId);
			for (int i = 0; i < attributes.length; i++) {
				if (attributes[i].getId().equals(attributeId)) {
					attributes[i].setValue(PropertyConstants.PROP_LIST, list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new List
	 * @param name
	 * @param values
	 * @param enabled
	 * @param cascaded
	 * @return
	 */
	public IAdminList createList(String name,String values, boolean enabled, boolean cascaded) {
		IAdmin admin = null;
		IListLibrary listLib = null;
		try {
			admin = agileSession.getAdminInstance();
			listLib = admin.getListLibrary();
			IAdminList tempList = listLib.getAdminList(name);
			if (tempList != null) {
				return tempList;
			}

			HashMap map = new HashMap();
			map.put(IAdminList.ATT_NAME, name);
			map.put(IAdminList.ATT_DESCRIPTION, name);
			map.put(IAdminList.ATT_ENABLED, Boolean.valueOf(enabled));
			map.put(IAdminList.ATT_CASCADED, Boolean.valueOf(cascaded));
			IAdminList listColors = listLib.createAdminList(map);

			assignValuestoList(listColors, values);
			return listColors;
		} catch (Exception e) {
			System.out.println("Meaasge:" + e.getMessage());
		}
		return null;
	}

	/**
	 * Assigns values to the list
	 * @param mlist
	 * @param values
	 */
	public void assignValuestoList(IAdminList mlist, String values) {
		try {
			IAgileList list = mlist.getValues();
			String[] temp = values.split("/");
			for (int i = 0; i < temp.length; i++) {
				list.addChild(temp[i]);
			}
			mlist.setValues(list);
		} catch (Exception e) {
			System.out.println("Meaasge:" + e.getMessage());
		}
	}
	
	/**
	 * Creates a new Program
	 * @param objectNumber
	 * @return
	 * @throws Exception
	 */
	public String createProgram(String objectNumber) throws Exception {
		Map param = new HashMap();
		param.put(ProgramConstants.ATT_GENERAL_INFO_NAME, objectNumber);
		
		Calendar startCal = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT"));
		startCal.set(2009, 10, 5,8,0,0); 
        Date start = startCal.getTime();
        param.put(ProgramConstants.ATT_GENERAL_INFO_SCHEDULE_START_DATE,start);
		
        Calendar endCal = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT"));
        endCal.set(2009, 10, 7,17,0,0); 
        Date end = endCal.getTime();
        param.put(ProgramConstants.ATT_GENERAL_INFO_SCHEDULE_END_DATE, end);

		IAttribute attr = agileSession.getAdminInstance().getAgileClass(ProgramConstants.CLASS_PROGRAM).getAttribute(ProgramConstants.ATT_GENERAL_INFO_DURATION_TYPE);
		IAgileList avail = attr.getAvailableValues();
		avail.setSelection(new Object[] { avail.getChildren()[0] });
		param.put(ProgramConstants.ATT_GENERAL_INFO_DURATION_TYPE, avail);

		IDataObject agileObject = (IDataObject) agileSession.createObject(ProgramConstants.CLASS_PROGRAM, param);

		return (String) agileObject.getValue(ProgramConstants.ATT_GENERAL_INFO_NUMBER);
	}
	
	/**
	 * Creates a new Program with an content attached to it
	 * @param prjName
	 * @return
	 * @throws Exception
	 */
	public String createProgramWithDeliverable(String prjName) throws Exception{
		Map param = new HashMap();
		param.put(ProgramConstants.ATT_GENERAL_INFO_NAME, prjName);

		Calendar startCal = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT"));
		startCal.set(2009, 10, 5,8,0,0); 
		Date start = startCal.getTime();
		param.put(ProgramConstants.ATT_GENERAL_INFO_SCHEDULE_START_DATE,start);

		Calendar endCal = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT"));
		endCal.set(2009, 10, 7,17,0,0); 
		Date end = endCal.getTime();
		param.put(ProgramConstants.ATT_GENERAL_INFO_SCHEDULE_END_DATE, end);

		param.put("projectState", new Integer(1));

		IAttribute attr = agileSession.getAdminInstance().getAgileClass(ProgramConstants.CLASS_PROGRAM).
		getAttribute(ProgramConstants.ATT_GENERAL_INFO_DURATION_TYPE);
		IAgileList avail = attr.getAvailableValues();
		avail.setSelection(new Object[] {avail.getChildren()[0]});
		param.put(ProgramConstants.ATT_GENERAL_INFO_DURATION_TYPE, avail);

		IProgram program = (IProgram) agileSession.createObject(ProgramConstants.CLASS_PROGRAM, param);
		Integer attrId = ProgramConstants.ATT_GENERAL_INFO_NUMBER;


		ITable table = program.getTable(ProgramConstants.TABLE_RELATIONSHIPS);
		IItem item  = (IItem) agileSession.createObject(ItemConstants.CLASS_PART,"CONTENT"+System.currentTimeMillis());
		IRow row = table.createRow(item);

		IStatus prgStatus = getStatus(program,"In Process");
		IStatus itemStatus = getStatus(item,"Inactive");

		Map map=new HashMap();
		map.put(CommonConstants.ATT_RELATIONSHIPS_RULE_CONTROLOBJECT,item);
		map.put(CommonConstants.ATT_RELATIONSHIPS_RULE_CONTROLOBJECTSTATUS,itemStatus);
		map.put(CommonConstants.ATT_RELATIONSHIPS_RULE_AFFECTEDOBJECT,program);
		map.put(CommonConstants.ATT_RELATIONSHIPS_RULE_AFFECTEDOBJECTSTATUS,prgStatus);
		row.setValue(CommonConstants.ATT_RELATIONSHIPS_RULE, map);

		return (String)program.getValue(ProgramConstants.ATT_GENERAL_INFO_NUMBER);
	}
	
	/**
	 * Gets the matching IStatus of teh status from the DataObject
	 * @param stateObj
	 * @param STATUS
	 * @return
	 * @throws Exception
	 */
	private IStatus getStatus(IDataObject stateObj, String STATUS) throws Exception {
		IStateful state = (IStateful)stateObj;
		IStatus[] statuses = state.getStates();
		IStatus status = null;
		for(int i=0; i<statuses.length; i++){
			if(statuses[i].getName().equals(STATUS)){
				status = statuses[i];
				break;
			}
		}
		return status;
	}
    
    public static void main(String args[]){
        System.out.println("Cannot run SDKDataPrepare in standalone mode");        
    }
}
