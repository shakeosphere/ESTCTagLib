package ESTCTagLib.personInByYear;


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
import ESTCTagLib.person.Person;
import ESTCTagLib.location.Location;

@SuppressWarnings("serial")
public class PersonInByYearIterator extends ESTCTagLibBodyTagSupport {
    int pid = 0;
    int lid = 0;
    int pubyear = 0;
    String locational = null;
    int count = 0;
	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(PersonInByYearIterator.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

   boolean usePerson = false;
   boolean useLocation = false;

	public static String personInByYearCountByPerson(String pid) throws JspTagException {
		int count = 0;
		PersonInByYearIterator theIterator = new PersonInByYearIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.person_in_by_year where 1=1"
						+ " and pid = ?"
						);

			stat.setInt(1,Integer.parseInt(pid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating PersonInByYear iterator", e);
			throw new JspTagException("Error: JDBC error generating PersonInByYear iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean personHasPersonInByYear(String pid) throws JspTagException {
		return ! personInByYearCountByPerson(pid).equals("0");
	}

	public static String personInByYearCountByLocation(String lid) throws JspTagException {
		int count = 0;
		PersonInByYearIterator theIterator = new PersonInByYearIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.person_in_by_year where 1=1"
						+ " and lid = ?"
						);

			stat.setInt(1,Integer.parseInt(lid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating PersonInByYear iterator", e);
			throw new JspTagException("Error: JDBC error generating PersonInByYear iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean locationHasPersonInByYear(String lid) throws JspTagException {
		return ! personInByYearCountByLocation(lid).equals("0");
	}

	public static Boolean personInByYearExists (String pid, String lid, String pubyear, String locational) throws JspTagException {
		int count = 0;
		PersonInByYearIterator theIterator = new PersonInByYearIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.person_in_by_year where 1=1"
						+ " and pid = ?"
						+ " and lid = ?"
						+ " and pubyear = ?"
						+ " and locational = ?"
						);

			stat.setInt(1,Integer.parseInt(pid));
			stat.setInt(2,Integer.parseInt(lid));
			stat.setInt(3,Integer.parseInt(pubyear));
			stat.setString(4,locational);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating PersonInByYear iterator", e);
			throw new JspTagException("Error: JDBC error generating PersonInByYear iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

	public static Boolean personLocationExists (String pid, String lid) throws JspTagException {
		int count = 0;
		PersonInByYearIterator theIterator = new PersonInByYearIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.person_in_by_year where 1=1"
						+ " and pid = ?"
						+ " and lid = ?"
						);

			stat.setInt(1,Integer.parseInt(pid));
			stat.setInt(2,Integer.parseInt(lid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating PersonInByYear iterator", e);
			throw new JspTagException("Error: JDBC error generating PersonInByYear iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

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


      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (pid == 0 ? "" : " and pid = ?")
                                                        + (lid == 0 ? "" : " and lid = ?")
                                                        + generateLimitCriteria());
            if (pid != 0) stat.setInt(webapp_keySeq++, pid);
            if (lid != 0) stat.setInt(webapp_keySeq++, lid);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT navigation.person_in_by_year.pid, navigation.person_in_by_year.lid, navigation.person_in_by_year.pubyear, navigation.person_in_by_year.locational from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (pid == 0 ? "" : " and pid = ?")
                                                        + (lid == 0 ? "" : " and lid = ?")
                                                        + " order by " + generateSortCriteria()  +  generateLimitCriteria());
            if (pid != 0) stat.setInt(webapp_keySeq++, pid);
            if (lid != 0) stat.setInt(webapp_keySeq++, lid);
            rs = stat.executeQuery();

            if ( rs != null && rs.next() ) {
                pid = rs.getInt(1);
                lid = rs.getInt(2);
                pubyear = rs.getInt(3);
                locational = rs.getString(4);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating PersonInByYear iterator: " + stat.toString(), e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating PersonInByYear iterator: " + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating PersonInByYear iterator: " + stat.toString(),e);
			}

        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("navigation.person_in_by_year");
       if (usePerson)
          theBuffer.append(", navigation.person");
       if (useLocation)
          theBuffer.append(", navigation.location");

      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
       if (usePerson)
          theBuffer.append(" and person.pid = person_in_by_year.pid");
       if (useLocation)
          theBuffer.append(" and location.lid = person_in_by_year.lid");

      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "pid,lid,pubyear,locational";
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
                pid = rs.getInt(1);
                lid = rs.getInt(2);
                pubyear = rs.getInt(3);
                locational = rs.getString(4);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across PersonInByYear", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error iterating across PersonInByYear" + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error iterating across PersonInByYear",e);
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
            log.error("JDBC error ending PersonInByYear iterator",e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving pubyear " + pubyear);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error ending PersonInByYear iterator",e);
			}

        } finally {
            clearServiceState();
            freeConnection();
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

   public boolean getUseLocation() {
        return useLocation;
    }

    public void setUseLocation(boolean useLocation) {
        this.useLocation = useLocation;
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
