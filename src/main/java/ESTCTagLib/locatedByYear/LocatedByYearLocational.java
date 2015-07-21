package ESTCTagLib.locatedByYear;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class LocatedByYearLocational extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(LocatedByYearLocational.class);

	public int doStartTag() throws JspException {
		try {
			LocatedByYear theLocatedByYear = (LocatedByYear)findAncestorWithClass(this, LocatedByYear.class);
			if (!theLocatedByYear.commitNeeded) {
				pageContext.getOut().print(theLocatedByYear.getLocational());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing LocatedByYear for locational tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocatedByYear for locational tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing LocatedByYear for locational tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getLocational() throws JspException {
		try {
			LocatedByYear theLocatedByYear = (LocatedByYear)findAncestorWithClass(this, LocatedByYear.class);
			return theLocatedByYear.getLocational();
		} catch (Exception e) {
			log.error("Can't find enclosing LocatedByYear for locational tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocatedByYear for locational tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing LocatedByYear for locational tag ");
			}
		}
	}

	public void setLocational(String locational) throws JspException {
		try {
			LocatedByYear theLocatedByYear = (LocatedByYear)findAncestorWithClass(this, LocatedByYear.class);
			theLocatedByYear.setLocational(locational);
		} catch (Exception e) {
			log.error("Can't find enclosing LocatedByYear for locational tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocatedByYear for locational tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing LocatedByYear for locational tag ");
			}
		}
	}

}
