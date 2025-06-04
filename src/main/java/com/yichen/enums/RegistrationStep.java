package com.yichen.enums;

import java.util.*;

public enum RegistrationStep {
    AGREEMENT("报名协议", true),
    QUAL_QUERY("资格信息查询", true),
    QUAL_CONFIRM("资格信息确认", true),
    WRITTEN_APPLY("笔试报考", true),
    WRITTEN_PAY("笔试缴费", true),
    ORAL_APPLY("口试报考", false),
    ORAL_PAY("口试缴费", false),
    COMPLETE("完成报名", true),
    PRINT_ADMIT("打印笔试准考证", true);

    private final String cnName;
    private final boolean mandatory;
    private static final Map<RegistrationStep, List<RegistrationStep>> FLOW = new LinkedHashMap<>();

    static {
        FLOW.put(AGREEMENT, List.of(QUAL_QUERY));
        FLOW.put(QUAL_QUERY, List.of(QUAL_CONFIRM));
        FLOW.put(QUAL_CONFIRM, List.of(WRITTEN_APPLY));
        FLOW.put(WRITTEN_APPLY, List.of(WRITTEN_PAY));
        FLOW.put(WRITTEN_PAY, Arrays.asList(ORAL_APPLY, COMPLETE));
        FLOW.put(ORAL_APPLY, List.of(ORAL_PAY));
        FLOW.put(ORAL_PAY, List.of(COMPLETE));
        FLOW.put(COMPLETE, List.of(PRINT_ADMIT));
    }

    RegistrationStep(String cnName, boolean mandatory) {
        this.cnName = cnName;
        this.mandatory = mandatory;
    }

    public String getCnName() {
        return cnName;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    // 获取有效下一步（含业务逻辑判断）
    public List<RegistrationStep> getNextSteps(boolean includeOral) {
        List<RegistrationStep> steps = new ArrayList<>();
        
        // 获取所有可能的下一步
        List<RegistrationStep> allNextSteps = FLOW.getOrDefault(this, Collections.emptyList());
        
        // 如果不包含口试步骤，则过滤掉口试相关的步骤
        if (!includeOral) {
            for (RegistrationStep step : allNextSteps) {
                if (step != ORAL_APPLY && step != ORAL_PAY) {
                    steps.add(step);
                }
            }
        } else {
            steps.addAll(allNextSteps);
        }
        
        return steps;
    }
} 