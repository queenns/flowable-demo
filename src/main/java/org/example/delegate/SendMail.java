package org.example.delegate;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * @author liu.xiaojian
 * @date 2023-01-11 15:11
 */
@Slf4j
public class SendMail implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        log.info("execute send mail ..........................................");
    }

}
