package springaop;

import com.baoge.springaop.Machine;
import com.baoge.springaop.MainConfig;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author shaoxubao
 * @Date 2019/10/12 16:36
 */
public class TestSpringAop {

    @Test
    public void testSpringAop() {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);

        Machine machine = applicationContext.getBean(Machine.class);

        System.out.println(machine);
        machine.div(1, 1);

        applicationContext.close();
    }

}
