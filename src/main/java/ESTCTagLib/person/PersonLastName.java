package ESTCTagLib.person;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PersonLastName extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(PersonLastName.class);

	public int doStartTag() throws JspException {
		try {
			Person thePerson = (Person)findAncestorWithClass(this, Person.class);
			if (!thePerson.commitNeeded) {
				pageContext.getOut().print(thePerson.getLastName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Person for lastName tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Person for lastName tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Person for lastName tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getLastName() throws JspException {
		try {
			Person thePerson = (Person)findAncestorWithClass(this, Person.class);
			return thePerson.getLastName();
		} catch (Exception e) {
			log.error("Can't find enclosing Person for lastName tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Person for lastName tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Person for lastName tag ");
			}
		}
	}

	public void setLastName(String lastName) throws JspException {
		try {
			Person thePerson = (Person)findAncestorWithClass(this, Person.class);
			thePerson.setLastName(lastName);
		} catch (Exception e) {
			log.error("Can't find enclosing Person for lastName tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Person for lastName tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Person for lastName tag ");
			}
		}
	}

}
