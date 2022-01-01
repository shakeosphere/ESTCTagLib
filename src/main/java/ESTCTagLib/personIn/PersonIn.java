package ESTCTagLib.personIn;

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
import ESTCTagLib.person.Person;

import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.Sequence;

@SuppressWarnings("serial")
public class PersonIn extends ESTCTagLibTagSupport {

	static PersonIn currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(PersonIn.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	int estcId = 0;
	int locationId = 0;
	int personId = 0;
	String locational = null;

	private String var = null;

	private PersonIn cachedPersonIn = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Location theLocation = (Location)findAncestorWithClass(this, Location.class);
			if (theLocation!= null)
				parentEntities.addElement(theLocation);
			Person thePerson = (Person)findAncestorWithClass(this, Person.class);
			if (thePerson!= null)
				parentEntities.addElement(thePerson);

			if (theLocation == null) {
			} else {
				locationId = theLocation.getLid();
			}
			if (thePerson == null) {
			} else {
				personId = thePerson.getPid();
			}

			PersonInIterator thePersonInIterator = (PersonInIterator)findAncestorWithClass(this, PersonInIterator.class);

			if (thePersonInIterator != null) {
				estcId = thePersonInIterator.getEstcId();
				locationId = thePersonInIterator.getLocationId();
				personId = thePersonInIterator.getPersonId();
			}

			if (thePersonInIterator == null && theLocation == null && thePerson == null && estcId == 0) {
				// no estcId was provided - the default is to assume that it is a new PersonIn and to generate a new estcId
				estcId = Sequence.generateID();
				insertEntity();
			} else if (thePersonInIterator == null && theLocation != null && thePerson == null) {
				// an estcId was provided as an attribute - we need to load a PersonIn from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select estc_id,location_id,person_id,locational from navigation.person_in where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (estcId == 0)
						estcId = rs.getInt(1);
					if (locationId == 0)
						locationId = rs.getInt(2);
					if (personId == 0)
						personId = rs.getInt(3);
					if (locational == null)
						locational = rs.getString(4);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (thePersonInIterator == null && theLocation == null && thePerson != null) {
				// an estcId was provided as an attribute - we need to load a PersonIn from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select estc_id,location_id,person_id,locational from navigation.person_in where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (estcId == 0)
						estcId = rs.getInt(1);
					if (locationId == 0)
						locationId = rs.getInt(2);
					if (personId == 0)
						personId = rs.getInt(3);
					if (locational == null)
						locational = rs.getString(4);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or estcId was provided as an attribute - we need to load a PersonIn from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select locational from navigation.person_in where estc_id = ? and location_id = ? and person_id = ?");
				stmt.setInt(1,estcId);
				stmt.setInt(2,locationId);
				stmt.setInt(3,personId);
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
			PersonIn currentPersonIn = (PersonIn) pageContext.getAttribute("tag_personIn");
			if(currentPersonIn != null){
				cachedPersonIn = currentPersonIn;
			}
			currentPersonIn = this;
			pageContext.setAttribute((var == null ? "tag_personIn" : var), currentPersonIn);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedPersonIn != null){
				pageContext.setAttribute((var == null ? "tag_personIn" : var), this.cachedPersonIn);
			}else{
				pageContext.removeAttribute((var == null ? "tag_personIn" : var));
				this.cachedPersonIn = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update navigation.person_in set locational = ? where estc_id = ?  and location_id = ?  and person_id = ? ");
				stmt.setString( 1, locational );
				stmt.setInt(2,estcId);
				stmt.setInt(3,locationId);
				stmt.setInt(4,personId);
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
			log.debug("generating new PersonIn " + estcId);
		}

		if (locationId == 0) {
			locationId = Sequence.generateID();
			log.debug("generating new PersonIn " + locationId);
		}

		if (personId == 0) {
			personId = Sequence.generateID();
			log.debug("generating new PersonIn " + personId);
		}

		if (locational == null){
			locational = "";
		}
		PreparedStatement stmt = getConnection().prepareStatement("insert into navigation.person_in(estc_id,location_id,person_id,locational) values (?,?,?,?)");
		stmt.setInt(1,estcId);
		stmt.setInt(2,locationId);
		stmt.setInt(3,personId);
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

	public int getLocationId () {
		return locationId;
	}

	public void setLocationId (int locationId) {
		this.locationId = locationId;
	}

	public int getActualLocationId () {
		return locationId;
	}

	public int getPersonId () {
		return personId;
	}

	public void setPersonId (int personId) {
		this.personId = personId;
	}

	public int getActualPersonId () {
		return personId;
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

	public static Integer locationIdValue() throws JspException {
		try {
			return currentInstance.getLocationId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function locationIdValue()");
		}
	}

	public static Integer personIdValue() throws JspException {
		try {
			return currentInstance.getPersonId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function personIdValue()");
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
		locationId = 0;
		personId = 0;
		locational = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
