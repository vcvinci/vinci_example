package com.vcvinci.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author vinci
 * @Title: JsonTest
 * @ProjectName vc-test
 * @Description: TODO
 * @date 2018/10/16上午10:09
 */
public class JsonTest {
    public static AtomicReference<String> version = new AtomicReference<String>();
    public static void main(String[] args) {
        System.out.println(version.compareAndSet(version.get(), "test"));
        System.out.println(version.get());
        System.out.println(version.compareAndSet("test1", "test"));
    }

    public static class DcsSwitchInfo {

        /**
         * version : 2018100901
         * businessLine : ucar
         * “status” : 1
         * fromCenter : logicA
         * targetCenter : logicB
         * “startTime” : 1535446582000
         * “endTime” : 1535446592000
         */

        private String version;
        private String businessLine;
        private int status;
        private String fromCenter;
        private String targetCenter;
        private long startTime;
        private long endTime;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getBusinessLine() {
            return businessLine;
        }

        public void setBusinessLine(String businessLine) {
            this.businessLine = businessLine;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getFromCenter() {
            return fromCenter;
        }

        public void setFromCenter(String fromCenter) {
            this.fromCenter = fromCenter;
        }

        public String getTargetCenter() {
            return targetCenter;
        }

        public void setTargetCenter(String targetCenter) {
            this.targetCenter = targetCenter;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }
    }
}
