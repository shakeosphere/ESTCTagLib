package ESTCTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class UserHandle extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(UserHandle.class);

	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getHandle());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for handle tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for handle tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for handle tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getHandle() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getHandle();
		} catch (Exception e) {
			log.error("Can't find enclosing User for handle tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for handle tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing User for handle tag ");
			}
		}
	}

	public void setHandle(String handle) throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setHandle(handle);
		} catch (Exception e) {
			log.error("Can't find enclosing User for handle tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for handle tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for handle tag ");
			}
		}
	}

}
