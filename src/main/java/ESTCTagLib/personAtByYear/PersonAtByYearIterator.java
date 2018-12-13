package ESTCTagLib.personAtByYear;


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
import ESTCTagLib.person.Person;
import ESTCTagLib.establishment.Establishment;

@SuppressWarnings("serial")
public class PersonAtByYearIterator extends ESTCTagLibBodyTagSupport {
    int pid = 0;
    int eid = 0;
    int pubyear = 0;
    String locational = null;
    int count = 0;
	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	private static final Log log = LogFactory.getLog(PersonAtByYearIterator.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

   boolean usePerson = false;
   boolean useEstablishment = false;

	public static String personAtByYearCountByPerson(String pid) throws JspTagException {
		int count = 0;
		PersonAtByYearIterator theIterator = new PersonAtByYearIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.person_at_by_year where 1=1"
						+ " and pid = ?"
						);

			stat.setInt(1,Integer.parseInt(pid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating PersonAtByYear iterator", e);
			throw new JspTagException("Error: JDBC error generating PersonAtByYear iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean personHasPersonAtByYear(String pid) throws JspTagException {
		return ! personAtByYearCountByPerson(pid).equals("0");
	}

	public static String personAtByYearCountByEstablishment(String eid) throws JspTagException {
		int count = 0;
		PersonAtByYearIterator theIterator = new PersonAtByYearIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.person_at_by_year where 1=1"
						+ " and eid = ?"
						);

			stat.setInt(1,Integer.parseInt(eid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating PersonAtByYear iterator", e);
			throw new JspTagException("Error: JDBC error generating PersonAtByYear iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean establishmentHasPersonAtByYear(String eid) throws JspTagException {
		return ! personAtByYearCountByEstablishment(eid).equals("0");
	}

	public static Boolean personAtByYearExists (String pid, String eid, String pubyear, String locational) throws JspTagException {
		int count = 0;
		PersonAtByYearIterator theIterator = new PersonAtByYearIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.person_at_by_year where 1=1"
						+ " and pid = ?"
						+ " and eid = ?"
						+ " and pubyear = ?"
						+ " and locational = ?"
						);

			stat.setInt(1,Integer.parseInt(pid));
			stat.setInt(2,Integer.parseInt(eid));
			stat.setInt(3,Integer.parseInt(pubyear));
			stat.setString(4,locational);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating PersonAtByYear iterator", e);
			throw new JspTagException("Error: JDBC error generating PersonAtByYear iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

	public static Boolean personEstablishmentExists (String pid, String eid) throws JspTagException {
		int count = 0;
		PersonAtByYearIterator theIterator = new PersonAtByYearIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.person_at_by_year where 1=1"
						+ " and pid = ?"
						+ " and eid = ?"
						);

			stat.setInt(1,Integer.parseInt(pid));
			stat.setInt(2,Integer.parseInt(eid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating PersonAtByYear iterator", e);
			throw new JspTagException("Error: JDBC error generating PersonAtByYear iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

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


      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (pid == 0 ? "" : " and pid = ?")
                                                        + (eid == 0 ? "" : " and eid = ?")
                                                        +  generateLimitCriteria());
            if (pid != 0) stat.setInt(webapp_keySeq++, pid);
            if (eid != 0) stat.setInt(webapp_keySeq++, eid);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT navigation.person_at_by_year.pid, navigation.person_at_by_year.eid, navigation.person_at_by_year.pubyear, navigation.person_at_by_year.locational from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (pid == 0 ? "" : " and pid = ?")
                                                        + (eid == 0 ? "" : " and eid = ?")
                                                        + " order by " + generateSortCriteria() + generateLimitCriteria());
            if (pid != 0) stat.setInt(webapp_keySeq++, pid);
            if (eid != 0) stat.setInt(webapp_keySeq++, eid);
            rs = stat.executeQuery();

            if (rs.next()) {
                pid = rs.getInt(1);
                eid = rs.getInt(2);
                pubyear = rs.getInt(3);
                locational = rs.getString(4);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating PersonAtByYear iterator: " + stat.toString(), e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating PersonAtByYear iterator: " + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating PersonAtByYear iterator: " + stat.toString(),e);
			}

        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("navigation.person_at_by_year");
       if (usePerson)
          theBuffer.append(", navigation.person");
       if (useEstablishment)
          theBuffer.append(", navigation.establishment");

      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
       if (usePerson)
          theBuffer.append(" and person.pid = person_at_by_year.null");
       if (useEstablishment)
          theBuffer.append(" and establishment.eid = person_at_by_year.null");

      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "pid,eid,pubyear,locational";
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
                pid = rs.getInt(1);
                eid = rs.getInt(2);
                pubyear = rs.getInt(3);
                locational = rs.getString(4);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across PersonAtByYear", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error iterating across PersonAtByYear" + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error iterating across PersonAtByYear",e);
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
            log.error("JDBC error ending PersonAtByYear iterator",e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving pubyear " + pubyear);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error ending PersonAtByYear iterator",e);
			}

        } finally {
            clearServiceState();
            freeConnection();
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


   public boolean getUsePerson() {
        return usePerson;
    }

    public void setUsePerson(boolean usePerson) {
        this.usePerson = usePerson;
    }

   public boolean getUseEstablishment() {
        return useEstablishment;
    }

    public void setUseEstablishment(boolean useEstablishment) {
        this.useEstablishment = useEstablishment;
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
