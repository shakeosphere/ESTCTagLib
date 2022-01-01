package ESTCTagLib.locationIn;


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
import ESTCTagLib.location.Location;

@SuppressWarnings("serial")
public class LocationInDeleter extends ESTCTagLibBodyTagSupport {
    int estcId = 0;
    int sublocationId = 0;
    int locationId = 0;
    String locational = null;
	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(LocationInDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		Location theLocation = (Location)findAncestorWithClass(this, Location.class);
		if (theLocation!= null)
			parentEntities.addElement(theLocation);

		if (theLocation == null) {
		} else {
			sublocationId = theLocation.getLid();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from navigation.location_in where 1=1"
                                                        + (estcId == 0 ? "" : " and estc_id = ? ")
                                                        + (sublocationId == 0 ? "" : " and sublocation_id = ? ")
                                                        + (locationId == 0 ? "" : " and location_id = ? ")
                                                        + (sublocationId == 0 ? "" : " and sublocation_id = ? "));
            if (estcId != 0) stat.setInt(webapp_keySeq++, estcId);
            if (sublocationId != 0) stat.setInt(webapp_keySeq++, sublocationId);
            if (locationId != 0) stat.setInt(webapp_keySeq++, locationId);
			if (sublocationId != 0) stat.setInt(webapp_keySeq++, sublocationId);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating LocationIn deleter", e);

			clearServiceState();
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating LocationIn deleter");
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating LocationIn deleter",e);
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
        sublocationId = 0;
        locationId = 0;
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

	public int getSublocationId () {
		return sublocationId;
	}

	public void setSublocationId (int sublocationId) {
		this.sublocationId = sublocationId;
	}

	public int getActualSublocationId () {
		return sublocationId;
	}

	public int getLocationId () {
		return locationId;
	}

	public void setLocationId (int locationId) {
		this.locationId = locationId;
	}

	public int getActualLocationId () {
		return locationId;
	}
}
