package ESTCTagLib.match;

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
import ESTCTagLib.gazetteer.Gazetteer;

import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.Sequence;

@SuppressWarnings("serial")
public class Match extends ESTCTagLibTagSupport {

	static Match currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(Match.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	String moemlId = null;
	int seqnum = 0;
	int ID = 0;
	String tag = null;

	private String var = null;

	private Match cachedMatch = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Record theRecord = (Record)findAncestorWithClass(this, Record.class);
			if (theRecord!= null)
				parentEntities.addElement(theRecord);
			Gazetteer theGazetteer = (Gazetteer)findAncestorWithClass(this, Gazetteer.class);
			if (theGazetteer!= null)
				parentEntities.addElement(theGazetteer);

			if (theRecord == null) {
			} else {
				ID = theRecord.getID();
			}
			if (theGazetteer == null) {
			} else {
				moemlId = theGazetteer.getMoemlId();
			}

			MatchIterator theMatchIterator = (MatchIterator)findAncestorWithClass(this, MatchIterator.class);

			if (theMatchIterator != null) {
				moemlId = theMatchIterator.getMoemlId();
				seqnum = theMatchIterator.getSeqnum();
				ID = theMatchIterator.getID();
				tag = theMatchIterator.getTag();
			}

			if (theMatchIterator == null && theRecord == null && theGazetteer == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new Match and to generate a new seqnum
				seqnum = Sequence.generateID();
				insertEntity();
			} else if (theMatchIterator == null && theRecord != null && theGazetteer == null) {
				// an seqnum was provided as an attribute - we need to load a Match from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select moeml_id,seqnum,tag from moeml.match where id = ?");
				stmt.setInt(1,ID);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (moemlId == null)
						moemlId = rs.getString(1);
					if (seqnum == 0)
						seqnum = rs.getInt(2);
					if (tag == null)
						tag = rs.getString(3);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (theMatchIterator == null && theRecord == null && theGazetteer != null) {
				// an seqnum was provided as an attribute - we need to load a Match from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select seqnum,id,tag from moeml.match where moeml_id = ?");
				stmt.setString(1,moemlId);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (seqnum == 0)
						seqnum = rs.getInt(1);
					if (ID == 0)
						ID = rs.getInt(2);
					if (tag == null)
						tag = rs.getString(3);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a Match from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select 1 from moeml.match where moeml_id = ? and seqnum = ? and id = ? and tag = ?");
				stmt.setString(1,moemlId);
				stmt.setInt(2,seqnum);
				stmt.setInt(3,ID);
				stmt.setString(4,tag);
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
			log.error("JDBC error retrieving seqnum " + seqnum, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving seqnum " + seqnum);
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error retrieving seqnum " + seqnum,e);
			}

		} finally {
			freeConnection();
		}

		if(pageContext != null){
			Match currentMatch = (Match) pageContext.getAttribute("tag_match");
			if(currentMatch != null){
				cachedMatch = currentMatch;
			}
			currentMatch = this;
			pageContext.setAttribute((var == null ? "tag_match" : var), currentMatch);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedMatch != null){
				pageContext.setAttribute((var == null ? "tag_match" : var), this.cachedMatch);
			}else{
				pageContext.removeAttribute((var == null ? "tag_match" : var));
				this.cachedMatch = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update moeml.match set where moeml_id = ? and seqnum = ? and id = ? and tag = ?");
				stmt.setString(1,moemlId);
				stmt.setInt(2,seqnum);
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
		if (seqnum == 0) {
			seqnum = Sequence.generateID();
			log.debug("generating new Match " + seqnum);
		}

		PreparedStatement stmt = getConnection().prepareStatement("insert into moeml.match(moeml_id,seqnum,id,tag) values (?,?,?,?)");
		stmt.setString(1,moemlId);
		stmt.setInt(2,seqnum);
		stmt.setInt(3,ID);
		stmt.setString(4,tag);
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

	public int getSeqnum () {
		return seqnum;
	}

	public void setSeqnum (int seqnum) {
		this.seqnum = seqnum;
	}

	public int getActualSeqnum () {
		return seqnum;
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

	public static Integer seqnumValue() throws JspException {
		try {
			return currentInstance.getSeqnum();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function seqnumValue()");
		}
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

	private void clearServiceState () {
		moemlId = null;
		seqnum = 0;
		ID = 0;
		tag = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
