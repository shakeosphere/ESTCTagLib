package ESTCTagLib.match;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class MatchSeqnum extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(MatchSeqnum.class);

	public int doStartTag() throws JspException {
		try {
			Match theMatch = (Match)findAncestorWithClass(this, Match.class);
			if (!theMatch.commitNeeded) {
				pageContext.getOut().print(theMatch.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Match for seqnum tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Match for seqnum tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Match for seqnum tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspException {
		try {
			Match theMatch = (Match)findAncestorWithClass(this, Match.class);
			return theMatch.getSeqnum();
		} catch (Exception e) {
			log.error("Can't find enclosing Match for seqnum tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Match for seqnum tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Match for seqnum tag ");
			}
		}
	}

	public void setSeqnum(int seqnum) throws JspException {
		try {
			Match theMatch = (Match)findAncestorWithClass(this, Match.class);
			theMatch.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing Match for seqnum tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Match for seqnum tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Match for seqnum tag ");
			}
		}
	}

}
