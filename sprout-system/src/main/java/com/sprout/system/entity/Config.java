package com.sprout.system.entity;

import com.sprout.core.jpa.entity.AbstractBaseEntity;
import com.sprout.system.utils.ConfigType;

import javax.persistence.*;

/**
 * 系统配置实体类
 * 
 * @author sofar
 */
@Entity
@Table(name="sys_config")
public class Config extends AbstractBaseEntity<Long> {

	/**
	 * 验证码校验 通过配置该对象来指定登陆时是否启用校验码校验功能。
	 */
	public static final String VALIDATE_CODE = "VALIDATE_CODE";

	private static final long serialVersionUID = 1L;

    /**
     * 配置代码
     */
	private String code;

    /**
     * 配置名称
     */
	private String name;

    /**
     * 配置值
     */
	private String value;

    /**
     * 配置类型
     */
	private ConfigType configType = ConfigType.B;

    /**
     * 配置说明
     */
	private String description;

    @Column(unique=true, length = 20)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(length = 50)
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 200)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Enumerated(EnumType.ORDINAL)
	public ConfigType getConfigType() {
		return configType;
	}

	public void setConfigType(ConfigType configType) {
		this.configType = configType;
	}

	@Column(length = 300)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Config [code=" + code + ", name="
				+ name + ", value=" + value + ", configType=" + configType.getTypeName()
				+ "]";
	}
	
}
