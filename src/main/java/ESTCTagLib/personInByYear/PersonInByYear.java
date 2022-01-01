package ESTCTagLib.personInByYear;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import ESTCTagLib.person.Person;
import ESTCTagLib.location.Location;

import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.Sequence;

@SuppressWarnings("serial")
public class PersonInByYear extends ESTCTagLibTagSupport {

	static PersonInByYear currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(PersonInByYear.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	int pid = 0;
	int lid = 0;
	int pubyear = 0;
	String locational = null;
	int count = 0;

	private String var = null;

	private PersonInByYear cachedPersonInByYear = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Person thePerson = (Person)findAncestorWithClass(this, Person.class);
			if (thePerson!= null)
				parentEntities.addElement(thePerson);
			Location theLocation = (Location)findAncestorWithClass(this, Location.class);
			if (theLocation!= null)
				parentEntities.addElement(theLocation);

			if (thePerson == null) {
			} else {
				pid = thePerson.getPid();
			}
			if (theLocation == null) {
			} else {
				lid = theLocation.getLid();
			}

			PersonInByYearIterator thePersonInByYearIterator = (PersonInByYearIterator)findAncestorWithClass(this, PersonInByYearIterator.class);

			if (thePersonInByYearIterator != null) {
				pid = thePersonInByYearIterator.getPid();
				lid = thePersonInByYearIterator.getLid();
				pubyear = thePersonInByYearIterator.getPubyear();
				locational = thePersonInByYearIterator.getLocational();
			}

			if (thePersonInByYearIterator == null && thePerson == null && theLocation == null && pubyear == 0) {
				// no pubyear was provided - the default is to assume that it is a new PersonInByYear and to generate a new pubyear
				pubyear = Sequence.generateID();
				insertEntity();
			} else if (thePersonInByYearIterator == null && thePerson != null && theLocation == null) {
				// an pubyear was provided as an attribute - we need to load a PersonInByYear from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select lid,pubyear,locational,count from navigation.person_in_by_year where pid = ?");
				stmt.setInt(1,pid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (lid == 0)
						lid = rs.getInt(1);
					if (pubyear == 0)
						pubyear = rs.getInt(2);
					if (locational == null)
						locational = rs.getString(3);
					if (count == 0)
						count = rs.getInt(4);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (thePersonInByYearIterator == null && thePerson == null && theLocation != null) {
				// an pubyear was provided as an attribute - we need to load a PersonInByYear from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select pid,pubyear,locational,count from navigation.person_in_by_year where lid = ?");
				stmt.setInt(1,lid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (pid == 0)
						pid = rs.getInt(1);
					if (pubyear == 0)
						pubyear = rs.getInt(2);
					if (locational == null)
						locational = rs.getString(3);
					if (count == 0)
						count = rs.getInt(4);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or pubyear was provided as an attribute - we need to load a PersonInByYear from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select count from navigation.person_in_by_year where pid = ? and lid = ? and pubyear = ? and locational = ?");
				stmt.setInt(1,pid);
				stmt.setInt(2,lid);
				stmt.setInt(3,pubyear);
				stmt.setString(4,locational);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (count == 0)
						count = rs.getInt(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving pubyear " + pubyear, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving pubyear " + pubyear);
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error retrieving pubyear " + pubyear,e);
			}

		} finally {
			freeConnection();
		}

		if(pageContext != null){
			PersonInByYear currentPersonInByYear = (PersonInByYear) pageContext.getAttribute("tag_personInByYear");
			if(currentPersonInByYear != null){
				cachedPersonInByYear = currentPersonInByYear;
			}
			currentPersonInByYear = this;
			pageContext.setAttribute((var == null ? "tag_personInByYear" : var), currentPersonInByYear);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedPersonInByYear != null){
				pageContext.setAttribute((var == null ? "tag_personInByYear" : var), this.cachedPersonInByYear);
			}else{
				pageContext.removeAttribute((var == null ? "tag_personInByYear" : var));
				this.cachedPersonInByYear = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update navigation.person_in_by_year set count = ? where pid = ?  and lid = ?  and pubyear = ?  and locational = ? ");
				stmt.setInt( 1, count );
				stmt.setInt(2,pid);
				stmt.setInt(3,lid);
				stmt.setInt(4,pubyear);
				stmt.setString(5,locational);
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
		if (pubyear == 0) {
			pubyear = Sequence.generateID();
			log.debug("generating new PersonInByYear " + pubyear);
		}

		PreparedStatement stmt = getConnection().prepareStatement("insert into navigation.person_in_by_year(pid,lid,pubyear,locational,count) values (?,?,?,?,?)");
		stmt.setInt(1,pid);
		stmt.setInt(2,lid);
		stmt.setInt(3,pubyear);
		stmt.setString(4,locational);
		stmt.setInt(5,count);
		stmt.executeUpdate();
		stmt.close();
		freeConnection();
	}

	public int getPid () {
		return pid;
	}

	public void setPid (int pid) {
		this.pid = pid;
	}

	public int getActualPid () {
		return pid;
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

	public int getPubyear () {
		return pubyear;
	}

	public void setPubyear (int pubyear) {
		this.pubyear = pubyear;
	}

	public int getActualPubyear () {
		return pubyear;
	}

	public String getLocational () {
		if (commitNeeded)
			return "";
		else
			return locational;
	}

	public void setLocational (String locational) {
		this.locational = locational;
	}

	public String getActualLocational () {
		return locational;
	}

	public int getCount () {
		return count;
	}

	public void setCount (int count) {
		this.count = count;
		commitNeeded = true;
	}

	public int getActualCount () {
		return count;
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

	public static Integer pidValue() throws JspException {
		try {
			return currentInstance.getPid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function pidValue()");
		}
	}

	public static Integer lidValue() throws JspException {
		try {
			return currentInstance.getLid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function lidValue()");
		}
	}

	public static Integer pubyearValue() throws JspException {
		try {
			return currentInstance.getPubyear();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function pubyearValue()");
		}
	}

	public static String locationalValue() throws JspException {
		try {
			return currentInstance.getLocational();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function locationalValue()");
		}
	}

	public static Integer countValue() throws JspException {
		try {
			return currentInstance.getCount();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function countValue()");
		}
	}

	private void clearServiceState () {
		pid = 0;
		lid = 0;
		pubyear = 0;
		locational = null;
		count = 0;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
