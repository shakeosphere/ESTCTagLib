package ESTCTagLib.publisher;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import ESTCTagLib.record.Record;
import ESTCTagLib.person.Person;

import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.Sequence;

@SuppressWarnings("serial")
public class Publisher extends ESTCTagLibTagSupport {

	static Publisher currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(Publisher.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	int ID = 0;
	int pid = 0;

	private String var = null;

	private Publisher cachedPublisher = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Record theRecord = (Record)findAncestorWithClass(this, Record.class);
			if (theRecord!= null)
				parentEntities.addElement(theRecord);
			Person thePerson = (Person)findAncestorWithClass(this, Person.class);
			if (thePerson!= null)
				parentEntities.addElement(thePerson);

			if (theRecord == null) {
			} else {
				ID = theRecord.getID();
			}
			if (thePerson == null) {
			} else {
				pid = thePerson.getPid();
			}

			PublisherIterator thePublisherIterator = (PublisherIterator)findAncestorWithClass(this, PublisherIterator.class);

			if (thePublisherIterator != null) {
				ID = thePublisherIterator.getID();
				pid = thePublisherIterator.getPid();
			}

			if (thePublisherIterator == null && theRecord == null && thePerson == null && ID == 0) {
				// no ID was provided - the default is to assume that it is a new Publisher and to generate a new ID
				ID = Sequence.generateID();
				insertEntity();
			} else if (thePublisherIterator == null && theRecord != null && thePerson == null) {
				// an ID was provided as an attribute - we need to load a Publisher from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select pid from navigation.publisher where id = ?");
				stmt.setInt(1,ID);
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
			} else if (thePublisherIterator == null && theRecord == null && thePerson != null) {
				// an ID was provided as an attribute - we need to load a Publisher from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select id from navigation.publisher where pid = ?");
				stmt.setInt(1,pid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (ID == 0)
						ID = rs.getInt(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or ID was provided as an attribute - we need to load a Publisher from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select 1 from navigation.publisher where id = ? and pid = ?");
				stmt.setInt(1,ID);
				stmt.setInt(2,pid);
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
			Publisher currentPublisher = (Publisher) pageContext.getAttribute("tag_publisher");
			if(currentPublisher != null){
				cachedPublisher = currentPublisher;
			}
			currentPublisher = this;
			pageContext.setAttribute((var == null ? "tag_publisher" : var), currentPublisher);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedPublisher != null){
				pageContext.setAttribute((var == null ? "tag_publisher" : var), this.cachedPublisher);
			}else{
				pageContext.removeAttribute((var == null ? "tag_publisher" : var));
				this.cachedPublisher = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update navigation.publisher set where id = ?  and pid = ? ");
				stmt.setInt(1,ID);
				stmt.setInt(2,pid);
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
		PreparedStatement stmt = getConnection().prepareStatement("insert into navigation.publisher(id,pid) values (?,?)");
		stmt.setInt(1,ID);
		stmt.setInt(2,pid);
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

	public int getPid () {
		return pid;
	}

	public void setPid (int pid) {
		this.pid = pid;
	}

	public int getActualPid () {
		return pid;
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

	public static Integer pidValue() throws JspException {
		try {
			return currentInstance.getPid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function pidValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		pid = 0;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
