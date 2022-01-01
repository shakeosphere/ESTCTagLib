package ESTCTagLib.subtag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class SubtagCode extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(SubtagCode.class);

	public int doStartTag() throws JspException {
		try {
			Subtag theSubtag = (Subtag)findAncestorWithClass(this, Subtag.class);
			if (!theSubtag.commitNeeded) {
				pageContext.getOut().print(theSubtag.getCode());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Subtag for code tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Subtag for code tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Subtag for code tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getCode() throws JspException {
		try {
			Subtag theSubtag = (Subtag)findAncestorWithClass(this, Subtag.class);
			return theSubtag.getCode();
		} catch (Exception e) {
			log.error("Can't find enclosing Subtag for code tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Subtag for code tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Subtag for code tag ");
			}
		}
	}

	public void setCode(String code) throws JspException {
		try {
			Subtag theSubtag = (Subtag)findAncestorWithClass(this, Subtag.class);
			theSubtag.setCode(code);
		} catch (Exception e) {
			log.error("Can't find enclosing Subtag for code tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Subtag for code tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Subtag for code tag ");
			}
		}
	}

}
