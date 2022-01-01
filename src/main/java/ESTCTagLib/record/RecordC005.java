package ESTCTagLib.record;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class RecordC005 extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(RecordC005.class);

	public int doStartTag() throws JspException {
		try {
			Record theRecord = (Record)findAncestorWithClass(this, Record.class);
			if (!theRecord.commitNeeded) {
				pageContext.getOut().print(theRecord.getC005());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Record for c005 tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Record for c005 tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Record for c005 tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getC005() throws JspException {
		try {
			Record theRecord = (Record)findAncestorWithClass(this, Record.class);
			return theRecord.getC005();
		} catch (Exception e) {
			log.error("Can't find enclosing Record for c005 tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Record for c005 tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Record for c005 tag ");
			}
		}
	}

	public void setC005(String c005) throws JspException {
		try {
			Record theRecord = (Record)findAncestorWithClass(this, Record.class);
			theRecord.setC005(c005);
		} catch (Exception e) {
			log.error("Can't find enclosing Record for c005 tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Record for c005 tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Record for c005 tag ");
			}
		}
	}

}
