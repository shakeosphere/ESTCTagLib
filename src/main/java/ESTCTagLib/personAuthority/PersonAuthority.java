package ESTCTagLib.personAuthority;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import ESTCTagLib.person.Person;
import ESTCTagLib.user.User;

import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.Sequence;

@SuppressWarnings("serial")
public class PersonAuthority extends ESTCTagLibTagSupport {

	static PersonAuthority currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(PersonAuthority.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	int pid = 0;
	int ID = 0;
	int alias = 0;
	Date defined = null;

	private String var = null;

	private PersonAuthority cachedPersonAuthority = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Person thePerson = (Person)findAncestorWithClass(this, Person.class);
			if (thePerson!= null)
				parentEntities.addElement(thePerson);
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (theUser!= null)
				parentEntities.addElement(theUser);

			if (thePerson == null) {
			} else {
				pid = thePerson.getPid();
			}
			if (theUser == null) {
			} else {
				ID = theUser.getID();
			}

			PersonAuthorityIterator thePersonAuthorityIterator = (PersonAuthorityIterator)findAncestorWithClass(this, PersonAuthorityIterator.class);

			if (thePersonAuthorityIterator != null) {
				pid = thePersonAuthorityIterator.getPid();
				ID = thePersonAuthorityIterator.getID();
			}

			if (thePersonAuthorityIterator == null && thePerson == null && theUser == null && pid == 0) {
				// no pid was provided - the default is to assume that it is a new PersonAuthority and to generate a new pid
				pid = Sequence.generateID();
				insertEntity();
			} else if (thePersonAuthorityIterator == null && thePerson != null && theUser == null) {
				// an pid was provided as an attribute - we need to load a PersonAuthority from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select id,alias,defined from navigation.person_authority where pid = ?");
				stmt.setInt(1,pid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (ID == 0)
						ID = rs.getInt(1);
					if (alias == 0)
						alias = rs.getInt(2);
					if (defined == null)
						defined = rs.getTimestamp(3);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (thePersonAuthorityIterator == null && thePerson == null && theUser != null) {
				// an pid was provided as an attribute - we need to load a PersonAuthority from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select pid,alias,defined from navigation.person_authority where id = ?");
				stmt.setInt(1,ID);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (pid == 0)
						pid = rs.getInt(1);
					if (alias == 0)
						alias = rs.getInt(2);
					if (defined == null)
						defined = rs.getTimestamp(3);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or pid was provided as an attribute - we need to load a PersonAuthority from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select alias,defined from navigation.person_authority where pid = ? and id = ?");
				stmt.setInt(1,pid);
				stmt.setInt(2,ID);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (alias == 0)
						alias = rs.getInt(1);
					if (defined == null)
						defined = rs.getTimestamp(2);
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
			PersonAuthority currentPersonAuthority = (PersonAuthority) pageContext.getAttribute("tag_personAuthority");
			if(currentPersonAuthority != null){
				cachedPersonAuthority = currentPersonAuthority;
			}
			currentPersonAuthority = this;
			pageContext.setAttribute((var == null ? "tag_personAuthority" : var), currentPersonAuthority);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedPersonAuthority != null){
				pageContext.setAttribute((var == null ? "tag_personAuthority" : var), this.cachedPersonAuthority);
			}else{
				pageContext.removeAttribute((var == null ? "tag_personAuthority" : var));
				this.cachedPersonAuthority = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update navigation.person_authority set alias = ?, defined = ? where pid = ? and id = ?");
				stmt.setInt(1,alias);
				stmt.setTimestamp(2,defined == null ? null : new java.sql.Timestamp(defined.getTime()));
				stmt.setInt(3,pid);
				stmt.setInt(4,ID);
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
		PreparedStatement stmt = getConnection().prepareStatement("insert into navigation.person_authority(pid,id,alias,defined) values (?,?,?,?)");
		stmt.setInt(1,pid);
		stmt.setInt(2,ID);
		stmt.setInt(3,alias);
		stmt.setTimestamp(4,defined == null ? null : new java.sql.Timestamp(defined.getTime()));
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

	public int getID () {
		return ID;
	}

	public void setID (int ID) {
		this.ID = ID;
	}

	public int getActualID () {
		return ID;
	}

	public int getAlias () {
		return alias;
	}

	public void setAlias (int alias) {
		this.alias = alias;
		commitNeeded = true;
	}

	public int getActualAlias () {
		return alias;
	}

	public Date getDefined () {
		return defined;
	}

	public void setDefined (Date defined) {
		this.defined = defined;
		commitNeeded = true;
	}

	public Date getActualDefined () {
		return defined;
	}

	public void setDefinedToNow ( ) {
		this.defined = new java.util.Date();
		commitNeeded = true;
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

	public static Integer IDValue() throws JspException {
		try {
			return currentInstance.getID();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function IDValue()");
		}
	}

	public static Integer aliasValue() throws JspException {
		try {
			return currentInstance.getAlias();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function aliasValue()");
		}
	}

	public static Date definedValue() throws JspException {
		try {
			return currentInstance.getDefined();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function definedValue()");
		}
	}

	private void clearServiceState () {
		pid = 0;
		ID = 0;
		alias = 0;
		defined = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
