package ESTCTagLib.publication;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PublicationGac extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(PublicationGac.class);

	public int doStartTag() throws JspException {
		try {
			Publication thePublication = (Publication)findAncestorWithClass(this, Publication.class);
			if (!thePublication.commitNeeded) {
				pageContext.getOut().print(thePublication.getGac());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Publication for gac tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Publication for gac tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Publication for gac tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getGac() throws JspException {
		try {
			Publication thePublication = (Publication)findAncestorWithClass(this, Publication.class);
			return thePublication.getGac();
		} catch (Exception e) {
			log.error("Can't find enclosing Publication for gac tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Publication for gac tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Publication for gac tag ");
			}
		}
	}

	public void setGac(String gac) throws JspException {
		try {
			Publication thePublication = (Publication)findAncestorWithClass(this, Publication.class);
			thePublication.setGac(gac);
		} catch (Exception e) {
			log.error("Can't find enclosing Publication for gac tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Publication for gac tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Publication for gac tag ");
			}
		}
	}

}
