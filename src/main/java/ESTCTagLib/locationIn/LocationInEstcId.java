package ESTCTagLib.locationIn;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class LocationInEstcId extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(LocationInEstcId.class);

	public int doStartTag() throws JspException {
		try {
			LocationIn theLocationIn = (LocationIn)findAncestorWithClass(this, LocationIn.class);
			if (!theLocationIn.commitNeeded) {
				pageContext.getOut().print(theLocationIn.getEstcId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing LocationIn for estcId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocationIn for estcId tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing LocationIn for estcId tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getEstcId() throws JspException {
		try {
			LocationIn theLocationIn = (LocationIn)findAncestorWithClass(this, LocationIn.class);
			return theLocationIn.getEstcId();
		} catch (Exception e) {
			log.error("Can't find enclosing LocationIn for estcId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocationIn for estcId tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing LocationIn for estcId tag ");
			}
		}
	}

	public void setEstcId(int estcId) throws JspException {
		try {
			LocationIn theLocationIn = (LocationIn)findAncestorWithClass(this, LocationIn.class);
			theLocationIn.setEstcId(estcId);
		} catch (Exception e) {
			log.error("Can't find enclosing LocationIn for estcId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing LocationIn for estcId tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing LocationIn for estcId tag ");
			}
		}
	}

}
