package ESTCTagLib.personAtByYear;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PersonAtByYearCount extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(PersonAtByYearCount.class);

	public int doStartTag() throws JspException {
		try {
			PersonAtByYear thePersonAtByYear = (PersonAtByYear)findAncestorWithClass(this, PersonAtByYear.class);
			if (!thePersonAtByYear.commitNeeded) {
				pageContext.getOut().print(thePersonAtByYear.getCount());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAtByYear for count tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAtByYear for count tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAtByYear for count tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getCount() throws JspException {
		try {
			PersonAtByYear thePersonAtByYear = (PersonAtByYear)findAncestorWithClass(this, PersonAtByYear.class);
			return thePersonAtByYear.getCount();
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAtByYear for count tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAtByYear for count tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAtByYear for count tag ");
			}
		}
	}

	public void setCount(int count) throws JspException {
		try {
			PersonAtByYear thePersonAtByYear = (PersonAtByYear)findAncestorWithClass(this, PersonAtByYear.class);
			thePersonAtByYear.setCount(count);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonAtByYear for count tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonAtByYear for count tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonAtByYear for count tag ");
			}
		}
	}

}
