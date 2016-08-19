package nishat.net;

import nishat.net.Helper.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import oracle.adf.controller.v2.lifecycle.Lifecycle;
import oracle.adf.controller.v2.lifecycle.PagePhaseEvent;
import oracle.adf.controller.v2.lifecycle.PagePhaseListener;
import oracle.apps.fnd.ext.common.AppsRequestWrapper;
import oracle.apps.fnd.ext.common.CookieStatus;
import oracle.apps.fnd.ext.common.EBiz;
import oracle.apps.fnd.ext.common.ProfileDirectory;
import oracle.apps.fnd.ext.common.Session;
import oracle.apps.fnd.ext.common.State;



import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;
import weblogic.management.configuration.JDBCConnectionPoolMBean;

public class PageListener implements PagePhaseListener {
    public static String userName = "Invalid User";
    public static String userID = "123";
    public static String responsibiltyID = "000";
    public static String responsibility = "1";
    private static String profileName;
    private String serverID = "6122";
    private String Application_id = "20003";
    private String date;
    //private UserInfo ui;


    public PageListener() {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("@       PagePhaseListener Created              @");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }

    public void afterPhase(PagePhaseEvent pagePhaseEvent) {
        if (pagePhaseEvent.getPhaseId() == Lifecycle.PREPARE_RENDER_ID) {
            print("afterPhase called");
        }
    }
    public void print(String message) {
        System.out.println("Org Chart PageListener: " + message);
    }

    public void beforePhase(PagePhaseEvent pagePhaseEvent) {

        if (pagePhaseEvent.getPhaseId() == Lifecycle.INIT_CONTEXT_ID) {
            print("beforePhase called");
            AppsRequestWrapper wrappedRequest = null;
            HttpServletRequest request = null;
            HttpServletResponse response = null;
            Session session;
            String agent = null;
            FacesContext fctx = FacesContext.getCurrentInstance();
            
            request =
                    (HttpServletRequest)fctx.getExternalContext().getRequest();
            response =
                    (HttpServletResponse)fctx.getExternalContext().getResponse();
            CookieStatus icxCookieStatus = null;
            
            
            
            try {

                Connection EBSconn = ConnectionProvider.getConnection();
            
                EBiz instance = EbizUtil.getEBizInstance();
                wrappedRequest =
                        new AppsRequestWrapper(request, response, EBSconn,
                                               instance);
                session = wrappedRequest.getAppsSession();
                
                if (session != null) {
                    icxCookieStatus =
                            session.getCurrentState().getIcxCookieStatus();
                    agent =
                            wrappedRequest.getEbizInstance().getAppsServletAgent();
                    if (!icxCookieStatus.equals(CookieStatus.VALID)) {
                        response.sendRedirect(agent + "AppsLocalLogin.jsp");
                        return;
                    }
                    setUserName(session.getUserName());
                    session.validate();
                    setUserID(session.getUserId());
                    Map columns = session.getInfo();
                    String rId = (String)columns.get("RESPONSIBILITY_ID");

                    if (rId.equals("52071") || rId.equals("52070") ||
                        rId.equals("52050")) {
                        setResponsibiltyID("1");
                    } else {
                        setResponsibiltyID("0");
                    }


                    ProfileDirectory pd = new ProfileDirectory();
                    profileName =
                            pd.getSpecificProfile("XX_HTD_SALES_PLANNING_ACCESS_LEVEL",
                                                  null, Application_id, rId,
                                                  "", serverID,
                                                  wrappedRequest.getConnection());


                    HttpSession sess = request.getSession();
                    
                    
                    if (sess.getAttribute("userID") == null) {
                        sess.setAttribute("userID", session.getUserId());
                        sess.setAttribute("userName", session.getUserName());
                        sess.setAttribute("profile", profileName);
                        sess.setAttribute("resp", "1");
                        String rID = (String)columns.get("RESPONSIBILITY_ID");

                        if (rId.equals("52071") || rId.equals("52070") ||
                            rId.equals("52050")) {
                            setResponsibiltyID("1");
                            sess.setAttribute("respID", "1");
                        } else {
                            setResponsibiltyID("0");
                            sess.setAttribute("respID", "0");
                        }
                        
                    }
                    System.out.println("Username= " +
                                       sess.getAttribute("userName") +
                                       " , UserID= " +
                                       sess.getAttribute("userID") +
                                       " , Profile: " +
                                       sess.getAttribute("profile") +
                                       " , " +
                                       sess.getAttribute("respID"));
                    //Logger logger = new Logger();
                    //logger.logDebugStmt("PageListener.java: "+" Connection Established",sess.getAttribute("userName").toString());

                    // writing user logs on to a file
                    //Logger infoLogger = new Logger();
                    //infoLogger.logInfo(sess);
                    
                } else {
                    Logger stmtLogger = new Logger();
                    stmtLogger.logDebugStmt("PageListener.java: "+" EBS Session is null",request.getSession().getAttribute("userName").toString());
                    response.sendRedirect("http://prodapp.nishat.net:8001");
                }
            } catch (Exception ob) {
                System.out.println("**************************************************************************");
                System.out.println("PageListener.java: "+ob.getMessage());
                System.out.println("**************************************************************************");
//                try {
//                    Logger logger = new Logger();
//                    logger.logError(ob,request.getSession().getAttribute("userName").toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            } finally {
                try {
                    wrappedRequest.getConnection().close();
                    if (wrappedRequest.getConnection().isClosed()) {
                        Logger logger = new Logger();
                        logger.logDebugStmt("PageListener.java: "+" Connection Closed",request.getSession().getAttribute("userName").toString());
                        logger.logDebugStmt("\n", request.getSession().getAttribute("userName").toString());
                    }
                } catch (Exception e) {
                    System.out.println("**************************************************************************");
                    System.out.println("PageListener.java: "+e.getMessage());
                    System.out.println("**************************************************************************");
//                    Logger logger = new Logger();
//                    try{
//                        logger.logError(e,request.getSession().getAttribute("userName").toString());  
//                    }catch(Exception ee){
//                        ee.printStackTrace();
//                    }
                    
                }
            }
        }
    }


    public static void runJavaScriptCode(String javascriptCode) {
        FacesContext facesCtx = FacesContext.getCurrentInstance();

        ExtendedRenderKitService service =
            Service.getRenderKitService(facesCtx,
                                        ExtendedRenderKitService.class);
        service.addScript(facesCtx, javascriptCode);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setResponsibiltyID(String responsibiltyID) {
        this.responsibiltyID = responsibiltyID;
    }

    public String getResponsibiltyID() {
        return responsibiltyID;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return new Date().toString();
    }


}
