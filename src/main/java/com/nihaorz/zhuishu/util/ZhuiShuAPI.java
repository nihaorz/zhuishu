package com.nihaorz.zhuishu.util;

/**
 * @author Nihaorz
 */
public class ZhuiShuAPI {

    /**
     * 替换字符
     */
    public static final String REPLACE_KEY = "${key}";

    /**
     * 模糊搜索
     */
    public static final String SEARCH_ADDR = "http://api.zhuishushenqi.com/book/fuzzy-search?query=" + REPLACE_KEY;

    /**
     * 小说信息
     */
    public static final String BOOK_INFO = "http://api.zhuishushenqi.com/book/" + REPLACE_KEY;

    /**
     * 获取小说正版源
     */
    public static final String BOOK_REPO1 = "http://api.zhuishushenqi.com/btoc?view=summary&book=" + REPLACE_KEY;

    /**
     * 获取小说正版源与盗版源(混合)
     */
    public static final String BOOK_REPO2 = "http://api.zhuishushenqi.com/atoc?view=summary&book=" + REPLACE_KEY;

    /**
     * 获取小说章节列表
     */
    public static final String CHAPTERS_INFO = "http://api.zhuishushenqi.com/atoc/" + REPLACE_KEY + "?view=chapters";

    /**
     * 获取小说章节内容
     */
    public static final String CHAPTERS_CONTENT = "http://chapterup.zhuishushenqi.com/chapter/" + REPLACE_KEY;

}
