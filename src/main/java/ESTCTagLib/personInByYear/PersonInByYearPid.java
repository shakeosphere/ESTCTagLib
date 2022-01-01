package ESTCTagLib.personInByYear;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PersonInByYearPid extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(PersonInByYearPid.class);

	public int doStartTag() throws JspException {
		try {
			PersonInByYear thePersonInByYear = (PersonInByYear)findAncestorWithClass(this, PersonInByYear.class);
			if (!thePersonInByYear.commitNeeded) {
				pageContext.getOut().print(thePersonInByYear.getPid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonInByYear for pid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonInByYear for pid tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonInByYear for pid tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getPid() throws JspException {
		try {
			PersonInByYear thePersonInByYear = (PersonInByYear)findAncestorWithClass(this, PersonInByYear.class);
			return thePersonInByYear.getPid();
		} catch (Exception e) {
			log.error("Can't find enclosing PersonInByYear for pid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonInByYear for pid tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonInByYear for pid tag ");
			}
		}
	}

	public void setPid(int pid) throws JspException {
		try {
			PersonInByYear thePersonInByYear = (PersonInByYear)findAncestorWithClass(this, PersonInByYear.class);
			thePersonInByYear.setPid(pid);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonInByYear for pid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing PersonInByYear for pid tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing PersonInByYear for pid tag ");
			}
		}
	}

}
