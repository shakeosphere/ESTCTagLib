package ESTCTagLib.personAuthority;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PersonAuthorityAlias extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(PersonAuthorityAlias.class);

	public int doStartTag() throws JspException {
		try {
			PersonAuthority thePersonAuthority = (PersonAuthority)findAncestorWithClass(this, PersonAuthority.class);
			if (!thePersonAuthority.commitNeeded) {
				pageContext.getOut().print(thePersonAuthority.getAlias());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAuthority for alias tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAuthority for alias tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAuthority for alias tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getAlias() throws JspException {
		try {
			PersonAuthority thePersonAuthority = (PersonAuthority)findAncestorWithClass(this, PersonAuthority.class);
			return thePersonAuthority.getAlias();
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAuthority for alias tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAuthority for alias tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAuthority for alias tag ");
			}
		}
	}

	public void setAlias(int alias) throws JspException {
		try {
			PersonAuthority thePersonAuthority = (PersonAuthority)findAncestorWithClass(this, PersonAuthority.class);
			thePersonAuthority.setAlias(alias);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAuthority for alias tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAuthority for alias tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAuthority for alias tag ");
			}
		}
	}

}
