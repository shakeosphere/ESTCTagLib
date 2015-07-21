package ESTCTagLib.locatedByYear;


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
public class LocatedByYearDeleter extends ESTCTagLibBodyTagSupport {
    int pid = 0;
    int lid = 0;
    int pubyear = 0;
    String locational = null;
    int count = 0;
	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	private static final Log log = LogFactory.getLog(LocatedByYearDeleter.class);


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
            stat = getConnection().prepareStatement("DELETE from navigation.located_by_year where 1=1"
                                                        + (pid == 0 ? "" : " and pid = ? ")
                                                        + (lid == 0 ? "" : " and lid = ? ")
                                                        + (pubyear == 0 ? "" : " and pubyear = ? ")
                                                        + (locational == null ? "" : " and locational = ? ")
                                                        + (pid == 0 ? "" : " and pid = ? ")
                                                        + (lid == 0 ? "" : " and lid = ? "));
            if (pid != 0) stat.setInt(webapp_keySeq++, pid);
            if (lid != 0) stat.setInt(webapp_keySeq++, lid);
            if (pubyear != 0) stat.setInt(webapp_keySeq++, pubyear);
            if (locational != null) stat.setString(webapp_keySeq++, locational);
			if (pid != 0) stat.setInt(webapp_keySeq++, pid);
			if (lid != 0) stat.setInt(webapp_keySeq++, lid);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating LocatedByYear deleter", e);

			clearServiceState();
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating LocatedByYear deleter");
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating LocatedByYear deleter",e);
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
        pubyear = 0;
        locational = null;
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

	public int getPubyear () {
		return pubyear;
	}

	public void setPubyear (int pubyear) {
		this.pubyear = pubyear;
	}

	public int getActualPubyear () {
		return pubyear;
	}

	public String getLocational () {
		return locational;
	}

	public void setLocational (String locational) {
		this.locational = locational;
	}

	public String getActualLocational () {
		return locational;
	}
}
