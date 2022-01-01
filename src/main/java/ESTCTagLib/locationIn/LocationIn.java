package ESTCTagLib.locationIn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import ESTCTagLib.location.Location;

import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.Sequence;

@SuppressWarnings("serial")
public class LocationIn extends ESTCTagLibTagSupport {

	static LocationIn currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(LocationIn.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	int estcId = 0;
	int sublocationId = 0;
	int locationId = 0;
	String locational = null;

	private String var = null;

	private LocationIn cachedLocationIn = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Location theLocation = (Location)findAncestorWithClass(this, Location.class);
			if (theLocation!= null)
				parentEntities.addElement(theLocation);

			if (theLocation == null) {
			} else {
				sublocationId = theLocation.getLid();
			}

			LocationInIterator theLocationInIterator = (LocationInIterator)findAncestorWithClass(this, LocationInIterator.class);

			if (theLocationInIterator != null) {
				estcId = theLocationInIterator.getEstcId();
				sublocationId = theLocationInIterator.getSublocationId();
				locationId = theLocationInIterator.getLocationId();
			}

			if (theLocationInIterator == null && theLocation == null && estcId == 0) {
				// no estcId was provided - the default is to assume that it is a new LocationIn and to generate a new estcId
				estcId = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or estcId was provided as an attribute - we need to load a LocationIn from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select locational from navigation.location_in where estc_id = ? and sublocation_id = ? and location_id = ?");
				stmt.setInt(1,estcId);
				stmt.setInt(2,sublocationId);
				stmt.setInt(3,locationId);
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
			LocationIn currentLocationIn = (LocationIn) pageContext.getAttribute("tag_locationIn");
			if(currentLocationIn != null){
				cachedLocationIn = currentLocationIn;
			}
			currentLocationIn = this;
			pageContext.setAttribute((var == null ? "tag_locationIn" : var), currentLocationIn);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedLocationIn != null){
				pageContext.setAttribute((var == null ? "tag_locationIn" : var), this.cachedLocationIn);
			}else{
				pageContext.removeAttribute((var == null ? "tag_locationIn" : var));
				this.cachedLocationIn = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update navigation.location_in set locational = ? where estc_id = ?  and sublocation_id = ?  and location_id = ? ");
				stmt.setString( 1, locational );
				stmt.setInt(2,estcId);
				stmt.setInt(3,sublocationId);
				stmt.setInt(4,locationId);
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
			log.debug("generating new LocationIn " + estcId);
		}

		if (sublocationId == 0) {
			sublocationId = Sequence.generateID();
			log.debug("generating new LocationIn " + sublocationId);
		}

		if (locationId == 0) {
			locationId = Sequence.generateID();
			log.debug("generating new LocationIn " + locationId);
		}

		if (locational == null){
			locational = "";
		}
		PreparedStatement stmt = getConnection().prepareStatement("insert into navigation.location_in(estc_id,sublocation_id,location_id,locational) values (?,?,?,?)");
		stmt.setInt(1,estcId);
		stmt.setInt(2,sublocationId);
		stmt.setInt(3,locationId);
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

	public int getSublocationId () {
		return sublocationId;
	}

	public void setSublocationId (int sublocationId) {
		this.sublocationId = sublocationId;
	}

	public int getActualSublocationId () {
		return sublocationId;
	}

	public int getLocationId () {
		return locationId;
	}

	public void setLocationId (int locationId) {
		this.locationId = locationId;
	}

	public int getActualLocationId () {
		return locationId;
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

	public static Integer sublocationIdValue() throws JspException {
		try {
			return currentInstance.getSublocationId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function sublocationIdValue()");
		}
	}

	public static Integer locationIdValue() throws JspException {
		try {
			return currentInstance.getLocationId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function locationIdValue()");
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
		sublocationId = 0;
		locationId = 0;
		locational = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
