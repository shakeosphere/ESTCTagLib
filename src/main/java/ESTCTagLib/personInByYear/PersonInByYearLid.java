package ESTCTagLib.personInByYear;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PersonInByYearLid extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(PersonInByYearLid.class);

	public int doStartTag() throws JspException {
		try {
			PersonInByYear thePersonInByYear = (PersonInByYear)findAncestorWithClass(this, PersonInByYear.class);
			if (!thePersonInByYear.commitNeeded) {
				pageContext.getOut().print(thePersonInByYear.getLid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonInByYear for lid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonInByYear for lid tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonInByYear for lid tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getLid() throws JspException {
		try {
			PersonInByYear thePersonInByYear = (PersonInByYear)findAncestorWithClass(this, PersonInByYear.class);
			return thePersonInByYear.getLid();
		} catch (Exception e) {
			log.error("Can't find enclosing PersonInByYear for lid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonInByYear for lid tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonInByYear for lid tag ");
			}
		}
	}

	public void setLid(int lid) throws JspException {
		try {
			PersonInByYear thePersonInByYear = (PersonInByYear)findAncestorWithClass(this, PersonInByYear.class);
			thePersonInByYear.setLid(lid);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonInByYear for lid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonInByYear for lid tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonInByYear for lid tag ");
			}
		}
	}

}
