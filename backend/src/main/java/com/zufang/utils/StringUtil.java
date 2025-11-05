package com.zufang.utils;

/**
* @ClassName: StringUtil
* @Description: 字符串工具类
* @author bfan@wiscom.com
*/
public final class StringUtil
{
    public static String EMPTY ="";

    /**
     * 判断字符串是否为空
     * @param parameter
     * @return boolean
     */
    public static boolean isBlank(String parameter)
    {
        if (null == parameter
                || parameter.trim().isEmpty()
                || "null".equalsIgnoreCase(parameter)
                || "0".equals(parameter.trim()))
        {
            return true;
        }
        //加上为0的判断

        return false;
    }

    /**
     * 判断字符串是否非空
     * @param parameter
     * @return boolean
     */
    public static boolean isNotBlank(String parameter)
    {
        return !isBlank(parameter);
    }
}
