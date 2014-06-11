package ESTCTagLib.person;

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
public class Person extends ESTCTagLibTagSupport {

	static Person currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(Person.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	int pid = 0;
	String firstName = null;
	String lastName = null;

	private String var = null;

	private Person cachedPerson = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			PersonIterator thePersonIterator = (PersonIterator)findAncestorWithClass(this, PersonIterator.class);

			if (thePersonIterator != null) {
				pid = thePersonIterator.getPid();
			}

			if (thePersonIterator == null && pid == 0) {
				// no pid was provided - the default is to assume that it is a new Person and to generate a new pid
				pid = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or pid was provided as an attribute - we need to load a Person from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select first_name,last_name from navigation.person where pid = ?");
				stmt.setInt(1,pid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (firstName == null)
						firstName = rs.getString(1);
					if (lastName == null)
						lastName = rs.getString(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving pid " + pid, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving pid " + pid);
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error retrieving pid " + pid,e);
			}

		} finally {
			freeConnection();
		}

		if(pageContext != null){
			Person currentPerson = (Person) pageContext.getAttribute("tag_person");
			if(currentPerson != null){
				cachedPerson = currentPerson;
			}
			currentPerson = this;
			pageContext.setAttribute((var == null ? "tag_person" : var), currentPerson);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedPerson != null){
				pageContext.setAttribute((var == null ? "tag_person" : var), this.cachedPerson);
			}else{
				pageContext.removeAttribute((var == null ? "tag_person" : var));
				this.cachedPerson = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update navigation.person set first_name = ?, last_name = ? where pid = ?");
				stmt.setString(1,firstName);
				stmt.setString(2,lastName);
				stmt.setInt(3,pid);
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
		if (pid == 0) {
			pid = Sequence.generateID();
			log.debug("generating new Person " + pid);
		}

		if (firstName == null){
			firstName = "";
		}
		if (lastName == null){
			lastName = "";
		}
		PreparedStatement stmt = getConnection().prepareStatement("insert into navigation.person(pid,first_name,last_name) values (?,?,?)");
		stmt.setInt(1,pid);
		stmt.setString(2,firstName);
		stmt.setString(3,lastName);
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

	public String getFirstName () {
		if (commitNeeded)
			return "";
		else
			return firstName;
	}

	public void setFirstName (String firstName) {
		this.firstName = firstName;
		commitNeeded = true;
	}

	public String getActualFirstName () {
		return firstName;
	}

	public String getLastName () {
		if (commitNeeded)
			return "";
		else
			return lastName;
	}

	public void setLastName (String lastName) {
		this.lastName = lastName;
		commitNeeded = true;
	}

	public String getActualLastName () {
		return lastName;
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

	public static String firstNameValue() throws JspException {
		try {
			return currentInstance.getFirstName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function firstNameValue()");
		}
	}

	public static String lastNameValue() throws JspException {
		try {
			return currentInstance.getLastName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function lastNameValue()");
		}
	}

	private void clearServiceState () {
		pid = 0;
		firstName = null;
		lastName = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
