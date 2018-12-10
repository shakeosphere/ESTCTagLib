package ESTCTagLib.personAt;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PersonAtEstablishmentId extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(PersonAtEstablishmentId.class);

	public int doStartTag() throws JspException {
		try {
			PersonAt thePersonAt = (PersonAt)findAncestorWithClass(this, PersonAt.class);
			if (!thePersonAt.commitNeeded) {
				pageContext.getOut().print(thePersonAt.getEstablishmentId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAt for establishmentId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAt for establishmentId tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAt for establishmentId tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getEstablishmentId() throws JspException {
		try {
			PersonAt thePersonAt = (PersonAt)findAncestorWithClass(this, PersonAt.class);
			return thePersonAt.getEstablishmentId();
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAt for establishmentId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAt for establishmentId tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAt for establishmentId tag ");
			}
		}
	}

	public void setEstablishmentId(int establishmentId) throws JspException {
		try {
			PersonAt thePersonAt = (PersonAt)findAncestorWithClass(this, PersonAt.class);
			thePersonAt.setEstablishmentId(establishmentId);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAt for establishmentId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAt for establishmentId tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAt for establishmentId tag ");
			}
		}
	}

}
