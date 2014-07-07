package ESTCTagLib.variant;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import ESTCTagLib.gazetteer.Gazetteer;

import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.Sequence;

@SuppressWarnings("serial")
public class Variant extends ESTCTagLibTagSupport {

	static Variant currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(Variant.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	String moemlId = null;
	int seqnum = 0;
	String variant = null;

	private String var = null;

	private Variant cachedVariant = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Gazetteer theGazetteer = (Gazetteer)findAncestorWithClass(this, Gazetteer.class);
			if (theGazetteer!= null)
				parentEntities.addElement(theGazetteer);

			if (theGazetteer == null) {
			} else {
				moemlId = theGazetteer.getMoemlId();
			}

			VariantIterator theVariantIterator = (VariantIterator)findAncestorWithClass(this, VariantIterator.class);

			if (theVariantIterator != null) {
				moemlId = theVariantIterator.getMoemlId();
				seqnum = theVariantIterator.getSeqnum();
			}

			if (theVariantIterator == null && theGazetteer == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new Variant and to generate a new seqnum
				seqnum = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a Variant from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select variant from moeml.variant where moeml_id = ? and seqnum = ?");
				stmt.setString(1,moemlId);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (variant == null)
						variant = rs.getString(1);
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
			Variant currentVariant = (Variant) pageContext.getAttribute("tag_variant");
			if(currentVariant != null){
				cachedVariant = currentVariant;
			}
			currentVariant = this;
			pageContext.setAttribute((var == null ? "tag_variant" : var), currentVariant);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedVariant != null){
				pageContext.setAttribute((var == null ? "tag_variant" : var), this.cachedVariant);
			}else{
				pageContext.removeAttribute((var == null ? "tag_variant" : var));
				this.cachedVariant = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update moeml.variant set variant = ? where moeml_id = ? and seqnum = ?");
				stmt.setString(1,variant);
				stmt.setString(2,moemlId);
				stmt.setInt(3,seqnum);
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
			log.debug("generating new Variant " + seqnum);
		}

		if (variant == null){
			variant = "";
		}
		PreparedStatement stmt = getConnection().prepareStatement("insert into moeml.variant(moeml_id,seqnum,variant) values (?,?,?)");
		stmt.setString(1,moemlId);
		stmt.setInt(2,seqnum);
		stmt.setString(3,variant);
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

	public String getVariant () {
		if (commitNeeded)
			return "";
		else
			return variant;
	}

	public void setVariant (String variant) {
		this.variant = variant;
		commitNeeded = true;
	}

	public String getActualVariant () {
		return variant;
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

	public static String variantValue() throws JspException {
		try {
			return currentInstance.getVariant();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function variantValue()");
		}
	}

	private void clearServiceState () {
		moemlId = null;
		seqnum = 0;
		variant = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
