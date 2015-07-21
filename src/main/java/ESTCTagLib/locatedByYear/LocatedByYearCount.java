package ESTCTagLib.locatedByYear;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class LocatedByYearCount extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(LocatedByYearCount.class);

	public int doStartTag() throws JspException {
		try {
			LocatedByYear theLocatedByYear = (LocatedByYear)findAncestorWithClass(this, LocatedByYear.class);
			if (!theLocatedByYear.commitNeeded) {
				pageContext.getOut().print(theLocatedByYear.getCount());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing LocatedByYear for count tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocatedByYear for count tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing LocatedByYear for count tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getCount() throws JspException {
		try {
			LocatedByYear theLocatedByYear = (LocatedByYear)findAncestorWithClass(this, LocatedByYear.class);
			return theLocatedByYear.getCount();
		} catch (Exception e) {
			log.error("Can't find enclosing LocatedByYear for count tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocatedByYear for count tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing LocatedByYear for count tag ");
			}
		}
	}

	public void setCount(int count) throws JspException {
		try {
			LocatedByYear theLocatedByYear = (LocatedByYear)findAncestorWithClass(this, LocatedByYear.class);
			theLocatedByYear.setCount(count);
		} catch (Exception e) {
			log.error("Can't find enclosing LocatedByYear for count tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocatedByYear for count tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing LocatedByYear for count tag ");
			}
		}
	}

}
