package ESTCTagLib.printer;


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
import ESTCTagLib.record.Record;
import ESTCTagLib.person.Person;

@SuppressWarnings("serial")
public class PrinterIterator extends ESTCTagLibBodyTagSupport {
    int ID = 0;
    int pid = 0;
	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	private static final Log log = LogFactory.getLog(PrinterIterator.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

   boolean useRecord = false;
   boolean usePerson = false;

	public static String printerCountByRecord(String ID) throws JspTagException {
		int count = 0;
		PrinterIterator theIterator = new PrinterIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.printer where 1=1"
						+ " and id = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating Printer iterator", e);
			throw new JspTagException("Error: JDBC error generating Printer iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean recordHasPrinter(String ID) throws JspTagException {
		return ! printerCountByRecord(ID).equals("0");
	}

	public static String printerCountByPerson(String pid) throws JspTagException {
		int count = 0;
		PrinterIterator theIterator = new PrinterIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.printer where 1=1"
						+ " and pid = ?"
						);

			stat.setInt(1,Integer.parseInt(pid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating Printer iterator", e);
			throw new JspTagException("Error: JDBC error generating Printer iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean personHasPrinter(String pid) throws JspTagException {
		return ! printerCountByPerson(pid).equals("0");
	}

	public static Boolean printerExists (String ID, String pid) throws JspTagException {
		int count = 0;
		PrinterIterator theIterator = new PrinterIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.printer where 1=1"
						+ " and id = ?"
						+ " and pid = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			stat.setInt(2,Integer.parseInt(pid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating Printer iterator", e);
			throw new JspTagException("Error: JDBC error generating Printer iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

	public static Boolean recordPersonExists (String ID, String pid) throws JspTagException {
		int count = 0;
		PrinterIterator theIterator = new PrinterIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.printer where 1=1"
						+ " and id = ?"
						+ " and pid = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			stat.setInt(2,Integer.parseInt(pid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating Printer iterator", e);
			throw new JspTagException("Error: JDBC error generating Printer iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

    public int doStartTag() throws JspException {
		Record theRecord = (Record)findAncestorWithClass(this, Record.class);
		if (theRecord!= null)
			parentEntities.addElement(theRecord);
		Person thePerson = (Person)findAncestorWithClass(this, Person.class);
		if (thePerson!= null)
			parentEntities.addElement(thePerson);

		if (theRecord == null) {
		} else {
			ID = theRecord.getID();
		}
		if (thePerson == null) {
		} else {
			pid = thePerson.getPid();
		}


      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (ID == 0 ? "" : " and id = ?")
                                                        + (pid == 0 ? "" : " and pid = ?")
                                                        +  generateLimitCriteria());
            if (ID != 0) stat.setInt(webapp_keySeq++, ID);
            if (pid != 0) stat.setInt(webapp_keySeq++, pid);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT navigation.printer.id, navigation.printer.pid from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (ID == 0 ? "" : " and id = ?")
                                                        + (pid == 0 ? "" : " and pid = ?")
                                                        + " order by " + generateSortCriteria() + generateLimitCriteria());
            if (ID != 0) stat.setInt(webapp_keySeq++, ID);
            if (pid != 0) stat.setInt(webapp_keySeq++, pid);
            rs = stat.executeQuery();

            if (rs.next()) {
                ID = rs.getInt(1);
                pid = rs.getInt(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating Printer iterator: " + stat.toString(), e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating Printer iterator: " + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating Printer iterator: " + stat.toString(),e);
			}

        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("navigation.printer");
       if (useRecord)
          theBuffer.append(", navigation.record");
       if (usePerson)
          theBuffer.append(", navigation.person");

      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
       if (useRecord)
          theBuffer.append(" and record.ID = printer.null");
       if (usePerson)
          theBuffer.append(" and person.pid = printer.null");

      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "id,pid";
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
                ID = rs.getInt(1);
                pid = rs.getInt(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across Printer", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error iterating across Printer" + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error iterating across Printer",e);
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
            log.error("JDBC error ending Printer iterator",e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving ID " + ID);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error ending Printer iterator",e);
			}

        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        ID = 0;
        pid = 0;
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


   public boolean getUseRecord() {
        return useRecord;
    }

    public void setUseRecord(boolean useRecord) {
        this.useRecord = useRecord;
    }

   public boolean getUsePerson() {
        return usePerson;
    }

    public void setUsePerson(boolean usePerson) {
        this.usePerson = usePerson;
    }



	public int getID () {
		return ID;
	}

	public void setID (int ID) {
		this.ID = ID;
	}

	public int getActualID () {
		return ID;
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
}
