package ESTCTagLib.record;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;


import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.Sequence;

@SuppressWarnings("serial")
public class Record extends ESTCTagLibTagSupport {

	static Record currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(Record.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	int ID = 0;
	String leader = null;
	String c001 = null;
	String c003 = null;
	String c005 = null;
	String c008 = null;
	String c009 = null;

	private String var = null;

	private Record cachedRecord = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			RecordIterator theRecordIterator = (RecordIterator)findAncestorWithClass(this, RecordIterator.class);

			if (theRecordIterator != null) {
				ID = theRecordIterator.getID();
			}

			if (theRecordIterator == null && ID == 0) {
				// no ID was provided - the default is to assume that it is a new Record and to generate a new ID
				ID = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or ID was provided as an attribute - we need to load a Record from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select leader,c001,c003,c005,c008,c009 from navigation.record where id = ?");
				stmt.setInt(1,ID);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (leader == null)
						leader = rs.getString(1);
					if (c001 == null)
						c001 = rs.getString(2);
					if (c003 == null)
						c003 = rs.getString(3);
					if (c005 == null)
						c005 = rs.getString(4);
					if (c008 == null)
						c008 = rs.getString(5);
					if (c009 == null)
						c009 = rs.getString(6);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving ID " + ID, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving ID " + ID);
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error retrieving ID " + ID,e);
			}

		} finally {
			freeConnection();
		}

		if(pageContext != null){
			Record currentRecord = (Record) pageContext.getAttribute("tag_record");
			if(currentRecord != null){
				cachedRecord = currentRecord;
			}
			currentRecord = this;
			pageContext.setAttribute((var == null ? "tag_record" : var), currentRecord);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedRecord != null){
				pageContext.setAttribute((var == null ? "tag_record" : var), this.cachedRecord);
			}else{
				pageContext.removeAttribute((var == null ? "tag_record" : var));
				this.cachedRecord = null;
			}
		}

		try {
			Boolean error = null; // (Boolean) pageContext.getAttribute("tagError");
			if(pageContext != null){
				error = (Boolean) pageContext.getAttribute("tagError");
			}

			if(error != null && error){

				freeConnection();
				clearServiceState();

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
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update navigation.record set leader = ?, c001 = ?, c003 = ?, c005 = ?, c008 = ?, c009 = ? where id = ? ");
				stmt.setString( 1, leader );
				stmt.setString( 2, c001 );
				stmt.setString( 3, c003 );
				stmt.setString( 4, c005 );
				stmt.setString( 5, c008 );
				stmt.setString( 6, c009 );
				stmt.setInt(7,ID);
				stmt.executeUpdate();
				stmt.close();
			}
		} catch (SQLException e) {
			log.error("Error: IOException while writing to the user", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: IOException while writing to the user");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: IOException while writing to the user");
			}

		} finally {
			clearServiceState();
			freeConnection();
		}
		return super.doEndTag();
	}

	public void insertEntity() throws JspException, SQLException {
		if (ID == 0) {
			ID = Sequence.generateID();
			log.debug("generating new Record " + ID);
		}

		if (leader == null){
			leader = "";
		}
		if (c001 == null){
			c001 = "";
		}
		if (c003 == null){
			c003 = "";
		}
		if (c005 == null){
			c005 = "";
		}
		if (c008 == null){
			c008 = "";
		}
		if (c009 == null){
			c009 = "";
		}
		PreparedStatement stmt = getConnection().prepareStatement("insert into navigation.record(id,leader,c001,c003,c005,c008,c009) values (?,?,?,?,?,?,?)");
		stmt.setInt(1,ID);
		stmt.setString(2,leader);
		stmt.setString(3,c001);
		stmt.setString(4,c003);
		stmt.setString(5,c005);
		stmt.setString(6,c008);
		stmt.setString(7,c009);
		stmt.executeUpdate();
		stmt.close();
		freeConnection();
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

	public String getLeader () {
		if (commitNeeded)
			return "";
		else
			return leader;
	}

	public void setLeader (String leader) {
		this.leader = leader;
		commitNeeded = true;
	}

	public String getActualLeader () {
		return leader;
	}

	public String getC001 () {
		if (commitNeeded)
			return "";
		else
			return c001;
	}

	public void setC001 (String c001) {
		this.c001 = c001;
		commitNeeded = true;
	}

	public String getActualC001 () {
		return c001;
	}

	public String getC003 () {
		if (commitNeeded)
			return "";
		else
			return c003;
	}

	public void setC003 (String c003) {
		this.c003 = c003;
		commitNeeded = true;
	}

	public String getActualC003 () {
		return c003;
	}

	public String getC005 () {
		if (commitNeeded)
			return "";
		else
			return c005;
	}

	public void setC005 (String c005) {
		this.c005 = c005;
		commitNeeded = true;
	}

	public String getActualC005 () {
		return c005;
	}

	public String getC008 () {
		if (commitNeeded)
			return "";
		else
			return c008;
	}

	public void setC008 (String c008) {
		this.c008 = c008;
		commitNeeded = true;
	}

	public String getActualC008 () {
		return c008;
	}

	public String getC009 () {
		if (commitNeeded)
			return "";
		else
			return c009;
	}

	public void setC009 (String c009) {
		this.c009 = c009;
		commitNeeded = true;
	}

	public String getActualC009 () {
		return c009;
	}

	public String getVar () {
		return var;
	}

	public void setVar (String var) {
		this.var = var;
	}

	public String getActualVar () {
		return var;
	}

	public static Integer IDValue() throws JspException {
		try {
			return currentInstance.getID();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function IDValue()");
		}
	}

	public static String leaderValue() throws JspException {
		try {
			return currentInstance.getLeader();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function leaderValue()");
		}
	}

	public static String c001Value() throws JspException {
		try {
			return currentInstance.getC001();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function c001Value()");
		}
	}

	public static String c003Value() throws JspException {
		try {
			return currentInstance.getC003();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function c003Value()");
		}
	}

	public static String c005Value() throws JspException {
		try {
			return currentInstance.getC005();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function c005Value()");
		}
	}

	public static String c008Value() throws JspException {
		try {
			return currentInstance.getC008();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function c008Value()");
		}
	}

	public static String c009Value() throws JspException {
		try {
			return currentInstance.getC009();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function c009Value()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		leader = null;
		c001 = null;
		c003 = null;
		c005 = null;
		c008 = null;
		c009 = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
