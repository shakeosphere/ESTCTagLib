package ESTCTagLib.establishment;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class EstablishmentEstablishment extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(EstablishmentEstablishment.class);

	public int doStartTag() throws JspException {
		try {
			Establishment theEstablishment = (Establishment)findAncestorWithClass(this, Establishment.class);
			if (!theEstablishment.commitNeeded) {
				pageContext.getOut().print(theEstablishment.getEstablishment());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Establishment for establishment tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Establishment for establishment tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Establishment for establishment tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getEstablishment() throws JspException {
		try {
			Establishment theEstablishment = (Establishment)findAncestorWithClass(this, Establishment.class);
			return theEstablishment.getEstablishment();
		} catch (Exception e) {
			log.error("Can't find enclosing Establishment for establishment tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Establishment for establishment tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Establishment for establishment tag ");
			}
		}
	}

	public void setEstablishment(String establishment) throws JspException {
		try {
			Establishment theEstablishment = (Establishment)findAncestorWithClass(this, Establishment.class);
			theEstablishment.setEstablishment(establishment);
		} catch (Exception e) {
			log.error("Can't find enclosing Establishment for establishment tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Establishment for establishment tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Establishment for establishment tag ");
			}
		}
	}

}
