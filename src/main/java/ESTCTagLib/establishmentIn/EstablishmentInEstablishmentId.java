package ESTCTagLib.establishmentIn;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class EstablishmentInEstablishmentId extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(EstablishmentInEstablishmentId.class);

	public int doStartTag() throws JspException {
		try {
			EstablishmentIn theEstablishmentIn = (EstablishmentIn)findAncestorWithClass(this, EstablishmentIn.class);
			if (!theEstablishmentIn.commitNeeded) {
				pageContext.getOut().print(theEstablishmentIn.getEstablishmentId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing EstablishmentIn for establishmentId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing EstablishmentIn for establishmentId tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing EstablishmentIn for establishmentId tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getEstablishmentId() throws JspException {
		try {
			EstablishmentIn theEstablishmentIn = (EstablishmentIn)findAncestorWithClass(this, EstablishmentIn.class);
			return theEstablishmentIn.getEstablishmentId();
		} catch (Exception e) {
			log.error("Can't find enclosing EstablishmentIn for establishmentId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing EstablishmentIn for establishmentId tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing EstablishmentIn for establishmentId tag ");
			}
		}
	}

	public void setEstablishmentId(int establishmentId) throws JspException {
		try {
			EstablishmentIn theEstablishmentIn = (EstablishmentIn)findAncestorWithClass(this, EstablishmentIn.class);
			theEstablishmentIn.setEstablishmentId(establishmentId);
		} catch (Exception e) {
			log.error("Can't find enclosing EstablishmentIn for establishmentId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing EstablishmentIn for establishmentId tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing EstablishmentIn for establishmentId tag ");
			}
		}
	}

}
