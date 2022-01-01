package ESTCTagLib;

import javax.servlet.jsp.JspWriter;
import java.sql.Statement;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


@SuppressWarnings("serial")
public class DBTest extends ESTCTagLibBodyTagSupport {

	private static final Logger log = LogManager.getLogger( DBTest.class );

	public int doStartTag() throws JspException {
		try { 
			JspWriter out = pageContext.getOut();
			Statement statement = getConnection().createStatement();
			boolean rs = statement.execute("select now()");
			if (rs) { 
				out.print("SUCCESS");
			} else {
				out.print("FAILED");
			}
		} catch ( Exception e ) {
			log.error( "Connection Failed", e );
			throw new JspTagException("Connection Failed: " + e);
		} finally {
			freeConnection();
		}
		return SKIP_BODY;
	}
}