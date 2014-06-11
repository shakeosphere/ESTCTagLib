package ESTCTagLib.located;


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
import ESTCTagLib.person.Person;
import ESTCTagLib.location.Location;

@SuppressWarnings("serial")
public class LocatedDeleter extends ESTCTagLibBodyTagSupport {
    int pid = 0;
    int lid = 0;
	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	private static final Log log = LogFactory.getLog(LocatedDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		Person thePerson = (Person)findAncestorWithClass(this, Person.class);
		if (thePerson!= null)
			parentEntities.addElement(thePerson);
		Location theLocation = (Location)findAncestorWithClass(this, Location.class);
		if (theLocation!= null)
			parentEntities.addElement(theLocation);

		if (thePerson == null) {
		} else {
			pid = thePerson.getPid();
		}
		if (theLocation == null) {
		} else {
			lid = theLocation.getLid();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from navigation.located where 1=1"
                                                        + (pid == 0 ? "" : " and pid = ? ")
                                                        + (lid == 0 ? "" : " and lid = ? ")
                                                        + (pid == 0 ? "" : " and pid = ? ")
                                                        + (lid == 0 ? "" : " and lid = ? "));
            if (pid != 0) stat.setInt(webapp_keySeq++, pid);
            if (lid != 0) stat.setInt(webapp_keySeq++, lid);
			if (pid != 0) stat.setInt(webapp_keySeq++, pid);
			if (lid != 0) stat.setInt(webapp_keySeq++, lid);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating Located deleter", e);

			clearServiceState();
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating Located deleter");
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating Located deleter",e);
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
        pid = 0;
        lid = 0;
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



	public int getPid () {
		return pid;
	}

	public void setPid (int pid) {
		this.pid = pid;
	}

	public int getActualPid () {
		return pid;
	}

	public int getLid () {
		return lid;
	}

	public void setLid (int lid) {
		this.lid = lid;
	}

	public int getActualLid () {
		return lid;
	}
}
