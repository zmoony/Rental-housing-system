package com.zufang.utils;

import lombok.extern.log4j.Log4j2;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import java.util.regex.Pattern;

@Log4j2
public class ChineseToPinYinUtil {

	public static String regex = "[\u4E00-\u9FA5]";

	private static String ID_CARD_REGEX = "\\d{15}|\\d{17}[\\dxX]";

	public static boolean judgeIdcard(String id_card) {
    	return id_card.matches(ID_CARD_REGEX);
    }


	public static String getFullNamePinyin(String name) {
		String pinyin = "";
		for(int i = 0; i < name.length();i++) {
			Pattern pattern = Pattern.compile(regex);
			if(pattern.matcher(String.valueOf(name.charAt(i))).find()) {
				String py = getPinyinByChinese(name.charAt(i));
//				log.info(name.charAt(i)+":"+py);
				pinyin += py;
			} else {
				pinyin += name.charAt(i);
			}
		}
//		log.info(name+":"+pinyin);
		return pinyin;
	}

	public static String getShortNamePinyin(String name) {
		String pinyin = "";
		for(int i = 0; i < name.length();i++) {
			Pattern pattern = Pattern.compile(regex);
			if(pattern.matcher(String.valueOf(name.charAt(i))).find()) {
				String py = getPinyinByChinese(name.charAt(i));
				if(StringUtil.isBlank(py)) {
					return "";
				}
				pinyin += py.charAt(0);
			} else {
				pinyin += name.charAt(i);
			}
		}
		return pinyin;
	}

	public static String getPinyinByChinese(char singleChar) {
		HanyuPinyinOutputFormat hanyuFormat = new HanyuPinyinOutputFormat();
		hanyuFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		String py = "";
		String [] res ;
		try {
			res = PinyinHelper.toHanyuPinyinStringArray(singleChar, hanyuFormat);
			py = res[0];
		} catch(Exception e) {
			log.error(singleChar+"转拼音失败："+e, e);
		}
		return py;
	}

//	public static void main(String[] args) {
//		String name = "六正则";
//		log.info(name+":"+getFullNamePinyin(name));
//		log.info(name+":"+getShortNamePinyin(name));
//	}
}
