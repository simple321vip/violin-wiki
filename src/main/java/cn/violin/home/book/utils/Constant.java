package cn.violin.home.book.utils;

import java.time.format.DateTimeFormatter;

public class Constant {

    /* 共通用　定数　false */
    public final static String FALSE_FLAG = "0";
    /* 共通用　定数　true */
    public final static String TRUE_FLAG = "1";
    /* 共通用　定数　undefined */
    public final static String UNDEFINED_FLAG = "2";
    /* 共通用　定数　undefined */
    public final static int AUTHORITY_LEVEL_TWO  = 2;

    public final static String LIKE_FIX = "%";

    public final static DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public final static DateTimeFormatter FORMATTER_DATETIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmSS");


}
