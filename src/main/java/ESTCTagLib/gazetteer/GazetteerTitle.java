package ESTCTagLib.gazetteer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class GazetteerTitle extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(GazetteerTitle.class);

	public int doStartTag() throws JspException {
		try {
			Gazetteer theGazetteer = (Gazetteer)findAncestorWithClass(this, Gazetteer.class);
			if (!theGazetteer.commitNeeded) {
				pageContext.getOut().print(theGazetteer.getTitle());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Gazetteer for title tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Gazetteer for title tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Gazetteer for title tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getTitle() throws JspException {
		try {
			Gazetteer theGazetteer = (Gazetteer)findAncestorWithClass(this, Gazetteer.class);
			return theGazetteer.getTitle();
		} catch (Exception e) {
			log.error("Can't find enclosing Gazetteer for title tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Gazetteer for title tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Gazetteer for title tag ");
			}
		}
	}

	public void setTitle(String title) throws JspException {
		try {
			Gazetteer theGazetteer = (Gazetteer)findAncestorWithClass(this, Gazetteer.class);
			theGazetteer.setTitle(title);
		} catch (Exception e) {
			log.error("Can't find enclosing Gazetteer for title tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Gazetteer for title tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Gazetteer for title tag ");
			}
		}
	}

}
