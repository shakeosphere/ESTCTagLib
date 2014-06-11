package ESTCTagLib.mtag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class MtagIndicator2 extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(MtagIndicator2.class);

	public int doStartTag() throws JspException {
		try {
			Mtag theMtag = (Mtag)findAncestorWithClass(this, Mtag.class);
			if (!theMtag.commitNeeded) {
				pageContext.getOut().print(theMtag.getIndicator2());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Mtag for indicator2 tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Mtag for indicator2 tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Mtag for indicator2 tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getIndicator2() throws JspException {
		try {
			Mtag theMtag = (Mtag)findAncestorWithClass(this, Mtag.class);
			return theMtag.getIndicator2();
		} catch (Exception e) {
			log.error("Can't find enclosing Mtag for indicator2 tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Mtag for indicator2 tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Mtag for indicator2 tag ");
			}
		}
	}

	public void setIndicator2(String indicator2) throws JspException {
		try {
			Mtag theMtag = (Mtag)findAncestorWithClass(this, Mtag.class);
			theMtag.setIndicator2(indicator2);
		} catch (Exception e) {
			log.error("Can't find enclosing Mtag for indicator2 tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Mtag for indicator2 tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Mtag for indicator2 tag ");
			}
		}
	}

}
