package com.mayj.demo.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

public interface MemberConstant {


    @Getter
    @AllArgsConstructor
    enum GoodsType implements IBaseEnum {
        /**
         * 付费购买设定的单位， 按天/月/年 购买
         */
        DAYS(1, "天"),
        MONTHS(2, "月"),
        YEARS(3, "年"),
        ;

        private final Integer value;
        private final String description;

    }

    @Getter
    @AllArgsConstructor
    enum ApplyStatus implements IBaseEnum {
        /**
         * 无需审批 直接通过
         */
        PASS(0),
        /**
         * 已提交待支付
         */
        COMMIT(1),
        /**
         * 已支付 审核中
         */
        APPROVE(2),
        /**
         * 审批 驳回
         */
        REJECT(3),
        /**
         * 审批 通过
         */
        ADOPT(4),
        ;
        private final Integer value;

    }

    @Getter
    @AllArgsConstructor
    enum ApplyType implements IBaseEnum {
        /**
         * 系统自动申请
         */
        SYSTEM(0),
        /**
         * 新注册
         */
        NEW(1),
        /**
         * 升级
         */
        UPGRADE(2),
        /**
         * 延期
         */
        DELAY(3),
        ;
        private final Integer value;

    }

    @Getter
    @AllArgsConstructor
    enum ApproveOperation implements IBaseEnum {
        /**
         * 审批操作
         * 审批 驳回
         */
        REJECT(0),
        /**
         * 审批 通过
         */
        ADOPT(1),
        ;
        private final Integer value;

        public static final ApproveOperation get(int value) {
            return value == ADOPT.getValue() ? ADOPT : REJECT;
        }
    }

    @Getter
    @AllArgsConstructor
    enum LogoutStatus implements IBaseEnum {
        /**
         * 已提交待审核
         */
        COMMIT(1),
        /**
         * 审批 驳回
         */
        REJECT(2),
        /**
         * 审批 通过
         */
        ADOPT(3),
        ;
        private final Integer value;

        public static final LogoutStatus get(int value) {
            return Arrays.asList(LogoutStatus.values()).stream().filter(s -> s.getValue() == value).findFirst().orElse(null);
        }
    }

    @Getter
    @AllArgsConstructor
    enum OrderStatus implements IBaseEnum {
        /**
         * 支付过期
         */
        EXPIRED(-1),
        /**
         * 已提交待支付
         */
        COMMIT(0),
        /**
         * 支付完成
         */
        SUCCESS(1),
        ;
        private final Integer value;

    }
}
