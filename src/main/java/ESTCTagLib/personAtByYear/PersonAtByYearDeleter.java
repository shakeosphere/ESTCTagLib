package ESTCTagLib.personAtByYear;


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
import ESTCTagLib.person.Person;
import ESTCTagLib.establishment.Establishment;

@SuppressWarnings("serial")
public class PersonAtByYearDeleter extends ESTCTagLibBodyTagSupport {
    int pid = 0;
    int eid = 0;
    int pubyear = 0;
    String locational = null;
    int count = 0;
	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(PersonAtByYearDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		Person thePerson = (Person)findAncestorWithClass(this, Person.class);
		if (thePerson!= null)
			parentEntities.addElement(thePerson);
		Establishment theEstablishment = (Establishment)findAncestorWithClass(this, Establishment.class);
		if (theEstablishment!= null)
			parentEntities.addElement(theEstablishment);

		if (thePerson == null) {
		} else {
			pid = thePerson.getPid();
		}
		if (theEstablishment == null) {
		} else {
			eid = theEstablishment.getEid();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from navigation.person_at_by_year where 1=1"
                                                        + (pid == 0 ? "" : " and pid = ? ")
                                                        + (eid == 0 ? "" : " and eid = ? ")
                                                        + (pubyear == 0 ? "" : " and pubyear = ? ")
                                                        + (locational == null ? "" : " and locational = ? ")
                                                        + (pid == 0 ? "" : " and pid = ? ")
                                                        + (eid == 0 ? "" : " and eid = ? "));
            if (pid != 0) stat.setInt(webapp_keySeq++, pid);
            if (eid != 0) stat.setInt(webapp_keySeq++, eid);
            if (pubyear != 0) stat.setInt(webapp_keySeq++, pubyear);
            if (locational != null) stat.setString(webapp_keySeq++, locational);
			if (pid != 0) stat.setInt(webapp_keySeq++, pid);
			if (eid != 0) stat.setInt(webapp_keySeq++, eid);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating PersonAtByYear deleter", e);

			clearServiceState();
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating PersonAtByYear deleter");
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating PersonAtByYear deleter",e);
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
        eid = 0;
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

	public int getEid () {
		return eid;
	}

	public void setEid (int eid) {
		this.eid = eid;
	}

	public int getActualEid () {
		return eid;
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
