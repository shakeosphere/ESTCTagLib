package ESTCTagLib.establishmentIn;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.ESTCTagLibBodyTagSupport;
import ESTCTagLib.establishment.Establishment;
import ESTCTagLib.location.Location;

@SuppressWarnings("serial")
public class EstablishmentInIterator extends ESTCTagLibBodyTagSupport {
    int estcId = 0;
    int establishmentId = 0;
    int llocationId = 0;
    String locational = null;
	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(EstablishmentInIterator.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

   boolean useEstablishment = false;
   boolean useLocation = false;

	public static String establishmentInCountByEstablishment(String eid) throws JspTagException {
		int count = 0;
		EstablishmentInIterator theIterator = new EstablishmentInIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.establishment_in where 1=1"
						+ " and establishment_id = ?"
						);

			stat.setInt(1,Integer.parseInt(eid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating EstablishmentIn iterator", e);
			throw new JspTagException("Error: JDBC error generating EstablishmentIn iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean establishmentHasEstablishmentIn(String eid) throws JspTagException {
		return ! establishmentInCountByEstablishment(eid).equals("0");
	}

	public static String establishmentInCountByLocation(String lid) throws JspTagException {
		int count = 0;
		EstablishmentInIterator theIterator = new EstablishmentInIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.establishment_in where 1=1"
						+ " and llocation_id = ?"
						);

			stat.setInt(1,Integer.parseInt(lid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating EstablishmentIn iterator", e);
			throw new JspTagException("Error: JDBC error generating EstablishmentIn iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean locationHasEstablishmentIn(String lid) throws JspTagException {
		return ! establishmentInCountByLocation(lid).equals("0");
	}

	public static Boolean establishmentInExists (String estcId, String establishmentId, String llocationId) throws JspTagException {
		int count = 0;
		EstablishmentInIterator theIterator = new EstablishmentInIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.establishment_in where 1=1"
						+ " and estc_id = ?"
						+ " and establishment_id = ?"
						+ " and llocation_id = ?"
						);

			stat.setInt(1,Integer.parseInt(estcId));
			stat.setInt(2,Integer.parseInt(establishmentId));
			stat.setInt(3,Integer.parseInt(llocationId));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating EstablishmentIn iterator", e);
			throw new JspTagException("Error: JDBC error generating EstablishmentIn iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

	public static Boolean establishmentLocationExists (String eid, String lid) throws JspTagException {
		int count = 0;
		EstablishmentInIterator theIterator = new EstablishmentInIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.establishment_in where 1=1"
						+ " and eid = ?"
						+ " and lid = ?"
						);

			stat.setInt(1,Integer.parseInt(eid));
			stat.setInt(2,Integer.parseInt(lid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating EstablishmentIn iterator", e);
			throw new JspTagException("Error: JDBC error generating EstablishmentIn iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

    public int doStartTag() throws JspException {
		Establishment theEstablishment = (Establishment)findAncestorWithClass(this, Establishment.class);
		if (theEstablishment!= null)
			parentEntities.addElement(theEstablishment);
		Location theLocation = (Location)findAncestorWithClass(this, Location.class);
		if (theLocation!= null)
			parentEntities.addElement(theLocation);

		if (theEstablishment == null) {
		} else {
			establishmentId = theEstablishment.getEid();
		}
		if (theLocation == null) {
		} else {
			llocationId = theLocation.getLid();
		}


      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (establishmentId == 0 ? "" : " and establishment_id = ?")
                                                        + (llocationId == 0 ? "" : " and llocation_id = ?")
                                                        + generateLimitCriteria());
            if (establishmentId != 0) stat.setInt(webapp_keySeq++, establishmentId);
            if (llocationId != 0) stat.setInt(webapp_keySeq++, llocationId);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT navigation.establishment_in.estc_id, navigation.establishment_in.establishment_id, navigation.establishment_in.llocation_id from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (establishmentId == 0 ? "" : " and establishment_id = ?")
                                                        + (llocationId == 0 ? "" : " and llocation_id = ?")
                                                        + " order by " + generateSortCriteria()  +  generateLimitCriteria());
            if (establishmentId != 0) stat.setInt(webapp_keySeq++, establishmentId);
            if (llocationId != 0) stat.setInt(webapp_keySeq++, llocationId);
            rs = stat.executeQuery();

            if ( rs != null && rs.next() ) {
                estcId = rs.getInt(1);
                establishmentId = rs.getInt(2);
                llocationId = rs.getInt(3);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating EstablishmentIn iterator: " + stat.toString(), e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating EstablishmentIn iterator: " + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating EstablishmentIn iterator: " + stat.toString(),e);
			}

        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("navigation.establishment_in");
       if (useEstablishment)
          theBuffer.append(", navigation.establishment");
       if (useLocation)
          theBuffer.append(", navigation.location");

      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
       if (useEstablishment)
          theBuffer.append(" and establishment.eid = establishment_in.establishment_id");
       if (useLocation)
          theBuffer.append(" and location.lid = establishment_in.llocation_id");

      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "estc_id,establishment_id,llocation_id";
        }
    }

    private String generateLimitCriteria() {
        if (limitCriteria > 0) {
            return " limit " + limitCriteria;
        } else {
            return "";
        }
    }

    public int doAfterBody() throws JspException {
        try {
            if ( rs != null && rs.next() ) {
                estcId = rs.getInt(1);
                establishmentId = rs.getInt(2);
                llocationId = rs.getInt(3);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across EstablishmentIn", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error iterating across EstablishmentIn" + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error iterating across EstablishmentIn",e);
			}

        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
			if( pageContext != null ){
				Boolean error = (Boolean) pageContext.getAttribute("tagError");
				if( error != null && error ){

					freeConnection();
					clearServiceState();

					Exception e = null; // (Exception) pageContext.getAttribute("tagErrorException");
					String message = null; // (String) pageContext.getAttribute("tagErrorMessage");

					if(pageContext != null){
						e = (Exception) pageContext.getAttribute("tagErrorException");
						message = (String) pageContext.getAttribute("tagErrorMessage");

					}
					Tag parent = getParent();
					if(parent != null){
						return parent.doEndTag();
					}else if(e != null && message != null){
						throw new JspException(message,e);
					}else if(parent == null && pageContext != null){
						pageContext.removeAttribute("tagError");
						pageContext.removeAttribute("tagErrorException");
						pageContext.removeAttribute("tagErrorMessage");
					}
				}
			}

            if( rs != null ){
                rs.close();
            }

            if( stat != null ){
                stat.close();
            }

        } catch ( SQLException e ) {
            log.error("JDBC error ending EstablishmentIn iterator",e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving estcId " + estcId);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error ending EstablishmentIn iterator",e);
			}

        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        estcId = 0;
        establishmentId = 0;
        llocationId = 0;
        parentEntities = new Vector<ESTCTagLibTagSupport>();

        this.rs = null;
        this.stat = null;
        this.sortCriteria = null;
        this.var = null;
        this.rsCount = 0;
    }

    public String getSortCriteria() {
        return sortCriteria;
    }

    public void setSortCriteria(String sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public int getLimitCriteria() {
        return limitCriteria;
    }

    public void setLimitCriteria(int limitCriteria) {
        this.limitCriteria = limitCriteria;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }


   public boolean getUseEstablishment() {
        return useEstablishment;
    }

    public void setUseEstablishment(boolean useEstablishment) {
        this.useEstablishment = useEstablishment;
    }

   public boolean getUseLocation() {
        return useLocation;
    }

    public void setUseLocation(boolean useLocation) {
        this.useLocation = useLocation;
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
