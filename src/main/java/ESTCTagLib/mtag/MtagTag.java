package ESTCTagLib.mtag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class MtagTag extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(MtagTag.class);

	public int doStartTag() throws JspException {
		try {
			Mtag theMtag = (Mtag)findAncestorWithClass(this, Mtag.class);
			if (!theMtag.commitNeeded) {
				pageContext.getOut().print(theMtag.getTag());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Mtag for tag tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Mtag for tag tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Mtag for tag tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getTag() throws JspException {
		try {
			Mtag theMtag = (Mtag)findAncestorWithClass(this, Mtag.class);
			return theMtag.getTag();
		} catch (Exception e) {
			log.error("Can't find enclosing Mtag for tag tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Mtag for tag tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Mtag for tag tag ");
			}
		}
	}

	public void setTag(String tag) throws JspException {
		try {
			Mtag theMtag = (Mtag)findAncestorWithClass(this, Mtag.class);
			theMtag.setTag(tag);
		} catch (Exception e) {
			log.error("Can't find enclosing Mtag for tag tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Mtag for tag tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Mtag for tag tag ");
			}
		}
	}

}
