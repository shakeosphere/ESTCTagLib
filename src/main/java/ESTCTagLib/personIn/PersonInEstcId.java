package ESTCTagLib.personIn;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PersonInEstcId extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(PersonInEstcId.class);

	public int doStartTag() throws JspException {
		try {
			PersonIn thePersonIn = (PersonIn)findAncestorWithClass(this, PersonIn.class);
			if (!thePersonIn.commitNeeded) {
				pageContext.getOut().print(thePersonIn.getEstcId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonIn for estcId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonIn for estcId tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonIn for estcId tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getEstcId() throws JspException {
		try {
			PersonIn thePersonIn = (PersonIn)findAncestorWithClass(this, PersonIn.class);
			return thePersonIn.getEstcId();
		} catch (Exception e) {
			log.error("Can't find enclosing PersonIn for estcId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonIn for estcId tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonIn for estcId tag ");
			}
		}
	}

	public void setEstcId(int estcId) throws JspException {
		try {
			PersonIn thePersonIn = (PersonIn)findAncestorWithClass(this, PersonIn.class);
			thePersonIn.setEstcId(estcId);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonIn for estcId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonIn for estcId tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonIn for estcId tag ");
			}
		}
	}

}
