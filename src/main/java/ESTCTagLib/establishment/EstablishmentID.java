package ESTCTagLib.establishment;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class EstablishmentID extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(EstablishmentID.class);

	public int doStartTag() throws JspException {
		try {
			Establishment theEstablishment = (Establishment)findAncestorWithClass(this, Establishment.class);
			if (!theEstablishment.commitNeeded) {
				pageContext.getOut().print(theEstablishment.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Establishment for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Establishment for ID tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Establishment for ID tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getID() throws JspException {
		try {
			Establishment theEstablishment = (Establishment)findAncestorWithClass(this, Establishment.class);
			return theEstablishment.getID();
		} catch (Exception e) {
			log.error("Can't find enclosing Establishment for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Establishment for ID tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Establishment for ID tag ");
			}
		}
	}

	public void setID(int ID) throws JspException {
		try {
			Establishment theEstablishment = (Establishment)findAncestorWithClass(this, Establishment.class);
			theEstablishment.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing Establishment for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Establishment for ID tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Establishment for ID tag ");
			}
		}
	}

}
