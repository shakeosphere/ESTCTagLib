package ESTCTagLib.personAt;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PersonAtEstcId extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(PersonAtEstcId.class);

	public int doStartTag() throws JspException {
		try {
			PersonAt thePersonAt = (PersonAt)findAncestorWithClass(this, PersonAt.class);
			if (!thePersonAt.commitNeeded) {
				pageContext.getOut().print(thePersonAt.getEstcId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAt for estcId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAt for estcId tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAt for estcId tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getEstcId() throws JspException {
		try {
			PersonAt thePersonAt = (PersonAt)findAncestorWithClass(this, PersonAt.class);
			return thePersonAt.getEstcId();
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAt for estcId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAt for estcId tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAt for estcId tag ");
			}
		}
	}

	public void setEstcId(int estcId) throws JspException {
		try {
			PersonAt thePersonAt = (PersonAt)findAncestorWithClass(this, PersonAt.class);
			thePersonAt.setEstcId(estcId);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAt for estcId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAt for estcId tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAt for estcId tag ");
			}
		}
	}

}
