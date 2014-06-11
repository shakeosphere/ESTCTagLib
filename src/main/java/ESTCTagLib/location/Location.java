package ESTCTagLib.location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;


import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.Sequence;

@SuppressWarnings("serial")
public class Location extends ESTCTagLibTagSupport {

	static Location currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(Location.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	int lid = 0;
	String label = null;

	private String var = null;

	private Location cachedLocation = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			LocationIterator theLocationIterator = (LocationIterator)findAncestorWithClass(this, LocationIterator.class);

			if (theLocationIterator != null) {
				lid = theLocationIterator.getLid();
			}

			if (theLocationIterator == null && lid == 0) {
				// no lid was provided - the default is to assume that it is a new Location and to generate a new lid
				lid = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or lid was provided as an attribute - we need to load a Location from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select label from navigation.location where lid = ?");
				stmt.setInt(1,lid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (label == null)
						label = rs.getString(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving lid " + lid, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving lid " + lid);
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error retrieving lid " + lid,e);
			}

		} finally {
			freeConnection();
		}

		if(pageContext != null){
			Location currentLocation = (Location) pageContext.getAttribute("tag_location");
			if(currentLocation != null){
				cachedLocation = currentLocation;
			}
			currentLocation = this;
			pageContext.setAttribute((var == null ? "tag_location" : var), currentLocation);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedLocation != null){
				pageContext.setAttribute((var == null ? "tag_location" : var), this.cachedLocation);
			}else{
				pageContext.removeAttribute((var == null ? "tag_location" : var));
				this.cachedLocation = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update navigation.location set label = ? where lid = ?");
				stmt.setString(1,label);
				stmt.setInt(2,lid);
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
		if (lid == 0) {
			lid = Sequence.generateID();
			log.debug("generating new Location " + lid);
		}

		if (label == null){
			label = "";
		}
		PreparedStatement stmt = getConnection().prepareStatement("insert into navigation.location(lid,label) values (?,?)");
		stmt.setInt(1,lid);
		stmt.setString(2,label);
		stmt.executeUpdate();
		stmt.close();
		freeConnection();
	}

	public int getLid () {
		return lid;
	}

	public void setLid (int lid) {
		this.lid = lid;
	}

	public int getActualLid () {
		return lid;
	}

	public String getLabel () {
		if (commitNeeded)
			return "";
		else
			return label;
	}

	public void setLabel (String label) {
		this.label = label;
		commitNeeded = true;
	}

	public String getActualLabel () {
		return label;
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

	public static Integer lidValue() throws JspException {
		try {
			return currentInstance.getLid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function lidValue()");
		}
	}

	public static String labelValue() throws JspException {
		try {
			return currentInstance.getLabel();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function labelValue()");
		}
	}

	private void clearServiceState () {
		lid = 0;
		label = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
