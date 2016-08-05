package common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.agile.ws.schema.common.v1.jaxws.AgileListEntryType;
import com.agile.ws.schema.common.v1.jaxws.ObjectFactory;
import com.agile.ws.schema.common.v1.jaxws.ObjectReferentIdType;

public class WSUtil {
    public static Element createMessageElement(String tagName) {
        Document document = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            document = docBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            return null;
        }
        return document.createElement(tagName);
    }
    
    public static Element createMessageElement(String tagName, AgileListEntryType agileListEntryType) {
    	
    	Element element = createMessageElement(tagName);
    	return	marshal(element,agileListEntryType);
    	
    }
    
  
    public static Element marshal(Element domElement,ObjectReferentIdType objectReferentIdType) {
 
        JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(ObjectReferentIdType.class);
			Marshaller marshaller = jc.createMarshaller();
			//marshaller.marshal(objectReferentIdType, domElement);
			JAXBElement<ObjectReferentIdType> jaxbEl = new JAXBElement<ObjectReferentIdType>(new QName("",domElement.getNodeName()),
					ObjectReferentIdType.class, objectReferentIdType);
			marshaller.marshal(jaxbEl, domElement);
			domElement =  (Element) domElement.getFirstChild();
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		//domElement.setAttributeNS("http://xmlns.oracle.com/AgileObjects/Core/Common/V1", "type", "ObjectReferentIdType");
		
		domElement.setAttribute("xmlns:xsi","http://xmlns.oracle.com/AgileObjects/Core/Common/V1");
		 domElement.setAttribute("xsi:type", "ObjectReferentIdType");
       
		return domElement;
    }
    
    public static Element marshal(Element domElement,AgileListEntryType agileListEntryType) {
    	 
        JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(AgileListEntryType.class);
			Marshaller marshaller = jc.createMarshaller();
		    //marshaller.marshal(agileListEntryType, domElement);
			
			JAXBElement<AgileListEntryType> jaxbEl = new JAXBElement<AgileListEntryType>(new QName("",domElement.getNodeName()),
		    		AgileListEntryType.class, agileListEntryType);
		    marshaller.marshal(jaxbEl, domElement);
		    domElement =  (Element) domElement.getFirstChild();

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		//domElement.setAttributeNS("http://xmlns.oracle.com/AgileObjects/Core/Common/V1", "type", "AgileListEntryType");
		domElement.setAttribute("xmlns:xsi","http://xmlns.oracle.com/AgileObjects/Core/Common/V1");
		 domElement.setAttribute("xsi:type", "AgileListEntryType");
		return domElement;
    }
    
  
    public static Object unmarshalToAgileListEntryType (Node node) {

        try {
            JAXBContext jc = null;
            Class<?> clz = null;

            String xsiType = ((Element)node).getAttributeNS("http://xmlns.oracle.com/AgileObjects/Core/Common/V1", "type");
            boolean isXSITypeEmpty = "".equals(xsiType) || null==xsiType ? true : false;

            jc = JAXBContext.newInstance(AgileListEntryType.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            Object obj = unmarshaller.unmarshal(node);
            JAXBElement<?> jaxbEle = (JAXBElement<?>) obj;
            
            return jaxbEle.getValue();
        } catch (JAXBException e) {
        	e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static byte[] streamToByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        int b = is.read();
        while (b > -1) {
            tmp.write(b);
            b = is.read();
        }
        tmp.flush();
        return tmp.toByteArray();
    }
}
