package ESTCTagLib.printer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PrinterID extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(PrinterID.class);

	public int doStartTag() throws JspException {
		try {
			Printer thePrinter = (Printer)findAncestorWithClass(this, Printer.class);
			if (!thePrinter.commitNeeded) {
				pageContext.getOut().print(thePrinter.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Printer for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Printer for ID tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Printer for ID tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getID() throws JspException {
		try {
			Printer thePrinter = (Printer)findAncestorWithClass(this, Printer.class);
			return thePrinter.getID();
		} catch (Exception e) {
			log.error("Can't find enclosing Printer for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Printer for ID tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Printer for ID tag ");
			}
		}
	}

	public void setID(int ID) throws JspException {
		try {
			Printer thePrinter = (Printer)findAncestorWithClass(this, Printer.class);
			thePrinter.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing Printer for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Printer for ID tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Printer for ID tag ");
			}
		}
	}

}
