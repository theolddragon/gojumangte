package kr.gojumangte.management.accounts.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 History.
 */
@Entity
@Table(name = "TB_LOGIN_HISTORY")
@Getter
@Setter
@EqualsAndHashCode(of = "index")
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_index")
    private Integer index;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_index", nullable = false)
    private Account account;   //로그인 계정

    @Column(name = "is_success", nullable = false)
    private boolean isSuccess = false;      //로그인 성공 여부

    @Column(name = "login_date_time", nullable = false)
    private LocalDateTime loginDatetime = LocalDateTime.now();     //로그인 일시

    @Column(name = "request_ip")
    private String requestIp;       //로그인 요청 IP

    @Column(name = "user_agent")
    private String userAgent;       //USER-AGENT

    /**
     * Constructor.
     *
     * @param account 로그인 계정 정보
     * @param requestIp 로그인 요청 IP
     * @param userAgent User Agent
     */
    private LoginHistory(Account account, String requestIp, String userAgent) {
        this.account = account;
        this.requestIp = requestIp;
        this.userAgent = userAgent;
    }

    /**
     * Create of.
     *
     * @param loginAccount 로그인 계정 정보
     * @param request Http 요청 정보
     * @return LoginHistory
     */
    public static LoginHistory of(Account loginAccount, HttpServletRequest request) {
        return new LoginHistory(
            loginAccount, request.getRemoteAddr(), request.getHeader("user-agent")
        );
    }

    /**
     * 로그인 성공 시.
     *
     * @return LoginHistory
     */
    public LoginHistory success() {
        this.isSuccess = true;
        return this;
    }
}
