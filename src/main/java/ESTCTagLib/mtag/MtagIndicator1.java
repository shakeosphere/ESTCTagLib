package ESTCTagLib.mtag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class MtagIndicator1 extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(MtagIndicator1.class);

	public int doStartTag() throws JspException {
		try {
			Mtag theMtag = (Mtag)findAncestorWithClass(this, Mtag.class);
			if (!theMtag.commitNeeded) {
				pageContext.getOut().print(theMtag.getIndicator1());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Mtag for indicator1 tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Mtag for indicator1 tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Mtag for indicator1 tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getIndicator1() throws JspException {
		try {
			Mtag theMtag = (Mtag)findAncestorWithClass(this, Mtag.class);
			return theMtag.getIndicator1();
		} catch (Exception e) {
			log.error("Can't find enclosing Mtag for indicator1 tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Mtag for indicator1 tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Mtag for indicator1 tag ");
			}
		}
	}

	public void setIndicator1(String indicator1) throws JspException {
		try {
			Mtag theMtag = (Mtag)findAncestorWithClass(this, Mtag.class);
			theMtag.setIndicator1(indicator1);
		} catch (Exception e) {
			log.error("Can't find enclosing Mtag for indicator1 tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Mtag for indicator1 tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Mtag for indicator1 tag ");
			}
		}
	}

}
