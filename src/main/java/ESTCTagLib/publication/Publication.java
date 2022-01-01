package ESTCTagLib.publication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import ESTCTagLib.record.Record;

import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.Sequence;

@SuppressWarnings("serial")
public class Publication extends ESTCTagLibTagSupport {

	static Publication currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(Publication.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	int ID = 0;
	String recType = null;
	String bibLevel = null;
	String multiLevel = null;
	String dateStatus = null;
	String date1 = null;
	String date2 = null;
	String location = null;
	String language = null;
	String illustrations = null;
	String form = null;
	String title = null;
	String remainder = null;
	String extent = null;
	String dimensions = null;
	String other = null;
	String gac = null;
	String pubLocation = null;
	String publisher = null;

	private String var = null;

	private Publication cachedPublication = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Record theRecord = (Record)findAncestorWithClass(this, Record.class);
			if (theRecord!= null)
				parentEntities.addElement(theRecord);

			if (theRecord == null) {
			} else {
				ID = theRecord.getID();
			}

			PublicationIterator thePublicationIterator = (PublicationIterator)findAncestorWithClass(this, PublicationIterator.class);

			if (thePublicationIterator != null) {
				ID = thePublicationIterator.getID();
			}

			if (thePublicationIterator == null && theRecord == null && ID == 0) {
				// no ID was provided - the default is to assume that it is a new Publication and to generate a new ID
				ID = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or ID was provided as an attribute - we need to load a Publication from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select rec_type,bib_level,multi_level,date_status,date1,date2,location,language,illustrations,form,title,remainder,extent,dimensions,other,gac,pub_location,publisher from navigation.publication where id = ?");
				stmt.setInt(1,ID);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (recType == null)
						recType = rs.getString(1);
					if (bibLevel == null)
						bibLevel = rs.getString(2);
					if (multiLevel == null)
						multiLevel = rs.getString(3);
					if (dateStatus == null)
						dateStatus = rs.getString(4);
					if (date1 == null)
						date1 = rs.getString(5);
					if (date2 == null)
						date2 = rs.getString(6);
					if (location == null)
						location = rs.getString(7);
					if (language == null)
						language = rs.getString(8);
					if (illustrations == null)
						illustrations = rs.getString(9);
					if (form == null)
						form = rs.getString(10);
					if (title == null)
						title = rs.getString(11);
					if (remainder == null)
						remainder = rs.getString(12);
					if (extent == null)
						extent = rs.getString(13);
					if (dimensions == null)
						dimensions = rs.getString(14);
					if (other == null)
						other = rs.getString(15);
					if (gac == null)
						gac = rs.getString(16);
					if (pubLocation == null)
						pubLocation = rs.getString(17);
					if (publisher == null)
						publisher = rs.getString(18);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving ID " + ID, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving ID " + ID);
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error retrieving ID " + ID,e);
			}

		} finally {
			freeConnection();
		}

		if(pageContext != null){
			Publication currentPublication = (Publication) pageContext.getAttribute("tag_publication");
			if(currentPublication != null){
				cachedPublication = currentPublication;
			}
			currentPublication = this;
			pageContext.setAttribute((var == null ? "tag_publication" : var), currentPublication);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedPublication != null){
				pageContext.setAttribute((var == null ? "tag_publication" : var), this.cachedPublication);
			}else{
				pageContext.removeAttribute((var == null ? "tag_publication" : var));
				this.cachedPublication = null;
			}
		}

		try {
			Boolean error = null; // (Boolean) pageContext.getAttribute("tagError");
			if(pageContext != null){
				error = (Boolean) pageContext.getAttribute("tagError");
			}

			if(error != null && error){

				freeConnection();
				clearServiceState();

				Exception e = (Exception) pageContext.getAttribute("tagErrorException");
				String message = (String) pageContext.getAttribute("tagErrorMessage");

				Tag parent = getParent();
				if(parent != null){
					return parent.doEndTag();
				}else if(e != null && message != null){
					throw new JspException(message,e);
				}else if(parent == null){
					pageContext.removeAttribute("tagError");
					pageContext.removeAttribute("tagErrorException");
					pageContext.removeAttribute("tagErrorMessage");
				}
			}
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update navigation.publication set rec_type = ?, bib_level = ?, multi_level = ?, date_status = ?, date1 = ?, date2 = ?, location = ?, language = ?, illustrations = ?, form = ?, title = ?, remainder = ?, extent = ?, dimensions = ?, other = ?, gac = ?, pub_location = ?, publisher = ? where id = ? ");
				stmt.setString( 1, recType );
				stmt.setString( 2, bibLevel );
				stmt.setString( 3, multiLevel );
				stmt.setString( 4, dateStatus );
				stmt.setString( 5, date1 );
				stmt.setString( 6, date2 );
				stmt.setString( 7, location );
				stmt.setString( 8, language );
				stmt.setString( 9, illustrations );
				stmt.setString( 10, form );
				stmt.setString( 11, title );
				stmt.setString( 12, remainder );
				stmt.setString( 13, extent );
				stmt.setString( 14, dimensions );
				stmt.setString( 15, other );
				stmt.setString( 16, gac );
				stmt.setString( 17, pubLocation );
				stmt.setString( 18, publisher );
				stmt.setInt(19,ID);
				stmt.executeUpdate();
				stmt.close();
			}
		} catch (SQLException e) {
			log.error("Error: IOException while writing to the user", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: IOException while writing to the user");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: IOException while writing to the user");
			}

		} finally {
			clearServiceState();
			freeConnection();
		}
		return super.doEndTag();
	}

	public void insertEntity() throws JspException, SQLException {
		if (recType == null){
			recType = "";
		}
		if (bibLevel == null){
			bibLevel = "";
		}
		if (multiLevel == null){
			multiLevel = "";
		}
		if (dateStatus == null){
			dateStatus = "";
		}
		if (date1 == null){
			date1 = "";
		}
		if (date2 == null){
			date2 = "";
		}
		if (location == null){
			location = "";
		}
		if (language == null){
			language = "";
		}
		if (illustrations == null){
			illustrations = "";
		}
		if (form == null){
			form = "";
		}
		if (title == null){
			title = "";
		}
		if (remainder == null){
			remainder = "";
		}
		if (extent == null){
			extent = "";
		}
		if (dimensions == null){
			dimensions = "";
		}
		if (other == null){
			other = "";
		}
		if (gac == null){
			gac = "";
		}
		if (pubLocation == null){
			pubLocation = "";
		}
		if (publisher == null){
			publisher = "";
		}
		PreparedStatement stmt = getConnection().prepareStatement("insert into navigation.publication(id,rec_type,bib_level,multi_level,date_status,date1,date2,location,language,illustrations,form,title,remainder,extent,dimensions,other,gac,pub_location,publisher) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		stmt.setInt(1,ID);
		stmt.setString(2,recType);
		stmt.setString(3,bibLevel);
		stmt.setString(4,multiLevel);
		stmt.setString(5,dateStatus);
		stmt.setString(6,date1);
		stmt.setString(7,date2);
		stmt.setString(8,location);
		stmt.setString(9,language);
		stmt.setString(10,illustrations);
		stmt.setString(11,form);
		stmt.setString(12,title);
		stmt.setString(13,remainder);
		stmt.setString(14,extent);
		stmt.setString(15,dimensions);
		stmt.setString(16,other);
		stmt.setString(17,gac);
		stmt.setString(18,pubLocation);
		stmt.setString(19,publisher);
		stmt.executeUpdate();
		stmt.close();
		freeConnection();
	}

	public int getID () {
		return ID;
	}

	public void setID (int ID) {
		this.ID = ID;
	}

	public int getActualID () {
		return ID;
	}

	public String getRecType () {
		if (commitNeeded)
			return "";
		else
			return recType;
	}

	public void setRecType (String recType) {
		this.recType = recType;
		commitNeeded = true;
	}

	public String getActualRecType () {
		return recType;
	}

	public String getBibLevel () {
		if (commitNeeded)
			return "";
		else
			return bibLevel;
	}

	public void setBibLevel (String bibLevel) {
		this.bibLevel = bibLevel;
		commitNeeded = true;
	}

	public String getActualBibLevel () {
		return bibLevel;
	}

	public String getMultiLevel () {
		if (commitNeeded)
			return "";
		else
			return multiLevel;
	}

	public void setMultiLevel (String multiLevel) {
		this.multiLevel = multiLevel;
		commitNeeded = true;
	}

	public String getActualMultiLevel () {
		return multiLevel;
	}

	public String getDateStatus () {
		if (commitNeeded)
			return "";
		else
			return dateStatus;
	}

	public void setDateStatus (String dateStatus) {
		this.dateStatus = dateStatus;
		commitNeeded = true;
	}

	public String getActualDateStatus () {
		return dateStatus;
	}

	public String getDate1 () {
		if (commitNeeded)
			return "";
		else
			return date1;
	}

	public void setDate1 (String date1) {
		this.date1 = date1;
		commitNeeded = true;
	}

	public String getActualDate1 () {
		return date1;
	}

	public String getDate2 () {
		if (commitNeeded)
			return "";
		else
			return date2;
	}

	public void setDate2 (String date2) {
		this.date2 = date2;
		commitNeeded = true;
	}

	public String getActualDate2 () {
		return date2;
	}

	public String getLocation () {
		if (commitNeeded)
			return "";
		else
			return location;
	}

	public void setLocation (String location) {
		this.location = location;
		commitNeeded = true;
	}

	public String getActualLocation () {
		return location;
	}

	public String getLanguage () {
		if (commitNeeded)
			return "";
		else
			return language;
	}

	public void setLanguage (String language) {
		this.language = language;
		commitNeeded = true;
	}

	public String getActualLanguage () {
		return language;
	}

	public String getIllustrations () {
		if (commitNeeded)
			return "";
		else
			return illustrations;
	}

	public void setIllustrations (String illustrations) {
		this.illustrations = illustrations;
		commitNeeded = true;
	}

	public String getActualIllustrations () {
		return illustrations;
	}

	public String getForm () {
		if (commitNeeded)
			return "";
		else
			return form;
	}

	public void setForm (String form) {
		this.form = form;
		commitNeeded = true;
	}

	public String getActualForm () {
		return form;
	}

	public String getTitle () {
		if (commitNeeded)
			return "";
		else
			return title;
	}

	public void setTitle (String title) {
		this.title = title;
		commitNeeded = true;
	}

	public String getActualTitle () {
		return title;
	}

	public String getRemainder () {
		if (commitNeeded)
			return "";
		else
			return remainder;
	}

	public void setRemainder (String remainder) {
		this.remainder = remainder;
		commitNeeded = true;
	}

	public String getActualRemainder () {
		return remainder;
	}

	public String getExtent () {
		if (commitNeeded)
			return "";
		else
			return extent;
	}

	public void setExtent (String extent) {
		this.extent = extent;
		commitNeeded = true;
	}

	public String getActualExtent () {
		return extent;
	}

	public String getDimensions () {
		if (commitNeeded)
			return "";
		else
			return dimensions;
	}

	public void setDimensions (String dimensions) {
		this.dimensions = dimensions;
		commitNeeded = true;
	}

	public String getActualDimensions () {
		return dimensions;
	}

	public String getOther () {
		if (commitNeeded)
			return "";
		else
			return other;
	}

	public void setOther (String other) {
		this.other = other;
		commitNeeded = true;
	}

	public String getActualOther () {
		return other;
	}

	public String getGac () {
		if (commitNeeded)
			return "";
		else
			return gac;
	}

	public void setGac (String gac) {
		this.gac = gac;
		commitNeeded = true;
	}

	public String getActualGac () {
		return gac;
	}

	public String getPubLocation () {
		if (commitNeeded)
			return "";
		else
			return pubLocation;
	}

	public void setPubLocation (String pubLocation) {
		this.pubLocation = pubLocation;
		commitNeeded = true;
	}

	public String getActualPubLocation () {
		return pubLocation;
	}

	public String getPublisher () {
		if (commitNeeded)
			return "";
		else
			return publisher;
	}

	public void setPublisher (String publisher) {
		this.publisher = publisher;
		commitNeeded = true;
	}

	public String getActualPublisher () {
		return publisher;
	}

	public String getVar () {
		return var;
	}

	public void setVar (String var) {
		this.var = var;
	}

	public String getActualVar () {
		return var;
	}

	public static Integer IDValue() throws JspException {
		try {
			return currentInstance.getID();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function IDValue()");
		}
	}

	public static String recTypeValue() throws JspException {
		try {
			return currentInstance.getRecType();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function recTypeValue()");
		}
	}

	public static String bibLevelValue() throws JspException {
		try {
			return currentInstance.getBibLevel();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function bibLevelValue()");
		}
	}

	public static String multiLevelValue() throws JspException {
		try {
			return currentInstance.getMultiLevel();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function multiLevelValue()");
		}
	}

	public static String dateStatusValue() throws JspException {
		try {
			return currentInstance.getDateStatus();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function dateStatusValue()");
		}
	}

	public static String date1Value() throws JspException {
		try {
			return currentInstance.getDate1();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function date1Value()");
		}
	}

	public static String date2Value() throws JspException {
		try {
			return currentInstance.getDate2();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function date2Value()");
		}
	}

	public static String locationValue() throws JspException {
		try {
			return currentInstance.getLocation();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function locationValue()");
		}
	}

	public static String languageValue() throws JspException {
		try {
			return currentInstance.getLanguage();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function languageValue()");
		}
	}

	public static String illustrationsValue() throws JspException {
		try {
			return currentInstance.getIllustrations();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function illustrationsValue()");
		}
	}

	public static String formValue() throws JspException {
		try {
			return currentInstance.getForm();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function formValue()");
		}
	}

	public static String titleValue() throws JspException {
		try {
			return currentInstance.getTitle();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function titleValue()");
		}
	}

	public static String remainderValue() throws JspException {
		try {
			return currentInstance.getRemainder();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function remainderValue()");
		}
	}

	public static String extentValue() throws JspException {
		try {
			return currentInstance.getExtent();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function extentValue()");
		}
	}

	public static String dimensionsValue() throws JspException {
		try {
			return currentInstance.getDimensions();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function dimensionsValue()");
		}
	}

	public static String otherValue() throws JspException {
		try {
			return currentInstance.getOther();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function otherValue()");
		}
	}

	public static String gacValue() throws JspException {
		try {
			return currentInstance.getGac();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function gacValue()");
		}
	}

	public static String pubLocationValue() throws JspException {
		try {
			return currentInstance.getPubLocation();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function pubLocationValue()");
		}
	}

	public static String publisherValue() throws JspException {
		try {
			return currentInstance.getPublisher();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function publisherValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		recType = null;
		bibLevel = null;
		multiLevel = null;
		dateStatus = null;
		date1 = null;
		date2 = null;
		location = null;
		language = null;
		illustrations = null;
		form = null;
		title = null;
		remainder = null;
		extent = null;
		dimensions = null;
		other = null;
		gac = null;
		pubLocation = null;
		publisher = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
