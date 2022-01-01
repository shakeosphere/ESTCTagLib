package ESTCTagLib.personAt;


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
import ESTCTagLib.person.Person;

@SuppressWarnings("serial")
public class PersonAtIterator extends ESTCTagLibBodyTagSupport {
    int estcId = 0;
    int establishmentId = 0;
    int personId = 0;
    String locational = null;
	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(PersonAtIterator.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

   boolean useEstablishment = false;
   boolean usePerson = false;

	public static String personAtCountByEstablishment(String eid) throws JspTagException {
		int count = 0;
		PersonAtIterator theIterator = new PersonAtIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.person_at where 1=1"
						+ " and establishment_id = ?"
						);

			stat.setInt(1,Integer.parseInt(eid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating PersonAt iterator", e);
			throw new JspTagException("Error: JDBC error generating PersonAt iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean establishmentHasPersonAt(String eid) throws JspTagException {
		return ! personAtCountByEstablishment(eid).equals("0");
	}

	public static String personAtCountByPerson(String pid) throws JspTagException {
		int count = 0;
		PersonAtIterator theIterator = new PersonAtIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.person_at where 1=1"
						+ " and person_id = ?"
						);

			stat.setInt(1,Integer.parseInt(pid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating PersonAt iterator", e);
			throw new JspTagException("Error: JDBC error generating PersonAt iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean personHasPersonAt(String pid) throws JspTagException {
		return ! personAtCountByPerson(pid).equals("0");
	}

	public static Boolean personAtExists (String estcId, String establishmentId, String personId) throws JspTagException {
		int count = 0;
		PersonAtIterator theIterator = new PersonAtIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.person_at where 1=1"
						+ " and estc_id = ?"
						+ " and establishment_id = ?"
						+ " and person_id = ?"
						);

			stat.setInt(1,Integer.parseInt(estcId));
			stat.setInt(2,Integer.parseInt(establishmentId));
			stat.setInt(3,Integer.parseInt(personId));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating PersonAt iterator", e);
			throw new JspTagException("Error: JDBC error generating PersonAt iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

	public static Boolean establishmentPersonExists (String eid, String pid) throws JspTagException {
		int count = 0;
		PersonAtIterator theIterator = new PersonAtIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.person_at where 1=1"
						+ " and eid = ?"
						+ " and pid = ?"
						);

			stat.setInt(1,Integer.parseInt(eid));
			stat.setInt(2,Integer.parseInt(pid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating PersonAt iterator", e);
			throw new JspTagException("Error: JDBC error generating PersonAt iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

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


      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (establishmentId == 0 ? "" : " and establishment_id = ?")
                                                        + (personId == 0 ? "" : " and person_id = ?")
                                                        + generateLimitCriteria());
            if (establishmentId != 0) stat.setInt(webapp_keySeq++, establishmentId);
            if (personId != 0) stat.setInt(webapp_keySeq++, personId);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT navigation.person_at.estc_id, navigation.person_at.establishment_id, navigation.person_at.person_id from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (establishmentId == 0 ? "" : " and establishment_id = ?")
                                                        + (personId == 0 ? "" : " and person_id = ?")
                                                        + " order by " + generateSortCriteria()  +  generateLimitCriteria());
            if (establishmentId != 0) stat.setInt(webapp_keySeq++, establishmentId);
            if (personId != 0) stat.setInt(webapp_keySeq++, personId);
            rs = stat.executeQuery();

            if ( rs != null && rs.next() ) {
                estcId = rs.getInt(1);
                establishmentId = rs.getInt(2);
                personId = rs.getInt(3);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating PersonAt iterator: " + stat.toString(), e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating PersonAt iterator: " + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating PersonAt iterator: " + stat.toString(),e);
			}

        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("navigation.person_at");
       if (useEstablishment)
          theBuffer.append(", navigation.establishment");
       if (usePerson)
          theBuffer.append(", navigation.person");

      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
       if (useEstablishment)
          theBuffer.append(" and establishment.eid = person_at.establishment_id");
       if (usePerson)
          theBuffer.append(" and person.pid = person_at.person_id");

      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "estc_id,establishment_id,person_id";
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
                personId = rs.getInt(3);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across PersonAt", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error iterating across PersonAt" + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error iterating across PersonAt",e);
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
            log.error("JDBC error ending PersonAt iterator",e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving estcId " + estcId);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error ending PersonAt iterator",e);
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


   public boolean getUseEstablishment() {
        return useEstablishment;
    }

    public void setUseEstablishment(boolean useEstablishment) {
        this.useEstablishment = useEstablishment;
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
