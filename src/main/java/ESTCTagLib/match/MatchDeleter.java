package ESTCTagLib.match;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.ESTCTagLibBodyTagSupport;
import ESTCTagLib.record.Record;
import ESTCTagLib.gazetteer.Gazetteer;

@SuppressWarnings("serial")
public class MatchDeleter extends ESTCTagLibBodyTagSupport {
    String moemlId = null;
    int seqnum = 0;
    int ID = 0;
    String tag = null;
	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(MatchDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
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


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from moeml.match where 1=1"
                                                        + (moemlId == null ? "" : " and moeml_id = ? ")
                                                        + (seqnum == 0 ? "" : " and seqnum = ? ")
                                                        + (ID == 0 ? "" : " and id = ? ")
                                                        + (tag == null ? "" : " and tag = ? ")
                                                        + (ID == 0 ? "" : " and id = ? ")
                                                        + (moemlId == null ? "" : " and moeml_id = ? "));
            if (moemlId != null) stat.setString(webapp_keySeq++, moemlId);
            if (seqnum != 0) stat.setInt(webapp_keySeq++, seqnum);
            if (ID != 0) stat.setInt(webapp_keySeq++, ID);
            if (tag != null) stat.setString(webapp_keySeq++, tag);
			if (ID != 0) stat.setInt(webapp_keySeq++, ID);
			if (moemlId != null) stat.setString(webapp_keySeq++, moemlId);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating Match deleter", e);

			clearServiceState();
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating Match deleter");
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating Match deleter",e);
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
        ID = 0;
        tag = null;
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
		return tag;
	}

	public void setTag (String tag) {
		this.tag = tag;
	}

	public String getActualTag () {
		return tag;
	}
}
