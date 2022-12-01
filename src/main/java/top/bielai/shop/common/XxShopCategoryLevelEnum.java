
package top.bielai.shop.common;


/**
 * @author Administrator
 */

public enum XxShopCategoryLevelEnum {

    /**
     * 默认结果
     */
    DEFAULT(0, "ERROR"),
    /**
     * 一级分类
     */
    LEVEL_ONE(1, "一级分类"),

    /**
     * 二级分类
     */
    LEVEL_TWO(2, "二级分类"),

    /**
     * 三级分类
     */
    LEVEL_THREE(3, "三级分类");

    private final int level;

    private final String name;

    XxShopCategoryLevelEnum(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public static XxShopCategoryLevelEnum getXxShopOrderStatusEnumByLevel(int level) {
        for (XxShopCategoryLevelEnum xxShopCategoryLevelEnum : XxShopCategoryLevelEnum.values()) {
            if (xxShopCategoryLevelEnum.getLevel() == level) {
                return xxShopCategoryLevelEnum;
            }
        }
        return DEFAULT;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

}
