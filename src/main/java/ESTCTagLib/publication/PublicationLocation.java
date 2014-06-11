package ESTCTagLib.publication;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PublicationLocation extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(PublicationLocation.class);

	public int doStartTag() throws JspException {
		try {
			Publication thePublication = (Publication)findAncestorWithClass(this, Publication.class);
			if (!thePublication.commitNeeded) {
				pageContext.getOut().print(thePublication.getLocation());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Publication for location tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Publication for location tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Publication for location tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getLocation() throws JspException {
		try {
			Publication thePublication = (Publication)findAncestorWithClass(this, Publication.class);
			return thePublication.getLocation();
		} catch (Exception e) {
			log.error("Can't find enclosing Publication for location tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Publication for location tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Publication for location tag ");
			}
		}
	}

	public void setLocation(String location) throws JspException {
		try {
			Publication thePublication = (Publication)findAncestorWithClass(this, Publication.class);
			thePublication.setLocation(location);
		} catch (Exception e) {
			log.error("Can't find enclosing Publication for location tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Publication for location tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Publication for location tag ");
			}
		}
	}

}
