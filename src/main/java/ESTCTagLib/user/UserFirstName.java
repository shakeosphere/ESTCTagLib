package ESTCTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class UserFirstName extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(UserFirstName.class);

	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getFirstName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for firstName tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for firstName tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for firstName tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getFirstName() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getFirstName();
		} catch (Exception e) {
			log.error("Can't find enclosing User for firstName tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for firstName tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing User for firstName tag ");
			}
		}
	}

	public void setFirstName(String firstName) throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setFirstName(firstName);
		} catch (Exception e) {
			log.error("Can't find enclosing User for firstName tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for firstName tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for firstName tag ");
			}
		}
	}

}
