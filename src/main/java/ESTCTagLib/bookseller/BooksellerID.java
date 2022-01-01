package ESTCTagLib.bookseller;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class BooksellerID extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(BooksellerID.class);

	public int doStartTag() throws JspException {
		try {
			Bookseller theBookseller = (Bookseller)findAncestorWithClass(this, Bookseller.class);
			if (!theBookseller.commitNeeded) {
				pageContext.getOut().print(theBookseller.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Bookseller for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Bookseller for ID tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Bookseller for ID tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getID() throws JspException {
		try {
			Bookseller theBookseller = (Bookseller)findAncestorWithClass(this, Bookseller.class);
			return theBookseller.getID();
		} catch (Exception e) {
			log.error("Can't find enclosing Bookseller for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Bookseller for ID tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Bookseller for ID tag ");
			}
		}
	}

	public void setID(int ID) throws JspException {
		try {
			Bookseller theBookseller = (Bookseller)findAncestorWithClass(this, Bookseller.class);
			theBookseller.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing Bookseller for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Bookseller for ID tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Bookseller for ID tag ");
			}
		}
	}

}
