package ESTCTagLib.printer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class PrinterPid extends ESTCTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(PrinterPid.class);

	public int doStartTag() throws JspException {
		try {
			Printer thePrinter = (Printer)findAncestorWithClass(this, Printer.class);
			if (!thePrinter.commitNeeded) {
				pageContext.getOut().print(thePrinter.getPid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Printer for pid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Printer for pid tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Printer for pid tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getPid() throws JspException {
		try {
			Printer thePrinter = (Printer)findAncestorWithClass(this, Printer.class);
			return thePrinter.getPid();
		} catch (Exception e) {
			log.error("Can't find enclosing Printer for pid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Printer for pid tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Printer for pid tag ");
			}
		}
	}

	public void setPid(int pid) throws JspException {
		try {
			Printer thePrinter = (Printer)findAncestorWithClass(this, Printer.class);
			thePrinter.setPid(pid);
		} catch (Exception e) {
			log.error("Can't find enclosing Printer for pid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Printer for pid tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Printer for pid tag ");
			}
		}
	}

}
