package ESTCTagLib.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Timestamp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;


import ESTCTagLib.ESTCTagLibTagSupport;
import ESTCTagLib.Sequence;

@SuppressWarnings("serial")
public class User extends ESTCTagLibTagSupport {

	static User currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(User.class);

	Vector<ESTCTagLibTagSupport> parentEntities = new Vector<ESTCTagLibTagSupport>();

	int ID = 0;
	String handle = null;
	String password = null;
	boolean isApproved = false;
	boolean isAdmin = false;
	String firstName = null;
	String lastName = null;
	String email = null;
	Timestamp created = null;
	Timestamp lastLogin = null;

	private String var = null;

	private User cachedUser = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			UserIterator theUserIterator = (UserIterator)findAncestorWithClass(this, UserIterator.class);

			if (theUserIterator != null) {
				ID = theUserIterator.getID();
			}

			if (theUserIterator == null && ID == 0) {
				// no ID was provided - the default is to assume that it is a new User and to generate a new ID
				ID = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or ID was provided as an attribute - we need to load a User from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select handle,password,is_approved,is_admin,first_name,last_name,email,created,last_login from navigation.user where id = ?");
				stmt.setInt(1,ID);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (handle == null)
						handle = rs.getString(1);
					if (password == null)
						password = rs.getString(2);
					if (isApproved == false)
						isApproved = rs.getBoolean(3);
					if (isAdmin == false)
						isAdmin = rs.getBoolean(4);
					if (firstName == null)
						firstName = rs.getString(5);
					if (lastName == null)
						lastName = rs.getString(6);
					if (email == null)
						email = rs.getString(7);
					if (created == null)
						created = rs.getTimestamp(8);
					if (lastLogin == null)
						lastLogin = rs.getTimestamp(9);
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
			User currentUser = (User) pageContext.getAttribute("tag_user");
			if(currentUser != null){
				cachedUser = currentUser;
			}
			currentUser = this;
			pageContext.setAttribute((var == null ? "tag_user" : var), currentUser);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedUser != null){
				pageContext.setAttribute((var == null ? "tag_user" : var), this.cachedUser);
			}else{
				pageContext.removeAttribute((var == null ? "tag_user" : var));
				this.cachedUser = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update navigation.user set handle = ?, password = ?, is_approved = ?, is_admin = ?, first_name = ?, last_name = ?, email = ?, created = ?, last_login = ? where id = ? ");
				stmt.setString( 1, handle );
				stmt.setString( 2, password );
				stmt.setBoolean( 3, isApproved );
				stmt.setBoolean( 4, isAdmin );
				stmt.setString( 5, firstName );
				stmt.setString( 6, lastName );
				stmt.setString( 7, email );
				stmt.setTimestamp( 8, created );
				stmt.setTimestamp( 9, lastLogin );
				stmt.setInt(10,ID);
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
			log.debug("generating new User " + ID);
		}

		if (handle == null){
			handle = "";
		}
		if (password == null){
			password = "";
		}
		if (firstName == null){
			firstName = "";
		}
		if (lastName == null){
			lastName = "";
		}
		if (email == null){
			email = "";
		}
		PreparedStatement stmt = getConnection().prepareStatement("insert into navigation.user(id,handle,password,is_approved,is_admin,first_name,last_name,email,created,last_login) values (?,?,?,?,?,?,?,?,?,?)");
		stmt.setInt(1,ID);
		stmt.setString(2,handle);
		stmt.setString(3,password);
		stmt.setBoolean(4,isApproved);
		stmt.setBoolean(5,isAdmin);
		stmt.setString(6,firstName);
		stmt.setString(7,lastName);
		stmt.setString(8,email);
		stmt.setTimestamp(9,created);
		stmt.setTimestamp(10,lastLogin);
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

	public String getHandle () {
		if (commitNeeded)
			return "";
		else
			return handle;
	}

