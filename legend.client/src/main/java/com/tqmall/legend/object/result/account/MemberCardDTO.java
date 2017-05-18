package com.tqmall.legend.object.result.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by majian on 16/9/6.
 */
public class MemberCardDTO implements Serializable {
    private Long id;
    private String isDeleted;
    private Date gmtCreate;
    private Date gmtModified;
    private Long creator;
    private Long modifier;
    private Long shopId;//门店id
    private String cardNumber;//会员卡号
    private BigDecimal balance;//卡内金额
    private BigDecimal expenseAmount;//累计消费金额
    private Integer expenseCount;//累计消费次数
    private Long receiver;//服务顾问
    private String receiverName;//服务顾问名字
    private Integer depositCount;//累计充值次数
    private BigDecimal depositAmount;//累计充值金额
    private Long publisher;//发卡人
    private String publisherName;//发卡人
    private Long cardTypeId;//会员卡类型id
    private String cardTypeName;//会员卡名称
    private Date expireDate;//失效时间
    private Long accountId;//账户id
    private Integer usedCount = 0;//使用会员卡消费次数
    private BigDecimal usedAmount = BigDecimal.ZERO;//使用会员卡消费金额
    private Integer discountType;//折扣类型:0.无折扣;1.全部工单折扣;2.全部服务折扣;3.全部配件折扣;4.多种类型 折扣（服务折扣类型、配件折扣类型）
    private Integer goodDiscountType;//配件折扣类型:0.无折扣;1.全部配件享用折扣;2.部分配件享受折扣
    private Integer serviceDiscountType;//服务折扣类型:0.无折扣;1.全部服务享受折扣;2部分服务享受折扣
    private BigDecimal orderDiscount;//工单折扣
    private BigDecimal serviceDiscount;//服务折扣
    private BigDecimal goodDiscount;//配件折扣
    private String discountDescription; //折扣描述信息
    private String discountDescriptionDetail; //详细折扣描述信息

    private List<String> licenseList;

    public Integer getGoodDiscountType() {
        return goodDiscountType;
    }

    public void setGoodDiscountType(Integer goodDiscountType) {
        this.goodDiscountType = goodDiscountType;
    }

    public Integer getServiceDiscountType() {
        return serviceDiscountType;
    }

    public void setServiceDiscountType(Integer serviceDiscountType) {
        this.serviceDiscountType = serviceDiscountType;
    }

    public String getDiscountDescriptionDetail() {
        return discountDescriptionDetail;
    }

    public void setDiscountDescriptionDetail(String discountDescriptionDetail) {
        this.discountDescriptionDetail = discountDescriptionDetail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(BigDecimal expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public Integer getExpenseCount() {
        return expenseCount;
    }

    public void setExpenseCount(Integer expenseCount) {
        this.expenseCount = expenseCount;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Integer getDepositCount() {
        return depositCount;
    }

    public void setDepositCount(Integer depositCount) {
        this.depositCount = depositCount;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public Long getPublisher() {
        return publisher;
    }

    public void setPublisher(Long publisher) {
        this.publisher = publisher;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public Long getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(Long cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public BigDecimal getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal usedAmount) {
        this.usedAmount = usedAmount;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getOrderDiscount() {
        return orderDiscount;
    }

    public void setOrderDiscount(BigDecimal orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public BigDecimal getServiceDiscount() {
        return serviceDiscount;
    }

    public void setServiceDiscount(BigDecimal serviceDiscount) {
        this.serviceDiscount = serviceDiscount;
    }

    public BigDecimal getGoodDiscount() {
        return goodDiscount;
    }

    public void setGoodDiscount(BigDecimal goodDiscount) {
        this.goodDiscount = goodDiscount;
    }

    public String getDiscountDescription() {
        return discountDescription;
    }

    public void setDiscountDescription(String discountDescription) {
        this.discountDescription = discountDescription;
    }

    public List<String> getLicenseList() {
        return licenseList;
    }

    public void setLicenseList(List<String> licenseList) {
        this.licenseList = licenseList;
    }
}
