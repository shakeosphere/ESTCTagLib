package ESTCTagLib.variant;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.ESTCTagLibBodyTagSupport;
import ESTCTagLib.gazetteer.Gazetteer;

@SuppressWarnings("serial")
public class VariantDeleter extends ESTCTagLibBodyTagSupport {
    String moemlId = null;
    int seqnum = 0;
    String variant = null;
	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	private static final Log log = LogFactory.getLog(VariantDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		Gazetteer theGazetteer = (Gazetteer)findAncestorWithClass(this, Gazetteer.class);
		if (theGazetteer!= null)
			parentEntities.addElement(theGazetteer);

		if (theGazetteer == null) {
		} else {
			moemlId = theGazetteer.getMoemlId();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from moeml.variant where 1=1"
                                                        + (moemlId == null ? "" : " and moeml_id = ? ")
                                                        + (seqnum == 0 ? "" : " and seqnum = ? ")
                                                        + (moemlId == null ? "" : " and moeml_id = ? "));
            if (moemlId != null) stat.setString(webapp_keySeq++, moemlId);
            if (seqnum != 0) stat.setInt(webapp_keySeq++, seqnum);
			if (moemlId != null) stat.setString(webapp_keySeq++, moemlId);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating Variant deleter", e);

			clearServiceState();
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating Variant deleter");
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating Variant deleter",e);
			}

        } finally {
            freeConnection();
        }

        return SKIP_BODY;
    }

	public int doEndTag() throws JspException {

		clearServiceState();
		Boolean error = (Boolean) pageContext.getAttribute("tagError");
		if(error != null && error){

			freeConnection();

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
		return super.doEndTag();
	}

    private void clearServiceState() {
        moemlId = null;
        seqnum = 0;
        parentEntities = new Vector<ESTCTagLibTagSupport>();

        this.rs = null;
        this.var = null;
        this.rsCount = 0;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }



	public String getMoemlId () {
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
}
