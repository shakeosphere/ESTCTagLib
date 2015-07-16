package ESTCTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class UserPassword extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(UserPassword.class);

	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getPassword());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for password tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for password tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for password tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getPassword() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getPassword();
		} catch (Exception e) {
			log.error("Can't find enclosing User for password tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for password tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing User for password tag ");
			}
		}
	}

	public void setPassword(String password) throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setPassword(password);
		} catch (Exception e) {
			log.error("Can't find enclosing User for password tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for password tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for password tag ");
			}
		}
	}

}
