package ESTCTagLib.gazetteer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;


import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class Gazetteer extends ESTCTagLibTagSupport {

	static Gazetteer currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(Gazetteer.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	String moemlId = null;
	String title = null;

	private String var = null;

	private Gazetteer cachedGazetteer = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			GazetteerIterator theGazetteerIterator = (GazetteerIterator)findAncestorWithClass(this, GazetteerIterator.class);

			if (theGazetteerIterator != null) {
				moemlId = theGazetteerIterator.getMoemlId();
			}

			if (theGazetteerIterator == null && moemlId == null) {
				// no moemlId was provided - the default is to assume that it is a new Gazetteer and to generate a new moemlId
				moemlId = null;
				insertEntity();
			} else {
				// an iterator or moemlId was provided as an attribute - we need to load a Gazetteer from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select title from moeml.gazetteer where moeml_id = ?");
				stmt.setString(1,moemlId);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (title == null)
						title = rs.getString(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving moemlId " + moemlId, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving moemlId " + moemlId);
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error retrieving moemlId " + moemlId,e);
			}

		} finally {
			freeConnection();
		}

		if(pageContext != null){
			Gazetteer currentGazetteer = (Gazetteer) pageContext.getAttribute("tag_gazetteer");
			if(currentGazetteer != null){
				cachedGazetteer = currentGazetteer;
			}
			currentGazetteer = this;
			pageContext.setAttribute((var == null ? "tag_gazetteer" : var), currentGazetteer);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedGazetteer != null){
				pageContext.setAttribute((var == null ? "tag_gazetteer" : var), this.cachedGazetteer);
			}else{
				pageContext.removeAttribute((var == null ? "tag_gazetteer" : var));
				this.cachedGazetteer = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update moeml.gazetteer set title = ? where moeml_id = ? ");
				stmt.setString( 1, title );
				stmt.setString(2,moemlId);
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
		if (title == null){
			title = "";
		}
		PreparedStatement stmt = getConnection().prepareStatement("insert into moeml.gazetteer(moeml_id,title) values (?,?)");
		stmt.setString(1,moemlId);
		stmt.setString(2,title);
		stmt.executeUpdate();
		stmt.close();
		freeConnection();
	}

	public String getMoemlId () {
		if (commitNeeded)
			return "";
		else
			return moemlId;
	}

	public void setMoemlId (String moemlId) {
		this.moemlId = moemlId;
	}

	public String getActualMoemlId () {
		return moemlId;
	}

	public String getTitle () {
		if (commitNeeded)
			return "";
		else
			return title;
	}

	public void setTitle (String title) {
		this.title = title;
		commitNeeded = true;
	}

	public String getActualTitle () {
		return title;
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

	public static String moemlIdValue() throws JspException {
		try {
			return currentInstance.getMoemlId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function moemlIdValue()");
		}
	}

	public static String titleValue() throws JspException {
		try {
			return currentInstance.getTitle();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function titleValue()");
		}
	}

	private void clearServiceState () {
		moemlId = null;
		title = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
