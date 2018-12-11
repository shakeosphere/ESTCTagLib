package ESTCTagLib.locationIn;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class LocationInLocational extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(LocationInLocational.class);

	public int doStartTag() throws JspException {
		try {
			LocationIn theLocationIn = (LocationIn)findAncestorWithClass(this, LocationIn.class);
			if (!theLocationIn.commitNeeded) {
				pageContext.getOut().print(theLocationIn.getLocational());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing LocationIn for locational tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocationIn for locational tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing LocationIn for locational tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getLocational() throws JspException {
		try {
			LocationIn theLocationIn = (LocationIn)findAncestorWithClass(this, LocationIn.class);
			return theLocationIn.getLocational();
		} catch (Exception e) {
			log.error("Can't find enclosing LocationIn for locational tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocationIn for locational tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing LocationIn for locational tag ");
			}
		}
	}

	public void setLocational(String locational) throws JspException {
		try {
			LocationIn theLocationIn = (LocationIn)findAncestorWithClass(this, LocationIn.class);
			theLocationIn.setLocational(locational);
		} catch (Exception e) {
			log.error("Can't find enclosing LocationIn for locational tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocationIn for locational tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing LocationIn for locational tag ");
			}
		}
	}

}
