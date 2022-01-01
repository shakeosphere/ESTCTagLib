package ESTCTagLib.subtag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import ESTCTagLib.mtag.Mtag;

import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.Sequence;

@SuppressWarnings("serial")
public class Subtag extends ESTCTagLibTagSupport {

	static Subtag currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(Subtag.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	int ID = 0;
	String tag = null;
	String code = null;
	String value = null;

	private String var = null;

	private Subtag cachedSubtag = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Mtag theMtag = (Mtag)findAncestorWithClass(this, Mtag.class);
			if (theMtag!= null)
				parentEntities.addElement(theMtag);

			if (theMtag == null) {
			} else {
				ID = theMtag.getID();
				tag = theMtag.getTag();
			}

			SubtagIterator theSubtagIterator = (SubtagIterator)findAncestorWithClass(this, SubtagIterator.class);

			if (theSubtagIterator != null) {
				ID = theSubtagIterator.getID();
				tag = theSubtagIterator.getTag();
				code = theSubtagIterator.getCode();
			}

			if (theSubtagIterator == null && theMtag == null && code == null) {
				// no code was provided - the default is to assume that it is a new Subtag and to generate a new code
				code = null;
				insertEntity();
			} else {
				// an iterator or code was provided as an attribute - we need to load a Subtag from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select value from navigation.subtag where id = ? and tag = ? and code = ?");
				stmt.setInt(1,ID);
				stmt.setString(2,tag);
				stmt.setString(3,code);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (value == null)
						value = rs.getString(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving code " + code, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving code " + code);
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error retrieving code " + code,e);
			}

		} finally {
			freeConnection();
		}

		if(pageContext != null){
			Subtag currentSubtag = (Subtag) pageContext.getAttribute("tag_subtag");
			if(currentSubtag != null){
				cachedSubtag = currentSubtag;
			}
			currentSubtag = this;
			pageContext.setAttribute((var == null ? "tag_subtag" : var), currentSubtag);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedSubtag != null){
				pageContext.setAttribute((var == null ? "tag_subtag" : var), this.cachedSubtag);
			}else{
				pageContext.removeAttribute((var == null ? "tag_subtag" : var));
				this.cachedSubtag = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update navigation.subtag set value = ? where id = ?  and tag = ?  and code = ? ");
				stmt.setString( 1, value );
				stmt.setInt(2,ID);
				stmt.setString(3,tag);
				stmt.setString(4,code);
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
		if (value == null){
			value = "";
		}
		PreparedStatement stmt = getConnection().prepareStatement("insert into navigation.subtag(id,tag,code,value) values (?,?,?,?)");
		stmt.setInt(1,ID);
		stmt.setString(2,tag);
		stmt.setString(3,code);
		stmt.setString(4,value);
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

	public String getTag () {
		if (commitNeeded)
			return "";
		else
			return tag;
	}

	public void setTag (String tag) {
		this.tag = tag;
	}

	public String getActualTag () {
		return tag;
	}

	public String getCode () {
		if (commitNeeded)
			return "";
		else
			return code;
	}

	public void setCode (String code) {
		this.code = code;
	}

	public String getActualCode () {
		return code;
	}

	public String getValue () {
		if (commitNeeded)
			return "";
		else
			return value;
	}

	public void setValue (String value) {
		this.value = value;
		commitNeeded = true;
	}

	public String getActualValue () {
		return value;
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

	public static String tagValue() throws JspException {
		try {
			return currentInstance.getTag();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function tagValue()");
		}
	}

	public static String codeValue() throws JspException {
		try {
			return currentInstance.getCode();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function codeValue()");
		}
	}

	public static String valueValue() throws JspException {
		try {
			return currentInstance.getValue();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function valueValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		tag = null;
		code = null;
		value = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
