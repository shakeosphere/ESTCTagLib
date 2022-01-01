package ESTCTagLib.subtag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class SubtagID extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(SubtagID.class);

	public int doStartTag() throws JspException {
		try {
			Subtag theSubtag = (Subtag)findAncestorWithClass(this, Subtag.class);
			if (!theSubtag.commitNeeded) {
				pageContext.getOut().print(theSubtag.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Subtag for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Subtag for ID tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Subtag for ID tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getID() throws JspException {
		try {
			Subtag theSubtag = (Subtag)findAncestorWithClass(this, Subtag.class);
			return theSubtag.getID();
		} catch (Exception e) {
			log.error("Can't find enclosing Subtag for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Subtag for ID tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Subtag for ID tag ");
			}
		}
	}

	public void setID(int ID) throws JspException {
		try {
			Subtag theSubtag = (Subtag)findAncestorWithClass(this, Subtag.class);
			theSubtag.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing Subtag for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Subtag for ID tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Subtag for ID tag ");
			}
		}
	}

}
