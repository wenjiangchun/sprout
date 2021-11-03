package com.sprout.dlyy.demo;

import com.sprout.common.util.SproutStringUtils;
import com.sprout.core.spring.SpringContextUtils;
import com.sprout.dlyy.message.MessageSender;
import com.sprout.flowable.service.ProcessInstanceService;
import com.sprout.oa.leave.entity.Leave;
import com.sprout.oa.leave.entity.LeaveTaskLog;
import com.sprout.oa.leave.flow.LeaveFlowVariable;
import com.sprout.oa.leave.service.LeaveService;
import com.sprout.oa.leave.service.LeaveTaskLogService;
import com.sprout.oa.leave.util.LeaveState;
import com.sprout.oa.util.UserHelper;
import com.sprout.shiro.ShiroUser;
import com.sprout.shiro.util.ShiroUtils;
import com.sprout.system.entity.User;
import com.sprout.system.service.DictService;
import com.sprout.system.service.UserService;
import com.sprout.web.base.BaseCrudController;
import com.sprout.web.util.RestResult;
import org.apache.tomcat.util.buf.HexUtils;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(value = "/dlyy/demo")
public class DemoController {

    private MessageSender messageSender;

    public DemoController(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @GetMapping("/message")
    public String messageView(Model model) {
        return "/dlyy/demo/message";
    }

    @PostMapping("/sendMessage")
    @ResponseBody
    public RestResult messageView(@RequestParam String message) {
        messageSender.sendMessage(message);
        return RestResult.createSuccessResult();
    }

    /**
     * 字符串转换为16进制
     * @param str 需要转换的字符串
     * @return 字符串对应的16进制表示
     */
    public String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString().trim();
    }
}