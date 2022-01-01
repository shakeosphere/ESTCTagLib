package ESTCTagLib.personAuthority;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import java.sql.Timestamp;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PersonAuthorityDefined extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(PersonAuthorityDefined.class);

	public int doStartTag() throws JspException {
		try {
			PersonAuthority thePersonAuthority = (PersonAuthority)findAncestorWithClass(this, PersonAuthority.class);
			if (!thePersonAuthority.commitNeeded) {
				pageContext.getOut().print(thePersonAuthority.getDefined());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAuthority for defined tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAuthority for defined tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAuthority for defined tag ");
			}

		}
		return SKIP_BODY;
	}

	public Timestamp getDefined() throws JspException {
		try {
			PersonAuthority thePersonAuthority = (PersonAuthority)findAncestorWithClass(this, PersonAuthority.class);
			return thePersonAuthority.getDefined();
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAuthority for defined tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAuthority for defined tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAuthority for defined tag ");
			}
		}
	}

	public void setDefined(Timestamp defined) throws JspException {
		try {
			PersonAuthority thePersonAuthority = (PersonAuthority)findAncestorWithClass(this, PersonAuthority.class);
			thePersonAuthority.setDefined(defined);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAuthority for defined tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAuthority for defined tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAuthority for defined tag ");
			}
		}
	}

}
