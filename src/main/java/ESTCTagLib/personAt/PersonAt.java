package ESTCTagLib.personAt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import ESTCTagLib.establishment.Establishment;
import ESTCTagLib.person.Person;

import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.Sequence;

@SuppressWarnings("serial")
public class PersonAt extends ESTCTagLibTagSupport {

	static PersonAt currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(PersonAt.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	int estcId = 0;
	int establishmentId = 0;
	int personId = 0;
	String locational = null;

	private String var = null;

	private PersonAt cachedPersonAt = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Establishment theEstablishment = (Establishment)findAncestorWithClass(this, Establishment.class);
			if (theEstablishment!= null)
				parentEntities.addElement(theEstablishment);
			Person thePerson = (Person)findAncestorWithClass(this, Person.class);
			if (thePerson!= null)
				parentEntities.addElement(thePerson);

			if (theEstablishment == null) {
			} else {
				establishmentId = theEstablishment.getEid();
			}
			if (thePerson == null) {
			} else {
				personId = thePerson.getPid();
			}

			PersonAtIterator thePersonAtIterator = (PersonAtIterator)findAncestorWithClass(this, PersonAtIterator.class);

			if (thePersonAtIterator != null) {
				estcId = thePersonAtIterator.getEstcId();
				establishmentId = thePersonAtIterator.getEstablishmentId();
				personId = thePersonAtIterator.getPersonId();
			}

			if (thePersonAtIterator == null && theEstablishment == null && thePerson == null && estcId == 0) {
				// no estcId was provided - the default is to assume that it is a new PersonAt and to generate a new estcId
				estcId = Sequence.generateID();
				insertEntity();
			} else if (thePersonAtIterator == null && theEstablishment != null && thePerson == null) {
				// an estcId was provided as an attribute - we need to load a PersonAt from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select estc_id,establishment_id,person_id,locational from navigation.person_at where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (estcId == 0)
						estcId = rs.getInt(1);
					if (establishmentId == 0)
						establishmentId = rs.getInt(2);
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
			} else if (thePersonAtIterator == null && theEstablishment == null && thePerson != null) {
				// an estcId was provided as an attribute - we need to load a PersonAt from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select estc_id,establishment_id,person_id,locational from navigation.person_at where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (estcId == 0)
						estcId = rs.getInt(1);
					if (establishmentId == 0)
						establishmentId = rs.getInt(2);
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
				// an iterator or estcId was provided as an attribute - we need to load a PersonAt from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select locational from navigation.person_at where estc_id = ? and establishment_id = ? and person_id = ?");
				stmt.setInt(1,estcId);
				stmt.setInt(2,establishmentId);
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
			PersonAt currentPersonAt = (PersonAt) pageContext.getAttribute("tag_personAt");
			if(currentPersonAt != null){
				cachedPersonAt = currentPersonAt;
			}
			currentPersonAt = this;
			pageContext.setAttribute((var == null ? "tag_personAt" : var), currentPersonAt);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedPersonAt != null){
				pageContext.setAttribute((var == null ? "tag_personAt" : var), this.cachedPersonAt);
			}else{
				pageContext.removeAttribute((var == null ? "tag_personAt" : var));
				this.cachedPersonAt = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update navigation.person_at set locational = ? where estc_id = ?  and establishment_id = ?  and person_id = ? ");
				stmt.setString( 1, locational );
				stmt.setInt(2,estcId);
				stmt.setInt(3,establishmentId);
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
			log.debug("generating new PersonAt " + estcId);
		}

		if (establishmentId == 0) {
			establishmentId = Sequence.generateID();
			log.debug("generating new PersonAt " + establishmentId);
		}

		if (personId == 0) {
			personId = Sequence.generateID();
			log.debug("generating new PersonAt " + personId);
		}

		if (locational == null){
			locational = "";
		}
		PreparedStatement stmt = getConnection().prepareStatement("insert into navigation.person_at(estc_id,establishment_id,person_id,locational) values (?,?,?,?)");
		stmt.setInt(1,estcId);
		stmt.setInt(2,establishmentId);
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

	public int getEstablishmentId () {
		return establishmentId;
	}

	public void setEstablishmentId (int establishmentId) {
		this.establishmentId = establishmentId;
	}

	public int getActualEstablishmentId () {
		return establishmentId;
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

	public static Integer establishmentIdValue() throws JspException {
		try {
			return currentInstance.getEstablishmentId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function establishmentIdValue()");
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
		establishmentId = 0;
		personId = 0;
		locational = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
