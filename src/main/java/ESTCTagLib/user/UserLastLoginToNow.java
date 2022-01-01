package ESTCTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.sql.Timestamp;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class UserLastLoginToNow extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(UserLastLoginToNow.class);


	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setLastLoginToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing User for lastLogin tag ", e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing User for lastLogin tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for lastLogin tag ");
			}

		}
		return SKIP_BODY;
	}

	public Timestamp getLastLogin() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getLastLogin();
		} catch (Exception e) {

			log.error("Can't find enclosing User for lastLogin tag ", e);

			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing User for lastLogin tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing User for lastLogin tag ");
			}

		}
	}

	public void setLastLogin() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setLastLoginToNow( );
		} catch (Exception e) {

			log.error("Can't find enclosing User for lastLogin tag ", e);

			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing User for lastLogin tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for lastLogin tag ");
			}

		}
	}
}