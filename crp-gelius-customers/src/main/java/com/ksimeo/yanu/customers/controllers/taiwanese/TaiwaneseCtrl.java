package com.ksimeo.yanu.customers.controllers.taiwanese;

import com.ksimeo.yanu.entities.models.Plan;
import com.ksimeo.yanu.entities.models.User;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author Ksimeo. Created on 23.08.2016 at 15:19 for "crp-gelius" project.
 * @version 1.0
 * @since 1.0
 */
@WebServlet(urlPatterns = "/taiwanese.do")
public class TaiwaneseCtrl extends HttpServlet {
    @Autowired
    private PlansService planServ;

    private static final Logger log = Logger.getLogger(TaiwaneseCtrl.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String login = user.getLogin();
        req.setAttribute("usrlogin", login);
        List<Plan> plans = planServ.getPlansForTaiwanese();
        if (plans != null && plans.size() != 0) {
            req.setAttribute("hiddentext", "hidden");
            req.setAttribute("plans", plans);
        } else {
            req.setAttribute("hiddentable", "hidden");
        }
        req.getRequestDispatcher("WEB-INF/taiwanese.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String orderID = req.getParameter("orderid");
        String quantity = req.getParameter("quant");
        int quant = 0;
        int id = 0;
        if (quantity != null && !(quantity.equals(""))) quant = Integer.parseInt(quantity);
        if (orderID != null && !(orderID.equals(""))) id = Integer.parseInt(orderID);
        int allQuant = planServ.getPlan(id).getOrder().getQuantity();
        int remainQuant = allQuant - quant;
        if (remainQuant == 0) planServ.getPlan(id).setTaiwaneseDateFact(new Date());
        else if (remainQuant > 0) planServ.getPlan(id).getOrder().setQuantity(remainQuant);
        else req.setAttribute("error", "Задано некорретное значение!");
        req.getRequestDispatcher("WEB-INF/taiwanese.jsp").forward(req, resp);
    }
}