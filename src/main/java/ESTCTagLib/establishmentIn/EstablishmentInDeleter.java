package ESTCTagLib.establishmentIn;


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
import ESTCTagLib.location.Location;

@SuppressWarnings("serial")
public class EstablishmentInDeleter extends ESTCTagLibBodyTagSupport {
    int estcId = 0;
    int establishmentId = 0;
    int llocationId = 0;
    String locational = null;
	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	private static final Log log = LogFactory.getLog(EstablishmentInDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		Establishment theEstablishment = (Establishment)findAncestorWithClass(this, Establishment.class);
		if (theEstablishment!= null)
			parentEntities.addElement(theEstablishment);
		Location theLocation = (Location)findAncestorWithClass(this, Location.class);
		if (theLocation!= null)
			parentEntities.addElement(theLocation);

		if (theEstablishment == null) {
		} else {
			establishmentId = theEstablishment.getID();
		}
		if (theLocation == null) {
		} else {
			llocationId = theLocation.getLid();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from navigation.establishment_in where 1=1"
                                                        + (estcId == 0 ? "" : " and estc_id = ? ")
                                                        + (establishmentId == 0 ? "" : " and establishment_id = ? ")
                                                        + (llocationId == 0 ? "" : " and llocation_id = ? ")
                                                        + (establishmentId == 0 ? "" : " and establishment_id = ? ")
                                                        + (llocationId == 0 ? "" : " and llocation_id = ? "));
            if (estcId != 0) stat.setInt(webapp_keySeq++, estcId);
            if (establishmentId != 0) stat.setInt(webapp_keySeq++, establishmentId);
            if (llocationId != 0) stat.setInt(webapp_keySeq++, llocationId);
			if (establishmentId != 0) stat.setInt(webapp_keySeq++, establishmentId);
			if (llocationId != 0) stat.setInt(webapp_keySeq++, llocationId);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating EstablishmentIn deleter", e);

			clearServiceState();
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating EstablishmentIn deleter");
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating EstablishmentIn deleter",e);
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
        llocationId = 0;
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

	public int getLlocationId () {
		return llocationId;
	}

	public void setLlocationId (int llocationId) {
		this.llocationId = llocationId;
	}

	public int getActualLlocationId () {
		return llocationId;
	}
}
