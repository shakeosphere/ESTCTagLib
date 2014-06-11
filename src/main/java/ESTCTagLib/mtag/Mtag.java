package ESTCTagLib.mtag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import ESTCTagLib.record.Record;

import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.Sequence;

@SuppressWarnings("serial")
public class Mtag extends ESTCTagLibTagSupport {

	static Mtag currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(Mtag.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	int ID = 0;
	String tag = null;
	String indicator1 = null;
	String indicator2 = null;

	private String var = null;

	private Mtag cachedMtag = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Record theRecord = (Record)findAncestorWithClass(this, Record.class);
			if (theRecord!= null)
				parentEntities.addElement(theRecord);

			if (theRecord == null) {
			} else {
				ID = theRecord.getID();
			}

			MtagIterator theMtagIterator = (MtagIterator)findAncestorWithClass(this, MtagIterator.class);

			if (theMtagIterator != null) {
				ID = theMtagIterator.getID();
				tag = theMtagIterator.getTag();
			}

			if (theMtagIterator == null && theRecord == null && tag == null) {
				// no tag was provided - the default is to assume that it is a new Mtag and to generate a new tag
				tag = null;
				insertEntity();
			} else {
				// an iterator or tag was provided as an attribute - we need to load a Mtag from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select indicator1,indicator2 from navigation.mtag where id = ? and tag = ?");
				stmt.setInt(1,ID);
				stmt.setString(2,tag);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (indicator1 == null)
						indicator1 = rs.getString(1);
					if (indicator2 == null)
						indicator2 = rs.getString(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving tag " + tag, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving tag " + tag);
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error retrieving tag " + tag,e);
			}

		} finally {
			freeConnection();
		}

		if(pageContext != null){
			Mtag currentMtag = (Mtag) pageContext.getAttribute("tag_mtag");
			if(currentMtag != null){
				cachedMtag = currentMtag;
			}
			currentMtag = this;
			pageContext.setAttribute((var == null ? "tag_mtag" : var), currentMtag);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedMtag != null){
				pageContext.setAttribute((var == null ? "tag_mtag" : var), this.cachedMtag);
			}else{
				pageContext.removeAttribute((var == null ? "tag_mtag" : var));
				this.cachedMtag = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update navigation.mtag set indicator1 = ?, indicator2 = ? where id = ? and tag = ?");
				stmt.setString(1,indicator1);
				stmt.setString(2,indicator2);
				stmt.setInt(3,ID);
				stmt.setString(4,tag);
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
		if (indicator1 == null){
			indicator1 = "";
		}
		if (indicator2 == null){
			indicator2 = "";
		}
		PreparedStatement stmt = getConnection().prepareStatement("insert into navigation.mtag(id,tag,indicator1,indicator2) values (?,?,?,?)");
		stmt.setInt(1,ID);
		stmt.setString(2,tag);
		stmt.setString(3,indicator1);
		stmt.setString(4,indicator2);
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

	public String getIndicator1 () {
		if (commitNeeded)
			return "";
		else
			return indicator1;
	}

	public void setIndicator1 (String indicator1) {
		this.indicator1 = indicator1;
		commitNeeded = true;
	}

	public String getActualIndicator1 () {
		return indicator1;
	}

	public String getIndicator2 () {
		if (commitNeeded)
			return "";
		else
			return indicator2;
	}

	public void setIndicator2 (String indicator2) {
		this.indicator2 = indicator2;
		commitNeeded = true;
	}

	public String getActualIndicator2 () {
		return indicator2;
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

	public static String indicator1Value() throws JspException {
		try {
			return currentInstance.getIndicator1();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function indicator1Value()");
		}
	}

	public static String indicator2Value() throws JspException {
		try {
			return currentInstance.getIndicator2();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function indicator2Value()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		tag = null;
		indicator1 = null;
		indicator2 = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
