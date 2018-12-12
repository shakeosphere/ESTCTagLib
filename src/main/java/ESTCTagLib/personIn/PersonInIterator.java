package ESTCTagLib.personIn;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.ESTCTagLibBodyTagSupport;
import ESTCTagLib.location.Location;
import ESTCTagLib.person.Person;

@SuppressWarnings("serial")
public class PersonInIterator extends ESTCTagLibBodyTagSupport {
    int estcId = 0;
    int locationId = 0;
    int personId = 0;
    String locational = null;
	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	private static final Log log = LogFactory.getLog(PersonInIterator.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

   boolean useLocation = false;
   boolean usePerson = false;

	public static String personInCountByLocation(String lid) throws JspTagException {
		int count = 0;
		PersonInIterator theIterator = new PersonInIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.person_in where 1=1"
						+ " and location_id = ?"
						);

			stat.setInt(1,Integer.parseInt(lid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating PersonIn iterator", e);
			throw new JspTagException("Error: JDBC error generating PersonIn iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean locationHasPersonIn(String lid) throws JspTagException {
		return ! personInCountByLocation(lid).equals("0");
	}

	public static String personInCountByPerson(String pid) throws JspTagException {
		int count = 0;
		PersonInIterator theIterator = new PersonInIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.person_in where 1=1"
						+ " and pid = ?"
						);

			stat.setInt(1,Integer.parseInt(pid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating PersonIn iterator", e);
			throw new JspTagException("Error: JDBC error generating PersonIn iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean personHasPersonIn(String pid) throws JspTagException {
		return ! personInCountByPerson(pid).equals("0");
	}

	public static Boolean personInExists (String estcId, String locationId, String personId) throws JspTagException {
		int count = 0;
		PersonInIterator theIterator = new PersonInIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.person_in where 1=1"
						+ " and estc_id = ?"
						+ " and location_id = ?"
						+ " and person_id = ?"
						);

			stat.setInt(1,Integer.parseInt(estcId));
			stat.setInt(2,Integer.parseInt(locationId));
			stat.setInt(3,Integer.parseInt(personId));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating PersonIn iterator", e);
			throw new JspTagException("Error: JDBC error generating PersonIn iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

	public static Boolean locationPersonExists (String lid, String pid) throws JspTagException {
		int count = 0;
		PersonInIterator theIterator = new PersonInIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.person_in where 1=1"
						+ " and lid = ?"
						+ " and pid = ?"
						);

			stat.setInt(1,Integer.parseInt(lid));
			stat.setInt(2,Integer.parseInt(pid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating PersonIn iterator", e);
			throw new JspTagException("Error: JDBC error generating PersonIn iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

    public int doStartTag() throws JspException {
		Location theLocation = (Location)findAncestorWithClass(this, Location.class);
		if (theLocation!= null)
			parentEntities.addElement(theLocation);
		Person thePerson = (Person)findAncestorWithClass(this, Person.class);
		if (thePerson!= null)
			parentEntities.addElement(thePerson);

		if (theLocation == null) {
		} else {
			locationId = theLocation.getLid();
		}
		if (thePerson == null) {
		} else {
			personId = thePerson.getPid();
		}


      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (locationId == 0 ? "" : " and location_id = ?")
                                                        + (personId == 0 ? "" : " and person_id = ?")
                                                        +  generateLimitCriteria());
            if (locationId != 0) stat.setInt(webapp_keySeq++, locationId);
            if (personId != 0) stat.setInt(webapp_keySeq++, personId);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT navigation.person_in.estc_id, navigation.person_in.location_id, navigation.person_in.person_id from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (locationId == 0 ? "" : " and location_id = ?")
                                                        + (personId == 0 ? "" : " and person_id = ?")
                                                        + " order by " + generateSortCriteria() + generateLimitCriteria());
            if (locationId != 0) stat.setInt(webapp_keySeq++, locationId);
            if (personId != 0) stat.setInt(webapp_keySeq++, personId);
            rs = stat.executeQuery();

            if (rs.next()) {
                estcId = rs.getInt(1);
                locationId = rs.getInt(2);
                personId = rs.getInt(3);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating PersonIn iterator: " + stat.toString(), e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating PersonIn iterator: " + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating PersonIn iterator: " + stat.toString(),e);
			}

        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("navigation.person_in");
       if (useLocation)
          theBuffer.append(", navigation.location");
       if (usePerson)
          theBuffer.append(", navigation.person");

      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
       if (useLocation)
          theBuffer.append(" and location.lid = person_in.null");
       if (usePerson)
          theBuffer.append(" and person.pid = person_in.null");

      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "estc_id,location_id,person_id";
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
            if (rs.next()) {
                estcId = rs.getInt(1);
                locationId = rs.getInt(2);
                personId = rs.getInt(3);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across PersonIn", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error iterating across PersonIn" + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error iterating across PersonIn",e);
			}

        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
			if(pageContext != null){
				Boolean error = (Boolean) pageContext.getAttribute("tagError");
				if(error != null && error){

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
            rs.close();
            stat.close();
        } catch (SQLException e) {
            log.error("JDBC error ending PersonIn iterator",e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving estcId " + estcId);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error ending PersonIn iterator",e);
			}

        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        estcId = 0;
        locationId = 0;
        personId = 0;
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


   public boolean getUseLocation() {
        return useLocation;
    }

    public void setUseLocation(boolean useLocation) {
        this.useLocation = useLocation;
    }

   public boolean getUsePerson() {
        return usePerson;
    }

    public void setUsePerson(boolean usePerson) {
        this.usePerson = usePerson;
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

	public int getLocationId () {
		return locationId;
	}

	public void setLocationId (int locationId) {
		this.locationId = locationId;
	}

	public int getActualLocationId () {
		return locationId;
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
