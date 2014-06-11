package ESTCTagLib.publication;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PublicationPublisher extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(PublicationPublisher.class);

	public int doStartTag() throws JspException {
		try {
			Publication thePublication = (Publication)findAncestorWithClass(this, Publication.class);
			if (!thePublication.commitNeeded) {
				pageContext.getOut().print(thePublication.getPublisher());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Publication for publisher tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Publication for publisher tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Publication for publisher tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getPublisher() throws JspException {
		try {
			Publication thePublication = (Publication)findAncestorWithClass(this, Publication.class);
			return thePublication.getPublisher();
		} catch (Exception e) {
			log.error("Can't find enclosing Publication for publisher tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Publication for publisher tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Publication for publisher tag ");
			}
		}
	}

	public void setPublisher(String publisher) throws JspException {
		try {
			Publication thePublication = (Publication)findAncestorWithClass(this, Publication.class);
			thePublication.setPublisher(publisher);
		} catch (Exception e) {
			log.error("Can't find enclosing Publication for publisher tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Publication for publisher tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Publication for publisher tag ");
			}
		}
	}

}
