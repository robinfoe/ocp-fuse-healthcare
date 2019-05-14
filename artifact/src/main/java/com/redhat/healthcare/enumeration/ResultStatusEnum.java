package com.redhat.healthcare.enumeration;

public enum ResultStatusEnum {
	NONE("-1","NONE"),
	NO_MATCH("0","NO MATCH"),
	MATCH("1","MATCH"),
	DONE("2","DONE"),
	ERROR("3","ERROR");
	
	private final String code;
	public String getCode() {return code;}

	private final String description;
	public String getDescription() {return description;}
	
	ResultStatusEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	
	public static ResultStatusEnum findByCode(String code) {
	
		for(ResultStatusEnum item : ResultStatusEnum.values()) {
			if(item.getCode().equalsIgnoreCase(code))
				return item;
		}
		
		return ResultStatusEnum.NONE;
	}
}
