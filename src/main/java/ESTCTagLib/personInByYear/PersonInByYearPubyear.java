package ESTCTagLib.personInByYear;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PersonInByYearPubyear extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(PersonInByYearPubyear.class);

	public int doStartTag() throws JspException {
		try {
			PersonInByYear thePersonInByYear = (PersonInByYear)findAncestorWithClass(this, PersonInByYear.class);
			if (!thePersonInByYear.commitNeeded) {
				pageContext.getOut().print(thePersonInByYear.getPubyear());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonInByYear for pubyear tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonInByYear for pubyear tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonInByYear for pubyear tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getPubyear() throws JspException {
		try {
			PersonInByYear thePersonInByYear = (PersonInByYear)findAncestorWithClass(this, PersonInByYear.class);
			return thePersonInByYear.getPubyear();
		} catch (Exception e) {
			log.error("Can't find enclosing PersonInByYear for pubyear tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonInByYear for pubyear tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonInByYear for pubyear tag ");
			}
		}
	}

	public void setPubyear(int pubyear) throws JspException {
		try {
			PersonInByYear thePersonInByYear = (PersonInByYear)findAncestorWithClass(this, PersonInByYear.class);
			thePersonInByYear.setPubyear(pubyear);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonInByYear for pubyear tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonInByYear for pubyear tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonInByYear for pubyear tag ");
			}
		}
	}

}
