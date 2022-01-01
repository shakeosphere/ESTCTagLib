package ESTCTagLib.subtag;


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
import ESTCTagLib.mtag.Mtag;

@SuppressWarnings("serial")
public class SubtagDeleter extends ESTCTagLibBodyTagSupport {
    int ID = 0;
    String tag = null;
    String code = null;
    String value = null;
	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(SubtagDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		Mtag theMtag = (Mtag)findAncestorWithClass(this, Mtag.class);
		if (theMtag!= null)
			parentEntities.addElement(theMtag);

		if (theMtag == null) {
		} else {
			ID = theMtag.getID();
			tag = theMtag.getTag();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from navigation.subtag where 1=1"
                                                        + (ID == 0 ? "" : " and id = ? ")
                                                        + (tag == null ? "" : " and tag = ? ")
                                                        + (code == null ? "" : " and code = ? ")
                                                        + (ID == 0 ? "" : " and id = ? ")
                                                        + (tag == null ? "" : " and tag = ? "));
            if (ID != 0) stat.setInt(webapp_keySeq++, ID);
            if (tag != null) stat.setString(webapp_keySeq++, tag);
            if (code != null) stat.setString(webapp_keySeq++, code);
			if (ID != 0) stat.setInt(webapp_keySeq++, ID);
			if (tag != null) stat.setString(webapp_keySeq++, tag);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating Subtag deleter", e);

			clearServiceState();
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating Subtag deleter");
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating Subtag deleter",e);
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
        ID = 0;
        tag = null;
        code = null;
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

	public String getCode () {
		return code;
	}

	public void setCode (String code) {
		this.code = code;
	}

	public String getActualCode () {
		return code;
	}
}
