package ESTCTagLib.locationIn;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class LocationInSublocationId extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(LocationInSublocationId.class);

	public int doStartTag() throws JspException {
		try {
			LocationIn theLocationIn = (LocationIn)findAncestorWithClass(this, LocationIn.class);
			if (!theLocationIn.commitNeeded) {
				pageContext.getOut().print(theLocationIn.getSublocationId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing LocationIn for sublocationId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocationIn for sublocationId tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing LocationIn for sublocationId tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getSublocationId() throws JspException {
		try {
			LocationIn theLocationIn = (LocationIn)findAncestorWithClass(this, LocationIn.class);
			return theLocationIn.getSublocationId();
		} catch (Exception e) {
			log.error("Can't find enclosing LocationIn for sublocationId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocationIn for sublocationId tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing LocationIn for sublocationId tag ");
			}
		}
	}

	public void setSublocationId(int sublocationId) throws JspException {
		try {
			LocationIn theLocationIn = (LocationIn)findAncestorWithClass(this, LocationIn.class);
			theLocationIn.setSublocationId(sublocationId);
		} catch (Exception e) {
			log.error("Can't find enclosing LocationIn for sublocationId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocationIn for sublocationId tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing LocationIn for sublocationId tag ");
			}
		}
	}

}
