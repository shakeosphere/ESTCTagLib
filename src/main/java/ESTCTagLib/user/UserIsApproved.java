package ESTCTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class UserIsApproved extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(UserIsApproved.class);

	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getIsApproved());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for isApproved tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for isApproved tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for isApproved tag ");
			}

		}
		return SKIP_BODY;
	}

	public boolean getIsApproved() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getIsApproved();
		} catch (Exception e) {
			log.error("Can't find enclosing User for isApproved tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for isApproved tag ");
				parent.doEndTag();
				return false;
			}else{
				throw new JspTagException("Error: Can't find enclosing User for isApproved tag ");
			}
		}
	}

	public void setIsApproved(boolean isApproved) throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setIsApproved(isApproved);
		} catch (Exception e) {
			log.error("Can't find enclosing User for isApproved tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for isApproved tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for isApproved tag ");
			}
		}
	}

}