	public void setHandle (String handle) {
		this.handle = handle;
		commitNeeded = true;
	}

	public String getActualHandle () {
		return handle;
	}

	public String getPassword () {
		if (commitNeeded)
			return "";
		else
			return password;
	}

	public void setPassword (String password) {
		this.password = password;
		commitNeeded = true;
	}

	public String getActualPassword () {
		return password;
	}

	public boolean getIsApproved () {
		return isApproved;
	}

	public void setIsApproved (boolean isApproved) {
		this.isApproved = isApproved;
		commitNeeded = true;
	}

	public boolean getActualIsApproved () {
		return isApproved;
	}

	public boolean getIsAdmin () {
		return isAdmin;
	}

	public void setIsAdmin (boolean isAdmin) {
		this.isAdmin = isAdmin;
		commitNeeded = true;
	}

	public boolean getActualIsAdmin () {
		return isAdmin;
	}

	public String getFirstName () {
		if (commitNeeded)
			return "";
		else
			return firstName;
	}

	public void setFirstName (String firstName) {
		this.firstName = firstName;
		commitNeeded = true;
	}

	public String getActualFirstName () {
		return firstName;
	}

	public String getLastName () {
		if (commitNeeded)
			return "";
		else
			return lastName;
	}

	public void setLastName (String lastName) {
		this.lastName = lastName;
		commitNeeded = true;
	}

	public String getActualLastName () {
		return lastName;
	}

	public String getEmail () {
		if (commitNeeded)
			return "";
		else
			return email;
	}

	public void setEmail (String email) {
		this.email = email;
		commitNeeded = true;
	}

	public String getActualEmail () {
		return email;
	}

	public Timestamp getCreated () {
		return created;
	}

	public void setCreated (Timestamp created) {
		this.created = created;
		commitNeeded = true;
	}

	public Timestamp getActualCreated () {
		return created;
	}

	public void setCreatedToNow ( ) {
		this.created = new java.sql.Timestamp(new java.util.Date().getTime());
		commitNeeded = true;
	}

	public Timestamp getLastLogin () {
		return lastLogin;
	}

	public void setLastLogin (Timestamp lastLogin) {
		this.lastLogin = lastLogin;
		commitNeeded = true;
	}

	public Timestamp getActualLastLogin () {
		return lastLogin;
	}

	public void setLastLoginToNow ( ) {
		this.lastLogin = new java.sql.Timestamp(new java.util.Date().getTime());
		commitNeeded = true;
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

	public static String handleValue() throws JspException {
		try {
			return currentInstance.getHandle();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function handleValue()");
		}
	}

	public static String passwordValue() throws JspException {
		try {
			return currentInstance.getPassword();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function passwordValue()");
		}
	}

	public static Boolean isApprovedValue() throws JspException {
		try {
			return currentInstance.getIsApproved();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function isApprovedValue()");
		}
	}

	public static Boolean isAdminValue() throws JspException {
		try {
			return currentInstance.getIsAdmin();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function isAdminValue()");
		}
	}

	public static String firstNameValue() throws JspException {
		try {
			return currentInstance.getFirstName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function firstNameValue()");
		}
	}

	public static String lastNameValue() throws JspException {
		try {
			return currentInstance.getLastName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function lastNameValue()");
		}
	}

	public static String emailValue() throws JspException {
		try {
			return currentInstance.getEmail();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function emailValue()");
		}
	}

	public static Timestamp createdValue() throws JspException {
		try {
			return currentInstance.getCreated();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function createdValue()");
		}
	}

	public static Timestamp lastLoginValue() throws JspException {
		try {
			return currentInstance.getLastLogin();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function lastLoginValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		handle = null;
		password = null;
		isApproved = false;
		isAdmin = false;
		firstName = null;
		lastName = null;
		email = null;
		created = null;
		lastLogin = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<ESTCTagLibTagSupport>();
		this.var = null;

	}

}
