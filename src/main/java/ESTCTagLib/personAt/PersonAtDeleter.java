package ESTCTagLib.personAt;


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
import ESTCTagLib.establishment.Establishment;
import ESTCTagLib.person.Person;

@SuppressWarnings("serial")
public class PersonAtDeleter extends ESTCTagLibBodyTagSupport {
    int estcId = 0;
    int establishmentId = 0;
    int personId = 0;
    String locational = null;
	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	private static final Log log = LogFactory.getLog(PersonAtDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		Establishment theEstablishment = (Establishment)findAncestorWithClass(this, Establishment.class);
		if (theEstablishment!= null)
			parentEntities.addElement(theEstablishment);
		Person thePerson = (Person)findAncestorWithClass(this, Person.class);
		if (thePerson!= null)
			parentEntities.addElement(thePerson);

		if (theEstablishment == null) {
		} else {
			establishmentId = theEstablishment.getEid();
		}
		if (thePerson == null) {
		} else {
			personId = thePerson.getPid();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from navigation.person_at where 1=1"
                                                        + (estcId == 0 ? "" : " and estc_id = ? ")
                                                        + (establishmentId == 0 ? "" : " and establishment_id = ? ")
                                                        + (personId == 0 ? "" : " and person_id = ? ")
                                                        + (establishmentId == 0 ? "" : " and establishment_id = ? ")
                                                        + (personId == 0 ? "" : " and person_id = ? "));
            if (estcId != 0) stat.setInt(webapp_keySeq++, estcId);
            if (establishmentId != 0) stat.setInt(webapp_keySeq++, establishmentId);
            if (personId != 0) stat.setInt(webapp_keySeq++, personId);
			if (establishmentId != 0) stat.setInt(webapp_keySeq++, establishmentId);
			if (personId != 0) stat.setInt(webapp_keySeq++, personId);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating PersonAt deleter", e);

			clearServiceState();
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating PersonAt deleter");
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating PersonAt deleter",e);
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
        estcId = 0;
        establishmentId = 0;
        personId = 0;
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



	public int getEstcId () {
		return estcId;
	}

	public void setEstcId (int estcId) {
		this.estcId = estcId;
	}

	public int getActualEstcId () {
		return estcId;
	}

	public int getEstablishmentId () {
		return establishmentId;
	}

	public void setEstablishmentId (int establishmentId) {
		this.establishmentId = establishmentId;
	}

	public int getActualEstablishmentId () {
		return establishmentId;
	}

	public int getPersonId () {
		return personId;
	}

	public void setPersonId (int personId) {
		this.personId = personId;
	}

	public int getActualPersonId () {
		return personId;
	}
}
