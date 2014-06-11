package ESTCTagLib.located;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import ESTCTagLib.person.Person;
import ESTCTagLib.location.Location;

import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.Sequence;

@SuppressWarnings("serial")
public class Located extends ESTCTagLibTagSupport {

	static Located currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(Located.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	int pid = 0;
	int lid = 0;

	private String var = null;

	private Located cachedLocated = null;

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

			LocatedIterator theLocatedIterator = (LocatedIterator)findAncestorWithClass(this, LocatedIterator.class);

			if (theLocatedIterator != null) {
				pid = theLocatedIterator.getPid();
				lid = theLocatedIterator.getLid();
			}

			if (theLocatedIterator == null && thePerson == null && theLocation == null && pid == 0) {
				// no pid was provided - the default is to assume that it is a new Located and to generate a new pid
				pid = Sequence.generateID();
				insertEntity();
			} else if (theLocatedIterator == null && thePerson != null && theLocation == null) {
				// an pid was provided as an attribute - we need to load a Located from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select lid from navigation.located where pid = ?");
				stmt.setInt(1,pid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (lid == 0)
						lid = rs.getInt(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (theLocatedIterator == null && thePerson == null && theLocation != null) {
				// an pid was provided as an attribute - we need to load a Located from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select pid from navigation.located where lid = ?");
				stmt.setInt(1,lid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (pid == 0)
						pid = rs.getInt(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or pid was provided as an attribute - we need to load a Located from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select 1 from navigation.located where pid = ? and lid = ?");
				stmt.setInt(1,pid);
				stmt.setInt(2,lid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
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
			Located currentLocated = (Located) pageContext.getAttribute("tag_located");
			if(currentLocated != null){
				cachedLocated = currentLocated;
			}
			currentLocated = this;
			pageContext.setAttribute((var == null ? "tag_located" : var), currentLocated);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedLocated != null){
				pageContext.setAttribute((var == null ? "tag_located" : var), this.cachedLocated);
			}else{
				pageContext.removeAttribute((var == null ? "tag_located" : var));
				this.cachedLocated = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update navigation.located set where pid = ? and lid = ?");
				stmt.setInt(1,pid);
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
		PreparedStatement stmt = getConnection().prepareStatement("insert into navigation.located(pid,lid) values (?,?)");
		stmt.setInt(1,pid);
		stmt.setInt(2,lid);
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

	private void clearServiceState () {
		pid = 0;
		lid = 0;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
