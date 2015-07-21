package ESTCTagLib.locatedByYear;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class LocatedByYearPid extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(LocatedByYearPid.class);

	public int doStartTag() throws JspException {
		try {
			LocatedByYear theLocatedByYear = (LocatedByYear)findAncestorWithClass(this, LocatedByYear.class);
			if (!theLocatedByYear.commitNeeded) {
				pageContext.getOut().print(theLocatedByYear.getPid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing LocatedByYear for pid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocatedByYear for pid tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing LocatedByYear for pid tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getPid() throws JspException {
		try {
			LocatedByYear theLocatedByYear = (LocatedByYear)findAncestorWithClass(this, LocatedByYear.class);
			return theLocatedByYear.getPid();
		} catch (Exception e) {
			log.error("Can't find enclosing LocatedByYear for pid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocatedByYear for pid tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing LocatedByYear for pid tag ");
			}
		}
	}

	public void setPid(int pid) throws JspException {
		try {
			LocatedByYear theLocatedByYear = (LocatedByYear)findAncestorWithClass(this, LocatedByYear.class);
			theLocatedByYear.setPid(pid);
		} catch (Exception e) {
			log.error("Can't find enclosing LocatedByYear for pid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocatedByYear for pid tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing LocatedByYear for pid tag ");
			}
		}
	}

}
