package top.bielai.shop.common;


/**
 * @author bielai
 */

public enum IndexConfigTypeEnum {

    /**
     * 配置类别
     */
    DEFAULT((byte) 0, "DEFAULT"),
    INDEX_SEARCH_HOTS((byte) 1, "INDEX_SEARCH_HOTS"),
    INDEX_SEARCH_DOWN_HOTS((byte) 2, "INDEX_SEARCH_DOWN_HOTS"),
    INDEX_GOODS_HOT((byte) 3, "INDEX_GOODS_HOTS"),
    INDEX_GOODS_NEW((byte) 4, "INDEX_GOODS_NEW"),
    INDEX_GOODS_RECOMMEND((byte) 5, "INDEX_GOODS_RECOMMEND");

    private final byte type;

    private final String name;

    IndexConfigTypeEnum(byte type, String name) {
        this.type = type;
        this.name = name;
    }

    public static IndexConfigTypeEnum getIndexConfigTypeEnumByType(byte type) {
        for (IndexConfigTypeEnum indexConfigTypeEnum : IndexConfigTypeEnum.values()) {
            if (indexConfigTypeEnum.getType() == type) {
                return indexConfigTypeEnum;
            }
        }
        return DEFAULT;
    }

    public byte getType() {
        return type;
    }


    public String getName() {
        return name;
    }

}
