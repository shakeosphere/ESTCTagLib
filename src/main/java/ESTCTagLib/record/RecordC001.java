package ESTCTagLib.record;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class RecordC001 extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(RecordC001.class);

	public int doStartTag() throws JspException {
		try {
			Record theRecord = (Record)findAncestorWithClass(this, Record.class);
			if (!theRecord.commitNeeded) {
				pageContext.getOut().print(theRecord.getC001());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Record for c001 tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Record for c001 tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Record for c001 tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getC001() throws JspException {
		try {
			Record theRecord = (Record)findAncestorWithClass(this, Record.class);
			return theRecord.getC001();
		} catch (Exception e) {
			log.error("Can't find enclosing Record for c001 tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Record for c001 tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Record for c001 tag ");
			}
		}
	}

	public void setC001(String c001) throws JspException {
		try {
			Record theRecord = (Record)findAncestorWithClass(this, Record.class);
			theRecord.setC001(c001);
		} catch (Exception e) {
			log.error("Can't find enclosing Record for c001 tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Record for c001 tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Record for c001 tag ");
			}
		}
	}

}
