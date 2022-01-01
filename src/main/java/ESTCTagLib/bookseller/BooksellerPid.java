package ESTCTagLib.bookseller;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class BooksellerPid extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(BooksellerPid.class);

	public int doStartTag() throws JspException {
		try {
			Bookseller theBookseller = (Bookseller)findAncestorWithClass(this, Bookseller.class);
			if (!theBookseller.commitNeeded) {
				pageContext.getOut().print(theBookseller.getPid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Bookseller for pid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Bookseller for pid tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Bookseller for pid tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getPid() throws JspException {
		try {
			Bookseller theBookseller = (Bookseller)findAncestorWithClass(this, Bookseller.class);
			return theBookseller.getPid();
		} catch (Exception e) {
			log.error("Can't find enclosing Bookseller for pid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Bookseller for pid tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Bookseller for pid tag ");
			}
		}
	}

	public void setPid(int pid) throws JspException {
		try {
			Bookseller theBookseller = (Bookseller)findAncestorWithClass(this, Bookseller.class);
			theBookseller.setPid(pid);
		} catch (Exception e) {
			log.error("Can't find enclosing Bookseller for pid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Bookseller for pid tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Bookseller for pid tag ");
			}
		}
	}

}
