package ESTCTagLib.subtag;


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
import ESTCTagLib.mtag.Mtag;

@SuppressWarnings("serial")
public class SubtagIterator extends ESTCTagLibBodyTagSupport {
    int ID = 0;
    String tag = null;
    String code = null;
    String value = null;
	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	private static final Log log = LogFactory.getLog(SubtagIterator.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

	public static String subtagCountByMtag(String ID, String tag) throws JspTagException {
		int count = 0;
		SubtagIterator theIterator = new SubtagIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.subtag where 1=1"
						+ " and id = ?"
						+ " and tag = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			stat.setString(2,tag);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating Subtag iterator", e);
			throw new JspTagException("Error: JDBC error generating Subtag iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean mtagHasSubtag(String ID, String tag) throws JspTagException {
		return ! subtagCountByMtag(ID, tag).equals("0");
	}

	public static Boolean subtagExists (String ID, String tag, String code) throws JspTagException {
		int count = 0;
		SubtagIterator theIterator = new SubtagIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from navigation.subtag where 1=1"
						+ " and id = ?"
						+ " and tag = ?"
						+ " and code = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			stat.setString(2,tag);
			stat.setString(3,code);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating Subtag iterator", e);
			throw new JspTagException("Error: JDBC error generating Subtag iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

    public int doStartTag() throws JspException {
		Mtag theMtag = (Mtag)findAncestorWithClass(this, Mtag.class);
		if (theMtag!= null)
			parentEntities.addElement(theMtag);

		if (theMtag == null) {
		} else {
			ID = theMtag.getID();
			tag = theMtag.getTag();
		}


      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (ID == 0 ? "" : " and id = ?")
                                                        + (tag == null ? "" : " and tag = ?")
                                                        +  generateLimitCriteria());
            if (ID != 0) stat.setInt(webapp_keySeq++, ID);
            if (tag != null) stat.setString(webapp_keySeq++, tag);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT navigation.subtag.id, navigation.subtag.tag, navigation.subtag.code from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (ID == 0 ? "" : " and id = ?")
                                                        + (tag == null ? "" : " and tag = ?")
                                                        + " order by " + generateSortCriteria() + generateLimitCriteria());
            if (ID != 0) stat.setInt(webapp_keySeq++, ID);
            if (tag != null) stat.setString(webapp_keySeq++, tag);
            rs = stat.executeQuery();

            if (rs.next()) {
                ID = rs.getInt(1);
                tag = rs.getString(2);
                code = rs.getString(3);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating Subtag iterator: " + stat.toString(), e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating Subtag iterator: " + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating Subtag iterator: " + stat.toString(),e);
			}

        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("navigation.subtag");
      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "id,tag,code";
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
                tag = rs.getString(2);
                code = rs.getString(3);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across Subtag", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error iterating across Subtag" + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error iterating across Subtag",e);
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
            log.error("JDBC error ending Subtag iterator",e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving code " + code);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error ending Subtag iterator",e);
			}

        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        ID = 0;
        tag = null;
        code = null;
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



	public int getID () {
		return ID;
	}

	public void setID (int ID) {
		this.ID = ID;
	}

	public int getActualID () {
		return ID;
	}

	public String getTag () {
		return tag;
	}

	public void setTag (String tag) {
		this.tag = tag;
	}

	public String getActualTag () {
		return tag;
	}

	public String getCode () {
		return code;
	}

	public void setCode (String code) {
		this.code = code;
	}

	public String getActualCode () {
		return code;
	}
}
