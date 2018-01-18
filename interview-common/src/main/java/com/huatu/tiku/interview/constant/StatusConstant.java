package com.huatu.tiku.interview.constant;

/**
 * Created by x6 on 2018/1/18.
 */
public class StatusConstant {

    public   enum StatusEnum {
        NORMAL(1, "正常"), DELETED(-1, "已删除");

        private StatusEnum(int status, String description) {
            this.status  = status;
            this.description = description;
        }

        private int status;
        private String description;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
