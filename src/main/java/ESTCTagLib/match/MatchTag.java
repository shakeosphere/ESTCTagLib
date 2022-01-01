package ESTCTagLib.match;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class MatchTag extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(MatchTag.class);

	public int doStartTag() throws JspException {
		try {
			Match theMatch = (Match)findAncestorWithClass(this, Match.class);
			if (!theMatch.commitNeeded) {
				pageContext.getOut().print(theMatch.getTag());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Match for tag tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Match for tag tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Match for tag tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getTag() throws JspException {
		try {
			Match theMatch = (Match)findAncestorWithClass(this, Match.class);
			return theMatch.getTag();
		} catch (Exception e) {
			log.error("Can't find enclosing Match for tag tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Match for tag tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Match for tag tag ");
			}
		}
	}

	public void setTag(String tag) throws JspException {
		try {
			Match theMatch = (Match)findAncestorWithClass(this, Match.class);
			theMatch.setTag(tag);
		} catch (Exception e) {
			log.error("Can't find enclosing Match for tag tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Match for tag tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Match for tag tag ");
			}
		}
	}

}
