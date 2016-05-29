package eccrm.web.filter;

import com.michael.base.emp.service.EmpService;
import com.michael.base.emp.vo.EmpVo;
import com.ycrl.core.SystemContainer;
import com.ycrl.core.context.Login;
import com.ycrl.core.context.SecurityContext;
import com.ycrl.utils.string.StringUtils;
import eccrm.core.security.LoginInfo;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * 用户登录过滤器，如果没有登录，则强制重定向到登录页面（默认的登录页面可以通过参数loginHtml进行设置）
 * 如果已经登录，则从session中取出登录的账户以及对应的权限放到登录上下文中
 *
 * @author miles
 * @datetime 13-12-15 下午11:43
 */
@WebFilter(filterName = "loginFilter", urlPatterns = "/*", asyncSupported = true)
public class LoginFilter implements Filter {

    private String defaultLoginHtml = "/index.html";

    private Logger logger = Logger.getLogger(LoginFilter.class);
    //静态资源
    private String[] static_suffix = new String[]{".js", ".less", ".css", ".jpg", ".png", ".gif", ".html", ".htm", ".ttf", ".svg", ".woff", ".map", ".ico", ".mp3", ".json"};


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("加载过滤器:登录验证过滤器....");
        String configLoginHtml = filterConfig.getInitParameter("loginHtml");
        if (configLoginHtml != null && !"".equals(configLoginHtml.trim())) {
            defaultLoginHtml = configLoginHtml;
        }
        if (!defaultLoginHtml.startsWith("/")) {
            defaultLoginHtml += "/";
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.addHeader("X-UA-Compatible", "chrome=1,IE=edge,IE=11,IE=10,IE=9,IE=8");

        // FIXME 使用nginx后将此部分逻辑移到nginx中
        String url = request.getRequestURI();
        if (url.matches(".+\\.(js|html|css|png|jpeg|jpg|gif)") || url.equals("/login") || url.equals("/base/emp/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        HttpSession session = request.getSession();
        String empId = (String) session.getAttribute(LoginInfo.EMPLOYEE);
        String user = request.getParameter("_u");
        Login login = new Login();
        if (StringUtils.isNotEmpty(empId)) {
            login.setEmpId(empId);
            login.setEmpName((String) session.getAttribute(LoginInfo.EMPLOYEE_NAME));
            login.setOrgId((String) session.getAttribute(LoginInfo.ORG));
            login.setOrgName((String) session.getAttribute(LoginInfo.ORG_NAME));
        } else if (StringUtils.isNotEmpty(user)) {
            EmpVo vo = SystemContainer.getInstance().getBean(EmpService.class).findById(user);

            Assert.notNull(vo, "用户不存在!");
            login.setEmpId(user);
            login.setEmpName(vo.getName());
            login.setEmpCode(vo.getCode());
            login.setOrgId(vo.getOrgId());
            login.setOrgName(vo.getOrgName());
        } else {
            response.sendRedirect(defaultLoginHtml);
            return;
        }

        // FIXME 使用nginx后将此部分逻辑移到nginx中
        String userAgent = request.getHeader("User-Agent");
        if (StringUtils.isNotEmpty(userAgent) && (userAgent.contains("Android ") || userAgent.contains("CFNetwork/"))) {
            login.setMobile(true);
        }
        SecurityContext.set(login);
        filterChain.doFilter(request, response);
        SecurityContext.remove();
    }

    @Override
    public void destroy() {

    }
}
