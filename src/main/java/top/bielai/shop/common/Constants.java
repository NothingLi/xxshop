package top.bielai.shop.common;


/**
 * @author bielai
 */
public class Constants {

    /**
     * 上传文件的默认url前缀
     */
    public final static String FILE_UPLOAD_DIC = "/opt/xxshop/upload/";

    /**
     * 首页轮播图数量
     */
    public final static int INDEX_CAROUSEL_NUMBER = 5;

    /**
     * 首页一级分类的最大数量
     */
    public final static int INDEX_CATEGORY_NUMBER = 10;

    /**
     * 首页热卖商品数量
     */
    public final static int INDEX_GOODS_HOT_NUMBER = 4;

    /**
     * 首页新品数量
     */
    public final static int INDEX_GOODS_NEW_NUMBER = 5;

    /**
     * 首页推荐商品数量
     */
    public final static int INDEX_GOODS_RECOMMEND_NUMBER = 10;

    /**
     * 购物车中商品的最大数量
     */
    public final static int SHOPPING_CART_ITEM_TOTAL_NUMBER = 200;

    /**
     * 购物车中单个商品的最大购买数量
     */
    public final static int SHOPPING_CART_ITEM_LIMIT_NUMBER = 100;

    /**
     * 搜索分页的默认条数
     */
    public final static int GOODS_SEARCH_PAGE_LIMIT = 10;

    /**
     * 购物车分页的默认条数
     */
    public final static int SHOPPING_CART_PAGE_LIMIT = 10;

    /**
     * 我的订单列表分页的默认条数
     */
    public final static int ORDER_SEARCH_PAGE_LIMIT = 10;

    /**
     * 商品上架状态
     */
    public final static int SELL_STATUS_UP = 0;

    /**
     * 商品下架状态
     */
    public final static int SELL_STATUS_DOWN = 1;

    /**
     * token字段长度
     */
    public final static int TOKEN_LENGTH = 32;

    /**
     * 默认简介
     */
    public final static String USER_INTRO = "随新所欲，丰富多彩";

    public final static String ORDER_BY_NEW = "new";

    public final static String ORDER_BY_PRICE = "price";
}
