package com.ksimeo.yanu.customers.controllers.bobst;

import com.ksimeo.yanu.api.services.PlansService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author Ksimeo. Created on 01.09.2016 at 20:37 for "crp-gelius" project.
 * @version 1.0
 * @since 1.0
 */
@WebServlet(urlPatterns = "/allbobstdone.do")
public class BobstDoneAllCtrl extends HttpServlet {

    @Autowired
    private PlansService planServ;

    private static final Logger log = Logger.getLogger(BobstDoneAllCtrl.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id != null && !(id.equals(""))) {
            int planId = Integer.parseInt(id);
            planServ.getPlan(planId).setBobstDateFact(new Date());
            resp.sendRedirect("/bobst.do");
        } else {
            req.setAttribute("error", "Вы ввели некорректные данные!");
            req.getRequestDispatcher("WEB-INF/bobst.jsp").forward(req, resp);
        }
    }
}
