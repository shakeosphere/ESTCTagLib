package ESTCTagLib.record;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class RecordC003 extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(RecordC003.class);

	public int doStartTag() throws JspException {
		try {
			Record theRecord = (Record)findAncestorWithClass(this, Record.class);
			if (!theRecord.commitNeeded) {
				pageContext.getOut().print(theRecord.getC003());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Record for c003 tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Record for c003 tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Record for c003 tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getC003() throws JspException {
		try {
			Record theRecord = (Record)findAncestorWithClass(this, Record.class);
			return theRecord.getC003();
		} catch (Exception e) {
			log.error("Can't find enclosing Record for c003 tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Record for c003 tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Record for c003 tag ");
			}
		}
	}

	public void setC003(String c003) throws JspException {
		try {
			Record theRecord = (Record)findAncestorWithClass(this, Record.class);
			theRecord.setC003(c003);
		} catch (Exception e) {
			log.error("Can't find enclosing Record for c003 tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Record for c003 tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Record for c003 tag ");
			}
		}
	}

}
