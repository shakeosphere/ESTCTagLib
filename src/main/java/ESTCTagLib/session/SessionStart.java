package ESTCTagLib.session;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import java.sql.Timestamp;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class SessionStart extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(SessionStart.class);

	public int doStartTag() throws JspException {
		try {
			Session theSession = (Session)findAncestorWithClass(this, Session.class);
			if (!theSession.commitNeeded) {
				pageContext.getOut().print(theSession.getStart());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Session for start tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Session for start tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Session for start tag ");
			}

		}
		return SKIP_BODY;
	}

	public Timestamp getStart() throws JspException {
		try {
			Session theSession = (Session)findAncestorWithClass(this, Session.class);
			return theSession.getStart();
		} catch (Exception e) {
			log.error("Can't find enclosing Session for start tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Session for start tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Session for start tag ");
			}
		}
	}

	public void setStart(Timestamp start) throws JspException {
		try {
			Session theSession = (Session)findAncestorWithClass(this, Session.class);
			theSession.setStart(start);
		} catch (Exception e) {
			log.error("Can't find enclosing Session for start tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Session for start tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Session for start tag ");
			}
		}
	}

}
