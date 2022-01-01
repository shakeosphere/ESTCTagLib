package ESTCTagLib.subtag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class SubtagTag extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(SubtagTag.class);

	public int doStartTag() throws JspException {
		try {
			Subtag theSubtag = (Subtag)findAncestorWithClass(this, Subtag.class);
			if (!theSubtag.commitNeeded) {
				pageContext.getOut().print(theSubtag.getTag());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Subtag for tag tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Subtag for tag tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Subtag for tag tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getTag() throws JspException {
		try {
			Subtag theSubtag = (Subtag)findAncestorWithClass(this, Subtag.class);
			return theSubtag.getTag();
		} catch (Exception e) {
			log.error("Can't find enclosing Subtag for tag tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Subtag for tag tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Subtag for tag tag ");
			}
		}
	}

	public void setTag(String tag) throws JspException {
		try {
			Subtag theSubtag = (Subtag)findAncestorWithClass(this, Subtag.class);
			theSubtag.setTag(tag);
		} catch (Exception e) {
			log.error("Can't find enclosing Subtag for tag tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Subtag for tag tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Subtag for tag tag ");
			}
		}
	}

}
