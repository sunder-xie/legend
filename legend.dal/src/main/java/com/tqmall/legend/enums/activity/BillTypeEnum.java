package com.tqmall.legend.enums.activity;

/**
 * 活动服务单类别
 * <p/>
 * OrderGoodType Enum
 */
public enum BillTypeEnum {
	PINGAN_BAOYANG("PINGAN.BAOYANG", "平安保养"),
	PINGAN_BUQI("PINGAN.BUQI", "平安补漆");

	private final String code;
	private final String sName;

	private BillTypeEnum(String code, String sName) {
		this.code = code;
		this.sName = sName;
	}

	public String getCode() {
		return this.code;
	}

	public String getsName() {
		return this.sName;
	}

	public static String getsNameByCode(String code) {
		for (BillTypeEnum e : BillTypeEnum.values()) {
			if (e.getCode().equals(code)) {
				return e.getsName();
			}
		}
		return null;
	}

	public static BillTypeEnum[] getMessages() {
		BillTypeEnum[] arr = values();
		return arr;
	}
}
