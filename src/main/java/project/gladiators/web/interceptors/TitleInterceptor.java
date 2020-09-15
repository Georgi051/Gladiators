package project.gladiators.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import project.gladiators.annotations.PageTitle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TitleInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

        if (handler instanceof HandlerMethod) {
            if (modelAndView != null && !modelAndView.getModel().containsKey("title")) {

                PageTitle pageTitle = ((HandlerMethod) handler).getMethodAnnotation(PageTitle.class);
                if (pageTitle != null) {
                    modelAndView.addObject("title", String.format("%s - Gladiators", pageTitle.value()));
                }

            }
        }

    }
}
