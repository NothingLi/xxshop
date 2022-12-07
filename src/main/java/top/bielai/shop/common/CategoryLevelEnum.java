package top.bielai.shop.common;


/**
 * @author bielai
 */

public enum CategoryLevelEnum {

    /**
     * 默认结果
     */
    DEFAULT((byte) 0, "ERROR"),
    /**
     * 一级分类
     */
    LEVEL_ONE((byte) 1, "一级分类"),

    /**
     * 二级分类
     */
    LEVEL_TWO((byte) 2, "二级分类"),

    /**
     * 三级分类
     */
    LEVEL_THREE((byte) 3, "三级分类");

    private final byte level;

    private final String name;

    CategoryLevelEnum(byte level, String name) {
        this.level = level;
        this.name = name;
    }

    public static CategoryLevelEnum getXxShopOrderStatusEnumByLevel(byte level) {
        for (CategoryLevelEnum categoryLevelEnum : CategoryLevelEnum.values()) {
            if (categoryLevelEnum.getLevel() == level) {
                return categoryLevelEnum;
            }
        }
        return DEFAULT;
    }

    public byte getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

}
