package ESTCTagLib.match;


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
import ESTCTagLib.record.Record;
import ESTCTagLib.gazetteer.Gazetteer;

@SuppressWarnings("serial")
public class MatchIterator extends ESTCTagLibBodyTagSupport {
    String moemlId = null;
    int seqnum = 0;
    int ID = 0;
    String tag = null;
	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(MatchIterator.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

   boolean useRecord = false;
   boolean useGazetteer = false;

	public static String matchCountByRecord(String ID) throws JspTagException {
		int count = 0;
		MatchIterator theIterator = new MatchIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from moeml.match where 1=1"
						+ " and id = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating Match iterator", e);
			throw new JspTagException("Error: JDBC error generating Match iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean recordHasMatch(String ID) throws JspTagException {
		return ! matchCountByRecord(ID).equals("0");
	}

	public static String matchCountByGazetteer(String moemlId) throws JspTagException {
		int count = 0;
		MatchIterator theIterator = new MatchIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from moeml.match where 1=1"
						+ " and moeml_id = ?"
						);

			stat.setString(1,moemlId);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating Match iterator", e);
			throw new JspTagException("Error: JDBC error generating Match iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean gazetteerHasMatch(String moemlId) throws JspTagException {
		return ! matchCountByGazetteer(moemlId).equals("0");
	}

	public static Boolean matchExists (String moemlId, String seqnum, String ID, String tag) throws JspTagException {
		int count = 0;
		MatchIterator theIterator = new MatchIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from moeml.match where 1=1"
						+ " and moeml_id = ?"
						+ " and seqnum = ?"
						+ " and id = ?"
						+ " and tag = ?"
						);

			stat.setString(1,moemlId);
			stat.setInt(2,Integer.parseInt(seqnum));
			stat.setInt(3,Integer.parseInt(ID));
			stat.setString(4,tag);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating Match iterator", e);
			throw new JspTagException("Error: JDBC error generating Match iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

	public static Boolean recordGazetteerExists (String ID, String moemlId) throws JspTagException {
		int count = 0;
		MatchIterator theIterator = new MatchIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from moeml.match where 1=1"
						+ " and id = ?"
						+ " and moeml_id = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			stat.setString(2,moemlId);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating Match iterator", e);
			throw new JspTagException("Error: JDBC error generating Match iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

    public int doStartTag() throws JspException {
		Record theRecord = (Record)findAncestorWithClass(this, Record.class);
		if (theRecord!= null)
			parentEntities.addElement(theRecord);
		Gazetteer theGazetteer = (Gazetteer)findAncestorWithClass(this, Gazetteer.class);
		if (theGazetteer!= null)
			parentEntities.addElement(theGazetteer);

		if (theRecord == null) {
		} else {
			ID = theRecord.getID();
		}
		if (theGazetteer == null) {
		} else {
			moemlId = theGazetteer.getMoemlId();
		}


      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (ID == 0 ? "" : " and id = ?")
                                                        + (moemlId == null ? "" : " and moeml_id = ?")
                                                        + generateLimitCriteria());
            if (ID != 0) stat.setInt(webapp_keySeq++, ID);
            if (moemlId != null) stat.setString(webapp_keySeq++, moemlId);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT moeml.match.moeml_id, moeml.match.seqnum, moeml.match.id, moeml.match.tag from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (ID == 0 ? "" : " and id = ?")
                                                        + (moemlId == null ? "" : " and moeml_id = ?")
                                                        + " order by " + generateSortCriteria()  +  generateLimitCriteria());
            if (ID != 0) stat.setInt(webapp_keySeq++, ID);
            if (moemlId != null) stat.setString(webapp_keySeq++, moemlId);
            rs = stat.executeQuery();

            if ( rs != null && rs.next() ) {
                moemlId = rs.getString(1);
                seqnum = rs.getInt(2);
                ID = rs.getInt(3);
                tag = rs.getString(4);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating Match iterator: " + stat.toString(), e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating Match iterator: " + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating Match iterator: " + stat.toString(),e);
			}

        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("moeml.match");
       if (useRecord)
          theBuffer.append(", moeml.record");
       if (useGazetteer)
          theBuffer.append(", moeml.gazetteer");

      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
       if (useRecord)
          theBuffer.append(" and record.id = match.id");
       if (useGazetteer)
          theBuffer.append(" and gazetteer.moeml_id = match.moeml_id");

      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "moeml_id,seqnum,id,tag";
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
                moemlId = rs.getString(1);
                seqnum = rs.getInt(2);
                ID = rs.getInt(3);
                tag = rs.getString(4);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across Match", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error iterating across Match" + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error iterating across Match",e);
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
            log.error("JDBC error ending Match iterator",e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving seqnum " + seqnum);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error ending Match iterator",e);
			}

        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        moemlId = null;
        seqnum = 0;
        ID = 0;
        tag = null;
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

   public boolean getUseGazetteer() {
        return useGazetteer;
    }

    public void setUseGazetteer(boolean useGazetteer) {
        this.useGazetteer = useGazetteer;
    }



	public String getMoemlId () {
		return moemlId;
	}

	public void setMoemlId (String moemlId) {
		this.moemlId = moemlId;
	}

	public String getActualMoemlId () {
		return moemlId;
	}

	public int getSeqnum () {
		return seqnum;
	}

	public void setSeqnum (int seqnum) {
		this.seqnum = seqnum;
	}

	public int getActualSeqnum () {
		return seqnum;
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
}
