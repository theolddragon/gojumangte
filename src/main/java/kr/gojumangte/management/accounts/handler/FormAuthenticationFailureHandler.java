package kr.gojumangte.management.accounts.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import kr.gojumangte.management.accounts.model.Account;
import kr.gojumangte.management.accounts.model.LoginHistory;
import kr.gojumangte.management.accounts.repository.LoginHistoryRepository;
import kr.gojumangte.management.accounts.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * 폼 인증 오류 handler.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class FormAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${admin.default.password}")
    private String defaultPassword;

    private final AccountService accountService;
    private final LoginHistoryRepository loginHistoryRepository;

    @Override
    @Transactional
    public void onAuthenticationFailure(
        HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        // 로그인 계정 정보
        String loginId = request.getParameter("loginId");
        Account loginAccount = accountService.getAccountByIdentify(loginId);

        //로그인 History 객체 생성
        LoginHistory loginHistory = LoginHistory.of(loginAccount, request);

        if (AccountExpiredException.class.isAssignableFrom(exception.getClass()) && isMatchedDefaultPassword(request)) {

            //초기 로그인 성공으로 기록
            loginHistoryRepository.save(loginHistory.success());
            request.getRequestDispatcher("/account/reset-password").forward(request, response);
            return;
        }

        //로그인 실패 기록
        loginHistoryRepository.save(loginHistory);

        loginAccount.incrementErrorCount();
        request.getSession().setAttribute("passwordErrorCount", loginAccount.getPasswordErrorCount());

        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
        response.sendRedirect("/login");
    }

    private boolean isMatchedDefaultPassword(HttpServletRequest request) {
        String password = request.getParameter("password");
        return defaultPassword.equals(password);
    }

}
