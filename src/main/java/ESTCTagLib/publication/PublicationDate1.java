package ESTCTagLib.publication;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PublicationDate1 extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(PublicationDate1.class);

	public int doStartTag() throws JspException {
		try {
			Publication thePublication = (Publication)findAncestorWithClass(this, Publication.class);
			if (!thePublication.commitNeeded) {
				pageContext.getOut().print(thePublication.getDate1());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Publication for date1 tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Publication for date1 tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Publication for date1 tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getDate1() throws JspException {
		try {
			Publication thePublication = (Publication)findAncestorWithClass(this, Publication.class);
			return thePublication.getDate1();
		} catch (Exception e) {
			log.error("Can't find enclosing Publication for date1 tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Publication for date1 tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Publication for date1 tag ");
			}
		}
	}

	public void setDate1(String date1) throws JspException {
		try {
			Publication thePublication = (Publication)findAncestorWithClass(this, Publication.class);
			thePublication.setDate1(date1);
		} catch (Exception e) {
			log.error("Can't find enclosing Publication for date1 tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Publication for date1 tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Publication for date1 tag ");
			}
		}
	}

}
