package ESTCTagLib.person;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PersonGenderFemale extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(PersonGenderFemale.class);

	public int doStartTag() throws JspException {
		try {
			Person thePerson = (Person)findAncestorWithClass(this, Person.class);
			if (!thePerson.commitNeeded) {
				pageContext.getOut().print(thePerson.getGenderFemale());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Person for genderFemale tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Person for genderFemale tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Person for genderFemale tag ");
			}

		}
		return SKIP_BODY;
	}

	public boolean getGenderFemale() throws JspException {
		try {
			Person thePerson = (Person)findAncestorWithClass(this, Person.class);
			return thePerson.getGenderFemale();
		} catch (Exception e) {
			log.error("Can't find enclosing Person for genderFemale tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Person for genderFemale tag ");
				parent.doEndTag();
				return false;
			}else{
				throw new JspTagException("Error: Can't find enclosing Person for genderFemale tag ");
			}
		}
	}

	public void setGenderFemale(boolean genderFemale) throws JspException {
		try {
			Person thePerson = (Person)findAncestorWithClass(this, Person.class);
			thePerson.setGenderFemale(genderFemale);
		} catch (Exception e) {
			log.error("Can't find enclosing Person for genderFemale tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Person for genderFemale tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Person for genderFemale tag ");
			}
		}
	}

}
