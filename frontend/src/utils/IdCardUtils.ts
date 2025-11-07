/**
 * 身份证的工具类
 * @author: yuez
 * @date: 2015/9/16
 */
export class IdCardUtils {
    /**
     * 验证身份证
     * @param idCard
     */
    public static isValidateIdCard(idCard:string):boolean {
        var reg:RegExp = /^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
        return reg.test(idCard);
    }

    /**
     * 获取性别信息
     * @param idCard
     */
    public static getGender(idCard:string):string {
        return idCard.substring(16,17)%2==0?"女":"男";
    }

    /**
     * 获取出生日期
     * @param idCard
     */
    public static getBirthday(idCard:string):string {
        return idCard.substring(6,14);
    }

    /**
     * 获取年龄
     * @param idCard
     */
    public static getAge(idCard:string):number {
        var birthday:string = IdCardUtils.getBirthday(idCard);
        return new Date().getFullYear() - parseInt(birthday.substring(0,4));
    }
}
