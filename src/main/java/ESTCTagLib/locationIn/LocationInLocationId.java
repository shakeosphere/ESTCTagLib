package ESTCTagLib.locationIn;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class LocationInLocationId extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(LocationInLocationId.class);

	public int doStartTag() throws JspException {
		try {
			LocationIn theLocationIn = (LocationIn)findAncestorWithClass(this, LocationIn.class);
			if (!theLocationIn.commitNeeded) {
				pageContext.getOut().print(theLocationIn.getLocationId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing LocationIn for locationId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocationIn for locationId tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing LocationIn for locationId tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getLocationId() throws JspException {
		try {
			LocationIn theLocationIn = (LocationIn)findAncestorWithClass(this, LocationIn.class);
			return theLocationIn.getLocationId();
		} catch (Exception e) {
			log.error("Can't find enclosing LocationIn for locationId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocationIn for locationId tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing LocationIn for locationId tag ");
			}
		}
	}

	public void setLocationId(int locationId) throws JspException {
		try {
			LocationIn theLocationIn = (LocationIn)findAncestorWithClass(this, LocationIn.class);
			theLocationIn.setLocationId(locationId);
		} catch (Exception e) {
			log.error("Can't find enclosing LocationIn for locationId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocationIn for locationId tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing LocationIn for locationId tag ");
			}
		}
	}

}
