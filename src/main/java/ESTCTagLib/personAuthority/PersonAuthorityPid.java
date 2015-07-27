package ESTCTagLib.personAuthority;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PersonAuthorityPid extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(PersonAuthorityPid.class);

	public int doStartTag() throws JspException {
		try {
			PersonAuthority thePersonAuthority = (PersonAuthority)findAncestorWithClass(this, PersonAuthority.class);
			if (!thePersonAuthority.commitNeeded) {
				pageContext.getOut().print(thePersonAuthority.getPid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAuthority for pid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAuthority for pid tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAuthority for pid tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getPid() throws JspException {
		try {
			PersonAuthority thePersonAuthority = (PersonAuthority)findAncestorWithClass(this, PersonAuthority.class);
			return thePersonAuthority.getPid();
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAuthority for pid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAuthority for pid tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAuthority for pid tag ");
			}
		}
	}

	public void setPid(int pid) throws JspException {
		try {
			PersonAuthority thePersonAuthority = (PersonAuthority)findAncestorWithClass(this, PersonAuthority.class);
			thePersonAuthority.setPid(pid);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAuthority for pid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAuthority for pid tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAuthority for pid tag ");
			}
		}
	}

}
