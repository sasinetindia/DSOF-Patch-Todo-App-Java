/*
 * The MIT License
 *
 *   Copyright (c) 2013, benas (md.benhassine@gmail.com) (md.benhassine@gmail.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package net.beans.todolist.web.servlet.user;

import net.benas.todolist.core.domain.User;
import net.benas.todolist.core.service.api.UserService;
import net.benas.todolist.web.common.util.TodolistUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author benas (md.benhassine@gmail.com)
 */

@WebServlet(name = "LoginServlet", urlPatterns = {"/login", "/login.do"})
public class LoginServlet extends HttpServlet {

    private UserService userService;

    private ResourceBundle resourceBundle;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletConfig.getServletContext());
        userService = applicationContext.getBean(UserService.class);
        resourceBundle = ResourceBundle.getBundle("todolist");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("loginTabStyle", "active");
        request.getRequestDispatcher("/WEB-INF/views/user/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        System.out.println("email = " + email);
        System.out.println("password = " + password);
        //TODO populate login form bean and validate it using JSR 303

        String nextPage;
        if (!userService.login(email, password)) {
            request.setAttribute("error", resourceBundle.getString("login.error.global.invalid"));
            nextPage = "/WEB-INF/views/user/login.jsp";
        } else {
            HttpSession session = request.getSession(true);//create session
            User user = userService.getUserByEmail(email);
            session.setAttribute(TodolistUtils.SESSION_USER, user);
            nextPage = "/user/todos";
        }
        request.getRequestDispatcher(nextPage).forward(request, response);
    }

}