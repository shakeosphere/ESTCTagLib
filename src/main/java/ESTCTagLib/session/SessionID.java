package ESTCTagLib.session;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class SessionID extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(SessionID.class);

	public int doStartTag() throws JspException {
		try {
			Session theSession = (Session)findAncestorWithClass(this, Session.class);
			if (!theSession.commitNeeded) {
				pageContext.getOut().print(theSession.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Session for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Session for ID tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Session for ID tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getID() throws JspException {
		try {
			Session theSession = (Session)findAncestorWithClass(this, Session.class);
			return theSession.getID();
		} catch (Exception e) {
			log.error("Can't find enclosing Session for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Session for ID tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Session for ID tag ");
			}
		}
	}

	public void setID(int ID) throws JspException {
		try {
			Session theSession = (Session)findAncestorWithClass(this, Session.class);
			theSession.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing Session for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Session for ID tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Session for ID tag ");
			}
		}
	}

}
