import com.bozhong.myswitch.core.SwitchRegister;
import com.bozhong.myswitch.util.WebSettingParam;

/**
 * Created by xiezg@317hu.com on 2017/4/19 0019.
 */
public class SwitchTest {
    public static void main(String args[]) throws Throwable {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(WebSettingParam.HTML_TITLE);
                    System.out.println(WebSettingParam.CORP);
                    System.out.println(WebSettingParam.DEPARTMENT);
                }
            }
        });
        thread.start();
        SwitchRegister.getSwitchRegister().init("11", WebSettingParam.class, "172.16.150.151:2181,172.16.150.151:2182,172.16.150.151:2183");

        while (true) {
        }
    }
}
