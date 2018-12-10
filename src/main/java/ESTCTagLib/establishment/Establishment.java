package ESTCTagLib.establishment;

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
public class Establishment extends ESTCTagLibTagSupport {

	static Establishment currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(Establishment.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	int ID = 0;
	String establishment = null;

	private String var = null;

	private Establishment cachedEstablishment = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			EstablishmentIterator theEstablishmentIterator = (EstablishmentIterator)findAncestorWithClass(this, EstablishmentIterator.class);

			if (theEstablishmentIterator != null) {
				ID = theEstablishmentIterator.getID();
			}

			if (theEstablishmentIterator == null && ID == 0) {
				// no ID was provided - the default is to assume that it is a new Establishment and to generate a new ID
				ID = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or ID was provided as an attribute - we need to load a Establishment from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select establishment from navigation.establishment where id = ?");
				stmt.setInt(1,ID);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (establishment == null)
						establishment = rs.getString(1);
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
			Establishment currentEstablishment = (Establishment) pageContext.getAttribute("tag_establishment");
			if(currentEstablishment != null){
				cachedEstablishment = currentEstablishment;
			}
			currentEstablishment = this;
			pageContext.setAttribute((var == null ? "tag_establishment" : var), currentEstablishment);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedEstablishment != null){
				pageContext.setAttribute((var == null ? "tag_establishment" : var), this.cachedEstablishment);
			}else{
				pageContext.removeAttribute((var == null ? "tag_establishment" : var));
				this.cachedEstablishment = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update navigation.establishment set establishment = ? where id = ?");
				stmt.setString(1,establishment);
				stmt.setInt(2,ID);
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
		if (ID == 0) {
			ID = Sequence.generateID();
			log.debug("generating new Establishment " + ID);
		}

		if (establishment == null){
			establishment = "";
		}
		PreparedStatement stmt = getConnection().prepareStatement("insert into navigation.establishment(id,establishment) values (?,?)");
		stmt.setInt(1,ID);
		stmt.setString(2,establishment);
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

	public String getEstablishment () {
		if (commitNeeded)
			return "";
		else
			return establishment;
	}

	public void setEstablishment (String establishment) {
		this.establishment = establishment;
		commitNeeded = true;
	}

	public String getActualEstablishment () {
		return establishment;
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

	public static String establishmentValue() throws JspException {
		try {
			return currentInstance.getEstablishment();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function establishmentValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		establishment = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
