package ESTCTagLib.personAtByYear;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PersonAtByYearPubyear extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(PersonAtByYearPubyear.class);

	public int doStartTag() throws JspException {
		try {
			PersonAtByYear thePersonAtByYear = (PersonAtByYear)findAncestorWithClass(this, PersonAtByYear.class);
			if (!thePersonAtByYear.commitNeeded) {
				pageContext.getOut().print(thePersonAtByYear.getPubyear());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAtByYear for pubyear tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAtByYear for pubyear tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAtByYear for pubyear tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getPubyear() throws JspException {
		try {
			PersonAtByYear thePersonAtByYear = (PersonAtByYear)findAncestorWithClass(this, PersonAtByYear.class);
			return thePersonAtByYear.getPubyear();
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAtByYear for pubyear tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAtByYear for pubyear tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAtByYear for pubyear tag ");
			}
		}
	}

	public void setPubyear(int pubyear) throws JspException {
		try {
			PersonAtByYear thePersonAtByYear = (PersonAtByYear)findAncestorWithClass(this, PersonAtByYear.class);
			thePersonAtByYear.setPubyear(pubyear);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAtByYear for pubyear tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAtByYear for pubyear tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAtByYear for pubyear tag ");
			}
		}
	}

}
