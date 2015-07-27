package ESTCTagLib.personAuthority;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PersonAuthorityDefinedToNow extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(PersonAuthorityDefinedToNow.class);


	public int doStartTag() throws JspException {
		try {
			PersonAuthority thePersonAuthority = (PersonAuthority)findAncestorWithClass(this, PersonAuthority.class);
			thePersonAuthority.setDefinedToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing PersonAuthority for defined tag ", e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing PersonAuthority for defined tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAuthority for defined tag ");
			}

		}
		return SKIP_BODY;
	}

	public Date getDefined() throws JspException {
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
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing PersonAuthority for defined tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAuthority for defined tag ");
			}

		}
	}

	public void setDefined() throws JspException {
		try {
			PersonAuthority thePersonAuthority = (PersonAuthority)findAncestorWithClass(this, PersonAuthority.class);
			thePersonAuthority.setDefinedToNow( );
		} catch (Exception e) {

			log.error("Can't find enclosing PersonAuthority for defined tag ", e);

			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing PersonAuthority for defined tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAuthority for defined tag ");
			}

		}
	}
}