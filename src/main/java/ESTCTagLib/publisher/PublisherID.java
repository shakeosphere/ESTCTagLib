package ESTCTagLib.publisher;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PublisherID extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(PublisherID.class);

	public int doStartTag() throws JspException {
		try {
			Publisher thePublisher = (Publisher)findAncestorWithClass(this, Publisher.class);
			if (!thePublisher.commitNeeded) {
				pageContext.getOut().print(thePublisher.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Publisher for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Publisher for ID tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Publisher for ID tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getID() throws JspException {
		try {
			Publisher thePublisher = (Publisher)findAncestorWithClass(this, Publisher.class);
			return thePublisher.getID();
		} catch (Exception e) {
			log.error("Can't find enclosing Publisher for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Publisher for ID tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Publisher for ID tag ");
			}
		}
	}

	public void setID(int ID) throws JspException {
		try {
			Publisher thePublisher = (Publisher)findAncestorWithClass(this, Publisher.class);
			thePublisher.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing Publisher for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Publisher for ID tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Publisher for ID tag ");
			}
		}
	}

}
