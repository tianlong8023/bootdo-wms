package com.bootdo.common.constants;

public enum DeleteStatusEnum {
	
	IsNotDeleted(0, "未删除"), IsDeleted(1, "已删除");
	
	private Integer code;
	private String name;
	
	private DeleteStatusEnum(Integer code, String name){
		this.code=code;
		this.name=name;
	}
	
	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static DeleteStatusEnum getDeleteStatusEnumByCode(Integer code) {
        if (null == code) return null;
        for (DeleteStatusEnum c : DeleteStatusEnum.values()) {
            if (c.code.intValue() == code.intValue()) return c;
        }
        throw new IllegalArgumentException("没找到匹配的类型：" + code);
    }
	
	public static DeleteStatusEnum getDeleteStatusEnumByName(String name) {
        if (null == name) return null;
        for (DeleteStatusEnum c : DeleteStatusEnum.values()) {
            if (c.name.equals(name)) return c;
        }
        throw new IllegalArgumentException("没找到匹配的类型：" + name);
    }
	
}
