package com.sprout.oa.util;

import com.sprout.common.util.SproutDateUtils;
import com.sprout.core.spring.SpringContextUtils;
import com.sprout.system.entity.Group;
import com.sprout.system.entity.User;
import com.sprout.system.service.UserService;
import com.sprout.system.utils.Status;

import java.util.*;

public class UserHelper {

    //普通员工
    private static final String ROLE_ORDINARY_EMPLOYEE = "ordinary_employee";

    //部门经理
    private static final String ROLE_DEPT_MANAGER = "dept_manager";

    //主管副总经理
    private static final String ROLE_DEPUTY_MANAGER = "deputy_manager";

    //总经理
    private static final String ROLE_GENERAL_MANAGER = "general_manager";

    //董事长
    private static final String ROLE_CHAIRMAN_MANAGER = "chairman_manager";

    /**
     * 查询普通员工所对应部门经理，如果不存在则查询单位主管副总经理角色人员
     * @param userId 员工ID
     * @param useDeputyManagerInstead 是否查询单位所有机构人员
     * @return 部门经理人员列表
     */
    public static List<User> getDeptManagerList(Long userId, boolean useDeputyManagerInstead) {
        List<User> deptManagerList = new ArrayList<>();
        User user = SpringContextUtils.getBean(UserService.class).findById(userId);
        Set<User> users = user.getGroup().getUsers();
        if (!users.isEmpty()) {
            users.stream().filter(u -> u.getRoles().stream().anyMatch(role -> role.getCode().equals(ROLE_DEPT_MANAGER)) && u.getStatus().equals(Status.ENABLE)).forEach(deptManagerList::add);
        } else {
            if (useDeputyManagerInstead) {
                //查询用户所有机构信息 查询角色为单位副总经理人员信息
                deptManagerList = getUserListByRoleName(userId, ROLE_DEPUTY_MANAGER);
            }
        }
        return deptManagerList;
    }

    /**
     * 获取用户所在单位主管副经理人员列表
     * @param userId 用户ID
     * @return 所有角色为"deputy_manager"人员信息
     */
    public static List<User> getDeputyManagerList(Long userId) {
        return getUserListByRoleName(userId, ROLE_DEPUTY_MANAGER);
    }

    /**
     * 获取用户所在单位总经理人员列表
     * @param userId 用户ID
     * @return 所有角色为"general_manager"人员信息
     */
    public static List<User> getGeneralManagerList(Long userId) {
        return getUserListByRoleName(userId, ROLE_GENERAL_MANAGER);
    }

    /**
     * 获取用户所在单位董事长人员列表
     * @param userId 用户ID
     * @return 所有角色为"chairman_manager"人员信息
     */
    public static List<User> getChairmanManagerList(Long userId) {
        return getUserListByRoleName(userId, ROLE_CHAIRMAN_MANAGER);
    }

    /**
     * 根据角色名称查询用户所在单位下所有赋有该角色的人员信息
     * @param userId 用户ID
     * @param roleName 角色名称
     * @return 人员列表信息
     */
    public static List<User> getUserListByRoleName(Long userId, String roleName) {
        List<User> userOfRoleList = new ArrayList<>();
        User user = SpringContextUtils.getBean(UserService.class).findById(userId);
        List<User> userOfGroupList = getUserOfGroupList(user.getGroup());
        userOfGroupList.stream().filter(u -> u.getRoles().stream().anyMatch(role -> role.getCode().equals(roleName))).forEach(userOfRoleList::add);
        return userOfRoleList;
    }

    /**
     * 获取机构所在单位下所有状态为启用的人员信息
     * @param g 机构信息
     * @return 顶层机构下状态为启用的所有人员信息
     */
    private static List<User> getUserOfGroupList(Group g) {
        List<User> userList = new ArrayList<>();
        g.getUsers().stream().filter(u -> u.getStatus().equals(Status.ENABLE)).forEach(userList:: add);
        if (Objects.nonNull(g.getParent())) {
            userList.addAll(getUserOfGroupList(g.getParent()));
        }
        return userList;
    }

    /**
     * 判断员工角色级别
     * @param userId 用户ID
     * @return [普通员工1，部门经理，主管副总经理及总经理2，董事长3，其它0]
     */
    public static int getUserLevel(Long userId) {
        User user = SpringContextUtils.getBean(UserService.class).findById(userId);
        if ( user.getRoles().stream().anyMatch(role -> role.getCode().equals(ROLE_ORDINARY_EMPLOYEE))) {
            return 1;
        } else if (user.getRoles().stream().anyMatch(role -> Arrays.asList(ROLE_DEPT_MANAGER, ROLE_DEPUTY_MANAGER, ROLE_GENERAL_MANAGER).contains(role.getCode()))) {
            return 2;
        } else if (user.getRoles().stream().anyMatch(role -> role.getCode().equals(ROLE_CHAIRMAN_MANAGER))) {
            return 3;
        } else {
            return 0;
        }
    }


    /**
     * 计算员工年休假天数
     * <strong>说明：</strong>如果员工入职不满一年则不享受该假期
     * @param user 员工信息
     * @param workDay 年份
     * @return 年休假天数
     */
    public static int getTotalHoliday(User user, Date workDay) {
        int totalYears = getTotalYearOfEntryDept(user, workDay);
        if (totalYears <= 0) {
            return 0;
        } else {
            //计算参加工作年份距离当前年份天数
            int startYear = user.getWorkStartYear();
            int endYear = SproutDateUtils.getYear(workDay);
            int diff = endYear - startYear;
            if (diff >= 1 && diff < 10) {
                return 5;
            } else if (diff >= 10 && diff < 20) {
                return 10;
            } else if (diff >= 20) {
                return 15;
            } else {
                return 0;
            }
        }
    }

    /**
     * 计算员工从入职开始到指定日期年数
     * @param user 员工信息
     * @param workDay 工作日
     * @return 入职年数
     */
    public static int getTotalYearOfEntryDept(User user, Date workDay) {
        Date d = user.getEntryDay() != null ? user.getEntryDay() : new Date();
        return SproutDateUtils.truncatedCompareTo(workDay, d, Calendar.YEAR);
    }
}
