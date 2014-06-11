package ESTCTagLib.publication;


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
import ESTCTagLib.record.Record;

@SuppressWarnings("serial")
public class PublicationDeleter extends ESTCTagLibBodyTagSupport {
    int ID = 0;
    String recType = null;
    String bibLevel = null;
    String multiLevel = null;
    String dateStatus = null;
    String date1 = null;
    String date2 = null;
    String location = null;
    String language = null;
    String illustrations = null;
    String form = null;
    String title = null;
    String remainder = null;
    String extent = null;
    String dimensions = null;
    String other = null;
    String gac = null;
    String pubLocation = null;
    String publisher = null;
	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	private static final Log log = LogFactory.getLog(PublicationDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		Record theRecord = (Record)findAncestorWithClass(this, Record.class);
		if (theRecord!= null)
			parentEntities.addElement(theRecord);

		if (theRecord == null) {
		} else {
			ID = theRecord.getID();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from navigation.publication where 1=1"
                                                        + (ID == 0 ? "" : " and id = ? ")
                                                        + (ID == 0 ? "" : " and id = ? "));
            if (ID != 0) stat.setInt(webapp_keySeq++, ID);
			if (ID != 0) stat.setInt(webapp_keySeq++, ID);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating Publication deleter", e);

			clearServiceState();
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating Publication deleter");
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating Publication deleter",e);
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
}
