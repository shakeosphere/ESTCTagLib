package ESTCTagLib.establishmentIn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import ESTCTagLib.establishment.Establishment;
import ESTCTagLib.location.Location;

import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.Sequence;

@SuppressWarnings("serial")
public class EstablishmentIn extends ESTCTagLibTagSupport {

	static EstablishmentIn currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(EstablishmentIn.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	int estcId = 0;
	int establishmentId = 0;
	int llocationId = 0;
	String locational = null;

	private String var = null;

	private EstablishmentIn cachedEstablishmentIn = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Establishment theEstablishment = (Establishment)findAncestorWithClass(this, Establishment.class);
			if (theEstablishment!= null)
				parentEntities.addElement(theEstablishment);
			Location theLocation = (Location)findAncestorWithClass(this, Location.class);
			if (theLocation!= null)
				parentEntities.addElement(theLocation);

			if (theEstablishment == null) {
			} else {
				establishmentId = theEstablishment.getID();
			}
			if (theLocation == null) {
			} else {
				llocationId = theLocation.getLid();
			}

			EstablishmentInIterator theEstablishmentInIterator = (EstablishmentInIterator)findAncestorWithClass(this, EstablishmentInIterator.class);

			if (theEstablishmentInIterator != null) {
				estcId = theEstablishmentInIterator.getEstcId();
				establishmentId = theEstablishmentInIterator.getEstablishmentId();
				llocationId = theEstablishmentInIterator.getLlocationId();
			}

			if (theEstablishmentInIterator == null && theEstablishment == null && theLocation == null && estcId == 0) {
				// no estcId was provided - the default is to assume that it is a new EstablishmentIn and to generate a new estcId
				estcId = Sequence.generateID();
				insertEntity();
			} else if (theEstablishmentInIterator == null && theEstablishment != null && theLocation == null) {
				// an estcId was provided as an attribute - we need to load a EstablishmentIn from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select estc_id,establishment_id,llocation_id,locational from navigation.establishment_in where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (estcId == 0)
						estcId = rs.getInt(1);
					if (establishmentId == 0)
						establishmentId = rs.getInt(2);
					if (llocationId == 0)
						llocationId = rs.getInt(3);
					if (locational == null)
						locational = rs.getString(4);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (theEstablishmentInIterator == null && theEstablishment == null && theLocation != null) {
				// an estcId was provided as an attribute - we need to load a EstablishmentIn from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select estc_id,establishment_id,llocation_id,locational from navigation.establishment_in where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (estcId == 0)
						estcId = rs.getInt(1);
					if (establishmentId == 0)
						establishmentId = rs.getInt(2);
					if (llocationId == 0)
						llocationId = rs.getInt(3);
					if (locational == null)
						locational = rs.getString(4);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or estcId was provided as an attribute - we need to load a EstablishmentIn from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select locational from navigation.establishment_in where estc_id = ? and establishment_id = ? and llocation_id = ?");
				stmt.setInt(1,estcId);
				stmt.setInt(2,establishmentId);
				stmt.setInt(3,llocationId);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (locational == null)
						locational = rs.getString(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving estcId " + estcId, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving estcId " + estcId);
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error retrieving estcId " + estcId,e);
			}

		} finally {
			freeConnection();
		}

		if(pageContext != null){
			EstablishmentIn currentEstablishmentIn = (EstablishmentIn) pageContext.getAttribute("tag_establishmentIn");
			if(currentEstablishmentIn != null){
				cachedEstablishmentIn = currentEstablishmentIn;
			}
			currentEstablishmentIn = this;
			pageContext.setAttribute((var == null ? "tag_establishmentIn" : var), currentEstablishmentIn);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedEstablishmentIn != null){
				pageContext.setAttribute((var == null ? "tag_establishmentIn" : var), this.cachedEstablishmentIn);
			}else{
				pageContext.removeAttribute((var == null ? "tag_establishmentIn" : var));
				this.cachedEstablishmentIn = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update navigation.establishment_in set locational = ? where estc_id = ? and establishment_id = ? and llocation_id = ?");
				stmt.setString(1,locational);
				stmt.setInt(2,estcId);
				stmt.setInt(3,establishmentId);
				stmt.setInt(4,llocationId);
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
		if (estcId == 0) {
			estcId = Sequence.generateID();
			log.debug("generating new EstablishmentIn " + estcId);
		}

		if (establishmentId == 0) {
			establishmentId = Sequence.generateID();
			log.debug("generating new EstablishmentIn " + establishmentId);
		}

		if (llocationId == 0) {
			llocationId = Sequence.generateID();
			log.debug("generating new EstablishmentIn " + llocationId);
		}

		if (locational == null){
			locational = "";
		}
		PreparedStatement stmt = getConnection().prepareStatement("insert into navigation.establishment_in(estc_id,establishment_id,llocation_id,locational) values (?,?,?,?)");
		stmt.setInt(1,estcId);
		stmt.setInt(2,establishmentId);
		stmt.setInt(3,llocationId);
		stmt.setString(4,locational);
		stmt.executeUpdate();
		stmt.close();
		freeConnection();
	}

	public int getEstcId () {
		return estcId;
	}

	public void setEstcId (int estcId) {
		this.estcId = estcId;
	}

	public int getActualEstcId () {
		return estcId;
	}

	public int getEstablishmentId () {
		return establishmentId;
	}

	public void setEstablishmentId (int establishmentId) {
		this.establishmentId = establishmentId;
	}

	public int getActualEstablishmentId () {
		return establishmentId;
	}

	public int getLlocationId () {
		return llocationId;
	}

	public void setLlocationId (int llocationId) {
		this.llocationId = llocationId;
	}

	public int getActualLlocationId () {
		return llocationId;
	}

	public String getLocational () {
		if (commitNeeded)
			return "";
		else
			return locational;
	}

	public void setLocational (String locational) {
		this.locational = locational;
		commitNeeded = true;
	}

	public String getActualLocational () {
		return locational;
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

	public static Integer estcIdValue() throws JspException {
		try {
			return currentInstance.getEstcId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function estcIdValue()");
		}
	}

	public static Integer establishmentIdValue() throws JspException {
		try {
			return currentInstance.getEstablishmentId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function establishmentIdValue()");
		}
	}

	public static Integer llocationIdValue() throws JspException {
		try {
			return currentInstance.getLlocationId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function llocationIdValue()");
		}
	}

	public static String locationalValue() throws JspException {
		try {
			return currentInstance.getLocational();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function locationalValue()");
		}
	}

	private void clearServiceState () {
		estcId = 0;
		establishmentId = 0;
		llocationId = 0;
		locational = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
