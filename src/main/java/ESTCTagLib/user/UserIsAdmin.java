package ESTCTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class UserIsAdmin extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(UserIsAdmin.class);

	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getIsAdmin());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for isAdmin tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for isAdmin tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for isAdmin tag ");
			}

		}
		return SKIP_BODY;
	}

	public boolean getIsAdmin() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getIsAdmin();
		} catch (Exception e) {
			log.error("Can't find enclosing User for isAdmin tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for isAdmin tag ");
				parent.doEndTag();
				return false;
			}else{
				throw new JspTagException("Error: Can't find enclosing User for isAdmin tag ");
			}
		}
	}

	public void setIsAdmin(boolean isAdmin) throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setIsAdmin(isAdmin);
		} catch (Exception e) {
			log.error("Can't find enclosing User for isAdmin tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for isAdmin tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for isAdmin tag ");
			}
		}
	}

}
