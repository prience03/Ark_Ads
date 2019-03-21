package com.ark.adkit.basics.configs;

public class ADPlatform {

    public static final String GDT = "gdt";
    public static final String WSKJ = "wskj";
    public static final String IFLY = "ifly";
    public static final String LYJH = "lyjh";
    public static final String SELF = "selfad";
    public static final String TTAD = "ttad";
    public static final String YDT = "ydt";

    public final String platform;

    public ADPlatform(String fileType) {
        this.platform = fileType;
    }
}
