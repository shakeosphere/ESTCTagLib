package ESTCTagLib.personInByYear;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PersonInByYearCount extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(PersonInByYearCount.class);

	public int doStartTag() throws JspException {
		try {
			PersonInByYear thePersonInByYear = (PersonInByYear)findAncestorWithClass(this, PersonInByYear.class);
			if (!thePersonInByYear.commitNeeded) {
				pageContext.getOut().print(thePersonInByYear.getCount());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonInByYear for count tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonInByYear for count tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonInByYear for count tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getCount() throws JspException {
		try {
			PersonInByYear thePersonInByYear = (PersonInByYear)findAncestorWithClass(this, PersonInByYear.class);
			return thePersonInByYear.getCount();
		} catch (Exception e) {
			log.error("Can't find enclosing PersonInByYear for count tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonInByYear for count tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonInByYear for count tag ");
			}
		}
	}

	public void setCount(int count) throws JspException {
		try {
			PersonInByYear thePersonInByYear = (PersonInByYear)findAncestorWithClass(this, PersonInByYear.class);
			thePersonInByYear.setCount(count);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonInByYear for count tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonInByYear for count tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonInByYear for count tag ");
			}
		}
	}

}
