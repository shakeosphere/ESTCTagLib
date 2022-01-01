package ESTCTagLib.subtag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class SubtagValue extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(SubtagValue.class);

	public int doStartTag() throws JspException {
		try {
			Subtag theSubtag = (Subtag)findAncestorWithClass(this, Subtag.class);
			if (!theSubtag.commitNeeded) {
				pageContext.getOut().print(theSubtag.getValue());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Subtag for value tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Subtag for value tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Subtag for value tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getValue() throws JspException {
		try {
			Subtag theSubtag = (Subtag)findAncestorWithClass(this, Subtag.class);
			return theSubtag.getValue();
		} catch (Exception e) {
			log.error("Can't find enclosing Subtag for value tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Subtag for value tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Subtag for value tag ");
			}
		}
	}

	public void setValue(String value) throws JspException {
		try {
			Subtag theSubtag = (Subtag)findAncestorWithClass(this, Subtag.class);
			theSubtag.setValue(value);
		} catch (Exception e) {
			log.error("Can't find enclosing Subtag for value tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Subtag for value tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Subtag for value tag ");
			}
		}
	}

}
