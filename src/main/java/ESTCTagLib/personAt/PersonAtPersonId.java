package ESTCTagLib.personAt;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PersonAtPersonId extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(PersonAtPersonId.class);

	public int doStartTag() throws JspException {
		try {
			PersonAt thePersonAt = (PersonAt)findAncestorWithClass(this, PersonAt.class);
			if (!thePersonAt.commitNeeded) {
				pageContext.getOut().print(thePersonAt.getPersonId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAt for personId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAt for personId tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAt for personId tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getPersonId() throws JspException {
		try {
			PersonAt thePersonAt = (PersonAt)findAncestorWithClass(this, PersonAt.class);
			return thePersonAt.getPersonId();
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAt for personId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAt for personId tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAt for personId tag ");
			}
		}
	}

	public void setPersonId(int personId) throws JspException {
		try {
			PersonAt thePersonAt = (PersonAt)findAncestorWithClass(this, PersonAt.class);
			thePersonAt.setPersonId(personId);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAt for personId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAt for personId tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAt for personId tag ");
			}
		}
	}

}
