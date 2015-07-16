package ESTCTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class UserLastName extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(UserLastName.class);

	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getLastName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for lastName tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for lastName tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for lastName tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getLastName() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getLastName();
		} catch (Exception e) {
			log.error("Can't find enclosing User for lastName tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for lastName tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing User for lastName tag ");
			}
		}
	}

	public void setLastName(String lastName) throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setLastName(lastName);
		} catch (Exception e) {
			log.error("Can't find enclosing User for lastName tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for lastName tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for lastName tag ");
			}
		}
	}

}
