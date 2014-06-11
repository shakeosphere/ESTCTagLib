package ESTCTagLib.location;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class LocationLabel extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(LocationLabel.class);

	public int doStartTag() throws JspException {
		try {
			Location theLocation = (Location)findAncestorWithClass(this, Location.class);
			if (!theLocation.commitNeeded) {
				pageContext.getOut().print(theLocation.getLabel());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Location for label tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Location for label tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Location for label tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getLabel() throws JspException {
		try {
			Location theLocation = (Location)findAncestorWithClass(this, Location.class);
			return theLocation.getLabel();
		} catch (Exception e) {
			log.error("Can't find enclosing Location for label tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Location for label tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Location for label tag ");
			}
		}
	}

	public void setLabel(String label) throws JspException {
		try {
			Location theLocation = (Location)findAncestorWithClass(this, Location.class);
			theLocation.setLabel(label);
		} catch (Exception e) {
			log.error("Can't find enclosing Location for label tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Location for label tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Location for label tag ");
			}
		}
	}

}
