package ESTCTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class UserCreatedToNow extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(UserCreatedToNow.class);


	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setCreatedToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing User for created tag ", e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing User for created tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for created tag ");
			}

		}
		return SKIP_BODY;
	}

	public Date getCreated() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getCreated();
		} catch (Exception e) {

			log.error("Can't find enclosing User for created tag ", e);

			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing User for created tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing User for created tag ");
			}

		}
	}

	public void setCreated() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setCreatedToNow( );
		} catch (Exception e) {

			log.error("Can't find enclosing User for created tag ", e);

			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing User for created tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for created tag ");
			}

		}
	}
}