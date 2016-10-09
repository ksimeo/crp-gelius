package com.ksimeo.yanu.admin.controllers;

import com.ksimeo.yanu.admin.gto.UserGTO;
import com.ksimeo.yanu.admin.models.User;
import com.ksimeo.yanu.admin.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * @author Ksimeo. Created on 08.10.2016 at 19:11 for "untitled" project.
 * @version 1.0
 * @since 1.0
 */
@WebServlet(urlPatterns = "/login")
public class LogInCtrl extends HttpServlet {
@Autowired
    private UserService usrServ;
//    = new UserServImpl();
    //Инициализация логера
    private static final Logger log = Logger.getLogger(LogInCtrl.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
        log.info("Начало работы.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        UserGTO userInfo = new UserGTO();
        userInfo.setLogin(login);
        userInfo.setPassword(password);
        log.info("Попытка зайти пользователя с логином " + login);
        try {
            User usr = usrServ.getUser(userInfo);
            if (usr != null && usr.getRole() == 0) {
                HttpSession session = req.getSession();
                session.setAttribute("user", usr);
                session.setMaxInactiveInterval(30 * 60);
                String name = usr.getLogin();
                Cookie userLogin = new Cookie("user", name);
                userLogin.setMaxAge(30 * 60);
                resp.addCookie(userLogin);
                resp.sendRedirect("/admin.do");
            } else {
                log.info("Пользователя не существует или не достаточно прав доступа.");
                req.setAttribute("error", "Вы ввели некорректные данные или у вас не достаточно прав доступа");
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("База данных недоступна.");
            req.setAttribute("error", "Вход в систему временно недоступен. Попробуйте зайти позже или обратитесь" +
                    " к администратору");
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }
}