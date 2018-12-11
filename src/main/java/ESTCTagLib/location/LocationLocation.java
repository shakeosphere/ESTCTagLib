package ESTCTagLib.location;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class LocationLocation extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(LocationLocation.class);

	public int doStartTag() throws JspException {
		try {
			Location theLocation = (Location)findAncestorWithClass(this, Location.class);
			if (!theLocation.commitNeeded) {
				pageContext.getOut().print(theLocation.getLocation());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Location for location tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Location for location tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Location for location tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getLocation() throws JspException {
		try {
			Location theLocation = (Location)findAncestorWithClass(this, Location.class);
			return theLocation.getLocation();
		} catch (Exception e) {
			log.error("Can't find enclosing Location for location tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Location for location tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Location for location tag ");
			}
		}
	}

	public void setLocation(String location) throws JspException {
		try {
			Location theLocation = (Location)findAncestorWithClass(this, Location.class);
			theLocation.setLocation(location);
		} catch (Exception e) {
			log.error("Can't find enclosing Location for location tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Location for location tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Location for location tag ");
			}
		}
	}

}
