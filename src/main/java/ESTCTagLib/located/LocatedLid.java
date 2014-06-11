package ESTCTagLib.located;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class LocatedLid extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(LocatedLid.class);

	public int doStartTag() throws JspException {
		try {
			Located theLocated = (Located)findAncestorWithClass(this, Located.class);
			if (!theLocated.commitNeeded) {
				pageContext.getOut().print(theLocated.getLid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Located for lid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Located for lid tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Located for lid tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getLid() throws JspException {
		try {
			Located theLocated = (Located)findAncestorWithClass(this, Located.class);
			return theLocated.getLid();
		} catch (Exception e) {
			log.error("Can't find enclosing Located for lid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Located for lid tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Located for lid tag ");
			}
		}
	}

	public void setLid(int lid) throws JspException {
		try {
			Located theLocated = (Located)findAncestorWithClass(this, Located.class);
			theLocated.setLid(lid);
		} catch (Exception e) {
			log.error("Can't find enclosing Located for lid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Located for lid tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Located for lid tag ");
			}
		}
	}

}
