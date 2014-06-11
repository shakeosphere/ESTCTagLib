package ESTCTagLib.publication;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PublicationBibLevel extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(PublicationBibLevel.class);

	public int doStartTag() throws JspException {
		try {
			Publication thePublication = (Publication)findAncestorWithClass(this, Publication.class);
			if (!thePublication.commitNeeded) {
				pageContext.getOut().print(thePublication.getBibLevel());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Publication for bibLevel tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Publication for bibLevel tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Publication for bibLevel tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getBibLevel() throws JspException {
		try {
			Publication thePublication = (Publication)findAncestorWithClass(this, Publication.class);
			return thePublication.getBibLevel();
		} catch (Exception e) {
			log.error("Can't find enclosing Publication for bibLevel tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Publication for bibLevel tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Publication for bibLevel tag ");
			}
		}
	}

	public void setBibLevel(String bibLevel) throws JspException {
		try {
			Publication thePublication = (Publication)findAncestorWithClass(this, Publication.class);
			thePublication.setBibLevel(bibLevel);
		} catch (Exception e) {
			log.error("Can't find enclosing Publication for bibLevel tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Publication for bibLevel tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Publication for bibLevel tag ");
			}
		}
	}

}
