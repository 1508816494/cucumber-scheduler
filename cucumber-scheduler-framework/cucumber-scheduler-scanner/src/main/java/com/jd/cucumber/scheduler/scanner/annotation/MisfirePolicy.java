package com.jd.cucumber.scheduler.scanner.annotation;

/**
 * 对于错过的任务的策略.
 *
 */
public enum MisfirePolicy {

    /**
     * 以下所有的策略请参考quartz的文档.
     */
    DoNothing {
        @Override
        public int getIntValue() {
            return 2;
        }
    }, IgnoreMisfires {
        @Override
        public int getIntValue() {
            return -1;
        }
    }, FireAndProceed {
        @Override
        public int getIntValue() {
            return 1;
        }
    }, None {
        @Override
        public int getIntValue() {
            return 0;
        }
    };

    public abstract int getIntValue();

}
