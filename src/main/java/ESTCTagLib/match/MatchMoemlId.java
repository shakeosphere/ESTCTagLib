package ESTCTagLib.match;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class MatchMoemlId extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(MatchMoemlId.class);

	public int doStartTag() throws JspException {
		try {
			Match theMatch = (Match)findAncestorWithClass(this, Match.class);
			if (!theMatch.commitNeeded) {
				pageContext.getOut().print(theMatch.getMoemlId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Match for moemlId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Match for moemlId tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Match for moemlId tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getMoemlId() throws JspException {
		try {
			Match theMatch = (Match)findAncestorWithClass(this, Match.class);
			return theMatch.getMoemlId();
		} catch (Exception e) {
			log.error("Can't find enclosing Match for moemlId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Match for moemlId tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Match for moemlId tag ");
			}
		}
	}

	public void setMoemlId(String moemlId) throws JspException {
		try {
			Match theMatch = (Match)findAncestorWithClass(this, Match.class);
			theMatch.setMoemlId(moemlId);
		} catch (Exception e) {
			log.error("Can't find enclosing Match for moemlId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Match for moemlId tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Match for moemlId tag ");
			}
		}
	}

}
