package ESTCTagLib.session;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class SessionFinishToNow extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(SessionFinishToNow.class);


	public int doStartTag() throws JspException {
		try {
			Session theSession = (Session)findAncestorWithClass(this, Session.class);
			theSession.setFinishToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing Session for finish tag ", e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing Session for finish tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Session for finish tag ");
			}

		}
		return SKIP_BODY;
	}

	public Date getFinish() throws JspException {
		try {
			Session theSession = (Session)findAncestorWithClass(this, Session.class);
			return theSession.getFinish();
		} catch (Exception e) {

			log.error("Can't find enclosing Session for finish tag ", e);

			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing Session for finish tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Session for finish tag ");
			}

		}
	}

	public void setFinish() throws JspException {
		try {
			Session theSession = (Session)findAncestorWithClass(this, Session.class);
			theSession.setFinishToNow( );
		} catch (Exception e) {

			log.error("Can't find enclosing Session for finish tag ", e);

			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing Session for finish tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Session for finish tag ");
			}

		}
	}
}