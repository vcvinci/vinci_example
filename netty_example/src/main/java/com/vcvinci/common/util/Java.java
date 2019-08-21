package com.vcvinci.common.util;

import java.util.StringTokenizer;

public final class Java {

    private Java() { }

    private static final Version VERSION = parseVersion(System.getProperty("java.specification.version"));

    /**
     * 打包私有用于测试
     */
    static Version parseVersion(String versionString) {
        final StringTokenizer st = new StringTokenizer(versionString, ".");
        int majorVersion = Integer.parseInt(st.nextToken());
        int minorVersion;
        if (st.hasMoreTokens()) {
            minorVersion = Integer.parseInt(st.nextToken());
        } else {
            minorVersion = 0;
        }
        return new Version(majorVersion, minorVersion);
    }

    /**
     * 将这些作为静态最终结果提供了最佳的编译优化机会
     */
    public static final boolean IS_JAVA9_COMPATIBLE = VERSION.isJava9Compatible();
    public static final boolean IS_JAVA8_COMPATIBLE = VERSION.isJava8Compatible();

    public static boolean isIbmJdk() {
        return System.getProperty("java.vendor").contains("IBM");
    }

    /**
     * 打包私有用于测试
     */
    static class Version {
        public final int majorVersion;
        public final int minorVersion;

        private Version(int majorVersion, int minorVersion) {
            this.majorVersion = majorVersion;
            this.minorVersion = minorVersion;
        }

        @Override
        public String toString() {
            return "Version(majorVersion=" + majorVersion +
                    ", minorVersion=" + minorVersion + ")";
        }

        /**
         * 打包私有用于测试
         */
        boolean isJava9Compatible() {
            return majorVersion >= 9;
        }

        /**
         * 打包私有用于测试
         */
        boolean isJava8Compatible() {
            return majorVersion > 1 || (majorVersion == 1 && minorVersion >= 8);
        }
    }

}