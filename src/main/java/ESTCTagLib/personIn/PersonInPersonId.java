package ESTCTagLib.personIn;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PersonInPersonId extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(PersonInPersonId.class);

	public int doStartTag() throws JspException {
		try {
			PersonIn thePersonIn = (PersonIn)findAncestorWithClass(this, PersonIn.class);
			if (!thePersonIn.commitNeeded) {
				pageContext.getOut().print(thePersonIn.getPersonId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonIn for personId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonIn for personId tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonIn for personId tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getPersonId() throws JspException {
		try {
			PersonIn thePersonIn = (PersonIn)findAncestorWithClass(this, PersonIn.class);
			return thePersonIn.getPersonId();
		} catch (Exception e) {
			log.error("Can't find enclosing PersonIn for personId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonIn for personId tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonIn for personId tag ");
			}
		}
	}

	public void setPersonId(int personId) throws JspException {
		try {
			PersonIn thePersonIn = (PersonIn)findAncestorWithClass(this, PersonIn.class);
			thePersonIn.setPersonId(personId);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonIn for personId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonIn for personId tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonIn for personId tag ");
			}
		}
	}

}
