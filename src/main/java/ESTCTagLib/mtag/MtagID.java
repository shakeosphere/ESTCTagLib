package ESTCTagLib.mtag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class MtagID extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(MtagID.class);

	public int doStartTag() throws JspException {
		try {
			Mtag theMtag = (Mtag)findAncestorWithClass(this, Mtag.class);
			if (!theMtag.commitNeeded) {
				pageContext.getOut().print(theMtag.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Mtag for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Mtag for ID tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Mtag for ID tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getID() throws JspException {
		try {
			Mtag theMtag = (Mtag)findAncestorWithClass(this, Mtag.class);
			return theMtag.getID();
		} catch (Exception e) {
			log.error("Can't find enclosing Mtag for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Mtag for ID tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Mtag for ID tag ");
			}
		}
	}

	public void setID(int ID) throws JspException {
		try {
			Mtag theMtag = (Mtag)findAncestorWithClass(this, Mtag.class);
			theMtag.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing Mtag for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Mtag for ID tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Mtag for ID tag ");
			}
		}
	}

}
