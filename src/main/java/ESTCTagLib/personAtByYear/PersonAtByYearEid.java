package ESTCTagLib.personAtByYear;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PersonAtByYearEid extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(PersonAtByYearEid.class);

	public int doStartTag() throws JspException {
		try {
			PersonAtByYear thePersonAtByYear = (PersonAtByYear)findAncestorWithClass(this, PersonAtByYear.class);
			if (!thePersonAtByYear.commitNeeded) {
				pageContext.getOut().print(thePersonAtByYear.getEid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAtByYear for eid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAtByYear for eid tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAtByYear for eid tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getEid() throws JspException {
		try {
			PersonAtByYear thePersonAtByYear = (PersonAtByYear)findAncestorWithClass(this, PersonAtByYear.class);
			return thePersonAtByYear.getEid();
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAtByYear for eid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAtByYear for eid tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAtByYear for eid tag ");
			}
		}
	}

	public void setEid(int eid) throws JspException {
		try {
			PersonAtByYear thePersonAtByYear = (PersonAtByYear)findAncestorWithClass(this, PersonAtByYear.class);
			thePersonAtByYear.setEid(eid);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAtByYear for eid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAtByYear for eid tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAtByYear for eid tag ");
			}
		}
	}

}
