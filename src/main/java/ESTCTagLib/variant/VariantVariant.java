package ESTCTagLib.variant;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ESTCTagLib.ESTCTagLibTagSupport;

@SuppressWarnings("serial")
public class VariantVariant extends ESTCTagLibTagSupport {

	private static final Log log = LogFactory.getLog(VariantVariant.class);

	public int doStartTag() throws JspException {
		try {
			Variant theVariant = (Variant)findAncestorWithClass(this, Variant.class);
			if (!theVariant.commitNeeded) {
				pageContext.getOut().print(theVariant.getVariant());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Variant for variant tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Variant for variant tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Variant for variant tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getVariant() throws JspException {
		try {
			Variant theVariant = (Variant)findAncestorWithClass(this, Variant.class);
			return theVariant.getVariant();
		} catch (Exception e) {
			log.error("Can't find enclosing Variant for variant tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Variant for variant tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Variant for variant tag ");
			}
		}
	}

	public void setVariant(String variant) throws JspException {
		try {
			Variant theVariant = (Variant)findAncestorWithClass(this, Variant.class);
			theVariant.setVariant(variant);
		} catch (Exception e) {
			log.error("Can't find enclosing Variant for variant tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Variant for variant tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Variant for variant tag ");
			}
		}
	}

}
