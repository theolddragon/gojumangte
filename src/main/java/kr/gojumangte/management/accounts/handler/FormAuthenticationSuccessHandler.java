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
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 폼 인증이 성공했을 경우의 handler.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class FormAuthenticationSuccessHandler
    extends SavedRequestAwareAuthenticationSuccessHandler {

    private final AccountService accountService;
    private final LoginHistoryRepository loginHistoryRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(
        HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {

        // 로그인 계정 정보
        String loginId = request.getParameter("loginId");
        Account loginAccount = accountService.getAccountByIdentify(loginId);

        //로그인 History 객체 생성
        LoginHistory loginHistory = LoginHistory.of(loginAccount, request);

        if (Account.class.isAssignableFrom(authentication.getPrincipal().getClass())) {

            //로그인 오류 횟수 초기화
            loginAccount.initializeErrorCount();

            // 로그인 성공으로 기록
            loginHistoryRepository.save(loginHistory.success());
            getRedirectStrategy().sendRedirect(request, response, "/index");
            return;
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
