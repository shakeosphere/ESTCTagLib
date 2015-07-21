package ESTCTagLib.locatedByYear;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class LocatedByYearPubyear extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(LocatedByYearPubyear.class);

	public int doStartTag() throws JspException {
		try {
			LocatedByYear theLocatedByYear = (LocatedByYear)findAncestorWithClass(this, LocatedByYear.class);
			if (!theLocatedByYear.commitNeeded) {
				pageContext.getOut().print(theLocatedByYear.getPubyear());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing LocatedByYear for pubyear tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocatedByYear for pubyear tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing LocatedByYear for pubyear tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getPubyear() throws JspException {
		try {
			LocatedByYear theLocatedByYear = (LocatedByYear)findAncestorWithClass(this, LocatedByYear.class);
			return theLocatedByYear.getPubyear();
		} catch (Exception e) {
			log.error("Can't find enclosing LocatedByYear for pubyear tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocatedByYear for pubyear tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing LocatedByYear for pubyear tag ");
			}
		}
	}

	public void setPubyear(int pubyear) throws JspException {
		try {
			LocatedByYear theLocatedByYear = (LocatedByYear)findAncestorWithClass(this, LocatedByYear.class);
			theLocatedByYear.setPubyear(pubyear);
		} catch (Exception e) {
			log.error("Can't find enclosing LocatedByYear for pubyear tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocatedByYear for pubyear tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing LocatedByYear for pubyear tag ");
			}
		}
	}

}
