package ESTCTagLib.establishment;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class EstablishmentEid extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(EstablishmentEid.class);

	public int doStartTag() throws JspException {
		try {
			Establishment theEstablishment = (Establishment)findAncestorWithClass(this, Establishment.class);
			if (!theEstablishment.commitNeeded) {
				pageContext.getOut().print(theEstablishment.getEid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Establishment for eid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Establishment for eid tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Establishment for eid tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getEid() throws JspException {
		try {
			Establishment theEstablishment = (Establishment)findAncestorWithClass(this, Establishment.class);
			return theEstablishment.getEid();
		} catch (Exception e) {
			log.error("Can't find enclosing Establishment for eid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Establishment for eid tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Establishment for eid tag ");
			}
		}
	}

	public void setEid(int eid) throws JspException {
		try {
			Establishment theEstablishment = (Establishment)findAncestorWithClass(this, Establishment.class);
			theEstablishment.setEid(eid);
		} catch (Exception e) {
			log.error("Can't find enclosing Establishment for eid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Establishment for eid tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Establishment for eid tag ");
			}
		}
	}

}
