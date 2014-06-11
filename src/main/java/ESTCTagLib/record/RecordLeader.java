package ESTCTagLib.record;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class RecordLeader extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(RecordLeader.class);

	public int doStartTag() throws JspException {
		try {
			Record theRecord = (Record)findAncestorWithClass(this, Record.class);
			if (!theRecord.commitNeeded) {
				pageContext.getOut().print(theRecord.getLeader());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Record for leader tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Record for leader tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Record for leader tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getLeader() throws JspException {
		try {
			Record theRecord = (Record)findAncestorWithClass(this, Record.class);
			return theRecord.getLeader();
		} catch (Exception e) {
			log.error("Can't find enclosing Record for leader tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Record for leader tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Record for leader tag ");
			}
		}
	}

	public void setLeader(String leader) throws JspException {
		try {
			Record theRecord = (Record)findAncestorWithClass(this, Record.class);
			theRecord.setLeader(leader);
		} catch (Exception e) {
			log.error("Can't find enclosing Record for leader tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Record for leader tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Record for leader tag ");
			}
		}
	}

}
