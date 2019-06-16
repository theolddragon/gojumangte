package kr.gojumangte.management.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
public class CustomErrorController implements ErrorController {

    public static final String ERROR_PATH     = "/error";
    public static final String ERROR_DEFAULT  = ERROR_PATH + "/default";
    public static final String ERROR_401      = ERROR_PATH + "/401";
    public static final String ERROR_403      = ERROR_PATH + "/403";
    public static final String ERROR_404      = ERROR_PATH + "/404";
    public static final String ERROR_500      = ERROR_PATH + "/500";


    @RequestMapping(value = ERROR_DEFAULT, method = RequestMethod.GET)
    public String defaultError() {
        return ERROR_DEFAULT;
    }

    @RequestMapping(value = ERROR_401, method = RequestMethod.GET)
    public String error401() {
        return ERROR_401;
    }

    @RequestMapping(value = ERROR_403, method = RequestMethod.GET)
    public String error403() {
        return ERROR_403;
    }

    @RequestMapping(value = ERROR_404, method = RequestMethod.GET)
    public String error404() {
        return ERROR_404;
    }

    @RequestMapping(value = ERROR_500, method = RequestMethod.GET)
    public String error500() {
        return ERROR_500;
    }

    @Override
    public String getErrorPath() {
        return ERROR_DEFAULT;
    }



}
