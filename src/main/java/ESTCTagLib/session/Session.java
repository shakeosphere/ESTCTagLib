package ESTCTagLib.session;

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

import ESTCTagLib.user.User;

import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.Sequence;

@SuppressWarnings("serial")
public class Session extends ESTCTagLibTagSupport {

	static Session currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(Session.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	int ID = 0;
	Date start = null;
	Date finish = null;

	private String var = null;

	private Session cachedSession = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (theUser!= null)
				parentEntities.addElement(theUser);

			if (theUser == null) {
			} else {
				ID = theUser.getID();
			}

			SessionIterator theSessionIterator = (SessionIterator)findAncestorWithClass(this, SessionIterator.class);

			if (theSessionIterator != null) {
				ID = theSessionIterator.getID();
				start = theSessionIterator.getStart();
			}

			if (theSessionIterator == null && theUser == null && start == null) {
				// no start was provided - the default is to assume that it is a new Session and to generate a new start
				start = new Date();
				insertEntity();
			} else {
				// an iterator or start was provided as an attribute - we need to load a Session from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select finish from admin.session where id = ? and start = ?");
				stmt.setInt(1,ID);
				stmt.setTimestamp(2,start == null ? null : new java.sql.Timestamp(start.getTime()));
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (finish == null)
						finish = rs.getTimestamp(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving start " + start, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving start " + start);
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error retrieving start " + start,e);
			}

		} finally {
			freeConnection();
		}

		if(pageContext != null){
			Session currentSession = (Session) pageContext.getAttribute("tag_session");
			if(currentSession != null){
				cachedSession = currentSession;
			}
			currentSession = this;
			pageContext.setAttribute((var == null ? "tag_session" : var), currentSession);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedSession != null){
				pageContext.setAttribute((var == null ? "tag_session" : var), this.cachedSession);
			}else{
				pageContext.removeAttribute((var == null ? "tag_session" : var));
				this.cachedSession = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update admin.session set finish = ? where id = ? and start = ?");
				stmt.setTimestamp(1,finish == null ? null : new java.sql.Timestamp(finish.getTime()));
				stmt.setInt(2,ID);
				stmt.setTimestamp(3,start == null ? null : new java.sql.Timestamp(start.getTime()));
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
		PreparedStatement stmt = getConnection().prepareStatement("insert into admin.session(id,start,finish) values (?,?,?)");
		stmt.setInt(1,ID);
		stmt.setTimestamp(2,start == null ? null : new java.sql.Timestamp(start.getTime()));
		stmt.setTimestamp(3,finish == null ? null : new java.sql.Timestamp(finish.getTime()));
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

	public Date getStart () {
		return start;
	}

	public void setStart (Date start) {
		this.start = start;
	}

	public Date getActualStart () {
		return start;
	}

	public void setStartToNow ( ) {
		this.start = new java.util.Date();
	}

	public Date getFinish () {
		return finish;
	}

	public void setFinish (Date finish) {
		this.finish = finish;
		commitNeeded = true;
	}

	public Date getActualFinish () {
		return finish;
	}

	public void setFinishToNow ( ) {
		this.finish = new java.util.Date();
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

	public static Integer IDValue() throws JspException {
		try {
			return currentInstance.getID();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function IDValue()");
		}
	}

	public static Date startValue() throws JspException {
		try {
			return currentInstance.getStart();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function startValue()");
		}
	}

	public static Date finishValue() throws JspException {
		try {
			return currentInstance.getFinish();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function finishValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		start = null;
		finish = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
