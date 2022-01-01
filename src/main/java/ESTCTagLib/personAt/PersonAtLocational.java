package ESTCTagLib.personAt;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PersonAtLocational extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(PersonAtLocational.class);

	public int doStartTag() throws JspException {
		try {
			PersonAt thePersonAt = (PersonAt)findAncestorWithClass(this, PersonAt.class);
			if (!thePersonAt.commitNeeded) {
				pageContext.getOut().print(thePersonAt.getLocational());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAt for locational tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAt for locational tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAt for locational tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getLocational() throws JspException {
		try {
			PersonAt thePersonAt = (PersonAt)findAncestorWithClass(this, PersonAt.class);
			return thePersonAt.getLocational();
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAt for locational tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAt for locational tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAt for locational tag ");
			}
		}
	}

	public void setLocational(String locational) throws JspException {
		try {
			PersonAt thePersonAt = (PersonAt)findAncestorWithClass(this, PersonAt.class);
			thePersonAt.setLocational(locational);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAt for locational tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAt for locational tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAt for locational tag ");
			}
		}
	}

}
